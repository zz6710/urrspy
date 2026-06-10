package com.kayak.pms.T82.uploadAction;

import com.alibaba.excel.util.StringUtils;
import com.kayak.core.action.BaseController;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.system.RequestSupport;
import com.kayak.core.system.SysBeans;
import com.kayak.core.util.Tools;
import com.kayak.graphql.annotation.GraphQLModel;
import com.kayak.graphql.model.FetcherData;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Scope("prototype")
@RestController
public class UploadController extends BaseController {

    @Value("${excel.maxlen:100}")
    private int maxLen;

    @Value("${excel.tem.path:null}")
    private String temPath;

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @PostMapping(value = "/excel/upload.json")
    @ResponseBody
    public String uploadExcel(@RequestParam(value = "file", required = false)MultipartFile file , HttpServletResponse response ){
        Map<String, Object> params = RequestSupport.getParameters();
        String modelClassName = Tools.obj2Str(params.get("modelClassName"));
        Class<?> modelClass = getClass(modelClassName);
        if (modelClass == null) {
            log.error("获取class失败：" + modelClassName);
            return RequestSupport.updateReturnJson(false, "获取class失败", null).toString();
        }
        Object fetcherBean = this.getBean(modelClass);
        if (fetcherBean == null) {
            log.error("获取操作对象失败，无fetcher配置对应实例，路径：" + modelClassName);
            return RequestSupport.updateReturnJson(false, "无法获取service", null).toString();
        }

        String fileName = file.getOriginalFilename();


        Map<String, Object> _params = new HashMap<>();
        _params.put("interfaceFileName",fileName);
        Map<String,String> respnoseMap = (Map<String,String>)this.execAction(modelClass,"checkAndReturn",fetcherBean,_params);//运行checkOne方法验证下是否唯一,并返回存储路径
        //放置路径与名称
        String  uploadDir = respnoseMap.get("path");
        //为了方便整理生成指定文件名称 ： uploadDir/YYMMDD/xxxx.extension
        String uploadFileName = respnoseMap.get("uploadFileName");

        if(respnoseMap == null||respnoseMap.get("check").equals("false")||StringUtils.isEmpty(uploadDir)){
            log.error("该模板代码已存在,请修改文件名后上传！fileName：" + fileName);
            return RequestSupport.updateReturnJson(false, "已有该数据禁止添加！fileName：" + fileName, null).toString();
        }

        File uploadFile = null;
        try {
            //文件后缀
            String extension = null;
            if (fileName.contains(".")) {
                extension = fileName.substring(fileName.lastIndexOf(".") + 1);
            }
            if (!"xlsx".equals(extension) && !"xls".equals(extension)) {
                return RequestSupport.updateReturnJson(false, "请上传Excel文件" + fileName, null).toString();
            }

            if (Tools.strIsEmpty(uploadDir)) {// 如果有指定上传的文件夹，这使用上传的文件夹作为上传目录
                return RequestSupport.updateReturnJson(false, "请在TA系统参数管理中指定接口Excel上传目录" + fileName, null).toString();
            }

            //用于保存必要参数

            if (fileName.indexOf("_") <= 0) {
                return RequestSupport.updateReturnJson(false, "文件名格式异常，规则：模板名称_模板代码.xlsx" + fileName, null).toString();
            }
            String interface_code = fileName.substring(fileName.indexOf("_") + 1, fileName.lastIndexOf("."));
            if (interface_code.getBytes("UTF-8").length > 10) {
                return RequestSupport.updateReturnJson(false, "模板代码过长，请控制在10字节内" + fileName, null).toString();
            }
            //SqlResult sqlResult = comnDao.sqlQuery("select count(1) count from TA_DIS_INTERFACE_TEMPLATE_INFO where interface_code = $S{interface_code}", SqlUtil.getDataSourceSys(), fileMap);
//			if (sqlResult.next() && sqlResult.getInteger("count") >= 1) {
//				throw new Exception("该模板代码已存在,请修改文件名后上传");
//			}

            uploadFile = new File(uploadFileName);
            uploadFile.mkdirs();
            HashMap fileMap = new HashMap<String, Object>();
            fileMap.put("interfaceCode", interface_code);
            fileMap.put("interfaceFileName",fileName);
            fileMap.put("interfaceFileRoute",uploadFileName);
            //保存文件
            file.transferTo(uploadFile);
            return (String)this.execAction(modelClass,"addExecl",fetcherBean,fileMap);//插入表
        }catch (Exception e){
            log.error(e.getMessage());
        }
        return RequestSupport.updateReturnJson(false, "文件上传失败！" + fileName, null).toString();
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @PostMapping("excel/downloadTest.json")
    @ResponseBody
    public void download(HttpServletResponse response){
        Map<String, Object> params = RequestSupport.getParameters();
        String filePath = Tools.obj2Str(params.get("interfaceFileRoute"));
        String fileName = Tools.obj2Str(params.get("interfaceFileName"));
        try(OutputStream os = response.getOutputStream();){
            response.reset();
            response.setCharacterEncoding("utf-8");
            response.setHeader("Access-Control-Expose-Headers", "filename");
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-disposition", "attachment;filename="+fileName);
            response.setHeader("filename", fileName);
            File file = new File(filePath);
            os.write(FileUtils.readFileToByteArray(file));
            os.flush();
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
    }

    private Object getBean(Class<?> modelClass){
        // 获取操作对象实例
        GraphQLModel graphQLModel = modelClass.getAnnotation(GraphQLModel.class);
        String fetcher = graphQLModel.fetcher();
        return SysBeans.getBean(fetcher);
    }

    private Class<?> getClass(String modelClassName){
        if(StringUtils.isEmpty(modelClassName)){
            return null;
        }
        Class<?> modelClass = null;
        try {
            modelClass =  Class.forName(modelClassName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return modelClass;
    }
    private Object execAction(Class<?> modelClass,String action,Object fetcherBean,Map<String, Object> _params){
        Method method = null;
        try {
            method = fetcherBean.getClass().getMethod(action, SqlParam.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        try {
            return   method.invoke(fetcherBean, new FetcherData(_params, modelClass));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
