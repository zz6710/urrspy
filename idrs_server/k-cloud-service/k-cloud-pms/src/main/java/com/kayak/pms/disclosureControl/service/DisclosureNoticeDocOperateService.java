package com.kayak.pms.disclosureControl.service;

import com.kayak.base.dao.util.DaoUtil;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.system.SysUtil;
import com.kayak.pms.disclosureControl.dao.DisclosureNoticeDocDao;
import com.kayak.pms.disclosureControl.model.DisclosureNoticeValue;
import com.kayak.pms.printTemp.utils.WordToPdfUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.xwpf.usermodel.*;
import org.apache.xmlbeans.XmlCursor;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.math.BigInteger;
import java.util.*;
import java.util.regex.Pattern;


@Service
public class DisclosureNoticeDocOperateService {

	private static final Logger logger = LoggerFactory.getLogger(DisclosureNoticeDocOperateService.class);

	@Autowired
	private DisclosureNoticeDocDao disclosureNoticeDocDao;
	@Autowired
	private WordToPdfUtil wordToPdfUtil;

	/**
	 * 将信批文档中${xxx}字符替换成存储的指定字符
	 * API docx适用
	 * @param temPath
	 * @param fileModName
	 * @param fileSavePath
	 * @param t8NoticeValueList
	 * @throws Exception
	 */
	public void replaceNoticeDocxCharContent (String temPath, String fileModName, String fileSavePath, List<DisclosureNoticeValue> t8NoticeValueList, String docType) throws Exception {
		FileOutputStream fileOutputStream = null;//文件处理输出流
		XWPFDocument xwpfDoc = null;
		XWPFParagraph paragraph = null;
		OPCPackage opcPackage = null;
		ByteArrayOutputStream boStream = null;

		try {
			//创建目标上传文件
			File newFile = new File(fileSavePath);
			if(!newFile.getParentFile().exists()){
				newFile.getParentFile().mkdirs();
			}
			if(!newFile.exists()){
				newFile.createNewFile();
			}

			opcPackage = POIXMLDocument.openPackage(temPath + fileModName);
			xwpfDoc = new XWPFDocument(opcPackage);
			Iterator<XWPFParagraph> paragraph_list = xwpfDoc.getParagraphsIterator();//轮循读取每一段进行操作

			while (paragraph_list.hasNext()) {//替换段落字段
				paragraph = paragraph_list.next();//获取段落对象

				List<XWPFRun> run = paragraph.getRuns();
				for (int i=0; i<run.size(); i++) {//往下读取段落内容
					if (t8NoticeValueList.size()!=0) {
						//临时注释getTextPosition,默认赋值为-1 ,待验证
						//int pos = run.get(i).getTextPosition();
						//String str = run.get(i).getText(pos);
						String text = run.get(i).getText(-1);
						for (DisclosureNoticeValue disclosureNoticeValue : t8NoticeValueList) {
							String wordKey = "${" + disclosureNoticeValue.getColumnKey() + "}";//${替换的字段}
							String columnValue = disclosureNoticeValue.getColumnValue();
							if (columnValue == null) {
								columnValue = "";
							}
							String replace = text.replace(wordKey, columnValue);
							text = replace;
							}
						   run.get(i).setText(text, 0);//替换原字符
						}else{//没有替换的字符集，则将全部转空
							if(run.get(i).getText(run.get(i).getTextPosition()).contains("${")){
								run.get(i).setText("",0);
							}
					}
					}
			}

			Iterator<XWPFTable> table_list = xwpfDoc.getTablesIterator();
			while (table_list.hasNext()) {//替换表单对象
				XWPFTable xwpfTable = table_list.next();//获取表单
				//表格动态添加子产品行
				addRows(xwpfTable,t8NoticeValueList);
				List<XWPFTableRow> tableRowList =  xwpfTable.getRows();
				for (XWPFTableRow tableRow : tableRowList) {
					List<XWPFTableCell> tableCellList = tableRow.getTableCells();
					for (XWPFTableCell tableCell : tableCellList) {
						//获取单元格值
						String text = tableCell.getText();
						final StringBuffer rs = new StringBuffer();
						ArrayList<String> constantArray= new ArrayList<>();
						// 初始位移
						int beginIndex = 0;
						int endIndex = 0;
						// 循环直到从位移开始匹配不到字符串
						while (text.indexOf("$", endIndex) >= 0) {
							// 获取下一个匹配到的开始位移
							beginIndex = text.indexOf("$", endIndex);
							// 非参数部分先堆入StringBuffer
							rs.append(text.substring(endIndex, beginIndex));
							// 获取下一个匹配到的结束位移
							endIndex = text.indexOf("}", beginIndex) + 1;
							// 如果位移合法
							if (beginIndex < endIndex) {
								// 获得参数信息
								constantArray.add(text.substring(beginIndex, endIndex));
							}
						}
						if (t8NoticeValueList.size()!=0) {
							for (DisclosureNoticeValue disclosureNoticeValue : t8NoticeValueList) {
								String replaceWord = "${" + disclosureNoticeValue.getColumnKey() + "}";
								if(constantArray.size()>0){
									for (String constant:constantArray){
										if (replaceWord.equals(constant.toString())) {
											//判断单元格字符是否为需要替换的字符
											if (disclosureNoticeValue.getColumnValue() == null) {
												disclosureNoticeValue.setColumnValue("");
											}
											rs.append(disclosureNoticeValue.getColumnValue());
										}
									}
								}else{
									break;
								}
							}
							// 非参数部分先堆入StringBuffer
							rs.append( text.substring( endIndex ) );
							if(rs.toString().contains("^P")){
								//表格替换当有换行符存在时·
								addBreakInCell(tableCell, rs.toString());
							}else{
								//删除单元格内容
								tableCell.removeParagraph(0);
								//将替换后的值加入单元格
								//设置单元格样式居中
								tableCell.addParagraph().setAlignment(ParagraphAlignment.CENTER);
								tableCell.setText(String.valueOf(rs));
							}
						}else {//没有替换的字符集，则将全部全部转空
							tableCell.getParagraphs().get(0).getRuns().get(0).setText("", 0);
						}
					}
				}
			}

			boStream = new ByteArrayOutputStream();
			xwpfDoc.write(boStream);//将替换的内容写进输入流

			fileOutputStream = new FileOutputStream(fileSavePath);//将输出流指定输出目录
			fileOutputStream.write(boStream.toByteArray());

			//删除临时文件夹下模板文件
			File tempFile = new File(temPath + fileModName);
			if(tempFile.exists()&&!"2".equals(docType)) {
				tempFile.delete();
			}
		} catch (Exception e) {
			logger.error("信批公告docx模板替换字符时异常:" + e);
			throw new Exception("信批公告docx模板替换字符时异常:" + e);
		} finally {
			try {
				fileOutputStream.close();
				xwpfDoc.close();
				boStream.close();
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
		}
	}

	/**
	 * 将信批文档中${xxx}字符替换成存储的指定字符
	 * API doc适用
	 * @param temPath
	 * @param fileModName
	 * @param fileSavePath
	 * @param t8NoticeValueList
	 * @throws Exception
	 */
	public void replaceNoticeDocCharContent (String temPath, String fileModName, String fileSavePath, List<DisclosureNoticeValue> t8NoticeValueList, String docType) throws Exception {
		File word = new File(temPath, fileModName);//获取模板文件
		FileInputStream fileInputStream = null;
		FileOutputStream fileOutputStream = null;//文件处理输出流
		HWPFDocument hwpfDoc = null;
		try {
			//创建目标上传文件
			File newFile = new File(fileSavePath);
			if(!newFile.getParentFile().exists()){
				newFile.getParentFile().mkdirs();
			}
			if(!newFile.exists()){
				newFile.createNewFile();
			}

			fileInputStream = new FileInputStream(word);
			hwpfDoc = new HWPFDocument( new POIFSFileSystem( fileInputStream));//获得要替换的word模板
			org.apache.poi.hwpf.usermodel.Range range = hwpfDoc.getRange();// range获取word中的内容

			// 对文档进行替换
			if (t8NoticeValueList.size()!=0){
				for (DisclosureNoticeValue disclosureNoticeValue : t8NoticeValueList) {
					if (disclosureNoticeValue.getColumnValue() == null || disclosureNoticeValue.getColumnValue().trim().equals("null")) {
						disclosureNoticeValue.setColumnValue("");
					}
					String wordValue = disclosureNoticeValue.getColumnValue();//需要替换的值
					String wordKey = disclosureNoticeValue.getColumnKey();//替换的字段
					// word换行符与java换行符不一致，所以进行替换
					wordValue = wordValue.replaceAll("\n", "\u000B");
					logger.info("${" + disclosureNoticeValue.getColumnValue() + "}");
					range.replaceText("${" + wordKey + "}", wordValue == null ? "" : wordValue);
				}
			}else {
				while (range.text().contains("$")) {
					range.replaceText(range.text().substring(range.text().indexOf("$"), range.text().indexOf("}")+1), "");
				}
			}
			fileOutputStream = new FileOutputStream(fileSavePath);//将输出流指定输出目录
			//判断保存目录是否存在文件，不存在则创建
			hwpfDoc.write(fileOutputStream);

			fileInputStream.close();
			//删除临时文件夹下模板文件
			File tempFile = new File(temPath + fileModName);
			if(tempFile.exists()&&!"2".equals(docType)) {
				tempFile.delete();
			}
		} catch (Exception e) {
			logger.error("信批公告doc模板替换字符时异常:" + e);
			throw new Exception("信批公告doc模板替换字符时异常:" + e);
		} finally {
			try {
				fileOutputStream.close();
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
		}
	}

	/**
	 * 根据信批公告模板表格配置表 idb_notice_grid_config
	 * 将信批文档中$G{xxx}字符替换成指定表格形式内容
	 * @param temPath
	 * @param fileModName
	 * @param fileSavePath
	 * @param noticeVersionId
	 * @throws Exception
	 */
	public void replaceNoticeDocGridContent (String temPath, String fileModName, String fileSavePath, String noticeVersionId) throws Exception {
		XWPFDocument xwpfDoc = null;
		XWPFParagraph paragraph = null;
		List<XWPFParagraph> paragraph_del_list = new ArrayList<>();
		ByteArrayOutputStream boStream = null;

		try {
			OPCPackage opcPackage = POIXMLDocument.openPackage(temPath + fileModName);
			xwpfDoc = new XWPFDocument(opcPackage);
			/** 获取公告模板配置信息 */
			List<Map<String, Object>> configParamsList = disclosureNoticeDocDao.getMaxRowColumn(noticeVersionId);
			Iterator<XWPFParagraph> iteratorParam = xwpfDoc.getParagraphsIterator();//轮循读取每一段进行操作
			while (iteratorParam.hasNext()) {
				paragraph = (XWPFParagraph)iteratorParam.next();//获取段落对象
				List<XWPFRun> run = paragraph.getRuns();
				for (XWPFRun xwpfRun : run) {//往下读取段落内容
					for (Map<String, Object> configParams : configParamsList) {//对公告版本需要替换的所有表格进行遍历
						//xwpfRun.getTextPosition()
						if (xwpfRun.getText(-1).equals(String.valueOf(configParams.get("replace_str")))) {
							paragraph_del_list.add(paragraph);
							//查询需要替换的表格数据
							configParams.put("noticeVersionId", noticeVersionId);
							configParams.put("deal_date", SysUtil.getSysDynamicParamValue("SYSDATE"));    //系统时间
							String remark = configParams.get("remark").toString();
							List<String[]> valueStrList = this.getCellFillingValueStr(String.valueOf(configParams.get("exe_id")), configParams);//根据公告版本id查询数据
							if (valueStrList.size() == 0) {
								//如果备注不为空则替换段落的值
								if(!remark.equals("")){
									if (xwpfRun.getText(-1).contains("$G{")) {
										xwpfRun.setText(remark, 0);
									}
									paragraph_del_list.remove(paragraph);
									continue;
								}
							}
							//查询对应表格行列名称数据,操作表格进行替换
							List<String[]> gridMessParams = disclosureNoticeDocDao.getDocGridReplaceContent(noticeVersionId, configParams);
							XmlCursor cursor = paragraph.getCTP().newCursor();
							XWPFTable table = xwpfDoc.insertNewTbl(cursor);
							CTTblPr tt = table.getCTTbl().getTblPr();
							tt.getTblW().setType(STTblWidth.DXA);
							tt.getTblW().setW(new BigInteger("8500"));//设置表宽

							int headSize = gridMessParams.size();//表格行数
							int columnSize = Integer.parseInt(String.valueOf(configParams.get("max_column")));//该表单列数
							if (headSize >= 1) {
								String[] headStr = gridMessParams.get(0);
								XWPFTableRow row_0 = table.getRow(0);//设置表格头
								row_0.getCell(0).setText(headStr[0]);
								for (int c=1; c<columnSize; c++) {
									row_0.addNewTableCell().setText(headStr[c]);
								}
							}

							if (headSize >= 2) {//当表头行数大于1行时,则需要对表头进行合并
								String[] headStr1 = gridMessParams.get(1);
								XWPFTableRow row_1 = table.createRow();
								for (int c1=0; c1<columnSize; c1++) {
		  							row_1.getCell(c1).setText(headStr1[c1]);
								}
							}

							if (headSize >= 3) {//最多三行
								String[] headStr2 = gridMessParams.get(2);
								XWPFTableRow row_2 = table.createRow();
								for (int c2=0; c2<columnSize; c2++) {
									row_2.getCell(c2).setText(headStr2[c2]);
								}
							}

							/*按照表格实际的样式进行处理*/
							String[] dataStr = null;
							for(int m=0;m<valueStrList.size();m++){
								dataStr = valueStrList.get(m);
								XWPFTableRow newRow = table.createRow();
								for(int j=0;j<columnSize;j++){
									if(!gridMessParams.get(0)[j].equals("证券代码")&&!gridMessParams.get(0)[j].equals("期限(天)")&&!gridMessParams.get(0)[j].equals("收益率对应日期")){
										newRow.getCell(j).setText(myFormatNumber(dataStr[j]));
									}else{
										newRow.getCell(j).setText(dataStr[j]);
									}
									CTTcPr tcpr= newRow.getCell(j).getCTTc().addNewTcPr();
									CTTblWidth cellW = tcpr.addNewTcW();
									cellW.setType(STTblWidth.DXA);
									//cellW.setW(BigInteger.valueOf(36*25));
									CTVerticalJc va = tcpr.addNewVAlign();
									va.setVal(STVerticalJc.CENTER);
								}
							}

							/** 对表头进行合并项操作 */
							List<Integer[]> horizonCellMergeList = getHorizonCellsIntegratedList(configParams);//获取水平方向需要合并的坐标
							for (Integer[] hSt : horizonCellMergeList) {
								mergeCellsHorizontal(table, hSt[0], hSt[1], hSt[2]);//合并水平单元格(表格, 行数, 合并列1, 合并列2)
							}

							List<Integer[]> verticalCellMergeList = getVerticalCellsIntegratedList(configParams);//获取垂直方向需要合并的坐标
							for (Integer[] vSt : verticalCellMergeList) {
								mergeCellsVertically(table, vSt[0], vSt[1], vSt[2]);//合并垂直单元格(表格, 列数, 合并行1, 合并行2)
							}

							/** 对所有表格的第一列进行合并操作 */
							int mergeStart = 0;//合并起始项指针从0开始
							int mergeEnd = 0;//合并结束项指针
							for (int k=1; k<valueStrList.size(); k++) {
								//对填充数据进行遍历获取数组第一项
								if (valueStrList.get(k)[0].equals(valueStrList.get(k - 1)[0])) {//判断该行与上一行数据是否相同，若相同，则起始指针记录为上一行结束指针记录为当前行
									if (mergeStart == 0) {//若初始指针不为0，代表当前有合并项，不进行重新赋值，待合并后清零
										mergeStart = k;
									}
									mergeEnd = k;
									//所有数据的第一列都相同时也要合并
									if (mergeEnd == valueStrList.size() - 1) {
										//合并垂直单元格(表格, 列数固定为1, 合并行1, 合并行2)
										mergeCellsVertically(table, 0, mergeStart, mergeEnd + 1);
										//合并完成后，重置合并起始结束指针
										mergeStart = 0;
										mergeEnd = 0;
									}
								} else if (!valueStrList.get(k)[0].equals(valueStrList.get(k - 1)[0]) && mergeStart > 0) {//当该行与上一行不同，且合并起始项不为0时，则进行合并操作
									mergeCellsVertically(table, 0, mergeStart, mergeEnd + 1);//合并垂直单元格(表格, 列数固定为1, 合并行1, 合并行2)
									//合并完成后，重置合并起始结束指针
									mergeStart = 0;
									mergeEnd = 0;
								} else {//当该行与上一行不同，且合并起始项不为0时，则跳过获取下一个需合并的行进行判断
									continue;
								}

							}

						}
					}
				}
			}

			/** 删除需要替换的原字符 */
			if(paragraph_del_list.size() > 0) {
				for (XWPFParagraph del_paragraph : paragraph_del_list) {
					xwpfDoc.removeBodyElement(xwpfDoc.getPosOfParagraph(del_paragraph));
				}
			}

			boStream = new ByteArrayOutputStream();
			xwpfDoc.write(boStream);

			/** 写入文件前进行判断,若文件存在则先删除文件 */
			File fileExist = new File(fileSavePath);
			if (fileExist.exists()) {
				fileExist.delete();
				fileExist.createNewFile();
			}
			//删除临时文件夹下模板文件
			File tempFile = new File(temPath + fileModName);
			if(tempFile.exists()) {
				tempFile.delete();
			}

			FileOutputStream fos = new FileOutputStream(fileSavePath);
			fos.write(boStream.toByteArray());
			fos.close();
		} catch (Exception e) {
			logger.error("信批公告模板替换表格时异常:" + e);
			//throw new Exception(e);
		} finally {
			if (boStream != null) {
				try {
					boStream.close();
				} catch (IOException e) {
					logger.error(e.getMessage(), e);
				}
			}
		}
	}

	/**
	 * 根据查询语句查询填充数据
	 * @return
	 * @throws Exception
	 */
	public List<String[]> getCellFillingValueStr (String exeId, Map<String, Object> params) throws Exception {
		List<String[]> valueStrList = new ArrayList<>();
		List<SqlRow> dataResList = disclosureNoticeDocDao.getFillingValueDataByExeId(exeId, params);//查询所有填充数据
		String[] column_order = disclosureNoticeDocDao.getFillingGridColumnOrder(params);//查询表格字段顺序，从左至右

		for (SqlRow sqlRow : dataResList) {
			String[] valueStr = new String[column_order.length];
			for(int i=0; i<column_order.length; i++) {
				valueStr[i] = sqlRow.getString(column_order[i]);
			}
			valueStrList.add(valueStr);
		}
		return valueStrList;
	}

	/**
	 * 获取单元格水平向合并项配置信息
	 * String[3] row, fromColumn, toColumn
	 * @return
	 */
	public List<Integer[]> getHorizonCellsIntegratedList (Map<String, Object> configParams) throws Exception {
		List<Integer[]> returnList = new ArrayList<>();
		List<SqlRow> horizonCellsResList = disclosureNoticeDocDao.getHorizonCellsList(configParams);
		for (SqlRow hColumnRes : horizonCellsResList) {
			Integer[] hCellsStr = new Integer[3];
			hCellsStr[0] = hColumnRes.getInteger("row_order") - 1;
			hCellsStr[1] = hColumnRes.getInteger("column_order") - hColumnRes.getInteger("merge_column_num") -1;
			hCellsStr[2] = hColumnRes.getInteger("column_order") -1;
			returnList.add(hCellsStr);
		}
		return returnList;
	}

	/**
	 * 获取单元格垂直向合并项配置信息
	 * String[3] column, fromRow, toRow
	 * @return
	 */
	public List<Integer[]> getVerticalCellsIntegratedList (Map<String, Object> configParams) throws Exception {
		List<Integer[]> returnList = new ArrayList<>();
		List<SqlRow> verticalCellsResList = disclosureNoticeDocDao.getVerticalCellsList(configParams);
		for (SqlRow vRowRes : verticalCellsResList) {
			Integer[] hCellsStr = new Integer[3];
			hCellsStr[0] = vRowRes.getInteger("column_order") -1;
			hCellsStr[1] = vRowRes.getInteger("row_order") - vRowRes.getInteger("merge_row_num") -1;
			hCellsStr[2] = vRowRes.getInteger("row_order") -1;
			returnList.add(hCellsStr);
		}
		return returnList;
	}


	/**
	 * 合并水平向单元格
	 * @param table
	 * @param row
	 * @param fromCell
	 * @param toCell
	 */
	public static void mergeCellsHorizontal(XWPFTable table, int row, int fromCell, int toCell) {
		for (int cellIndex = fromCell; cellIndex <= toCell; cellIndex++) {
			XWPFTableCell cell = table.getRow(row).getCell(cellIndex);
			if ( cellIndex == fromCell ) {
				cell.getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.RESTART);
			} else {
				cell.getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.CONTINUE);
			}
		}
	}

	/**
	 * 合并垂直向单元格
	 * @param table
	 * @param col
	 * @param fromRow
	 * @param toRow
	 */
	public static void mergeCellsVertically(XWPFTable table, int col, int fromRow, int toRow) {
		for (int rowIndex = fromRow; rowIndex <= toRow; rowIndex++) {
			XWPFTableCell cell = table.getRow(rowIndex).getCell(col);
			if ( rowIndex == fromRow ) {
				cell.getCTTc().addNewTcPr().addNewVMerge().setVal(STMerge.RESTART);
			} else {
				cell.getCTTc().addNewTcPr().addNewVMerge().setVal(STMerge.CONTINUE);
			}
		}
	}

	/**
	 * 根据日期和公告类型/子类型选择生成日期前生效的最新表单数据配置信息
	 * @throws Exception
	 */
	public void generateGridConfigByDisclosureTypeAndLatestDate (Map<String, Object> params) throws Exception {

		DaoUtil.doTrans(() ->{
			try {
				//根据基准日期重新配置信批模板最新配置信息
				disclosureNoticeDocDao.delNoticeGridConfig(params);
				disclosureNoticeDocDao.replaceNoticeGridConfig(params);
			}catch (Exception e) {
				logger.error("配置信批公告表单配置异常：" + e.getMessage());
				throw new Exception("配置信批公告表单配置异常：" + e.getMessage());
			}
		});
	}
	public String myFormatNumber(String number) {
		 Pattern NUMBER_PATTERN = Pattern.compile("-?\\d+(\\.\\d+)?");
		 if( number != null && NUMBER_PATTERN.matcher(number).matches()){
			 //split()方法支持正则表达式，‘.’表示匹配任意字符，所以这里需要转义，表示匹配‘.’
			 String[] strArr = number.split("\\.");
			 //将整数部分分离出来
			 StringBuffer sb = new StringBuffer(strArr[0]);
			 //小于等于三位，不需要该操作，返回原数字
			 if (sb.length() <= 3) {
				 return number;
			 }
			 //大于三位
			 int last = sb.length();
			 //从后往前，每三位前插入一个逗号
			 for (int i = last - 3; i > 0; i -= 3) {
				 sb.insert(i, ",");
			 }
			 StringBuffer doubleStr = new StringBuffer(".");
			 try {
				 //如果有小数部分，那就小数点加上小数部分
				 doubleStr.append(strArr[1]);
			 } catch (ArrayIndexOutOfBoundsException e) {
				 //进入这里表示没有小数部分，那就将doubleStr置为空字符
				 doubleStr.deleteCharAt(doubleStr.length() - 1);
			 }
			 //整数和doubleStr的结果拼接
			 sb.append(doubleStr);
			 return sb.toString();
		 }
		 return number;

	}

	/**
	 * des:表格内容换行展示
	 * @param cell
	 * @param replaceText
	 */
	private static void addBreakInCell(XWPFTableCell cell, String replaceText) {
		for (XWPFParagraph paragraph : cell.getParagraphs()) {
			paragraph.setAlignment(ParagraphAlignment.CENTER);
			for (XWPFRun run : paragraph.getRuns()) {
				int preIndex =0;
				int index = replaceText.indexOf("^P");
				if (index >=0) {
					run.setText("",0);
					if (index ==0) {
						run.addBreak();
						run.setText(replaceText.replace("^P",""));
						index = replaceText.indexOf("^P", index +1);
					}
				}
				while (index >=0) {
					String sub = replaceText.substring(preIndex, index);
					sub = sub.replace("^P","");
					if (sub.length() >0) {
						run.setText(sub);
					}
					run.addBreak();
					preIndex = index;
					index = replaceText.indexOf("^P", preIndex +1);
					if(index<0){
						sub =  replaceText.substring(preIndex +2);
						if (sub.length() >0) {
							run.setText(sub);
						}
					}
				}
			}
		}
	}

	/**
	 * des:表格动态添加行
	 * @param table
	 * @param t8NoticeValueList
	 */
	public void addRows(XWPFTable table, List<DisclosureNoticeValue> t8NoticeValueList) {
		try {
			List<XWPFTableRow> tableRowList = table.getRows();
			for (int j=0; j<tableRowList.size(); j++) {
				List<XWPFTableCell> tableCellList = tableRowList.get(j).getTableCells();
				for (XWPFTableCell tableCell : tableCellList) {
					//获取单元格值
					String text = tableCell.getText();
					if (text.contains("$R{")) {
						//获取表格的总行数
						int index = table.getNumberOfRows();
						int rowIndex = index;
						/* 获取公告模板配置信息 */
						String noticeVersionId = t8NoticeValueList.get(0).getT8DisclosureVersionId();
						List<Map<String, Object>> configParamsList = disclosureNoticeDocDao.getMaxRowColumn(noticeVersionId);
						//对公告版本需要替换的所有行进行遍历
						for (Map<String, Object> configParams : configParamsList) {
							if (text.equals(String.valueOf(configParams.get("replace_str")))) {
								configParams.put("noticeVersionId", noticeVersionId);
								//系统时间
								configParams.put("deal_date", SysUtil.getSysDynamicParamValue("SYSDATE"));
								//根据公告版本id查询数据
								List<String[]> valueStrList = this.getCellFillingValueStr(String.valueOf(configParams.get("exe_id")), configParams);
								//循环添加行和和单元格
								for (int i = 0; i < valueStrList.size(); i++) {
									//获取要复制样式的行
									XWPFTableRow sourceRow = table.getRow(index - 2);
									//添加新行
									XWPFTableRow targetRow = table.insertNewTableRow(rowIndex++);
									//复制行的样式给新行
									//targetRow.getCtRow().setTrPr(sourceRow.getCtRow().getTrPr());
									//获取要复制样式的行的单元格
									List<XWPFTableCell> sourceCells = sourceRow.getTableCells();
									//循环复制单元格
									int dataIndex = 0;
									for (XWPFTableCell sourceCell : sourceCells) {
										//添加新列
										XWPFTableCell newCell = targetRow.addNewTableCell();
										//复制单元格的样式给新单元格
										newCell.getCTTc().setTcPr(sourceCell.getCTTc().getTcPr());
										//该表单列数
										int columnSize = Integer.parseInt(String.valueOf(configParams.get("max_column")));
										newCell.removeParagraph(0);
										String[] dataStr = null;
										dataStr = valueStrList.get(i);
										newCell.addParagraph().createRun().setText(dataStr[dataIndex]);
										dataIndex++;
									}
								}
							}
						}
						//刪除标志行
						table.removeRow(index - 1);
					}
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
}
