package com.kayak.pms.global.constants;

/**
 * 常量值
 */
public class ServerConst {


    /**
     * 外部服务名称
     *
     */
    public enum AppServer{
        TA_TRANS(0,"ta-trans");
        private String serverHost;
        private int code;
        AppServer(int code,String serverHost){
            this.code=code;
            this.serverHost=serverHost;
        }

        public String getServerHost() {
            return serverHost;
        }

        public int getCode() {
            return code;
        }

    }


    /**
     * 外部服务地址
     *
     */
    public enum ServerUrl{
        TA_CUST_ACCT_REQ(1,"/TA4005","ta系统查询账户Req流水"),
        TA_CUST_ACCT_CFM(2,"/TA4006","ta系统查询账户Cfm流水"),
        TA_CUST_TRANS_REQ(3,"/TA4007","ta系统查询交易Req流水"),
        TA_CUST_TRANS_CFM(4,"/TA4008","ta系统查询交易Cfm流水"),
        ;
        private int code;
        private String url;
        private String desc;


        ServerUrl( int code,String url, String desc) {
            this.url = url;
            this.desc = desc;
            this.code = code;
        }

        public int getCode() {
            return code;
        }

        public String getUrl() {
            return url;
        }

        public String getDesc() {
            return desc;
        }
    }
}
