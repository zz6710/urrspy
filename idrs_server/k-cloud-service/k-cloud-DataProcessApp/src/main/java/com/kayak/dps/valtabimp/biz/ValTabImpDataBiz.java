package com.kayak.dps.valtabimp.biz;

import cn.hutool.core.collection.CollectionUtil;
import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlRow;
import com.kayak.dps.valtabimp.Params_value;
import com.kayak.dps.valtabimp.Share_Expr;
import com.kayak.dps.valtabimp.dbfUtils.DbfFileUtils;
import com.kayak.dps.valtabimp.model.Base_fa_reporttab_parset;
import com.kayak.dps.valtabimp.repository.ValTabImpDataDao;
import com.kayak.dps.valtabimp.excel.ExcelParse;
import com.linuxense.javadbf.DBFReader;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.sql.SQLException;
import java.util.*;

@Slf4j
@Service(value = "valTabImpDataBiz")
public class ValTabImpDataBiz {

	@Autowired
	private ValTabImpDataDao valTabImpDataDao;

	@Autowired
	private ComnDao comnDao;

	/**
	 * 插入解析估值表参数
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public boolean addvalparamset(Map<String, Object> params ) throws Exception {
		boolean reslut=true;
		
		String t8_val_reporttab_id= params.get("t8_val_reporttab_id").toString();//估值表主体idparam_type
		Object[] param_type_array=(Object[])params.get("param_type");//估值参数
		Object[] param_code_array=(Object[])params.get("param_code");//估值参数
		Object[] param_name_array=(Object[])params.get("param_name");//估值参数
		Object[] param_data_type_array=(Object[])params.get("param_data_type");//估值参数
		Object[] param_value_array=(Object[])params.get("param_value");//估值参数
		Object[] param_condition_array=(Object[])params.get("param_condition");//估值参数
		Object[] order_num_array=(Object[])params.get("order_num");//估值参数
		Object[] note_array=(Object[])params.get("note");//估值参数
		
		List<Map<String, Object>> saveobj_ls=new ArrayList<>();
		for (int i=0;i<param_type_array.length;i++) {
			Map<String,Object> saveobj=new HashMap<>();
			saveobj.put("t8_val_reporttab_id", t8_val_reporttab_id);
			saveobj.put("param_code", param_code_array[i].toString());
			saveobj.put("param_type", param_type_array[i].toString());
			saveobj.put("param_name", param_name_array[i].toString());
			saveobj.put("param_data_type", param_data_type_array[i].toString());
			saveobj.put("param_value", param_value_array[i].toString());
			saveobj.put("param_condition", param_condition_array[i].toString());
			saveobj.put("order_num", order_num_array[i].toString());
			saveobj.put("note", note_array[i].toString());
			saveobj_ls.add(saveobj);
		}

		valTabImpDataDao.addvalparamset(saveobj_ls,t8_val_reporttab_id);//新增参数异常
		
		
		return reslut; 
	}
	
	
    
    /**
     * 解析估值表
     * @param files 估值表
     * @throws Exception 
     * @throws SQLException
     */
    public Map<String, String> parseAnalysisGzData(MultipartFile files, String asset_code,String tghType,String isprodorasset) throws Exception {

//    	20210517 先注释掉 微服务验证是否跑批不一样
//		//查看是否正在跑批中
//		DataArchiveService das = new DataArchiveService();
//		String isarchiveblean = das.getisarchiveb();//查询是否处于跑批汇整状态中
//		//如果在跑批中中断估值表导入
//		if ("true".equals(isarchiveblean)) {
//			return;
//		}
		Map<String, String> return_params = new HashMap<>();

    	if(isornoGX(asset_code)) {
    		log.error("请设置产品的关联主体");
    		return null;
    	}
  	
    	HashMap<String, Object> val_cal_params = new HashMap<>();//默认中文名key
    	HashMap<String, Object> val_cal_params_filecode = new HashMap<>();//英文名为key
    	String datenow = "";//导入时间

    	//Process
    	Map<String, List<Base_fa_reporttab_parset>> valparasm = init_val_reporttabparset(asset_code, tghType,isprodorasset);

    	//装入文件对象，
    	String filename = files.getOriginalFilename();
    	val_cal_params.put("file_name", filename);//装入文件名称
    	val_cal_params.put("file_inputstream", InputSttoByte(files.getInputStream()));//装入文件的流
    	val_cal_params.put("type_file", tghType);//装入文件类型对象
		
		//1类型全局参数
		List<Base_fa_reporttab_parset> lsvalsets = valparasm.get("paramskey_1");//1：系统全局参数配置

    	if(lsvalsets==null) {
    		throw new Exception("估值表未找到对应的解析配置，请检查！");
    	}

		for(Base_fa_reporttab_parset ob:lsvalsets) {
			Object sObject = getvalueformula(ob, val_cal_params);
			val_cal_params_filecode.put(ob.param_code, sObject);
			val_cal_params.put(ob.param_name, sObject);
		}
		//获取数据落地表字段集合
		Set<String> set_reportfile = initLossFiledS("ods_fa_readassets");
		
		String headerNumber;//解析Excel文件标题头部占行数
		int rowStart;
		//设置的度需要-1，从报表上看到的行号使用的时候需要减1,读取dbf类型文件时无用
		headerNumber = val_cal_params_filecode.get("headerNumber")==null?"0":val_cal_params_filecode.get("headerNumber").toString();
		rowStart = val_cal_params_filecode.get("rowStart")==null?0:Integer.parseInt(val_cal_params_filecode.get("rowStart").toString());
		
		//2类型数据筛选参数
		List<Base_fa_reporttab_parset> lsvalsets_2 = valparasm.get("paramskey_2");//2：数据筛选条件
		
		if(lsvalsets!=null) {
			for(Base_fa_reporttab_parset ob:lsvalsets_2) {
				Object sObject=getvalueformula(ob, val_cal_params);
				val_cal_params_filecode.put(ob.param_code, sObject);
				val_cal_params.put(ob.param_name, sObject);
			}
		}
		//读取所有DBF行数据后，传入该行选择条件对数据进行筛选依据
		String select_connion = val_cal_params_filecode.get("selelct_connion")==null?null:val_cal_params_filecode.get("selelct_connion").toString();
		
		datenow = val_cal_params_filecode.get("change_date")==null?datenow:val_cal_params_filecode.get("change_date").toString();

		//3类型数据装载配置
		List<Base_fa_reporttab_parset> lsvalsets_3 = valparasm.get("paramskey_3");//1：系统全局参数配置

		//放入产品和时间
    	val_cal_params.put("asset_code", asset_code);
    	val_cal_params.put("isprodorasset", isprodorasset);
    	val_cal_params.put("change_date", datenow);
		
		List<Map<String, Object>> insert_list=new ArrayList<>();//插入对象集
		try {
			List<Map<String, Object>> data = null;
			if("XLS".equals(tghType)){//03版excel表格API
				data = ExcelParse.readExcelData2003(files.getInputStream(), 0, headerNumber, rowStart, true);
			} else if ("DBF".equals(tghType)){//DBF文件
				//File fFile = DbfFileUtils.multipartFileTransferToFile(files);
				data = DbfFileUtils.readDbfFileValData(files.getInputStream(), true, val_cal_params);
			} else {//07版excel表格API
				data = ExcelParse.readExcelData(files.getInputStream(), 0, headerNumber, rowStart, true);
			}

			insert_list = cal_jsgs(val_cal_params, set_reportfile, select_connion, lsvalsets_3, data);

			//添加插入语句
			valTabImpDataDao.addvaldata(insert_list, asset_code, datenow,isprodorasset);//新增参数异常

		} catch (Exception e) {
			log.error("", e);
			throw new Exception(e);
		}

		return_params.put("settle_date", datenow);//估值日期
		return return_params;
	}

    
    /**
     * 解析估值表的函数
     * @param filename 文件名
     * @param tghType 文件类型
     * @param inputstream 文件流
     * @throws Exception
     */
	public void parseAnalysisGzDataByAuto(String filename, String tghType,InputStream inputstream) throws Exception {
		HashMap<String, Object> val_cal_params=new HashMap<>();//默认中文名key
    	HashMap<String, Object> val_cal_params_filecode=new HashMap<>();//英文名为key

    	String datenow="20201225";//先默认导入时间取值
    	
    	//Process
    	Map<String, List<Base_fa_reporttab_parset>> valparasm=init_val_reporttabparsetbyFlName(filename, tghType);
    	
    	//转换成字节数组使用时候在转换成流
    	byte[] dataBytes=InputSttoByte(inputstream);
    	//装入文件对象，
//    	val_cal_params.put("xls_file", file);//装入文件对象
    	val_cal_params.put("file_name", filename);//装入文件对象
    	val_cal_params.put("file_inputstream", dataBytes);//装入文件的流字节
    	val_cal_params.put("type_file", tghType);//装入文件类型对象

		
		//1类型全局参数
		List<Base_fa_reporttab_parset> lsvalsets=valparasm.get("paramskey_1");//1：系统全局参数配置
		
    	
    	if(lsvalsets==null) {
    		throw new Exception("估值表未找到对应的解析配置，请检查！");
    	}
		
		
		for(Base_fa_reporttab_parset ob:lsvalsets) {
			Object sObject=getvalueformula(ob, val_cal_params);
			val_cal_params_filecode.put(ob.param_code, sObject);
			val_cal_params.put(ob.param_name, sObject);
		}
		//初始化表字段---此表为落地数据表
		Set<String> set_reportfile=initLossFiledS("ods_fa_readassets");
		
		String headerNumber;
		int rowStart;
		//设置的度需要-1，从报表上看到的行号使用的时候需要减1
		headerNumber=val_cal_params_filecode.get("headerNumber")==null?"0":val_cal_params_filecode.get("headerNumber").toString();
		rowStart=val_cal_params_filecode.get("rowStart")==null?0:Integer.parseInt(val_cal_params_filecode.get("rowStart").toString());
		
		//2类型数据筛选参数
		List<Base_fa_reporttab_parset> lsvalsets_2=valparasm.get("paramskey_2");//2：数据筛选条件
		
		if(lsvalsets!=null) {
			for(Base_fa_reporttab_parset ob:lsvalsets_2) {
				Object sObject=getvalueformula(ob, val_cal_params);
				val_cal_params_filecode.put(ob.param_code, sObject);
				val_cal_params.put(ob.param_name, sObject);
			}
		}
		
		String select_connion=val_cal_params_filecode.get("selelct_connion")==null?null:val_cal_params_filecode.get("selelct_connion").toString();
		
		datenow=val_cal_params_filecode.get("change_date")==null?datenow:val_cal_params_filecode.get("change_date").toString();
		
		
		//3类型数据装载配置
		List<Base_fa_reporttab_parset> lsvalsets_3=valparasm.get("paramskey_3");//1：系统全局参数配置

		//放入产品和时间
//    	val_cal_params.put("t8_prod_base_id", t8_prod_base_id);
    	val_cal_params.put("change_date", datenow);
		
		List<Map<String, Object>> insert_list=new ArrayList<>();//插入对象集

            try {
            	List<Map<String, Object>> data = null;
            	InputStream inputstream_new = new ByteArrayInputStream(dataBytes);//转换字节成文件流
            	if("XLS".equals(tghType)){
            		data = ExcelParse.readExcelData2003(inputstream_new, 0, headerNumber, rowStart, true);
            	}else{
            		data = ExcelParse.readExcelData(inputstream_new, 0, headerNumber, rowStart, true);
            	}

            	insert_list=cal_jsgs(val_cal_params, set_reportfile, select_connion, lsvalsets_3, data);

                //添加插入语句
				valTabImpDataDao.addvaldataByauto(insert_list, filename, datenow,"1");;//新增参数异常
            	
            		
            } catch (Exception e) {
                log.error("", e);
                throw new Exception(e);
            }
	}
	
	/**
	 * 工作流转换成字节数组，
	 * @return
	 * @throws IOException 
	 */
	private byte[] InputSttoByte(InputStream inputstream) throws IOException {
		
    	//文件流只能被读取一次所以需要转换byte用的时候在转换成字节数组
    	ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = inputstream.read(buffer)) > -1 ) {
            baos.write(buffer, 0, len);
        }
        baos.flush();
        byte[] inputstreamBytes = baos.toByteArray();//转换成字节数组
        
        return inputstreamBytes;
	}
    
    
    /**
     * 计算公式处理
     * @param val_cal_params 计算参数值
     * @param set_reportfile 需要插入表的字段列表字段code
     * @param select_connion 数据条件筛选
     * @param lsvalsets_3 计算公式参数设置(全局参数)
     * @param data 文件解析数据
     * @return
     * @throws Exception
     */
	private List<Map<String, Object>>  cal_jsgs(HashMap<String, Object> val_cal_params, Set<String> set_reportfile, String select_connion,
			List<Base_fa_reporttab_parset> lsvalsets_3, List<Map<String, Object>> data) throws Exception {
		
		
		List<String> expNameList =new ArrayList<String>();//需要计算装值的字段
		//没有通过条件时候不走此流程
		Map<String,List<Base_fa_reporttab_parset>> expressionMapt = this.SiftExpression(lsvalsets_3, expNameList);//公式分组和计算字段的收集
		//此表没地方调用插入和查询   axin20220718
//		String sqldel="delete from ods_fa_readassets_merge_ftab where 1=1 and t8_prod_base_id = $S{t8_prod_base_id} and change_date=$S{change_date} ";
//		comnDao.update(sqldel, val_cal_params);
		
		HashMap<String, Object> cal_params;
		HashMap<String, Object> val_cal_params_old = new HashMap<>();//上级集合;
		
		List<Map<String, Object>> insert_list=new ArrayList<>();//插入集合
		for(Map<String, Object> mapob: data ) {/** 遍历读取dbf文件每行数据 */
			
				cal_params=new HashMap<>();
				cal_params.putAll(val_cal_params);//装入传入产品和时间
				cal_params.putAll(mapob);//装入解析行数据
//				val_cal_params.putAll(mapob);//装入集合
				
		    	//加入新集合里面，获取上级数据
		    	this.getval_cal_params_old(val_cal_params_old, expNameList,cal_params,mapob.keySet());
				
		    	if(select_connion!=null) {/** 判断每行数据是否符合行筛选条件，不符合条件的数据放入上级数据map中 */
		    		if(!this.checkCondition(select_connion,cal_params,null)) {//如果条件不满足返回false情况下此数据直接跳出,进入下一条数据  特殊情况下科目代码为“”不受条件限制
		    			
						val_cal_params_old.clear();//清理上级对象
						val_cal_params_old.putAll(cal_params);//保留本次处理对象为上级的集合数据
		    			continue;
		    		}
		    	}

				for (String key : expNameList) {//进行公式计算
					
					
					
					List<Base_fa_reporttab_parset> expressionList=expressionMapt.get(key);
					Base_fa_reporttab_parset setob=this.SiftExpression(expressionList, cal_params, null);
					if(setob==null) {//跳出
						log.info("-------字段："+key+"未取到公式");
						cal_params.put(key,"");
						
						continue;
					}
					this.getvalueformula(setob, cal_params);

				}
				
				val_cal_params_old.clear();//清理上级对象
				val_cal_params_old.putAll(cal_params);//保留本次处理对象为上级的集合数据
				
				
				Map<String, Object> obj_insert=backValue(cal_params, set_reportfile, Params_value.code_name_map);
				insert_list.add(obj_insert);
			}
		
		return insert_list;
	}
	
	
	private void clpoc(HashMap<String, Object> val_cal_params) throws Exception {
		
		HashMap<String, Object> params=new HashMap<>();

		if(val_cal_params.get("科目代码").toString().contains("基金单位净值")) {
			
			params.put("ftool_code", val_cal_params.get("科目代码").toString().replaceAll(":", ""));
			params.put("t8_prod_base_id", val_cal_params.get("t8_prod_base_id"));
			params.put("change_date", val_cal_params.get("change_date"));
			params.put("ss_value", val_cal_params.get("科目名称").toString());
			
			String sqladd=" insert into ods_fa_readassets_merge_ftab(id,ftool_code,t8_prod_base_id,change_date,ss_value) values( $AUTOID{ods_fa_readassets_merge_ftab},$S{ftool_code},$S{t8_prod_base_id},$S{change_date},$S{ss_value}) ";
					
			comnDao.update(sqladd, params);

		}
		
		
	}
    
    /**
     * 加工上级的map集合，放入新集合里面
     * @param val_cal_params_old
     * @param expNameList  公式配置里面的字段名称
     * @param val_cal_params 
     * @param filename 文档里面标题名称
     * @return
     */
    private Map<String,Object> getval_cal_params_old(HashMap<String, Object> val_cal_params_old,List<String> expNameList,HashMap<String, Object> val_cal_params,Set<String> filename){
    	
    	
    	for (String key : expNameList) {
    		val_cal_params.put("上级"+key, val_cal_params_old.get(key)==null?"":val_cal_params_old.get(key));//保证取值不能是null
    	}
    	
    	for (String key : filename) {
    		val_cal_params.put("上级"+key, val_cal_params_old.get(key)==null?"":val_cal_params_old.get(key));//保证取值不能是null
    	}
    	
    	
    	return val_cal_params;
    }
    
    public boolean isornoGX(String asset_code) {
    	boolean bl=true;
    	Map<String, Object> params=new HashMap<>();
    	params.put("asset_code", asset_code);
    	String queryParamSetsql="select 1 from ods_fa_readassets_prodreportrel where asset_code = $S{asset_code}" ;
    	List<SqlRow> sqlRows;
    	try {
			sqlRows = comnDao.findRows(queryParamSetsql, params);
			if (CollectionUtil.isNotEmpty(sqlRows)) {
				bl=false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("-----初始化化估值表解析参数异常"+e.getLocalizedMessage() );
		}
		return bl;
    }
    
    
    /**
     * 
     * @param asset_code
     * @param tghType
     * @return
     */
    private Map<String, List<Base_fa_reporttab_parset>> init_val_reporttabparset(String asset_code,String tghType,String isprodorasset) {
    	Map<String, String> code_name=new HashMap<>();
    	Map<String, List<Base_fa_reporttab_parset>> valparasm=new HashMap<>();
    	String Paramskey ="paramskey_";//参数类型:1-系统全局参数配置/2-数据筛选类型/3-资产分类取值/4-最后取数计算
    	//参数的初始化留存起来
    	String queryParamSetsql=" select t.* from base_fa_reporttab_parset t left join ods_fa_readassets_prodreportrel t2 on t.t8_val_reporttab_id=t2.t8_val_reporttab_id where 1=1"
    			+ " and t2.asset_code = $S{asset_code} and t2.isprodorasset = $S{isprodorasset} order by t.param_type,t.order_num " ;
    	Map<String, Object> params=new HashMap<>();
    	params.put("asset_code", asset_code);
    	params.put("isprodorasset", isprodorasset);//估值表类型
		List<SqlRow> sqlRows;
		try {
			sqlRows = comnDao.findRows(queryParamSetsql, params);
			for (SqlRow sr : sqlRows) {
				Base_fa_reporttab_parset obReporttabparset=new Base_fa_reporttab_parset();
				obReporttabparset.id=sr.getInteger("id");
				obReporttabparset.t8_val_reporttab_id=sr.getInteger("t8_val_reporttab_id");
				obReporttabparset.param_type=sr.getString("param_type");
				obReporttabparset.param_code=sr.getString("param_code");
				obReporttabparset.param_name=sr.getString("param_name");
				obReporttabparset.param_data_type=sr.getString("param_data_type");
				obReporttabparset.param_value=sr.getString("param_value");
				obReporttabparset.param_condition=sr.getString("param_condition");
				List<Base_fa_reporttab_parset> param_ls = valparasm.get(Paramskey+obReporttabparset.param_type);
				if(param_ls==null) {
					param_ls=new ArrayList<>();
					valparasm.put(Paramskey+obReporttabparset.param_type, param_ls);
				}
				param_ls.add(obReporttabparset);
				code_name.put(obReporttabparset.param_code.toLowerCase(), obReporttabparset.param_name);
			}
			
			Params_value.code_name_map.putAll(code_name);//缓存配置
		} catch (Exception e) {
			log.error("-----初始化化估值表解析参数异常"+e.getMessage(),e );
		} 
		return valparasm;
	}
    
    
    
    
    /**
     * 根据文件名称查找对应的配置信息
     * @param tghType
     * @return
     */
    private Map<String, List<Base_fa_reporttab_parset>> init_val_reporttabparsetbyFlName(String filename,String tghType) {
    	
    	String repid="";
    	if(filename.indexOf("乐惠-双季赢")>=0 || filename.indexOf("乐惠天天盈")>=0 ) {//以名字确定解析配置id
    		repid="12";
    	}else{
    		repid="11";
    	}
    	
    	
    	Map<String, String> code_name=new HashMap<>();
    	Map<String, List<Base_fa_reporttab_parset>> valparasm=new HashMap<>();
    	String Paramskey ="paramskey_";
    	//参数的初始化留存起来
    	String queryParamSetsql=" select t.* from Base_fa_reporttab_parset t where 1=1"
    			+ " and t.t8_val_reporttab_id="+repid
    			+ "  order by  t.param_type,t.order_num " ;
    	Map<String, Object> params=new HashMap<>();
//    	params.put("t8_prod_base_id", t8_prod_base_id);
		List<SqlRow> sqlRows;
		try {
			sqlRows = comnDao.findRows(queryParamSetsql, params);
			for (SqlRow sr : sqlRows) {
				Base_fa_reporttab_parset obReporttabparset=new Base_fa_reporttab_parset();
				obReporttabparset.id=sr.getInteger("id");
				obReporttabparset.t8_val_reporttab_id=sr.getInteger("t8_val_reporttab_id");
				obReporttabparset.param_type=sr.getString("param_type");
				obReporttabparset.param_code=sr.getString("param_code");
				obReporttabparset.param_name=sr.getString("param_name");
				obReporttabparset.param_data_type=sr.getString("param_data_type");
				obReporttabparset.param_value=sr.getString("param_value");
				obReporttabparset.param_condition=sr.getString("param_condition");
				List<Base_fa_reporttab_parset> param_ls=valparasm.get(Paramskey+obReporttabparset.param_type);
				if(param_ls==null) {
					param_ls=new ArrayList<>();
					valparasm.put(Paramskey+obReporttabparset.param_type, param_ls);
				}
				param_ls.add(obReporttabparset);
				code_name.put(obReporttabparset.param_code.toLowerCase(), obReporttabparset.param_name);
			}
			
			Params_value.code_name_map.putAll(code_name);//缓存配置
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error("-----初始化化估值表解析参数异常"+e.getLocalizedMessage() );
		} 
		return valparasm;
	}
    
    /**
     * 解析返回值
     * @param reporttabparset
     * @param val_cal_params
     * @return
     */
    private Object getvalueformula(Base_fa_reporttab_parset reporttabparset,HashMap<String, Object> val_cal_params) {
    	
    	Object value = null;
//    	DataParam.share.key = reporttabparset.getParam_name();
    	Share_Expr share_Expr = Params_value.getShare();
    	share_Expr.key = reporttabparset.getParam_name();
    	
    	if(reporttabparset.param_data_type.compareTo("3")<0) {
			if(reporttabparset.param_data_type.compareTo("1")==0) {//INt数字
				value=Integer.parseInt(reporttabparset.param_value);
			}else if(reporttabparset.param_data_type.compareTo("2")==0) {//字符
				value=reporttabparset.param_value.toString();
			}
		}else if(reporttabparset.param_data_type.compareTo("5")==0) {//double数字
			value=new BigDecimal(reporttabparset.param_value);//金额字段转换
		}else if(reporttabparset.param_data_type.compareTo("6")==0){//如果为三点定位字段
			/**
			 * 该类EXCEL解析情形:第一位数值为sheet页，第二位为字段行数，第三位为字段列数
			 * 该类DBF解析情形:第一位数值无意义，第二位无意义，第三位为字段列数
			 * */
			String tghType=val_cal_params.get("type_file").toString();

			byte[] dataBytes = (byte[])val_cal_params.get("file_inputstream");//取字节
			InputStream inputstream = new ByteArrayInputStream(dataBytes);

			String dw = reporttabparset.param_value.toString();
			String[] dwStrem=dw.split(",");

			try {
				if("XLS".equals(tghType)){
					HSSFWorkbook workbook;
					workbook = new HSSFWorkbook(inputstream);
					// 获得第一个工作表对象
					Sheet sheet = workbook.getSheetAt(Integer.parseInt(dwStrem[0]));
					// 得到第一列第一行的单元格
					Row header = sheet.getRow(Integer.parseInt(dwStrem[1]));
					Cell cl=header.getCell(Integer.parseInt(dwStrem[2]));
					DataFormatter dataFormatter = new DataFormatter();
					value = dataFormatter.formatCellValue(cl);
				} else if ("DBF".equals(tghType)) {
					DBFReader reader = new DBFReader(inputstream, Charset.forName("GBK"));
					Object[] row = reader.nextRecord();
					value = DbfFileUtils.dateToStrFormat((Date)row[Integer.parseInt(dwStrem[2])]);//将获取的Date估值日期转换成yyyyMMdd字符
				} else {
					Workbook workbook2007;
					workbook2007= WorkbookFactory.create(inputstream);
					// 获得第一个工作表对象
					Sheet sheet = workbook2007.getSheetAt(Integer.parseInt(dwStrem[0]));
					// 得到第一列第一行的单元格
					Row header = sheet.getRow(Integer.parseInt(dwStrem[1]));
					Cell cl=header.getCell(Integer.parseInt(dwStrem[2]));
					DataFormatter dataFormatter = new DataFormatter();
					value = dataFormatter.formatCellValue(cl);
				}
			} catch (Exception e) {
				e.printStackTrace();
				log.error("--------------------三点定位字段异常检查配置：字段 "+reporttabparset.getParam_name()+":"+e.getLocalizedMessage());
			}
		}else if(reporttabparset.param_data_type.compareTo("3")==0){//公式取值
				Share_Expr.TResult result = share_Expr.CalcExprWithLog(val_cal_params, reporttabparset.param_value);//此处计算金额
				if(result.isOK){
					if (result.Kind == Share_Expr.TEvResultType.resDouble) {
						value = (new BigDecimal(result.Value.toString()));
					} else if (result.Kind == Share_Expr.TEvResultType.resInt) {
						value = Integer.parseInt(result.Value.toString());
					} else {
						value = result.Value;
					}
				}else {
					if(result.isException){
						log.error("----------公式计算返回值异常:"+result.CalcLogStr +"----:"+reporttabparset.getParam_name());
					}
				}
		}else if( reporttabparset.param_data_type.compareTo("4")==0) {//字符串拼接
    		String result=reporttabparset.param_value;
			for(String key:val_cal_params.keySet()){
				String pvalue = toString().valueOf(val_cal_params.get(key));
				result=result.replace(key,pvalue);
			}
			value=result;
		}

    	val_cal_params.put(reporttabparset.getParam_name(), value);
    	return value;
    }
    
    

	/***
	 * 将公式按照公式名分组
	 *
	 * @param expressionList
	 *            公式集
	 * @param expNameList
	 *            公式名集合
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static Map<String,List<Base_fa_reporttab_parset>> SiftExpression(List<Base_fa_reporttab_parset> expressionList,
			List<String> expNameList) {
		Map expressionNames = new HashMap();
		for (Base_fa_reporttab_parset expression : expressionList) {
			String fieldname = expression.param_name;
			List<Base_fa_reporttab_parset> expressions = new ArrayList<Base_fa_reporttab_parset>();
			if (expressionNames.containsKey(fieldname)) {
				expressions = (List<Base_fa_reporttab_parset>) expressionNames.get(fieldname);
			} else {
				expNameList.add(fieldname);
			}
			expressions.add(expression);
			expressionNames.put(fieldname, expressions);
		}

		return expressionNames;
	}
	

	/***
	 * 过滤计算公式---进行筛选出一个满足的配置
	 *
	 * @param expressionList
	 * @param
	 * @param computerCtx
	 * @param computMsg
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	private Base_fa_reporttab_parset SiftExpression(
			List<Base_fa_reporttab_parset> expressionList, HashMap computerCtx,
			StringBuffer computMsg) throws Exception {
		
//		int oldFiellevel = 0;
		Base_fa_reporttab_parset selectExpression = null;
		for (Base_fa_reporttab_parset expression : expressionList) {
			String condition = expression.param_condition;
			log.info("--------------------参数值："+expression.param_condition);
			if (this.checkCondition(condition, computerCtx, computMsg) == false) {
//				this.setComputMsg("      结果:false\r\n", computMsg);
				continue;
			}else if(!condition.trim().equals("")){
//				this.setComputMsg("      结果:true\r\n", computMsg);
			}
			
			selectExpression=expression;//满足条件的公式

		}

		return selectExpression;
	}
    
	
	/***
	 * 判断计算条件
	 *
	 * @param condition
	 * @param
	 * @param computerCtx
	 * @param computMsg
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	private boolean checkCondition(String condition, HashMap computerCtx, StringBuffer computMsg) throws Exception {
		if ("".equals(condition) || condition == null || " ".equals(condition)) {
			return true;
		} else {
			if (computMsg != null) {
//				this.setComputMsg("      判断条件:" + condition, computMsg);
			}

			Share_Expr.TResult result = new Share_Expr.TResult();
			result = Params_value.getShare().CalcExprWithLog(computerCtx, condition);


			if (result.isException) {
//				setExcMessage("判断计算条件时异常：公式配置错误或数据收集不完全，请优先检查公式配置."+"("+condition+")");
				throw new Exception("判断计算条件时异常：公式配置错误或数据收集不完全，请优先检查公式配置."+"("+condition+")");
			}
			if (!result.isOK) {
//				setExcMessage("判断计算条件出错：\r\n" + result.CalcLogStr + "--" + result.Value);
				throw new Exception("判断计算条件【" +condition+"】出错：\r\n" + result.CalcLogStr + "--" + result.Value);
			}

//			log.info("计算条件：" + condition + "返回结果" + result.Value);
			if ("TRUE".equals(result.Value.toUpperCase())) {
				return true;
			}
		}
		return false;
	}
	
	
	/**
	 * 映射传入的损益表结构
	 * @param tablename
	 * @return
	 * @throws SQLException 
	 * @throws  
	 */
	public Set<String> initLossFiledS(String tablename){
		
		Set<String> set = new HashSet<String>();
		
		//mysql查询
		//String sql =" show  full  columns  from  "+tablename.toLowerCase() ;
		String sql ="  select COLUMN_NAME from information_schema.`COLUMNS` where table_name = '"+tablename.toLowerCase() + "'";

			List<SqlRow> sqlRows;
			try {
				sqlRows = comnDao.findRows(sql);
				for (SqlRow sr : sqlRows) {
					set.add(sr.getString("COLUMN_NAME").toUpperCase());
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				log.error("---------映射表结构异常：表名"+tablename+":"+e.getLocalizedMessage());
			} 
		return set;
	}
	
	
    /***
     * 回写数据
     *
     * @param computerCtx
     * @return
     */
    @SuppressWarnings("rawtypes")
    private Map<String, Object> backValue(HashMap computerCtx, Set<String> lossfiled,Map<String,String> configFields) throws Exception {

        Map<String, Object> resultloss = new HashMap<>();

        Iterator<String> iter = lossfiled.iterator();
        while (iter.hasNext()) {
            String fieldname = iter.next().toLowerCase();

            if (configFields.containsKey(fieldname)) {
            	String parameter  = configFields.get(fieldname);//取对应的中文名
                Object value = "";
                if (parameter != null) {
                    value = computerCtx.get(parameter);
                }
                
                resultloss.put(fieldname,value);//全部默认放置中文字符
                
            }else {
            	 resultloss.put(fieldname,"");//如果没有值则默认放空
            }
        }


        return resultloss;
    }

}



