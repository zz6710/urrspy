package com.kayak.dps.dws.service;

import com.alibaba.excel.EasyExcel;
import com.kayak.base.dao.ComnDao;
import com.kayak.dps.dws.dao.ConcentrationCustDao;
import com.kayak.dps.dws.model.ConcentrationCust;
import com.kayak.dps.pub.ICallback;
import com.kayak.dps.py.excel.ExcelImportListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 客户集中度 service
 * @author lc-renxw
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ConcentrationCustService {

    private static final String[] sheets = new String[] {"0601","0602","0603","0604","0605","0606","0607"};

    private final ConcentrationCustDao concentrationCustDao;

    private final ComnDao comnDao;


    /**
     *  客户集中度排序导入
     * @author lc-renxw
     * @param file 文件
     * @param acdDt 数据日期
     */
    public void uploadData(MultipartFile file, String acdDt) throws Exception {
        comnDao.doTrans(()->{
            deleteConcentration(acdDt);
            for (String sheet : sheets) {
                EasyExcel.read(file.getInputStream(),
                                ConcentrationCust.class,new ExcelImportListener<ConcentrationCust>((ICallback<List<ConcentrationCust>>) cacheList ->{
                                    this.saveConcentration(cacheList,acdDt,sheet);
                                }))
                        .sheet(sheet).doRead();
            }
        });
    }

    /**
     * @methodName deleteConcentration
     * @description 删除对应的数据
     * @author lc-renxw
     * @param actDt 日期
     * @return void
     */
    private void deleteConcentration(String actDt) throws Exception {
        String sql = "delete from dws_zy_concentration_cust where ACT_DT = '" + actDt + "'";
        comnDao.update(sql);
    }
    /**
     * 保存客户集中度排序
     * @author lc-renxw
     * @param cacheList 客户集中度排序list
     */
    private void saveConcentration(List<ConcentrationCust> cacheList,String acdDt,String sType) throws Exception {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String dealDate = LocalDate.now().format(formatter);
        String sql = "insert into dws_zy_concentration_cust(XH,CUST_NAME,S_TYPE,ACT_DT,DEAL_DATE) values(?,?,'" + sType + "','" + acdDt + "','" + dealDate + "')";
        Connection connection = comnDao.getConnection();
        PreparedStatement ps  = connection.prepareStatement(sql);
        try {
            for (ConcentrationCust concentrationCust : cacheList) {
                if (StringUtils.isNumeric(concentrationCust.getXhStr())) {
                    ps.setInt(1,Integer.parseInt(concentrationCust.getXhStr()));
                    ps.setString(2,concentrationCust.getCustName());
                    ps.addBatch();
                }
            }
            ps.executeBatch();
        } catch (Exception e) {
            log.error("导入资管产品存续期募集信息异常!", e);
            throw new Exception(e.getMessage());
        } finally{
            ps.close();
        }
    }
}
