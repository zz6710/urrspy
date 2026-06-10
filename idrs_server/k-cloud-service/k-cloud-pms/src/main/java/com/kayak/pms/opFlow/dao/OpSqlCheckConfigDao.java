package com.kayak.pms.opFlow.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.exception.PromptException;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.util.Tools;
import com.kayak.pms.opFlow.constant.CheckSign;
import com.kayak.pms.opFlow.model.OpSqlCheckConfig;
import com.kayak.pms.opFlow.model.OpSqlConfig;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class OpSqlCheckConfigDao extends ComnDao {
    public boolean checkSql(OpSqlConfig opSqlConfig, Map<String, Object> map) throws Exception {
        List<OpSqlCheckConfig> checks = opSqlConfig.getCheckData();
        if (checks == null) {
            return true;
        }

        for (OpSqlCheckConfig check : checks) {// 遍历单个SQL更新的校验配置信息
            String checkName = check.getCheckName();// 校验名称
            String checkField = check.getCheckField();// 校验值
            String compareVal = check.getCheckTarget();// 比较值
            String compareSign = check.getCheckSign();// 比较条件
            String errText = check.getCheckMsg();// 出错提示
//            Boolean exitAll = check.getExitall();// 是否中止同步SQL执行
            String checkSql = check.getCheckSql();// 查询SQL
            String checkType = check.getCheckType();

            Object checkVal = null;// 校验值

            if ("2".equals(checkType) && !Tools.strIsEmpty(checkSql)) {
                // 执行校验检查SQL
                List<SqlRow> sqlRows = super.findRows(checkSql, Integer.parseInt(opSqlConfig.getDatasource()), map);

                if (sqlRows.size() > 0) {// SQL执行成功，进行校验检查
                    SqlRow sqlRow = sqlRows.get(0);
                    // 只取查询结果的第一行第一列的值
                    checkVal = sqlRow.get(sqlRow.keySet().iterator().next());
                }
            } else if ("1".equals(checkType) && !Tools.strIsEmpty(checkField)) {
                checkVal = map.get(checkField);
            } else {
                throw new PromptException("校验配置【" + checkName + "】中有没有配置checkSql或checkField");
            }

            if (!compare(checkVal, compareVal, compareSign)) {// 校验检查失败条件成立
                throw new PromptException("校验失败：" + errText);
            }
        }

        return true;
    }

    private boolean compare(Object sqlObj, String compareVal, String sign) {
        if (sqlObj == null) {
            sqlObj = "";
        }

        int res;
        if (sqlObj.getClass().equals(String.class)) {// 字符串比较
            res = ((String) sqlObj).compareTo(compareVal);
        } else {// 数值比较
            res = Tools.str2BigDecimal(String.valueOf(sqlObj)).compareTo(Tools.str2BigDecimal(compareVal));
        }
        boolean result = false;
        switch (sign) {
            case CheckSign.EQ:
                result = res == 0;
                break;
            case CheckSign.NEQ:
                result = res != 0;
                break;
            case CheckSign.LT:
                result = res < 0;
                break;
            case CheckSign.LTE:
                result = res <= 0;
                break;
            case CheckSign.GT:
                result = res > 0;
                break;
            case CheckSign.GTE:
                result = res >= 0;
                break;
        }
        log.info("##### compare : " + sqlObj + " " + sign + " " + compareVal + " " + result);
        return result;
    }
}
