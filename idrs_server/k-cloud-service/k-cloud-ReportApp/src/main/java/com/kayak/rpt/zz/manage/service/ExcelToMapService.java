package com.kayak.rpt.zz.manage.service;

import com.kayak.core.sql.SqlRow;
import com.kayak.core.system.RequestSupport;
import com.kayak.rpt.zz.manage.dao.SysDictDao;
import com.kayak.rpt.zz.manage.enums.ExcelEnum;
import com.kayak.rpt.zz.manage.model.ExcelToMapInfo;
import com.kayak.rpt.zz.manage.model.SysDict;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.*;
import java.util.regex.Pattern;

@Service
public class ExcelToMapService {

	@Autowired
	private SysDictDao sysDictDao;

	Pattern pattern = Pattern.compile("[0-9]*");


	public Map<String,Object> toMapAndCheck(List<ExcelToMapInfo> list, Sheet sheet) throws Exception {


		 boolean isError = false;
		 String  msg = "";
		Map<String,Object>  resMap = new  HashMap<String,Object>();
		List<Map<String,Object>> resList = new ArrayList<Map<String,Object>>();
		// 默认最大1万行数据
		for (int r = 1; r <= 10000; r++) {
			Row row = sheet.getRow(r);

			if (isEmptyRow(row)) {
				if (r > 1) { // 数据读取完成
					msg = RequestSupport.updateReturnJson(true, "批量导入成功,共导入" + (r - 1) + "条数据", null).toString();
				}else {   //   无数据
					msg = RequestSupport.updateReturnJson(false, "导入失败(无数据)", null).toString();
					isError = true;
				}
				break; //行数据为空时 中止
			}

			Map<String,Object> map = new HashMap<String,Object>();
			// 拿出需要的字段
			for(ExcelToMapInfo excelToMapInfo:list){

				Cell cell = row.getCell(excelToMapInfo.getFieldIndex());

				if(cell != null){
					cell.setCellType(CellType.STRING);
					String cellValue = cell.getStringCellValue();
					if(!StringUtils.isEmpty(cellValue)){
						// 数据非空，开始校验数据
						if(excelToMapInfo.getFieldType() == ExcelEnum.NUM){
							// 判断是否数字
							 if(!pattern.matcher(cellValue).matches()){
								 msg = RequestSupport.updateReturnJson(true, "导入失败," + (r +1) + "行，"+excelToMapInfo.getFieldName()+":必须是数字", null).toString();
								 isError = true;
								 break; //数据类型不合法时 中止
							 }
						}else if (excelToMapInfo.getFieldType() == ExcelEnum.ENUM) {
							// 判断是否正确枚举，需要查询字典表
							SysDict sysDict = new SysDict();
							sysDict.setItemval(cellValue);
							sysDict.setDict(excelToMapInfo.getDict());
							SqlRow res = sysDictDao.qry(sysDict);
							if(res != null  && res.get("itemkey") !=null ){
								cellValue = res.get("itemkey").toString(); // 枚举数据转换
							}else {
								msg = RequestSupport.updateReturnJson(true, "导入失败," + (r +1) + "行，"+excelToMapInfo.getFieldName()+":数据非法", null).toString();
								isError = true;
								break; //数据类型不合法时 中止
							}

						} else if (excelToMapInfo.getFieldType() == ExcelEnum.DATE){

							// 剔除间隔符
							cellValue = cellValue.replaceAll("/","");
							cellValue = cellValue.replaceAll("-","");
							// 长度截取
							if(cellValue.length() != excelToMapInfo.getLength()  && excelToMapInfo.getLength()!=0 ){
								cellValue = cellValue.substring(0,excelToMapInfo.getLength());
							}

						}


					// 通过所有校验
						map.put(excelToMapInfo.getField(),cellValue);


					}else if (excelToMapInfo.isNotNULL()) {
						msg = RequestSupport.updateReturnJson(true, "导入失败," + (r +1) + "行，"+excelToMapInfo.getFieldName()+":不能为空", null).toString();
						isError = true;
						break; //非空数据为空时 中止
					}


				}else if(excelToMapInfo.isNotNULL()) {
					msg = RequestSupport.updateReturnJson(true, "导入失败," + (r +1) + "行，"+excelToMapInfo.getFieldName()+":不能为空", null).toString();
					isError = true;
					break; //非空数据为空时 中止
				}

			}

			resList.add(map);

			if (isError){
				break; //导入异常时 中止
			}


		}

		resMap.put("list",resList);
		resMap.put("isError",isError);
		resMap.put("msg",msg);

		return resMap;
	}



	public static boolean isEmptyRow(Row row) {
		if (row == null || row.toString().isEmpty()) {
			return true;
		} else {
			Iterator<Cell> it = row.iterator();
			boolean isEmpty = true;
			while (it.hasNext()) {
				Cell cell = it.next();
				if (cell.getCellType() != Cell.CELL_TYPE_BLANK) {
					isEmpty = false;
					break;
				}
			}
			return isEmpty;
		}
	}


	public static void main(String[] args) {

		Pattern pattern = Pattern.compile("[0-9]*");



		System.out.println(pattern.matcher("400").matches());

	}




	public static void exportFile(HttpServletResponse response, String fileName) {
		// 第一种获取静态资源
		ClassPathResource classPathResource = new ClassPathResource("static/" + fileName);// "static/excleTemplate/ImportModel.xlsx"
		// 第二种获取静态资源
		// InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("static/excleTemplate/" + fileName);
		// 第三种获取静态资源
		// InputStream inputStream = this.getClass().getResourceAsStream("static/excleTemplate/" + fileName);
		InputStream inputStream = null;
		OutputStream outputStream = null;
		try {
			inputStream = classPathResource.getInputStream();
			outputStream = response.getOutputStream();
			int BUFFER_SIZE = 1024 * 4;
			byte[] buffer = new byte[BUFFER_SIZE];
			int reader = 0;
			while ((reader = inputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, reader);
			}
			response.setContentType("application/octet-stream");
			response.setCharacterEncoding("utf-8");
			String newFileName = URLEncoder.encode(classPathResource.getFilename(), "UTF-8");
			response.setHeader("Content-disposition", "attachment;filename=" + newFileName);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (outputStream != null) {
					/**flush():仅仅刷新缓冲区(一般写字符时要用,因为字符时先进入缓冲区),然后将内存中的数据立刻写出(因为缓冲区是装满之后才会写出
					 ,用flush()就不必等到缓冲区满,立刻写出,流对象还可以继续使用) */
					outputStream.flush();
					/**close():关闭流对象. 也会先刷新一次缓冲区,再关闭. 关闭之后,流对象不可以继续使用 */
					outputStream.close();
					inputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}


}
