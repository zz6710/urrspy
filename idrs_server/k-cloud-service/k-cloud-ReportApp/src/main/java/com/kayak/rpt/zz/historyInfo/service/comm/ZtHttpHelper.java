package com.kayak.rpt.zz.historyInfo.service.comm;

import com.kayak.core.exception.PromptException;
import com.kayak.core.system.SysUtil;
import com.kayak.rpt.pub.ICallback;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Map;

@Service
@Slf4j
@RefreshScope
public class ZtHttpHelper {
    @Value("${zt.api.state:true}")
    private Boolean state;//启用状态
    @Value("${zt.api.authorization}")
    private String authorization;
    @Value("${zt.api.url}")
    private String url;
    @Value("${zt.api.max_send_times}")
    private Integer max_send_times;//单接口最大发送次数
    @Value("${zt.api.pageSize}")
    private String pageSize;//单次接入数量

    @Autowired
    private ZtDao ztDao;
    /**
     * 接口
     * @param
     * @return
     */
    public int saveData(String port_address, Map<String, Object> param, ICallback handle) throws Exception {
        if(!state) {
            log.info("未开启中台实时接口");
            return 0;
        }
        String zt_code= port_address; //(String) ztDao.getPortInfo(code).get("port_address");
        RestTemplate restTemplateTemp = new RestTemplate();
        param.put("dmnsCd", zt_code);
        param.put("pageSize", param.get("limit"));
        param.put("inqrMthd", "api");

        int pageNo = Integer.parseInt(param.get("start").toString())/Integer.parseInt(param.get("limit").toString()) + 1;//计算查询页数据,仅查询一页数据
        param.put("pageNo", pageNo);
        ResponseEntity<String> responseEntity = sendHttpToZT(restTemplateTemp, param);//发送接收报文并接收返回信息
        parseResult(responseEntity, param,handle);//解析报文及入库

        return new JSONObject(responseEntity.getBody()).getJSONObject("data").getInt("totalRows");
    }

    /**
     * 投资者历史数据查询中台接口
     * @param
     * @return
     */
    public void saveLoopData(String port_address, Map<String, Object> param, ICallback handle) throws Exception {
        if(!state) {
            log.info("未开启中台实时接口");
            return;
        }
        String zt_code = port_address;
        RestTemplate restTemplateTemp = new RestTemplate();
        param.put("dmnsCd", zt_code);
        param.put("pageSize", pageSize);
        param.put("inqrMthd", "api");
        Boolean hasNextPage;//有下一页
        int send_times=0;//发送次数
        do {
            param.put("pageNo", ++send_times);
            ResponseEntity<String> responseEntity = sendHttpToZT(restTemplateTemp, param);//发送接收报文
            hasNextPage =  parseResult(responseEntity, param,handle);//解析报文及入库

            if(param.get("custNo")==null || StringUtils.isEmpty(param.get("custNo").toString())) {//如果不带客户识别标识只请求一次
                break;
            }
            //TODO 风险点，生产环境数据量大循环次数多
        }while(hasNextPage && send_times<max_send_times );//有下一页且不超过最大发送次数则继续查询
    }

    /**
     * 根据条件查询中台接口返回数据量总数
     * @param zt_code
     * @param param
     * @return
     * @throws Exception
     */
    public int queryMaxCount(String zt_code ,Map<String, Object> param) throws Exception {
        if(!state) {
            log.info("未开启中台实时接口");
            return 0;
        }

        RestTemplate restTemplateTemp = new RestTemplate();
        param.put("dmnsCd", zt_code);
        param.put("pageSize", param.get("limit"));
        param.put("inqrMthd", "api");
        //调用中台接口
        ResponseEntity<String> responseEntity = sendHttpToZT(restTemplateTemp, param);//发送接收报文

        return Integer.parseInt(new JSONObject(responseEntity.getBody()).getString("count"));
    }

    /**
     * 发送文件中台
     * @param restTemplateTemp
     * @param param
     * @return
     * @throws Exception
     */
    private ResponseEntity<String> sendHttpToZT(RestTemplate restTemplateTemp,  Map<String, Object> param) throws Exception {
        // 设置请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("authorization", authorization);
        String filedMapString  = SysUtil.getSystemParamsByParaid("90000051501"); // 映射表
        String[] pairArray=filedMapString.split(",");
        //转换为中台系统要的字段名
        for (int i = 0; i <pairArray.length ; i++) {
            String [] pair=pairArray[i].split(":");
            if(param.containsKey(pair[0])){
                param.put(pair[1],param.get(pair[0]));
            }
        }
        ObjectMapper objectMapper = new ObjectMapper();
        String body = objectMapper.writeValueAsString(param);
        log.info("中台实时接口[{}]请求参数:{}",param.get("dmnsCd"),body);
        // 创建请求体
        HttpEntity<String> requestEntity = new HttpEntity<>(body, headers);
        long startTime = System.currentTimeMillis();
        // 发送POST请求
        ResponseEntity<String> responseEntity = restTemplateTemp.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                String.class
        );
        log.info("中台实时接口[{}]耗时: {} ms,返回数据:{}",param.get("dmnsCd"),System.currentTimeMillis() - startTime,responseEntity.getBody());

        return responseEntity;
    }

    private boolean parseResult(ResponseEntity<String> responseEntity,Map<String, Object> param,  ICallback handle) throws Exception {
        // 获取响应状态码和响应体
        HttpStatus statusCode = responseEntity.getStatusCode();
        if(!statusCode.is2xxSuccessful()){
            throw new PromptException("请求数据中台报错"+statusCode);
        }
        String body = responseEntity.getBody();
        JSONObject bodyJson = new JSONObject(body);
        String rtnCode=bodyJson.getString("code");
        String rtnMsg=bodyJson.getString("msg");
        if(!"200".equals(rtnCode)){
            throw new PromptException("数据中台返回异常："+rtnMsg);
        }
        JSONObject data =bodyJson.getJSONObject("data");

        Boolean hasNextPage=data.getBoolean("hasNextPage");//是否有下一页
        JSONArray content=data.getJSONArray("content");//业务数据list
        handle.call(content);
        return hasNextPage;
    }
}
