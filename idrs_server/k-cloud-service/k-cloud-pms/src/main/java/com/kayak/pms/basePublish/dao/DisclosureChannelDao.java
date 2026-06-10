package com.kayak.pms.basePublish.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.pms.basePublish.model.DisclosureChannel;
import com.kayak.pms.global.constants.IsDocking;
import com.kayak.pms.global.constants.XpStatus;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


/**
 * com.kayak.pms.basePublish.dao
 * 渠道服务
 */
@Repository
public class DisclosureChannelDao extends ComnDao {
    public SqlResult<DisclosureChannel> findDisclosureChannel(SqlParam<DisclosureChannel> params) throws Exception {
        return super.findRows("SELECT ID,CHANNEL_NAME,HOST_IP,IS_DOCKING,DOCKING_WAY,USER_NAME, concat(left(PASSWORD,3),'****',right(PASSWORD,2)) PASSWORD,STATUS,PROTOCOL,PORT_CODE,FILE_PATH,REMARK FROM idb_disclosure_channel where 1= 1 order by id desc\n",
                DataSourceProperty.IDB, params);
    }

    public int insertDisclosureChannel(DisclosureChannel disclosureChannel) throws Exception {
        return super.update("insert into idb_disclosure_channel (ID,CHANNEL_NAME,IS_DOCKING,DOCKING_WAY,HOST_IP,PROTOCOL,PORT_CODE,USER_NAME,PASSWORD,STATUS,FILE_PATH,REMARK,CRT_DATE,CRT_TIME,CRT_USER_ID,CRT_USER_NAME) values ($AUTOIDS{idb_disclosure_channel},$S{channelName},$S{isDocking}," +
                "$S{dockingWay},$S{hostIp},$S{protocol},$S{portCode},$S{userName},$S{password},$S{status},$S{filePath},$S{remark},$S{crtDate},$S{crtTime},$S{crtUserId},$S{crtUserName})",
                DataSourceProperty.IDB,disclosureChannel).getEffect();
    }

    public Integer findExistsByChannelNameAndId(SqlParam<DisclosureChannel> param) throws Exception {
        String sql = "select count(*) count from idb_disclosure_channel where  1=1 ";
        if (StringUtils.isNotEmpty(param.getModel().getId())) {
            sql = sql  + " and ID != $S{id}";
        }
        if (StringUtils.isNotEmpty(param.getModel().getChannelName())) {
            sql = sql + " and CHANNEL_NAME =$S{channelName}";
        }
        return super.findRow(sql,
                DataSourceProperty.IDB,param.getModel()).getInteger("count");

    }

    /*页面信披渠道多选框查询*/
    public SqlResult<DisclosureChannel> findDisChannel(SqlParam<DisclosureChannel> param) throws Exception {
        String sql = "select id,channel_name from idb_disclosure_channel where status='1'";
        return super.findRows(sql,
                DataSourceProperty.IDB,param);
    }
    /*页面信披渠道多选框查询*/
    public SqlResult<DisclosureChannel> findDisChannelDetail(SqlParam<DisclosureChannel> param) throws Exception {
        String sql = "select id,channel_name from idb_disclosure_channel ";
        return super.findRows(sql,
                DataSourceProperty.IDB,param);
    }
    //重复信披名称校验
    public String findDuplicateDisChannelName(DisclosureChannel disclosureChannel) throws Exception {
        String sql = "select channel_name from idb_disclosure_channel where 1=1 and id=$S{id} ";
        return super.findRow(sql,
                DataSourceProperty.IDB,disclosureChannel).getString("channel_name");
    }

    public int updateDisclosureChannel(DisclosureChannel disclosureChannel) throws Exception {
        String sql = "update idb_disclosure_channel set" +
                " CHANNEL_NAME = $S{channelName}," +
                "IS_DOCKING = $S{isDocking}," +
                "DOCKING_WAY = $S{dockingWay}," +
                "HOST_IP = $S{hostIp}," +
                "PROTOCOL = $S{protocol}," +
                "PORT_CODE = $S{portCode}," +
                "USER_NAME = $S{userName}," ;
         if(!StringUtils.equals(disclosureChannel.getInitPassword(),disclosureChannel.getPassword())){
             sql += "PASSWORD = $S{password},";
         }
             sql += "FILE_PATH = $S{filePath}," +
                "REMARK = $S{remark}," +
                "UPD_USER_NAME=$S{updUserName}," +
                "UPD_USER_ID=$S{updUserId}," +
                "UPD_TIME=$S{updTime}," +
                "UPD_DATE=$S{updDate}" +
                "where ID = $S{id}";
        return super.update(sql,
                DataSourceProperty.IDB,disclosureChannel).getEffect();
    }

    public int deleteDisclosureChannelById(DisclosureChannel model) throws Exception {
        String sql = "delete from idb_disclosure_channel where ID = $S{id}";
        return super.update(sql,
                DataSourceProperty.IDB,model).getEffect();
    }



    public int stopChannel(DisclosureChannel model) throws Exception {
        String sql = "update idb_disclosure_channel set status = '0' where ID = $S{id}";
        return super.update(sql,
                DataSourceProperty.IDB,model).getEffect();
    }

    public int enableChannel(DisclosureChannel model) throws Exception {
        String sql = "update idb_disclosure_channel set status = '1' where ID = $S{id}";
        return super.update(sql,
                DataSourceProperty.IDB,model).getEffect();
    }

    /**
    * @功能描述:i==0 ->查询启用状态的渠道数量
    * @params:[params, i]
    * @return:com.kayak.core.sql.SqlRow
    * @Athor:ouyifan
    * @date:2022/6/20
    */
    public Integer checkChannel( Map<String, Object> params, int i) throws Exception {
        String sql = "SELECT COUNT(*) con FROM idb_disclosure_channel_rule WHERE FIND_IN_SET($S{id},channel_ids)";
        //判断是删除方法进来的还是停用方法进来的
        if (i == 0){
            //停用方法需要查询状态启用，如果是启用则不能停用
            sql = sql + " and status = '1'";
        }
        return super.findRow(sql,
                DataSourceProperty.IDB,params).getInteger("con");
    }
    /**
    * @功能描述:校验渠道在产品信披规则中是否引用
    * @params:[params]
    * @return:java.lang.Integer
    * @Athor:ouyifan
    * @date:2022/9/9
    */
    public Integer checkChannelInRule( Map<String, Object> params) throws Exception {
        String sql = "SELECT COUNT(*) con FROM idb_disclosure_prod_rule WHERE FIND_IN_SET($S{id},channel_ids)";
        return super.findRow(sql,
                DataSourceProperty.IDB,params).getInteger("con");
    }

    public List<DisclosureChannel> findChannelsByIds(String channelIds) throws Exception {
        return super.findRows(DisclosureChannel.class, "select *  from idb_disclosure_channel  where id in ("+channelIds+")",
                DataSourceProperty.IDB, null);
    }

    public Integer findIsDocking(String channelIds) throws Exception {
        return super.findRow( "select count(*) count from idb_disclosure_channel  where id in ("+channelIds+") and IS_DOCKING ='"+ IsDocking.yes.getItemKey() +"' AND status = '"+ XpStatus.start.getItemKey() +"'",
                DataSourceProperty.IDB, null).getInteger("count");
    }

    public SqlResult<DisclosureChannel> findDisclosureChannelById(SqlParam<DisclosureChannel> params) throws Exception {
        return super.findRows("SELECT ID,CHANNEL_NAME,HOST_IP,IS_DOCKING,DOCKING_WAY,USER_NAME,concat(left(PASSWORD,3),'****',right(PASSWORD,2)) PASSWORD,STATUS,PROTOCOL,PORT_CODE,FILE_PATH,REMARK FROM idb_disclosure_channel where id='"+params.getModel().getId()+"'",
                DataSourceProperty.IDB,params);
    }

}
