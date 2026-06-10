package com.kayak.login.service;

import cn.hutool.core.codec.Base64;
import com.kayak.core.exception.PromptException;
import com.kayak.core.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.http.*;

@Slf4j
@Service
@RefreshScope
public class SsoLoginService {
    public static final String RTN_CODE_SUCCESS = "000000000000";
    public static final String RTN_CODE_EROOR01 = "020900000001";//用户不存在
    @Value("${esb.url}")
    public String esbURL;
    @Value("${uias.isAuth}")
    public Boolean isAuth;

    /**
     * 统一认证登录
     * @param request
     * @param params
     * @return
     * @throws PromptException
     */
    public Boolean ssoLoginProcess(HttpServletRequest request, Map<String, Object> params) throws PromptException{
        //统一登录
        String requestSSOToken = request.getParameter("SAMLResponse");//统一登录页面的参数
        if (requestSSOToken != null) {//从统一登录返回
            String remoteUser = this.getRemoteUser(requestSSOToken);
            params.put("username", remoteUser);
            params.put("sso_login", "true");
            jq(params);
            return true;
        }
        return false;
    }

    /**
     * 鉴权接口
     * @param params
     * @return
     * @throws PromptException
     */
    public void jq(Map<String, Object> params) throws PromptException{
        if(!isAuth){
            log.info("不使用鉴权接口");
            return;
        }
        RestTemplate restTemplateTemp = new RestTemplate();
            // 设置请求头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_XML);
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_XML));
            String sequence=UUID.randomUUID().toString();
            log.info("调用统一认证鉴权接口MsgId[{}]",sequence);
            String sequence1=UUID.randomUUID().toString();
            log.info("调用统一认证鉴权接口GlobalSeqNo[{}]",sequence1);
            String nowDate= DateUtil.getNowDate();
            String nowTime= DateUtil.getNowTime();
            // 构建XML报文
            String xmlPayload = "<?xml version='1.0' encoding='UTF-8'?>\n" +
                    "<soap:Envelope xmlns:soap=\"\"http://schemas.xmlsoap.org/soap/envelope/\"\" xmlns:soapenc=\"\"http://schemas.xmlsoap.org/soap/encoding/\"\" xmlns:xsd=\"\"http://www.w3.org/2001/XMLSchema\"\" xmlns:d=\"\"http://esb.spdbbiz.com/metadata\"\" xmlns:s=\"\"http://esb.spdbbiz.com/services/S120030044\"\">\n" +
                    "\t<soap:Header>\n" +
                    "\t\t<s:ReqHeader>\n" +
                    "\t\t\t<d:MsgId>${sequence}</d:MsgId>\n" +
                    "\t\t\t<d:SourceSysId>1026</d:SourceSysId>\n" +
                    "\t\t\t<d:ConsumerId>1026</d:ConsumerId>\n" +
                    "\t\t\t<d:ServiceAdr>http://esb.spdbbiz.com:7701/services/S120030044</d:ServiceAdr>\n" +
                    "\t\t\t<d:ServiceAction>urn:/NewAuthrQuery</d:ServiceAction>\n" +
                    "\t\t</s:ReqHeader>\n" +
                    "\t</soap:Header>\n" +
                    "\t<soap:Body>\n" +
                    "\t\t<s:ReqNewAuthrQuery>\n" +
                    "\t\t\t<s:ReqSvcHeader>\n" +
                    "\t\t\t\t<s:TranDate>${nowDate}</s:TranDate>\n" +
                    "\t\t\t\t<s:TranTime>${nowTime}</s:TranTime>\n" +
                    "\t\t\t\t<s:TranTellerNo></s:TranTellerNo>\n" +
                    "\t\t\t\t<s:TranSeqNo></s:TranSeqNo>\n" +
                    "\t\t\t\t<s:ConsumerId>1026</s:ConsumerId>\n" +
                    "\t\t\t\t<s:GlobalSeqNo>${sequence1}</s:GlobalSeqNo>\n" +
                    "\t\t\t\t<s:BranchId>9901</s:BranchId>\n" +
                    "\t\t\t</s:ReqSvcHeader>\n" +
                    "\t\t\t<s:SvcBody>\n" +
                    "\t\t\t\t<s:UserInfoQryInArgs>\n" +
                    "\t\t\t\t\t<s:UserDomainName>${userid}</s:UserDomainName>\n" +
                    "\t\t\t\t\t<s:AppId>URRS</s:AppId>\n" +
                    "\t\t\t\t</s:UserInfoQryInArgs>\n" +
                    "\t\t\t\t<s:UserInfoQryInOpts>\n" +
                    "\t\t\t\t\t<s:RetUserBscInfoFlag>true</s:RetUserBscInfoFlag>\n" +
                    "\t\t\t\t\t<s:RetUserAdvInfoFlag>true</s:RetUserAdvInfoFlag>\n" +
                    "\t\t\t\t\t<s:RetUserCpcInfoFlag>false</s:RetUserCpcInfoFlag>\n" +
                    "\t\t\t\t\t<s:RetInstInfoFlag>true</s:RetInstInfoFlag>\n" +
                    "\t\t\t\t\t<s:RetUserGrpInfoFlag>false</s:RetUserGrpInfoFlag>\n" +
                    "\t\t\t\t\t<s:RetRoleInfoFlag>false</s:RetRoleInfoFlag>\n" +
                    "\t\t\t\t\t<s:RetTaskInfoFlag>false</s:RetTaskInfoFlag>\n" +
                    "\t\t\t\t\t<s:RetFrontFuncInfoFlag>false</s:RetFrontFuncInfoFlag>\n" +
                    "\t\t\t\t</s:UserInfoQryInOpts>\n" +
                    "\t\t\t</s:SvcBody>\n" +
                    "\t\t</s:ReqNewAuthrQuery>\n" +
                    "\t</soap:Body>\n" +
                    "</soap:Envelope>";
        xmlPayload=xmlPayload.replace("${sequence}",sequence);
        xmlPayload=xmlPayload.replace("${sequence1}",sequence1);
        xmlPayload=xmlPayload.replace("${nowDate}",nowDate);
        xmlPayload=xmlPayload.replace("${nowTime}",nowTime);
        xmlPayload=xmlPayload.replace("${userid}",params.get("username").toString());
            // 创建请求体
            HttpEntity<String> requestEntity = new HttpEntity<>(xmlPayload, headers);
            // 发送POST请求
            ResponseEntity<String> responseEntity = restTemplateTemp.exchange(
                    esbURL,
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );

            // 获取响应状态码和响应体
            HttpStatus statusCode = responseEntity.getStatusCode();
            String responseBody = responseEntity.getBody();
            System.out.println("Response Code: " + statusCode);
            System.out.println("Response Body: " + responseBody);
            String returnCode= getReturnCode(responseBody);
            if(RTN_CODE_SUCCESS.equals(returnCode)){//返回成功
                log.info("鉴权接口调用返回成功");
            }else if(RTN_CODE_EROOR01.equals(returnCode)){//用户不存在
                throw new PromptException("在统一认证系统该用户无本系统权限");
            }else{
                String returnMsg= getReturnMsg(responseBody);
                log.error("鉴权接口调用返回失败,接口返回；[{}]",returnMsg);
                throw new PromptException(returnMsg);
            }
    }
    public String getReturnCode(String xml) {
        String returnCode = null;
        if (xml != null && !"".equals(xml)) {
            try {
                String pattern = "<s:ReturnCode>(.+?)</s:ReturnCode>";
                Pattern compile = Pattern.compile(pattern);
                Matcher matcher = compile.matcher(xml);
                if (matcher.find()) {
                    returnCode = matcher.group(1);
                } else {
                    log.debug("未获取到ReturnCode！");
                }
            } catch (Exception var7) {
                var7.printStackTrace();
            }
        }
        return returnCode;
    }
    public String getReturnMsg(String xml) {
        String returnMsg = null;
        if (xml != null && !"".equals(xml)) {
            try {
                String pattern = "<s:ReturnMsg>(.+?)</s:ReturnMsg>";
                Pattern compile = Pattern.compile(pattern);
                Matcher matcher = compile.matcher(xml);
                if (matcher.find()) {
                    returnMsg = matcher.group(1);
                } else {
                    log.debug("未获取到ReturnMsg！");
                }
            } catch (Exception var7) {
                var7.printStackTrace();
            }
        }
        return returnMsg;
    }

    public String getRemoteUser(String token) {
        String remoteUser = null;
        if (token != null && !"".equals(token)) {
            try {
                String myToken = new String(Base64.decode(token.getBytes("UTF-8")), "UTF-8");
                String pattern = "<Attribute Name=\"buid\"><AttributeValue>(.+?)</AttributeValue>";
                Pattern compile = Pattern.compile(pattern);
                Matcher matcher = compile.matcher(myToken);
                if (matcher.find()) {
                    remoteUser = matcher.group(1);
                } else {
                    log.debug("令牌结构异常，无法获取用户ID！");
                }
            } catch (Exception var7) {
                var7.printStackTrace();
            }
        }

        return remoteUser;
    }

}
