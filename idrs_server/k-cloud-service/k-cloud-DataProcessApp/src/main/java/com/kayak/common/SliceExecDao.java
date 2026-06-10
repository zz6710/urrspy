package com.kayak.common;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.system.SysUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.sql.SQLException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Repository
public class SliceExecDao {
    @Autowired
    public ComnDao comnDao;
    public Integer  exec(String sqlStr,Map params ) throws Exception{
        AtomicInteger total=new AtomicInteger();//总数量
        Instant startTime = Instant.now();
            try {
                // 初始化线程池
                String newMaximumPoolSize = SysUtil.getSystemParamsByParaid("90000030001");
                String newCorePoolSize = SysUtil.getSystemParamsByParaid("90000030002");
                int size = Integer.parseInt(newMaximumPoolSize);
                int coreSize = Integer.parseInt(newCorePoolSize);
                ExecutorService executorService = new ThreadPoolExecutor(coreSize, size, 0L, TimeUnit.MILLISECONDS,
                        new LinkedBlockingQueue<>());
                    Pattern pattern = Pattern.compile("\\$LIST\\{(\\w+)\\}");
                    Matcher matcher = pattern.matcher(sqlStr);
                    if (matcher.find()) {
                        String paramKey = matcher.group(1); //括号内内容
                        Map<String, Object> tmpMap = new HashMap<>();
                        tmpMap.put("code", paramKey);
                        List<SqlRow> rows = comnDao.findRows("select * from base_port_sql_param_info where code = $S{code}", tmpMap); //sql
                        if (!rows.isEmpty()) {
                            String paramSqlstr = rows.get(0).getString("sqlstr");// 参数的sql查询语句
                            List<SqlRow> paramRows = comnDao.findRows(paramSqlstr, params);
                            List<Future> futureList = new ArrayList<>();
                            for (int i = 0; i < paramRows.size(); i++) {
                                int index = i;
                                Runnable runnable = () -> {
                                    String sqlstr1 = sqlStr.replace("$LIST{" + paramKey + "}", paramRows.get(index).getString(paramKey));
                                    try {
                                        total.getAndAdd(comnDao.update(sqlstr1, params).getEffect()) ;
                                    } catch (Exception e) {
                                        log.error(e.getMessage(),e);
                                        throw new RuntimeException(e);
                                    }
                                };
                                Future<?> future = executorService.submit(runnable);
                                futureList.add(future);
                            }

                            for (Future future : futureList) {
                                try {
                                    future.get();
                                } catch (Exception e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                    } else {
                        total.getAndAdd(comnDao.update(sqlStr, params).getEffect());
                    }
                executorService.shutdown();
            }catch (Exception e){
                log.error(e.getMessage(),e);
                throw new SQLException("执行SQL["+ "" +"]报错："+e.getMessage(),e);
            }
        // 获取当前系统时间点
        Instant endTime = Instant.now();
        // 计算时间间隔
        Duration duration = Duration.between(startTime, endTime);
        long seconds = duration.getSeconds();
        log.info("SQL{}:用时{}秒", "" ,seconds);
        return total.get();
    }
}
