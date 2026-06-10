package com.kayak.dps.ods.service;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Map;
import com.kayak.core.dao.DaoService;
import com.kayak.core.system.SysBeans;
import com.kayak.dps.ods.constants.Constants;
import com.kayak.dps.pub.ICallback;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class CallbackService implements ICallback {

        Map<String, Object> params;
        public CallbackService(Map<String, Object> params){
            this.params=params;
        }
        private static String data_sources = "";
        @Override
        public void call(Object obj) throws Exception {
            long startTime = System.currentTimeMillis();
            List<Object[]> data =(List<Object[]>) obj;
            if(CollectionUtils.isEmpty(data)){		// 文件内容为空，不处理
                log.info(" 文件内容为空不处理 ");
                return;
            }
            params.put("totalNum", data.size());
            StringBuffer sb= (StringBuffer)params.get("sb");
            List<String> fieldType= (List<String> )params.get("fieldType");
            DaoService daoService=null;
            PreparedStatement ps = null;
            AutoCloseable autoCloseable = null;
            try {
                 daoService = SysBeans.getBean("daoService");
                autoCloseable=daoService.selectDataSource(0);
                Connection connection = daoService.getConnection();
                daoService.begin();
                 ps = connection.prepareStatement(sb.toString());
                for (int rowNum = 0; rowNum < data.size(); rowNum++) {
                    for(int i=0; i < data.get(rowNum).length; i++){
                        if ((fieldType.get(i).equalsIgnoreCase("number")||fieldType.get(i).equalsIgnoreCase("decimal")||fieldType.get(i).equalsIgnoreCase("date")||fieldType.get(i).equalsIgnoreCase("double")
                                ||fieldType.get(i).equalsIgnoreCase("int")||fieldType.get(i).equalsIgnoreCase("tinyint"))
                                && ( "".equals(data.get(rowNum)[i]) ||  data.get(rowNum)[i] == null)){
                            ps.setString(i+1,null);
                        }else {
                            ps.setString(i+1,data.get(rowNum)[i].toString().trim());
                        }
                    }
                    ps.setString(data.get(rowNum).length+1,params.get("deal_date").toString());
                    if("1".equals(params.get("addcolumsflag").toString())){
                        ps.setString(data.get(rowNum).length+2,"2");
                    }
                    ps.addBatch();
                    if (rowNum % 10000 == 0 && rowNum != 0) {
                        ps.executeBatch();
                        connection.commit();
                    }
                }
                ps.executeBatch();
                daoService.commit();

                log.info(" ##### 批量入库{}耗时: {}", data.size(),System.currentTimeMillis() - startTime);
            }catch (Exception e) {
                log.error("数据入库异常",e);
                daoService.end();
                throw new Exception(e.getMessage());
            } finally{
                ps.close();
               if(autoCloseable!=null) autoCloseable.close();
                //连接池不用关闭
               // connection.close();
            }
        }
}