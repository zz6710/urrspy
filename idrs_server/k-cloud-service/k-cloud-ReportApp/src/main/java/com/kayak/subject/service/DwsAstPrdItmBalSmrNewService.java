package com.kayak.subject.service;

import com.alibaba.excel.EasyExcel;
import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlRow;
import com.kayak.rpt.rhzg.listener.ExcelImportListener;
import com.kayak.rpt.rhzg.service.ExcelImportService;
import com.kayak.subject.dao.DwdPrdPrdBasInfDao;
import com.kayak.subject.dao.DwsAstPrdItmBalSmrDao;
import com.kayak.subject.model.DwsAstPrdItmBalSmr;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class DwsAstPrdItmBalSmrNewService implements ExcelImportService<DwsAstPrdItmBalSmr> {

	@Autowired
	private DwsAstPrdItmBalSmrDao dwsAstPrdItmBalSmrDao;
	@Autowired
	private DwdPrdPrdBasInfDao dwdPrdPrdBasInfDao;

	public String importDwsAstPrdItmBalSmr(MultipartFile file, Map<String, Object> params) throws Exception {
		long startTime = System.currentTimeMillis();
		log.info("导入月度资产负债信息中间表【{}】开始",file.getOriginalFilename());
		final DwsAstPrdItmBalSmrNewService dwsAstPrdItmBalSmrNewService = this;
		ExcelImportListener<DwsAstPrdItmBalSmr> excelImportListener = new ExcelImportListener<DwsAstPrdItmBalSmr>(params) {
			@Override
			protected ExcelImportService<DwsAstPrdItmBalSmr> getImportService() {
				return dwsAstPrdItmBalSmrNewService;
			}
		};
		try {
			EasyExcel.read(file.getInputStream())
					.head(DwsAstPrdItmBalSmr.class)
					.registerReadListener(excelImportListener)
					.sheet()
					.doRead();
		} catch (Exception e) {
			throw new Exception(excelImportListener.getStopMsg());
		}
		log.info("导入月度资产负债信息中间表【{}】结束，耗时：{} ms", file.getOriginalFilename(), System.currentTimeMillis() - startTime);
		return excelImportListener.getStopMsg();
	}

    @Autowired
    private ComnDao comnDao;

    @Override
    public void importFile(List<DwsAstPrdItmBalSmr> list, Map map) throws Exception {
        long startTime = System.currentTimeMillis();
		String batchSql = "REPLACE INTO dws_ast_prd_itm_bal_smr (prod_cd,prod_intr_cd,ccy_cd,ctg_cd,amt_bal,act_dt) VALUES(?,?,?,?,?,?)";
		if (list == null || list.isEmpty()) {
			throw new Exception("没有数据");
		}
        comnDao.doTrans(() -> {
            Connection connection = comnDao.getConnection();
            PreparedStatement ps  = connection.prepareStatement(batchSql);
            try {
				// 查询数据库当前业务日期的数据
				List<SqlRow> sqlRows = dwsAstPrdItmBalSmrDao.findDwsAstPrdItmBalSmrs(map);

				for (SqlRow sqlRow : sqlRows) {
					String prod_cd = sqlRow.getString("prod_cd");
					String ccy_cd = sqlRow.getString("ccy_cd");
					String ctg_cd = sqlRow.getString("ctg_cd");
					String amt_bal = sqlRow.getString("amt_bal");

					for (DwsAstPrdItmBalSmr dwsAstPrdItmBalSmr : list) {
						if (StringUtils.isNotEmpty(prod_cd) && prod_cd.equals(dwsAstPrdItmBalSmr.getProdCd()) &&
								StringUtils.isNotEmpty(ccy_cd) && ccy_cd.equals(dwsAstPrdItmBalSmr.getCcyCd()) &&
								StringUtils.isNotEmpty(ctg_cd) && ctg_cd.equals(dwsAstPrdItmBalSmr.getCtgCd())) {
							//金额相加
							String amt_bal1 = dwsAstPrdItmBalSmr.getAmtBal();
							amt_bal = StringUtils.isNotEmpty(amt_bal) ? amt_bal : "0";
							amt_bal1 = StringUtils.isNotEmpty(amt_bal1) ? amt_bal1 : "0";

							BigDecimal bigDecimal = new BigDecimal(amt_bal);
							BigDecimal bigDecimal1 = new BigDecimal(amt_bal1);
							dwsAstPrdItmBalSmr.setAmtBal(String.valueOf(bigDecimal.add(bigDecimal1)));
						}
					}
				}
				// 将金额汇总后的数据插入到数据库中
                for (DwsAstPrdItmBalSmr info : list) {
					// TODO 待优化
					String prdcCdPbc = dwdPrdPrdBasInfDao.findPrdcCdPbc(info.getProdIntrCd());
					info.setProdCd(prdcCdPbc);
                    ps.setString(1, info.getProdCd());
                    ps.setString(2, info.getProdIntrCd());
                    ps.setString(3, info.getCcyCd());
                    ps.setString(4, info.getCtgCd());
                    ps.setString(5, info.getAmtBal());
                    ps.setString(6, (String) map.get("dealDate"));
                    ps.addBatch();
                }
                ps.executeBatch();
                log.info(" ##### 批量入库{}耗时: {} ms", list.size(), System.currentTimeMillis() - startTime);
            } catch (Exception e) {
                log.error("导入报表指标科目映射异常!", e);
                throw new Exception(e.getMessage());
            } finally {
                ps.close();
            }
        });
    }

}
