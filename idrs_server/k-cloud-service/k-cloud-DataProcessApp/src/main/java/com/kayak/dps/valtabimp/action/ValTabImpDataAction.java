package com.kayak.dps.valtabimp.action;

import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.action.BaseController;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.system.RequestSupport;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.DateUtil;
import com.kayak.dps.app.service.JXSystemPushingDataGenService;
import com.kayak.dps.app.service.ValidationIndexInitiateService;
import com.kayak.dps.valtabimp.service.AutoImportService;
import com.kayak.dps.valtabimp.biz.ValTabImpDataBiz;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/valtabImpdata")
public class ValTabImpDataAction extends BaseController {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    // 招商银行：Excel2007，xlsx
    public static final String TGH_XLSX = "XLSX";
    // 兴业银行：Excel2003  ,XLS格式
    public static final String TGH_XLS = "XLS";
    //DBF格式估值文件
    public static final String TGH_DBF = "DBF";

    @Autowired
    public ComnDao comnDao;

    // 托管行类型
    private String tghType;

    @Resource(name = "valTabImpDataBiz")
    private ValTabImpDataBiz valTabImpDataBiz;

    @Resource(name = "autoImportService")
    private AutoImportService autoImportService;

    @Resource(name = "fileUploadBiz")
    private FileUploadBiz fileUploadBiz;

	@Autowired
	private JXSystemPushingDataGenService jXSystemPushingDataGenService;

	@Autowired
	private ValidationIndexInitiateService validationIndexInitiateService;

	/**
	 * 委外估值表解析入口
	 * @param request
	 * @param response
	 * @param files
	 */
    @RequestMapping(value = "/valTabImport.action", method = RequestMethod.POST)
    public void impDataToTemp(MultipartHttpServletRequest request, HttpServletResponse response,
								@RequestParam(value = "files", required = false) MultipartFile[] files) {
		response.setContentType("application/json;chartset=UTF-8");
    	String asset_code = request.getParameter("assetCode");
    	String isprodorasset = request.getParameter("isprodorasset");//估值表导入类型
        tghType = this.getTghType(files);
        	try {
        		for (MultipartFile file : files) {//前端传递文件个数
        			// 解析委外估值表
        			Map<String, String> gzParams = valTabImpDataBiz.parseAnalysisGzData(file, asset_code,tghType,isprodorasset);
                    //上传委外估值表到指定目录(接口管理估值回传目录)  --axin
                    fileUploadBiz.uploadExternalGZFile(response, file, gzParams);
                    //插入上传记录
					InsertFileRecord(file.getOriginalFilename());
        		}
				// 写入返回值
				response.getWriter().write(updateSuccess("委外估值文件解析完成"));
			} catch (Exception e) {
				log.error("委外估值文件解析异常", e);
			}
    }

    /**
     * 将导入的估值表信息新增到表中
     * @param file_name
     */
    public void InsertFileRecord(String file_name){
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("file_name",file_name);
		map.put("crtDate", DateUtil.getNowDate());
		map.put("crtTime", DateUtil.getNowTime());
        try{
            //先查询是否又同名估值表
            List<SqlRow> sqlRows = comnDao.findRows(
                    "select * from ods_fa_readassets_tab where excel_name = $S{file_name}", map
            );
            if (sqlRows.size() > 0) {
                comnDao.update("delete from ods_fa_readassets_tab where excel_name = $S{file_name}", DataSourceProperty.PUB, map);
            }

            //插入表格信息
            comnDao.update("insert into ods_fa_readassets_tab(id , excel_name,upload_date,upload_time,inputuser)" +
					"values($AUTOIDS{id},$S{file_name},$S{crtDate},$S{crtTime},$S{inputuser})", DataSourceProperty.PUB, map);

        }catch (Exception e){
            log.error("导入估值表异常" , e);
        }
    }

	/**
	 * 获取托管行类型
	 * @param files 托管行估值表
	 * @return 文件类型
	 */
    private String getTghType(MultipartFile[] files){
    	String tghType = "";
    	for (MultipartFile f : files) {
        	if (f.getOriginalFilename().endsWith(".xls") || f.getOriginalFilename().endsWith(".XLS")){
        		// 兴业银行
        		tghType = TGH_XLS;
        	} else if (f.getOriginalFilename().endsWith(".dbf") || f.getOriginalFilename().endsWith(".DBF")) {
				tghType = TGH_DBF;
        	} else {
				tghType = TGH_XLSX;
			}
        	break;
    	}
    	return tghType;
    }

    @RequestMapping("addvalparamset.json")
    public @ResponseBody String addvalparamset(){
        Map<String, Object> params = RequestSupport.getParameters();
        try{

        	log.info("-----------------"+params.toString());

        	 valTabImpDataBiz.addvalparamset(params);
            return updateSuccess();

        }catch (Exception e){
        	log.error("设置估值参数处理异常" + e.getStackTrace());
            return updateFailure(e.getMessage());
        }
    }

    /**
     * 自动导入
     * @return
     */
    @RequestMapping("autoImportac.json")
    public @ResponseBody String autoImportac(){
        Map<String, Object> params = RequestSupport.getParameters();
        try{

            log.info("-----------------"+params.toString());

            String menger=autoImportService.autoImportService();

//            prodBonusService.addNewProdBonus(params);
//            prodBonusService.addNewProdBonusJX(params);

            return updateSuccess(menger);
        }catch (Exception e){
            log.error("撤销估值调整处理异常" + e.getStackTrace());
            return updateFailure(e.getMessage());
        }
    }

	/**
	 * 查询上传文件地址文件
	 * @return
	 */
	@RequestMapping("queryfile.json")
	public @ResponseBody String queryfile(){
		Map<String, Object> params = RequestSupport.getParameters();
		try{

			log.info("-----------------"+params.toString());

			List<Map> ls=autoImportService.queryfile();

			Map returndata=new HashMap();
			returndata.put("filelist",ls);

			//            prodBonusService.addNewProdBonus(params);
			//            prodBonusService.addNewProdBonusJX(params);

			return updateSuccess("成功",returndata);
		}catch (Exception e){
			log.error("撤销估值调整处理异常" + e.getStackTrace());
			return updateFailure(e.getMessage());
		}
	}


	/**
	 * 删除文件
	 * @return
	 */
	@RequestMapping("deletefile.json")
	public @ResponseBody String deletefile(){
		Map<String, Object> params = RequestSupport.getParameters();
		try{

			log.info("-----------------"+params.toString());
			if(params.get("name")==null || params.get("path")==null){
				return updateFailure("参数异常!");
			}

			String messge=autoImportService.deletefile(params.get("path").toString(),params.get("name").toString());

			return updateSuccess(messge);
		}catch (Exception e){
			log.error("撤销估值调整处理异常" + e.getStackTrace());
			return updateFailure(e.getMessage());
		}
	}


	/**
	 * 报送数据指标初始数据转换方法
	 * @return
	 */
	@RequestMapping("/validateIndexChecking/indexInitialDataTransfer.action")
	public @ResponseBody String validateIndexDataTransferTempForDelete(){

		try{
			validationIndexInitiateService.validationIndexTransferHandler();
		}catch (Exception e){
			log.error("指标初始数据转换异常" + e.getStackTrace());
			return updateFailure(e.getMessage());
		}
		return "";
	}

}

