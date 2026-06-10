package com.kayak.rpt.rhzg.service;


import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.base.dao.ComnDao;

import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.util.DateUtil;
import com.kayak.rpt.rhzg.dao.ZG05Dao;
import com.kayak.rpt.rhzg.model.ZG05;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@APIDefine(desc = "资管产品资产负债信息服务", model = ZG05.class)
public class ZG05Service implements ExcelImportService<ZG05> {

    private static final Logger log = LoggerFactory.getLogger(ZG05Service.class);

    @Autowired
    private ZG05Dao zG05Dao;

    @Autowired
    private ComnDao comnDao;


    @API(desc = "查询资管产品资产负债信息", auth = APIAuth.YES)
    public SqlResult<ZG05> findZG05s(SqlParam<ZG05> params) throws Exception {
        //params.setMakeSql(true);
        return zG05Dao.findZG05s(params);
    }


    @API(desc = "修改资管产品资产负债信息",params = "id,prod_cd,theory_report_start_date,trans_ccy,data_typ,a0000,a1000,a2000,a2100,a2110,a2120,a2200,a2210,a2220,a3000,a3100,a3200,a4000,a4100,a4200,a4900,a4910,a4a00,a4400,a4500,a4600,a4700,a4800,a5000,a5100,a5200,a5210,a5220,a5230,a5240,a5250,a5260,a5261,a5270,a5271,a5272,a5273,a5274,a5275,a5276,a5277,a5278,a5279,a527a,a5280,ad000,ad100,ad110,ad120,ad130,ad140,ad150,ad160,ad170,ad200,a7000,a7100,a7110,a7120,a7200,a7210,a7220,a7230,a7240,a7250,a7260,a7270,a7280,a7290,a72a0,a7300,a7310,a7320,a7400,a8000,a8100,a8200,a9000,aa000,ab000,ac000,d0000,b0000,b1000,b1100,b1200,b1210,b1220,b1230,b1240,b1250,b1260,b1261,b1270,b1271,b1272,b1273,b1274,b1275,b1276,b1277,b1278,b1279,b127a,b1280,b2000,b3000,b3100,b3200,b4000,b5000,c0000,c1000,c1100,c1110,c1120,c1130,c1140,c1150,c1160,c1170,c1171,c1172,c1173,c1174,c1175,c1176,c1177,c1178,c1179,c117a,c1180,c1200,c1210,c1220,c1230,c1240,c1250,c1260,c1270,c1271,c1272,c1273,c1274,c1275,c1276,c1277,c1278,c1279,c127a,c1280,c3000,c4000" , auth = APIAuth.NO)
    public int updateZG05(SqlParam<ZG05> params) throws Exception {
        return zG05Dao.updateZG05(params).getEffect();
    }


    @API(desc = "删除资管产品资产负债信息", params = "id", auth = APIAuth.NO)
    public int deleteZG05(SqlParam<ZG05> params) throws Exception {
        return zG05Dao.deleteZG05(params).getEffect();
    }




    public void importFile(List<ZG05> zg05s, Map map) throws Exception {
        long startTime = System.currentTimeMillis();

        String batchSql = "INSERT INTO app_pbc_report_zg05(prod_cd,report_date,register_status,trans_ccy,data_typ,a0000,a1000,a2000,a2100,a2110,a2120,a2200,a2210,a2220,a3000,a3100,a3200,a4000,a4100,a4200,a4900,a4910,a4a00,a4400,a4500,a4600,a4700,a4800,a5000,a5100,a5200,a5210,a5220,a5230,a5240,a5250,a5260,a5261,a5270,a5271,a5272,a5273,a5274,a5275,a5276,a5277,a5278,a5279,a527a,a5280,ad000,ad100,ad110,ad120,ad130,ad140,ad150,ad160,ad170,ad200,a7000,a7100,a7110,a7120,a7200,a7210,a7220,a7230,a7240,a7250,a7260,a7270,a7280,a7290,a72a0,a7300,a7310,a7320,a7400,a8000,a8100,a8200,a9000,aa000,ab000,ac000,d0000,b0000,b1000,b1100,b1200,b1210,b1220,b1230,b1240,b1250,b1260,b1261,b1270,b1271,b1272,b1273,b1274,b1275,b1276,b1277,b1278,b1279,b127a,b1280,b2000,b3000,b3100,b3200,b4000,b5000,c0000,c1000,c1100,c1110,c1120,c1130,c1140,c1150,c1160,c1170,c1171,c1172,c1173,c1174,c1175,c1176,c1177,c1178,c1179,c117a,c1180,c1200,c1210,c1220,c1230,c1240,c1250,c1260,c1270,c1271,c1272,c1273,c1274,c1275,c1276,c1277,c1278,c1279,c127a,c1280,c3000,c4000,sys_data_status,sys_data_source,sys_data_version,register_serno,imp_date,theory_report_start_date) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        comnDao.doTrans(() -> {
            Connection connection = comnDao.getConnection();
            PreparedStatement ps  = connection.prepareStatement(batchSql);
            try {

                for (ZG05 info : zg05s){

                    ps.setString(1,info.getProdCd());
                    ps.setString(2,map.get("beginDate").toString());
                    ps.setString(3,"0");
                    ps.setString(4,info.getTransCcy());
                    ps.setString(5,info.getDataTyp()==null ? null : info.getDataTyp().split(":")[0]);
                    ps.setString(6,info.getA0000());
                    ps.setString(7,info.getA1000());
                    ps.setString(8,info.getA2000());
                    ps.setString(9,info.getA2100());
                    ps.setString(10,info.getA2110());
                    ps.setString(11,info.getA2120());
                    ps.setString(12,info.getA2200());
                    ps.setString(13,info.getA2210());
                    ps.setString(14,info.getA2220());
                    ps.setString(15,info.getA3000());
                    ps.setString(16,info.getA3100());
                    ps.setString(17,info.getA3200());
                    ps.setString(18,info.getA4000());
                    ps.setString(19,info.getA4100());
                    ps.setString(20,info.getA4200());
                    ps.setString(21,info.getA4900());
                    ps.setString(22,info.getA4910());
                    ps.setString(23,info.getA4a00());
                    ps.setString(24,info.getA4400());
                    ps.setString(25,info.getA4500());
                    ps.setString(26,info.getA4600());
                    ps.setString(27,info.getA4700());
                    ps.setString(28,info.getA4800());
                    ps.setString(29,info.getA5000());
                    ps.setString(30,info.getA5100());
                    ps.setString(31,info.getA5200());
                    ps.setString(32,info.getA5210());
                    ps.setString(33,info.getA5220());
                    ps.setString(34,info.getA5230());
                    ps.setString(35,info.getA5240());
                    ps.setString(36,info.getA5250());
                    ps.setString(37,info.getA5260());
                    ps.setString(38,info.getA5261());
                    ps.setString(39,info.getA5270());
                    ps.setString(40,info.getA5271());
                    ps.setString(41,info.getA5272());
                    ps.setString(42,info.getA5273());
                    ps.setString(43,info.getA5274());
                    ps.setString(44,info.getA5275());
                    ps.setString(45,info.getA5276());
                    ps.setString(46,info.getA5277());
                    ps.setString(47,info.getA5278());
                    ps.setString(48,info.getA5279());
                    ps.setString(49,info.getA527a());
                    ps.setString(50,info.getA5280());
                    ps.setString(51,info.getAd000());
                    ps.setString(52,info.getAd100());
                    ps.setString(53,info.getAd110());
                    ps.setString(54,info.getAd120());
                    ps.setString(55,info.getAd130());
                    ps.setString(56,info.getAd140());
                    ps.setString(57,info.getAd150());
                    ps.setString(58,info.getAd160());
                    ps.setString(59,info.getAd170());
                    ps.setString(60,info.getAd200());
                    ps.setString(61,info.getA7000());
                    ps.setString(62,info.getA7100());
                    ps.setString(63,info.getA7110());
                    ps.setString(64,info.getA7120());
                    ps.setString(65,info.getA7200());
                    ps.setString(66,info.getA7210());
                    ps.setString(67,info.getA7220());
                    ps.setString(68,info.getA7230());
                    ps.setString(69,info.getA7240());
                    ps.setString(70,info.getA7250());
                    ps.setString(71,info.getA7260());
                    ps.setString(72,info.getA7270());
                    ps.setString(73,info.getA7280());
                    ps.setString(74,info.getA7290());
                    ps.setString(75,info.getA72a0());
                    ps.setString(76,info.getA7300());
                    ps.setString(77,info.getA7310());
                    ps.setString(78,info.getA7320());
                    ps.setString(79,info.getA7400());
                    ps.setString(80,info.getA8000());
                    ps.setString(81,info.getA8100());
                    ps.setString(82,info.getA8200());
                    ps.setString(83,info.getA9000());
                    ps.setString(84,info.getAa000());
                    ps.setString(85,info.getAb000());
                    ps.setString(86,info.getAc000());
                    ps.setString(87,info.getD0000());
                    ps.setString(88,info.getB0000());
                    ps.setString(89,info.getB1000());
                    ps.setString(90,info.getB1100());
                    ps.setString(91,info.getB1200());
                    ps.setString(92,info.getB1210());
                    ps.setString(93,info.getB1220());
                    ps.setString(94,info.getB1230());
                    ps.setString(95,info.getB1240());
                    ps.setString(96,info.getB1250());
                    ps.setString(97,info.getB1260());
                    ps.setString(98,info.getB1261());
                    ps.setString(99,info.getB1270());
                    ps.setString(100,info.getB1271());
                    ps.setString(101,info.getB1272());
                    ps.setString(102,info.getB1273());
                    ps.setString(103,info.getB1274());
                    ps.setString(104,info.getB1275());
                    ps.setString(105,info.getB1276());
                    ps.setString(106,info.getB1277());
                    ps.setString(107,info.getB1278());
                    ps.setString(108,info.getB1279());
                    ps.setString(109,info.getB127a());
                    ps.setString(110,info.getB1280());
                    ps.setString(111,info.getB2000());
                    ps.setString(112,info.getB3000());
                    ps.setString(113,info.getB3100());
                    ps.setString(114,info.getB3200());
                    ps.setString(115,info.getB4000());
                    ps.setString(116,info.getB5000());
                    ps.setString(117,info.getC0000());
                    ps.setString(118,info.getC1000());
                    ps.setString(119,info.getC1100());
                    ps.setString(120,info.getC1110());
                    ps.setString(121,info.getC1120());
                    ps.setString(122,info.getC1130());
                    ps.setString(123,info.getC1140());
                    ps.setString(124,info.getC1150());
                    ps.setString(125,info.getC1160());
                    ps.setString(126,info.getC1170());
                    ps.setString(127,info.getC1171());
                    ps.setString(128,info.getC1172());
                    ps.setString(129,info.getC1173());
                    ps.setString(130,info.getC1174());
                    ps.setString(131,info.getC1175());
                    ps.setString(132,info.getC1176());
                    ps.setString(133,info.getC1177());
                    ps.setString(134,info.getC1178());
                    ps.setString(135,info.getC1179());
                    ps.setString(136,info.getC117a());
                    ps.setString(137,info.getC1180());
                    ps.setString(138,info.getC1200());
                    ps.setString(139,info.getC1210());
                    ps.setString(140,info.getC1220());
                    ps.setString(141,info.getC1230());
                    ps.setString(142,info.getC1240());
                    ps.setString(143,info.getC1250());
                    ps.setString(144,info.getC1260());
                    ps.setString(145,info.getC1270());
                    ps.setString(146,info.getC1271());
                    ps.setString(147,info.getC1272());
                    ps.setString(148,info.getC1273());
                    ps.setString(149,info.getC1274());
                    ps.setString(150,info.getC1275());
                    ps.setString(151,info.getC1276());
                    ps.setString(152,info.getC1277());
                    ps.setString(153,info.getC1278());
                    ps.setString(154,info.getC1279());
                    ps.setString(155,info.getC127a());
                    ps.setString(156,info.getC1280());
                    ps.setString(157,info.getC3000());
                    ps.setString(158,info.getC4000());
                    ps.setString(159,"1");
                    ps.setString(160, "2");//sys_data_source 2
                    ps.setString(161, "1.0");//sys_data_version
                    ps.setString(162, UUID.randomUUID().toString());//register_serno 2
                    ps.setString(163, DateUtil.getNowDate());// imp_date
                    ps.setString(164,map.get("beginDate").toString());
                    ps.addBatch();

                }
                ps.executeBatch();

                log.info(" ##### 批量入库{}耗时: {} ms", zg05s.size(),System.currentTimeMillis() - startTime);
            }catch (Exception e) {
                log.error("导入资管产品资产负债信息异常!", e);
                throw new Exception(e.getMessage());
            } finally{
                ps.close();

            }
        });

    }


    public void deleteZg05ByDate(Object params) throws Exception{
        try {
            zG05Dao.deleteZg05ByDate(params);
        } catch (Exception e) {
            throw e;
        }
    }



}
