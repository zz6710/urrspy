package com.kayak.clear.service.business;


import com.kayak.clear.req.PubReq;
import com.kayak.core.sql.SqlRow;
import com.kayak.dps.ods.util.SequenceUtil;
import com.kayakwise.kcloud.batch.annotation.StepNo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@Scope("prototype")
public class PrdPBCTaskService extends BusinessBaseTaskService {
    /**产品人行代码生成
     * @param request
     * @throws Exception
     */
    @StepNo(stepNo = 1)
    protected void stepProcess(PubReq request) throws Exception{
        log.info(" ###### 产品人行代码生成开始 ");
        Map<String,Object> params = new HashMap<>();
        params.put("deal_date",workDate);
        comnDao.doTrans( () ->{
            getPrdPBCInfo(params);
        });
        log.info(" ###### 产品人行代码生成结束 ");

    }

    /**
     * 获取需要生成人行代码的产品
     * @param params
     * @return
     * @throws Exception
     */
    private void getPrdPBCInfo(Map<String,Object> params) throws Exception{
        String sql="select k.prod_code,pc.pbc_regcode,k.subs_bdate,k.end_date,(select paravalue from sys_param where paraid='80000047') as isu_org_cd " +
                "     from ods_prod_base_info k " +
                "     left join mid_prod_core_info pc on pc.prod_cd = k.prod_code " +
                "    where k.subs_bdate <= $S{deal_date}  " +
                "      and k.end_date >= $S{deal_date} " +
                "      and k.prod_status in ('04','06','07') " +
                "      and k.mother_fund_flag in ('0','1') " +/*取母产品和普通产品*/
                "      and not exists (select 1 from mid_prod_core_info where prod_cd=k.prod_code) order by k.subs_bdate";
        List<SqlRow> prdList = comnDao.findRows(sql,params);
        for (SqlRow prdInfo : prdList) {
            params.put("prod_code",prdInfo.getString("prod_code"));
            params.put("pbc_regcode",prdInfo.getString("pbc_regcode"));
            params.put("subs_bdate",prdInfo.getString("subs_bdate"));//根据认购起始日判断产品年份
            params.put("end_date",prdInfo.getString("end_date"));//产品原始到期日
            params.put("isu_org_cd",prdInfo.getString("isu_org_cd"));
            formPrdPBCInfo(params);
        }
    }

    /**
     *生成产品人行代码
     * @param params
     * @throws Exception
     */
    private void formPrdPBCInfo(Map<String,Object> params) throws Exception{
        String addsql="insert into mid_prod_core_info(prod_cd, pbc_regcode, ori_end_date) values ($S{prod_code},$S{pbc_regcode}, $S{end_date}) ";
        //String updsql="update dwd_prd_prd_bas_inf set pbc_cd = $S{pbc_regcode} where prod_cd= $S{prod_code} ";
        String tablename="pbc_cd_"+params.get("subs_bdate").toString().substring(0,4);
        String prdSeq = SequenceUtil.getSequence(tablename,5);
        int check_dight=0;
        String pbc_cd=params.get("isu_org_cd").toString()+"1"+params.get("subs_bdate").toString().substring(2,4)+prdSeq;
        for (int i=pbc_cd.length()-1;i>=0;i--){
            String num = pbc_cd.substring(i,i+1);
            if(i==0){
                switch (num.toUpperCase()){
                    case "A": num="1"; break;
                    case "B": num="2"; break;
                    case "C": num="3"; break;
                    case "D": num="4"; break;
                    case "E": num="5"; break;
                    case "F": num="6"; break;
                    case "G": num="7"; break;
                    case "H": num="8"; break;
                    case "I": num="9"; break;
                    case "J": num="10"; break;
                    case "K": num="11"; break;
                    case "L": num="12"; break;
                    case "M": num="13"; break;
                    case "N": num="14"; break;
                    case "O": num="15"; break;
                    case "P": num="16"; break;
                    case "Q": num="17"; break;
                    case "R": num="18"; break;
                    case "S": num="19"; break;
                    case "T": num="20"; break;
                    case "U": num="21"; break;
                    case "V": num="22"; break;
                    case "W": num="23"; break;
                    case "X": num="24"; break;
                    case "Y": num="25"; break;
                    default:num="26" ;break;
                }
            }
            if(i%2==1){
                num = Integer.parseInt(num)*2+"";
                if(num.length()>1){
                    check_dight = check_dight+Integer.parseInt(num.substring(0,1))+Integer.parseInt(num.substring(1,2));
                }else{
                    check_dight=check_dight+Integer.parseInt(num);
                }
            }else{
                if(num.length()>1){
                    check_dight = check_dight+Integer.parseInt(num.substring(0,1))+Integer.parseInt(num.substring(1,2));
                }else{
                    check_dight=check_dight+Integer.parseInt(num);
                }
            }
        }
        if(check_dight<=100){
            if(check_dight%10==0){
                check_dight=0;
            }else{
                check_dight =10-check_dight%10;
            }
        }else{
            if(check_dight%100==0){
                check_dight=0;
            }else{
                check_dight =10-check_dight%100;
            }
        }
        pbc_cd = pbc_cd+check_dight;
        params.put("pbc_regcode",pbc_cd);
        comnDao.update(addsql,params);
        //comnDao.update(updsql,params);
    }

}
