package com.kayak.pms.T82.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.aspect.annotations.APIOperation;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.system.RequestSupport;
import com.kayak.graphql.model.FetcherData;
import com.kayak.pms.T82.dao.T8DictDao;
import com.kayak.pms.T82.model.T8Dict;
import com.kayak.pms.global.constants.ProdProfitType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@APIDefine(desc = "常用字典服务", model = T8Dict.class)
public class T8DictService {

	@Autowired
	private T8DictDao t8DictDao;

	@API(desc = "查询字典", auth = APIAuth.NO, operation = APIOperation.SELECT)
	public SqlResult<T8Dict> findTaProdInfos(SqlParam<T8Dict> params) throws Exception {
		params.setMakeSql(true);
		return t8DictDao.findAllTaProdInfos(params);
	}

	@API(desc = "查询启用的接口名称作为字典", auth = APIAuth.NO, operation = APIOperation.SELECT)
	public SqlResult<T8Dict> findPortInfos(SqlParam<T8Dict> params) throws Exception {
		params.setMakeSql(true);
		return t8DictDao.findPortInfos(params);
	}

	@API(desc = "查询所有用户字典", auth = APIAuth.NO, operation = APIOperation.SELECT)
	public SqlResult<T8Dict> findAllUserInfos(SqlParam<T8Dict> params) throws Exception {
		return t8DictDao.findAllUserInfos(params);
	}

	@API(desc = "根据code查询所有产品代码字典", auth = APIAuth.NO)
	public String findAllTaProdInfosByCode(SqlParam<T8Dict> params) throws Exception {
		String prodeName = t8DictDao.findAllTaProdInfosByCode(params.getModel().getProdCode());
		Map<String,Object> map=new HashMap<>();
		map.put("prodeName",prodeName);
		return RequestSupport.updateReturnJson(true, "", map).toString();
	}

	@API(desc = "查询产品排期产品代码字典", auth = APIAuth.NO)
	public SqlResult<T8Dict> findScheduleProdInfos(SqlParam<T8Dict> params) throws Exception {
		params.setMakeSql(true);
		return t8DictDao.findScheduleProdInfos(params);
	}


	/**
	 * 功能：查询创设会可选择产品  请勿随意修改
	 * 作者：rennannan
	 * 日期：20210409
	 *
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@API(desc = "查询创设会可选择产品", auth = APIAuth.NO)
    public SqlResult<T8Dict> findMeetProds(SqlParam<T8Dict> params) throws Exception {
        params.setMakeSql(false);
        return t8DictDao.findMeetProds(params);
    }

	@API(desc = "查询所有开放式产品代码", auth = APIAuth.NO)
	public SqlResult<T8Dict> findTaOpenProdInfos(SqlParam<T8Dict> params) throws Exception {
		return t8DictDao.findTaOpenProdInfos(params);
	}

	@API(desc = "查询生效产品代码字典", auth = APIAuth.NO)
	public SqlResult<T8Dict> findAliveTaProdInfos(SqlParam<T8Dict> params) throws Exception {
		params.setMakeSql(true);
		return t8DictDao.findTaProdInfos(params);
	}

	@API(desc = "查询产品代码字典", auth = APIAuth.NO)
	public SqlResult<T8Dict> findDpbProdInfos(SqlParam<T8Dict> params) throws Exception {
		params.setMakeSql(true);
		return t8DictDao.findDpbProdInfos(params);
	}

	@API(desc = "查询产品系列代码字典", auth = APIAuth.NO)
	public SqlResult<T8Dict> findDpbProdSerInfos(SqlParam<T8Dict> params) throws Exception {

		return t8DictDao.findDpbProdSerInfos(params);
	}

	@API(desc = "查询子份额产品代码字典", auth = APIAuth.NO)
	public SqlResult<T8Dict> findSonProdInfos(SqlParam<T8Dict> params) throws Exception {
		return t8DictDao.findSonProdInfos(params);
	}

	@API(desc = "查询发布渠道字典", auth = APIAuth.NO)
	public SqlResult<T8Dict> findT8ChannelInfos(SqlParam<T8Dict> params) throws Exception {
		params.setMakeSql(true);
		return t8DictDao.findT8ChannelInfos(params);
	}


	@API(desc = "查询销售商未设置产品信息的产品信息字典", auth = APIAuth.NO)
	public SqlResult<T8Dict> findTaDistributorInfoNoSetProd(SqlParam<T8Dict> params) throws Exception {
		params.setMakeSql(false);
		return t8DictDao.findTaDistributorInfoSetProd(params);
	}

	@API(desc = "查询信披渠道信息字典", auth = APIAuth.NO)
	public SqlResult<T8Dict> findDisclosureChannel(SqlParam<T8Dict> params) throws Exception {
		params.setMakeSql(false);
		return t8DictDao.findDisclosureChannel(params);
	}

	@API(desc = "查询货币产品代码字典", auth = APIAuth.NO)
	public SqlResult<T8Dict> findCurProdInfo(SqlParam<T8Dict> params) throws Exception {
		params.setMakeSql(true);

		params.getModel().setProfitType(ProdProfitType.CUR);
		return t8DictDao.findTaProdInfos(params);
	}

	@API(desc = "查询净值产品代码字典", auth = APIAuth.NO)
	public SqlResult<T8Dict> findNavProdInfo(SqlParam<T8Dict> params) throws Exception {
		//params.setMakeSql(true);
		params.getModel().setProfitType(ProdProfitType.NAV);
		return t8DictDao.findTaProdInfos(params);
	}

	@API(desc = "查询销售商信息字典", auth = APIAuth.NO)
	public SqlResult<T8Dict> findTaDistributorInfos(SqlParam<T8Dict> params) throws Exception {
		params.setMakeSql(true);
		return t8DictDao.findTaDistributorInfos(params);
	}

	@API(desc = "查询产品关联销售商信息字典", auth = APIAuth.NO)
	public SqlResult<T8Dict> findProdDistributorInfos(SqlParam<T8Dict> params) throws Exception {
		//params.setMakeSql(true);
		return t8DictDao.findProdDistributorInfos(params);
	}

	@API(desc = "查询销售商信息字典", auth = APIAuth.NO)
	public SqlResult<T8Dict> findTaProdDistributorInfos(SqlParam<T8Dict> params) throws Exception {
		params.setMakeSql(false);
		SqlResult<T8Dict> result = t8DictDao.findTaProdDistributorCode(params);
		Map<String,Object> map = new HashMap<>();
		if(result.getRows().size() > 0){
			map.put("distributorCode", result.getRows().get(0).getDistributorCode());
		}
		SqlParam<T8Dict> parm = new FetcherData(map,T8Dict.class);
		return t8DictDao.findTaProdDistributorInfos(parm);
	}

	@API(desc = "查询销售商信息字典 ", auth = APIAuth.NO)
	public SqlResult<T8Dict> findTaDisWithoutDefault(SqlParam<T8Dict> params) throws Exception {
		params.setMakeSql(true);
		return t8DictDao.findTaDisWithoutDefault(params);
	}

	@API(desc = "查询07所有产品代码字典", auth = APIAuth.NO)
	public SqlResult<T8Dict> findTaProdInfos07(SqlParam<T8Dict> params) throws Exception {
		params.setMakeSql(true);
		return t8DictDao.findTaProdInfos07(params);
	}

	@API(desc = "查询所有已成立产品代码字典", auth = APIAuth.NO)
	public SqlResult<T8Dict> findEstablishProdInfos(SqlParam<T8Dict> params) throws Exception {
		return t8DictDao.findEstablishProdInfos(params);
	}

	@API(desc = "查询所有已成立无托管邮箱产品代码字典", auth = APIAuth.NO)
	public SqlResult<T8Dict> findEstablishProdInfosForCustEmail(SqlParam<T8Dict> params) throws Exception {
		return t8DictDao.findEstablishProdInfosForCustEmail(params);
	}

	@API(desc = "查询所有未成立产品代码字典", auth = APIAuth.NO)
	public SqlResult<T8Dict> findNotEstablishProdInfos(SqlParam<T8Dict> params) throws Exception {
		return t8DictDao.findNotEstablishProdInfos(params);
	}

	@API(desc = "查询所有未成立产品代码字典", auth = APIAuth.NO)
	public SqlResult<T8Dict> findProdInfosByCustNo(SqlParam<T8Dict> params) throws Exception {
		return t8DictDao.findProdInfosByCustNo(params);
	}

	@API(desc = "查询...", auth = APIAuth.NO)
	public SqlResult<T8Dict> findDictTrutee(SqlParam<T8Dict> params) throws Exception {
		return t8DictDao.findDictTrutee(params);
	}

	@API(desc = "查询所有产品代码字典用于获取产品相应的托管协议和代销协议", auth = APIAuth.NO)
	public SqlResult<T8Dict> findDocumentInfoByProdCode(SqlParam<T8Dict> params) throws Exception {
		return t8DictDao.findDocumentInfoByProdCode(params);
	}


	@API(desc = "TempType", auth = APIAuth.NO)
	public SqlResult<Map<String, Object>>  findTempType(SqlParam<T8Dict> params) throws Exception {
		return t8DictDao.findTempType(params.getModel().getDict());
	}

	/**
	 * 功能：查询非封闭式的产品  请勿擅自改动
	 * 作者：rennannan
	 * 日期：20210309
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@API(desc = "查询非封闭式的产品", auth = APIAuth.NO,operation = APIOperation.SELECT)
	public SqlResult<T8Dict> findOpenInfos(SqlParam<T8Dict> params) throws Exception {
		return t8DictDao.findOpenInfos(params);
	}

	/**
	 * 功能：查询非封闭式的产品  请勿擅自改动
	 * 作者：rennannan
	 * 日期：20210309
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@API(desc = "查询产品系列", auth = APIAuth.NO,operation = APIOperation.SELECT)
	public SqlResult<T8Dict> findSeriesInfos(SqlParam<T8Dict> params) throws Exception {
		return t8DictDao.findSeriesInfos(params);
	}

    @API(desc = "查询产品子系列", auth = APIAuth.NO,operation = APIOperation.SELECT)
    public SqlResult<T8Dict> findSonSeriesInfos(SqlParam<T8Dict> params) throws Exception {
        return t8DictDao.findSonSeriesInfos(params);
    }

    @API(desc = "查询产品系列(创社会议)", auth = APIAuth.NO,operation = APIOperation.SELECT)
    public SqlResult<T8Dict> findSonSeriesInfos1(SqlParam<T8Dict> params) throws Exception {
        return t8DictDao.findSonSeriesInfos1(params);
    }

    @API(desc = "查询系列说明", auth = APIAuth.NO,operation = APIOperation.SELECT)
    public SqlResult<T8Dict> findSeriesExplain(SqlParam<T8Dict> params) throws Exception {
        return t8DictDao.findSeriesExplain(params);
    }

	@API(desc = "t8PrintDoc", auth = APIAuth.NO)
	public SqlResult<Map<String, Object>>  t8PrintDoc(SqlParam<T8Dict> params) throws Exception {
		return t8DictDao.t8PrintDoc();
	}
	
	
	@API(desc = "XPPrintDoc", auth = APIAuth.NO)
	public SqlResult<Map<String, Object>>  XPPrintDoc(SqlParam<T8Dict> params) throws Exception {
		return t8DictDao.XPPrintDoc();
	}

	@API(desc = "获取投资经理名字字典", auth = APIAuth.NO,operation = APIOperation.SELECT)
	public SqlResult<T8Dict> findT8InvestManagerInfos(SqlParam<T8Dict> params) throws Exception {
		return t8DictDao.findT8InvestManagerInfos(params);
	}

	@API(desc = "基准日期下拉框", auth = APIAuth.NO,operation = APIOperation.SELECT)
	public SqlResult<T8Dict> findBaseDate(SqlParam<T8Dict> params) throws Exception {
		return t8DictDao.findBaseDate(params);
	}

	@API(desc = "产品形态", auth = APIAuth.NO,operation = APIOperation.SELECT)
	public SqlResult<T8Dict> findProdMod(SqlParam<T8Dict> params) throws Exception {
		return t8DictDao.findProdMod(params);
	}

	
	@API(desc = "查询字典", auth = APIAuth.NO, operation = APIOperation.SELECT)
	public SqlResult<T8Dict> findOperatingAgency(SqlParam<T8Dict> params) throws Exception {
		params.setMakeSql(true);
		return t8DictDao.findOperatingAgency(params);
	}

    @API(desc = "查询字典", auth = APIAuth.NO, operation = APIOperation.SELECT)
    public SqlResult<T8Dict> getProdSonSeries(SqlParam<T8Dict> params) throws Exception {
        params.setMakeSql(true);
        return t8DictDao.getProdSonSeries(params);
    }
}
