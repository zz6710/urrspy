package com.kayak.dps.dws.service;

import com.alibaba.excel.EasyExcel;
import com.kayak.base.dao.ComnDao;
import com.kayak.dps.dws.model.DwsZyShcommonCust;
import com.kayak.dps.pub.ICallback;
import com.kayak.dps.py.excel.ExcelImportListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @className: DwsZyShcommonCustService
 * @description: 上海国际集团共同客户名录  dws 表 action
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DwsZyShcommonCustService {

    private final ComnDao comnDao;

    /**
     * @methodName uploadData
     * @description 上传并保存数据
     * @param file 文件
     * @return void
     */
    public void uploadData(MultipartFile file) throws Exception {
        comnDao.doTrans(()->{
            EasyExcel.read(file.getInputStream(),
                            DwsZyShcommonCust.class,new ExcelImportListener<DwsZyShcommonCust>((ICallback<List<DwsZyShcommonCust>>) this::saveShcommonCust))
                    .sheet().doRead();
        });
    }

    /**
     * @methodName saveDwsZyShcommonCust
     * @description 保存数据
     * @author lc-renxw
     * @date 2024/11/5 10:02
     * @param datas dataList
     * @return void
     */
    private void saveShcommonCust(List<DwsZyShcommonCust> datas) throws Exception {
        String dealDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String sql = "INSERT INTO dws_zy_shcommon_cust  " +
                "(CUST_NAME, REGISTERNUMBER, S_INFO_ORG_CODE, S_INFO_OTH_CODE, S_INFO_OTH_TYPE, CUST_NUMBER, NE_IND_CODE, NE_IND_TYPE, S_RELEVANCE, ACT_DT, DEAL_DATE)" +
                "VALUES" +
                "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?," + dealDate +")";
        Set<String> deleteDates = datas.stream().map(DwsZyShcommonCust::getActDt).collect(Collectors.toSet());
        for (String deleteDate : deleteDates) {
            String delSql = "delete from dws_zy_shcommon_cust where ACT_DT = " + deleteDate;
            comnDao.update(delSql);
        }
        Connection connection = comnDao.getConnection();
        PreparedStatement ps = connection.prepareStatement(sql);
        try{
            for (DwsZyShcommonCust data : datas) {
                ps.setString(1,data.getCustName());
                ps.setString(2,data.getRegisternumber());
                ps.setString(3,data.getInfoOrgCode());
                ps.setString(4,data.getInfoOthCode());
                ps.setString(5,data.getInfoOthType());
                ps.setString(6,data.getCustNumber());
                ps.setString(7,data.getNeIndCode());
                ps.setString(8,data.getNeIndType());
                ps.setString(9,data.getRelevance());
                // String neIndCode = data.getNeIndCode();
                // String neIndCodeInsertTarget = "";
                // if (StringUtils.isNotEmpty(neIndCode) && StringUtils.contains(neIndCode,".")) {
                //     neIndCodeInsertTarget = neIndCode.split("\\.")[0];
                // }
                // ps.setString(7,neIndCodeInsertTarget);
                // String neIndType = data.getNeIndType();
                // String neIndTypeInsertTarget = "";
                // if (StringUtils.isNotEmpty(neIndType) && StringUtils.contains(neIndType,"-")) {
                //     neIndTypeInsertTarget = neIndType.split("-")[0];
                // }
                // ps.setString(8,neIndTypeInsertTarget);
                // String sRelevance = data.getRelevance();
                // String sRelevanceInsertTarget = "";
                // if (StringUtils.isNotEmpty(sRelevance) && StringUtils.contains(sRelevance,"-")) {
                //     sRelevanceInsertTarget = sRelevance.split("-")[0];
                // }
                // ps.setString(9,sRelevanceInsertTarget);
                ps.setString(10,data.getActDt());
                ps.addBatch();
            }
            ps.executeBatch();
        } catch (Exception e) {
            log.error("导入上海国际集团共同客户名单异常!", e);
            throw new Exception(e.getMessage());
        } finally {
            ps.close();
        }
    }
}
