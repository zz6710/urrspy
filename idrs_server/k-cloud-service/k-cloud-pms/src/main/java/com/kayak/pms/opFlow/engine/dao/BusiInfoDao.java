package com.kayak.pms.opFlow.engine.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.pms.opFlow.engine.entity.BusiInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 业务审批表 Dao
 * @author  xiamh
 * @date    2020-01-25
 */
@Repository
public class BusiInfoDao extends ComnDao {

    /**
     * 查询所有流程
     * @param queryCriteria
     * @return
     */
    public List<BusiInfo> list(Map<String, Object> queryCriteria) {
        return null;
    }

    /**
     * 保存
     * @param busiInfo
     * @return
     */
    public int save(@Param("vo")BusiInfo busiInfo) {
        return 0;
    }

    /**
     * 根据id查询
     * @param busiId
     * @return
     */
//    @Select("SELECT * FROM wf_busi_info WHERE busi_id = #{busiId}")
    public BusiInfo get(@Param("busiId")String busiId) {
        return null;
    }


    /**
     * 修改审批状态
     * @param busiId
     * @param processInstanceId
     * @param processStatus
     * @param updateDate
     * @param updateTime
     * @return
     */
//    @Update("UPDATE wf_busi_info " +
//            " SET process_status = #{processStatus}, " +
//            "  process_instance_id = #{processInstanceId}," +
//            "  update_date = #{updateDate}, " +
//            "  update_time = #{updateTime} " +
//            "  WHERE busi_id = #{busiId}")
    public int updateProcessStatus(String busiId, String processInstanceId, String processStatus, String updateDate, String updateTime) {
        return 0;
    }


    /**
     * 根据processInstanceId修改审批状态
     * @param processInstanceId
     * @param processStatus
     * @param updateDate
     * @param updateTime
     * @return
     */
//    @Update("UPDATE wf_busi_info " +
//            "    SET process_status = #{processStatus}, " +
//            "    update_date = #{updateDate}, " +
//            "    update_time = #{updateTime} " +
//            "    WHERE process_instance_id = #{processInstanceId} ")
    public int updateProcessStatusByInstanceId(String processInstanceId, String processStatus, String updateDate, String updateTime) {
        return 0;
    }

    /**
     * 删除业务审批表
     * @param busiId
     * @return
     */
//    @Delete("DELETE FROM wf_busi_info WHERE busi_id = #{busiId}")
    public int delete(String busiId) {
        return 0;
    }

    /**
     * 删除业务审批表（根据process_instance_id）
     * @param processInstanceId
     * @return
     */
//    @Delete("DELETE FROM wf_busi_info WHERE process_instance_id = #{processInstanceId}")
    public int deleteByInstanceId(@Param("processInstanceId")String processInstanceId) {
        return 0;
    }

    /**
     * 查询当前流程+unkey是否有未完成的审批
     * @param processId
     * @param values
     * @return
     */
//    @Select("SELECT * FROM wf_busi_info " +
//            " WHERE process_id = #{processId} " +
//            " AND `values` = #{values} AND NOT" +
//            " ( " +
//            "   (process_status = '" + ProcessInstanceStatus.FINISH + "' AND bus_status = '" + BusinessStatus.FINISH + "')" +
//            "   OR process_status = '" + ProcessInstanceStatus.REFUSE + "'" +
//            "   OR (process_status = '" + ProcessInstanceStatus.FINISH + "' AND bus_status = '" + BusinessStatus.ERROR_CONFIRMED + "')" +
//            " ) ORDER BY start_date,start_time ")
    public List<BusiInfo> findNotFinishData(String processId, String values) {
        return null;
    }

    /**
     * 查询待回调流程（完成审批，未发起流程）
     * @return
     */
//    @Select("SELECT * FROM wf_busi_info WHERE process_status='"+ ProcessInstanceStatus.FINISH +"' AND bus_status = '"+ BusinessStatus.READY +"' ORDER BY update_date DESC, update_time DESC")
    public List<BusiInfo> findCallData() {
        return null;
    }

    /**
     * 业务状态更新为处理中
     * @param processInstanceId
     * @param updateDate
     * @param updateTime
     * @return
     */
//    @Update("UPDATE wf_busi_info SET bus_status='" + BusinessStatus.PROCESSING + "',update_date = #{updateDate},update_time = #{updateTime} WHERE process_instance_id=#{processInstanceId} AND bus_status in ('" + BusinessStatus.READY +"','" + BusinessStatus.ERROR + "')")
    public int updateBusiToProcessingIfReady(@Param("processInstanceId")String processInstanceId, @Param("updateDate") String updateDate, @Param("updateTime")String updateTime) {
        return 0;
    }


    /**
     * 更新业务状态和返回值
     * @param map
     * @return
     */
    public int updateBusStatusByProcessId(Map<String,Object> map) {
        return 0;
    }

}
