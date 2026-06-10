package com.kayak.pms.getmodel.controller;

import com.kayak.core.action.BaseController;
import com.kayak.core.sql.SqlRow;
import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.pms.getmodel.dto.ModelFieldMap;
import com.kayak.pms.getmodel.service.GetModelFieldService;
import com.kayak.utils.ClassScaner;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(value = "/getFormModel")
public class GetModelFieldMapController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(GetModelFieldMapController.class);

    @Autowired
    private GetModelFieldService getModelFieldService;

    /**
     * 功能：根据server 获取实体属性
     * 作者：zhanghao
     * 日期：20220322
     */
    @RequestMapping(value = "/getModelFieldMap.json", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public String getModelFieldMap() {
        try {
            SqlRow modelMap = getModelFieldService.getModelNameByServer();
            String tableName = modelMap.getString("model_name");
            if (tableName == null || "".equals(tableName)) {
                throw new Exception("获取表单失败，未找到该业务对应的实体类！");
            }
            //扫描com.kayak.pms 路径下 以tableName结尾的文件名
            Set<Class> classes = ClassScaner.scan("com.kayak.pms", tableName, null);
            if (classes.isEmpty()) {
                throw new Exception("获取表单失败，该业务对应的实体不在配置的扫描路径下！");
            }
            //只取第一个 正常情况下 就是唯一的
            Class<?> classModel = classes.iterator().next();
            if (classModel == null) {
                throw new Exception("该菜单无法配置流程！");
            }
            Field[] fields = classModel.getDeclaredFields();
            List<ModelFieldMap> mapList = new ArrayList<>();
            for (Field field : fields) {
                ModelFieldMap fieldMap = new ModelFieldMap();
                GraphQLField graphQLField = field.getAnnotation(GraphQLField.class);
                if (graphQLField != null) {
                    //如果field_id 或者name 为空时  不记录
                    if (graphQLField.label() != null && graphQLField.field() != null) {
                        //字段lable
                        fieldMap.setName(field.getName());
                        //字段名
                        fieldMap.setDisplayName(graphQLField.label());
                        //字段类型 如果是空则默认text
                        if (graphQLField.kkhtml() == null) {
                            fieldMap.setFieldType("KFieldText");
                        } else {
                            fieldMap.setFieldType(graphQLField.kkhtml());
                        }
                        //字段字典
                        fieldMap.setDict(graphQLField.kkhtmlExt());
                        mapList.add(fieldMap);
                    }
                }

            }
            JSONObject json = new JSONObject();
            json.put("success", true);
            json.put("formModel", mapList);
            logger.info("实体json串：", json);
            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return updateFailure(e.getMessage());
        }


    }
   /* public static void main(String[] args) throws ClassNotFoundException {
        //String tablename = "T8ProdInfo.class";
     *//*   T8ProdInfo tablename = new T8ProdInfo();
        Class<?> classModel = T8ProdInfo.class;*//*
        String tablename = "T8ProdInfo";
        Class<?> classModel  = Class.forName("com.kayak.pms.T81.model."+tablename);
        Field[] fields = classModel.getDeclaredFields();
        List<ModelFieldMap> mapList = new ArrayList<>();
        for (Field field : fields) {
            ModelFieldMap fieldMap = new ModelFieldMap();
            GraphQLField graphQLField = field.getAnnotation(GraphQLField.class);
            //字段lable
            fieldMap.setField_id(graphQLField.label());
            //字段名
            fieldMap.setField_name(graphQLField.field());
            //字段类型
            fieldMap.setField_type(graphQLField.kkhtml());
            //字段字典
            fieldMap.setDict(graphQLField.kkhtmlExt());
            mapList.add(fieldMap);
        }
        JSONObject json=new JSONObject();
        json.put("success", true);
        json.put("formModel", mapList);
        logger.info("实体json串：",json);
        System.out.println(json.toString());
    }*/

}
