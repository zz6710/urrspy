package com.kayak.code.action;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kayak.code.model.DbTable;
import com.kayak.code.model.DbTableField;
import com.kayak.code.service.DbTableService;
import com.kayak.core.action.BaseController;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.system.RequestSupport;
import com.kayak.core.util.Tools;
import com.kayak.graphql.model.FetcherData;

import freemarker.template.Configuration;
import freemarker.template.Template;

@Controller
public class CodeAction extends BaseController {

	@Value("${code.tem.path:null}")
	private String temPath;

	@Autowired
	private DbTableService dbTableService;

	@RequestMapping("code/server/download.json")
	public void downloadServerCode(HttpServletResponse response) {
		try {
			OutputStream os = response.getOutputStream();
			response.setCharacterEncoding("utf-8");
			response.setContentType("application/octet-stream; charset=utf-8");
			response.setHeader("Content-disposition", "attachment;filename=download.zip");

			Map<String, Object> params = RequestSupport.getParameters();

			String table = Tools.obj2Str(params.get("table"));
			String tableComment = Tools.obj2Str(params.get("tableComment"));
			String model = Tools.obj2Str(params.get("model"));
			String _package = Tools.obj2Str(params.get("package"));

			Map<String, Object> dataMap = new HashMap<>();

			String lowHeadModel = model.substring(0, 1).toLowerCase() + model.substring(1);

			dataMap.put("model", model);
			dataMap.put("lowHeadModel", lowHeadModel);
			dataMap.put("package", _package);

			SqlResult<DbTableField> sqlResult = dbTableService
					.findTableFields(new FetcherData<DbTableField>(params, DbTableField.class));

			DbTable dbTable = new DbTable();

			dbTable.setName(table);
			dbTable.setComment(tableComment);

			List<DbTableField> tableFields = sqlResult.getRows();

			dbTable.setTableFields(tableFields);

			StringBuilder fieldsStringBuilder = new StringBuilder();
			StringBuilder insertParamsBuilder = new StringBuilder();
			StringBuilder updateParamsBuilder = new StringBuilder();
			StringBuilder keyParamsBuilder = new StringBuilder();

			for (DbTableField tableField : tableFields) {
				fieldsStringBuilder.append(",");
				fieldsStringBuilder.append(tableField.getDbField());

				insertParamsBuilder.append(",");
				insertParamsBuilder.append(tableField.getParam());

				if (tableField.isKey()) {
					keyParamsBuilder.append("AND ");
					keyParamsBuilder.append(tableField.getDbField());
					keyParamsBuilder.append("=");
					keyParamsBuilder.append(tableField.getAutoIdParam());
					keyParamsBuilder.append(" ");
				} else {
					updateParamsBuilder.append(",");
					updateParamsBuilder.append(tableField.getDbField());
					updateParamsBuilder.append("=");
					updateParamsBuilder.append(tableField.getParam());
					updateParamsBuilder.append(" ");
				}
			}

			dbTable.setFields(fieldsStringBuilder.toString().substring(1));
			dbTable.setInsertParams(insertParamsBuilder.toString().substring(1));
			dbTable.setUpdateParams(updateParamsBuilder.toString().substring(1));
			String keys = keyParamsBuilder.toString();
			if (keys.length() > 3) {
				dbTable.setKeyParams(keyParamsBuilder.toString().substring(3));
			} else {
				dbTable.setKeyParams(keys);
			}

			dataMap.put("dbTable", dbTable);

			ClassLoader classLoader = getClass().getClassLoader();
			URL url = classLoader.getResource("ftl");

			File file = new File(temPath);{
				if(!file.exists()){
					file.mkdirs();
				}
			}

			// 生成临时文件
			Configuration configuration = new Configuration(Configuration.VERSION_2_3_26);
			configuration.setDirectoryForTemplateLoading(new File(url.getFile()));

			// 生成实体类
			Template modelTemplate = configuration.getTemplate("model.ftl");

			String modelFileName = model + ".java";
			File modelFile = new File(temPath + modelFileName);
			modelFile.delete();
			modelFile.createNewFile();
			Writer modelFileOut = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(modelFile)));
			modelTemplate.process(dataMap, modelFileOut);
			modelFileOut.flush();
			modelFileOut.close();

			// 生成Dao
			Template daoTemplate = configuration.getTemplate("dao.ftl");

			String daoFileName = model + "Dao.java";
			File daoFile = new File(temPath + daoFileName);
			daoFile.delete();
			daoFile.createNewFile();
			Writer daoFileOut = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(daoFile)));
			daoTemplate.process(dataMap, daoFileOut);
			daoFileOut.flush();
			daoFileOut.close();

			// 生成Service
			Template serviceTemplate = configuration.getTemplate("service.ftl");

			String serviceFileName = model + "Service.java";
			File serviceFile = new File(temPath + serviceFileName);
			serviceFile.delete();
			serviceFile.createNewFile();
			Writer serviceFileOut = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(serviceFile)));
			serviceTemplate.process(dataMap, serviceFileOut);
			serviceFileOut.flush();
			serviceFileOut.close();

			// 生成page

			Template pageTemplate = configuration.getTemplate("page.ftl");

			String pageFileName = model + ".vue";
			File pageFile = new File(temPath + pageFileName);
			pageFile.delete();
			pageFile.createNewFile();
			Writer pageFileOut = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(pageFile)));
			pageTemplate.process(dataMap, pageFileOut);
			pageFileOut.flush();
			pageFileOut.close();

			// 生成压缩包
			String temZip = temPath + "/" + UUID.randomUUID().toString() + ".zip";

			File temZipFile = new File(temZip);
			temZipFile.createNewFile();

			ZipOutputStream out = new ZipOutputStream(new FileOutputStream(temZipFile)); // 创建ZipOutputStream类对象

			addZipFile(out, modelFileName, modelFile);
			addZipFile(out, daoFileName, daoFile);
			addZipFile(out, serviceFileName, serviceFile);
			addZipFile(out, pageFileName, pageFile);

			out.close();
			os.write(FileUtils.readFileToByteArray(temZipFile));
			temZipFile.delete();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

	}

	private void addZipFile(ZipOutputStream out, String zipEntryName, File zipEntryFile) throws Exception {
		out.putNextEntry(new ZipEntry(zipEntryName)); // 创建新的进入点
		// 创建FileInputStream对象
		FileInputStream in = new FileInputStream(zipEntryFile);
		int b; // 定义int型变量
		while ((b = in.read()) != -1) { // 如果没有到达流的尾部
			out.write(b); // 将字节写入当前ZIP条目
		}
		in.close(); // 关闭流

		zipEntryFile.delete();
	}

}
