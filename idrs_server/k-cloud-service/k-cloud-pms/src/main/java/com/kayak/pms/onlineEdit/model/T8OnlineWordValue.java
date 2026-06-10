package com.kayak.pms.onlineEdit.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

/**
 * @program: k-cloud  
 * @description: 文档在线编辑数据集表
 * @author:  WangZhenXin
 * @create: 2021-01-29 19:04:01 
 * @memo 备注信息
 */

@Data
@GraphQLModel(fetcher="t8OnlineWordValueService",table="t8_online_word_value")
public class T8OnlineWordValue {


	/**
	 * ID
	 */
   	@GraphQLField(label="ID", sql="id=$S{id}",field="id")
	private String id;

	/**
	 * 数据Key
	 */
   	@GraphQLField(label="数据Key", sql="word_key=$S{wordKey}",field="word_key")
	private String wordKey;

	/**
	 * 数据value
	 */
   	@GraphQLField(label="数据value", sql="word_value=$S{wordValue}",field="word_value")
	private String wordValue;

	/**
	 * 数据字典映射
	 */
	@GraphQLField(label="数据dict", sql="word_value=$S{dict}",field="dict")
	private String dict;

	/**
	 * 数据描述
	 */
	@GraphQLField(label="数据描述", sql="word_comment=$S{wordComment}",field="word_comment")
	private String wordComment;

	/**
	 * 产品文档版本Id
	 */
   	@GraphQLField(label="产品文档版本Id", sql="t8_prod_document_version_id=$S{t8ProdDocumentVersionId}",field="t8_prod_document_version_id")
	private String t8ProdDocumentVersionId;

	/**
	 * 文档模板版本Id
	 */
   	@GraphQLField(label="文档模板版本Id", sql="t8_print_temp_version_id=$S{t8PrintTempVersionId}",field="t8_print_temp_version_id")
	private String t8PrintTempVersionId;

	/**
	 * 是否可编辑:1-是,0-否
	 */
   	@GraphQLField(label="是否可编辑", sql="is_disabled=$S{isDisabled}",field="is_disabled")
	private String isDisabled;

	/**
	 * 文件名称
	 */
	@GraphQLField(label = "文件名称",sql = " file_name = $S{fileName}",field = "file_name")
	private String fileName;

	/**
	 * 预览地址
	 */
	@GraphQLField(label = "预览地址",sql = " view_url = $S{viewUrl}",field = "view_url")
	private String viewUrl;

	/**
	 * 上传地址
	 */
	@GraphQLField(label = "上传地址",sql = " upload_path = $S{uploadPath}",field = "upload_path")
	private String uploadPath;
	@GraphQLField
	private String defaultValue;
	@GraphQLField
	private String columnComment;
	@GraphQLField
	private String emptyDefaultVal;
	@GraphQLField
	private String dataDigits;
	@GraphQLField
	private String dataType;
	@GraphQLField
	private String columnName;
	@GraphQLField
	private String sqlInfo;

	@GraphQLField(field="process_instance_id")
	private String processInstanceId;
	
	@GraphQLField
	private String prodCode;
	
	@GraphQLField
	private String docType;

}
