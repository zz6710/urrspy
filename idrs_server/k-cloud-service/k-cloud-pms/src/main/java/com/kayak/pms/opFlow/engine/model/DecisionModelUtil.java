package com.kayak.pms.opFlow.engine.model;

import com.kayak.core.spring.SpringContextHolder;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.Tools;
import com.kayak.helper.StringHelper;
import com.kayak.pms.opFlow.engine.constant.EnvExprTypeConstant;
import com.kayak.pms.opFlow.engine.dao.CommonDao;
import com.kayak.pms.opFlow.engine.dao.EnvItemDao;
import com.kayak.pms.opFlow.engine.exception.DbException;
import com.kayak.pms.opFlow.engine.utils.EnvExprModel;
import com.kayak.pms.opFlow.engine.utils.GroovyUtil;
import com.kayak.pms.opFlow.engine.utils.RegexUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * Created by daniel on 20/03/2017.
 */
public class DecisionModelUtil {
    private static final Logger logger = LoggerFactory.getLogger(DecisionModelUtil.class);

    /**
     * 通过执行上下文来判断
     * @param execution
     * @return
     */
    public static boolean foundByEnvExp(Execution execution, List<TransitionModel> outputs) {
        boolean isfound = false;

        // 分支加判断节点
        boolean isSpecial = false;
        for (int i = 0; i < outputs.size(); i++) {
            TransitionModel tm = outputs.get(i);
            isSpecial = tm.getSource() instanceof DecisionModel && tm.getTarget() instanceof JoinModel;
            if (isSpecial) {
                break;
            }
        }

        for (int i = 0; i < outputs.size(); i++) {
            TransitionModel tm = outputs.get(i);

            String envTransitionExpr = tm.getEnvExp();
            if (StringHelper.isNotEmpty(envTransitionExpr)) {
                logger.info("==============================开始执行【{}】连线==============================, ", tm.getName());
//              String envTransitionExpr = "$N{  一级审批条件  } == 1";
                try {
                    // 执行sql 返回值
                    CommonDao commonDao = SpringContextHolder.getBean("commonDao");
                    EnvItemDao envItemDao = SpringContextHolder.getBean("envItemDao");
                    List<EnvExprModel> envExprModels = RegexUtil.extractPlaceholder(envTransitionExpr);

                    // 参与计算的参数
                    Map<String, Object> params = execution.getLatestSubmitParams();
                    if (!"true".equals(params.get("_wfExecQuartz")) && !"true".equals(execution.getParams().get("_wf_auto_approval"))) {
                        params.putAll(SysUtil.getUserInfo());
                    }
                    params.put("process_instance_id", execution.getProcessInstance().getProcessInstanceId());
                    params.put("current_date", Tools.getCurrentDate());

                    logger.info("原始条件{}, ", envTransitionExpr);

                    for (EnvExprModel envExprModel : envExprModels) {
                        String value = (String) params.get(envExprModel.getKey());
                        // 先从参数里获取, 如果有则不执行sql
                        if (StringHelper.isNotEmpty(value)) {
                            envExprModel.setValue(value);
                        } else {
                            String sql = envItemDao.getSqlByItemKey(execution.getProcess().getId(), envExprModel.getKey());
                            sql = RegexUtil.getSql(sql, params);
                            if (EnvExprTypeConstant.STRING.equals(envExprModel.getType())) {
                                String result = commonDao.getBySqlRetStr(sql);
                                if (StringHelper.isEmpty(result)) {
                                    result = "";
                                }
                                logger.info("上下文【{}】对应的sql为【{}】,执行结果【{}】", envExprModel.getKey(), sql, result);
                                envExprModel.setValue(result);
                            } else {
                                Integer result = commonDao.getBySqlRetInteger(sql);
                                envExprModel.setValue(result + "");
                                logger.info("上下文【{}】对应的sql为【{}】,执行结果【{}】", envExprModel.getKey(), sql, result);
                            }
                        }
                    }

                    // 生成执行表达式
                    for (EnvExprModel envExprModel : envExprModels) {
                        if (EnvExprTypeConstant.STRING.equals(envExprModel.getType())) {
                            envTransitionExpr = envTransitionExpr.replace(envExprModel.getOriginal(), "'" + envExprModel.getValue() + "'");
                        } else {
                            envTransitionExpr = envTransitionExpr.replace(envExprModel.getOriginal(), envExprModel.getValue());
                        }
                    }

                    isfound = GroovyUtil.eval(envTransitionExpr);
                    logger.info("最终执行的groovy表达式为【{}】,执行的结果为【{}】", envTransitionExpr, isfound);

                    logger.info("==============================结束执行【{}】连线==============================, ", tm.getName());
                    // TODO 导致最后任务被执行了两次, 第一次分支执行, 当满足条件, 继续执行, 再做任务判断的时候, 不一定有任务在等待合并, 所以导致了后面的节点被多次执行

                    /**
                     * 思路
                     * 如果有任务产生, 则什么也不做
                     * 如果没有任务产生, 最终只执行一次连线
                     */

                    // 分支加判断节点 (特殊处理逻辑, 不要乱动）
                    if (isSpecial) {
                        boolean isEmptyTm = false;

                        if (tm.getTarget() instanceof JoinModel) {
                            isEmptyTm = true;
                            execution.setTm(tm);
                        }

                        // 不执行空连线
                        if (isfound && !isEmptyTm) {
                            execution.setHasExec(true);
                            tm.setEnabled(true);
                            tm.execute(execution);
                            break;//找到不在继续寻找
                        }

                        execution.setExecNums(execution.getExecNums() + 1);

                        // 最后一次, 找到执行找到的，还没有找到执行空连线
                        if (execution.getExecNums() == 4 && !execution.getHasExec()) {
                            if (isfound) {
                                tm.setEnabled(true);
                                tm.execute(execution);
                            } else {// 没有找到单独执行一次空连线
                                TransitionModel tm1 = execution.getTm();
                                tm1.setEnabled(true);
                                tm1.execute(execution);
                            }
                        }

                    } else {
                        if (isfound) {
                            tm.setEnabled(true);
                            tm.execute(execution);
                            break;//找到不在继续寻找
                        }
                    }

                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                    throw new DbException("分支节点【" + tm.getName() + "】判断失败", e);
                }
            }
        }
        return isfound;
    }

}
