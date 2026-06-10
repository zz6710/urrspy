package com.kayak.utils;

import com.aspose.words.License;
import com.kayak.pms.printTemp.utils.WordToPdfUtil;
import com.spire.doc.Document;
import com.spire.doc.FileFormat;
import com.spire.doc.documents.BreakType;
import com.spire.doc.documents.Paragraph;
import com.spire.doc.documents.TextWrappingStyle;
import com.spire.doc.fields.DocPicture;
import com.spire.doc.fields.TextRange;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.awt.*;
import java.io.*;


public class TestSpireDoc {

//	 public static void main(String[]args) throws Exception {
//		 String distributors[] = new String[] {"銷售商1","銷售商2","銷售商3","銷售商4"};
//		 String distributorAddresss[] = new String[] {"銷售商住址1","銷售商住址2","銷售商住址3","銷售商住址4"};
//		 String customerServiceHotlines[] = new String[] {"客户服务热线1","客户服务热线2","客户服务热线3","客户服务热线4"};
//		 String officialWebsites[] = new String[] {"銷售商官方网站1","銷售商官方网站2","銷售商官方网站3","銷售商官方网站4"};
//		 String mainDutys[] = new String[] {"銷售商主要职责1","銷售商主要职责2","銷售商主要职责3","銷售商主要职责4"};
//		 	
//	        
//		 String path = "C:\\Users\\18621\\Desktop\\1.docx";
//			
//	        for(int n=0;n<1;n++) {
//	        	Document doc = new Document();
//				   
//		        doc.loadFromFile(path);
//	    	    Table table = new Table(doc, true);
//			    table.resetCells(5, 2);
//			    String[][] data ={
//		                    new String[]{"销售服务机构", distributors[n]},
//		                    new String[]{"住址", distributorAddresss[n]},
//		                    new String[]{"客户服务热线", customerServiceHotlines[n]},
//		                    new String[]{"官方网站", officialWebsites[n]},
//		                    new String[]{"主要职责", mainDutys[n]}
//		            };
//			    for (int i = 0; i < data.length; i++) {
//			        TableRow dataRow = table.getRows().get(i);
//			        for (int j = 0; j < data[i].length; j++) {
//			        	if(j==0) {
//			        		dataRow.getCells().get(j).setCellWidth(new Float(80.0), CellWidthType.Point);
//			        	}
//			            TextRange range = dataRow.getCells().get(j).addParagraph().appendText(data[i][j]);
//			            range.getOwnerParagraph().getFormat().setHorizontalAlignment(HorizontalAlignment.Center);
//			            range.getCharacterFormat().setFontName("宋体");
//			            dataRow.getRowFormat().setHorizontalAlignment(RowAlignment.Center);
//			            dataRow.getCells().get(j).getCellFormat().setVerticalAlignment(VerticalAlignment.Middle);
//			        }
//			    }
//			    
//			    
//			    Table table2 = new Table(doc, true);
//			    table2.resetCells(5, 2);
//			    String[][] data2 ={
//		                    new String[]{"销售服务机构", distributors[n]},
//		                    new String[]{"住址", distributorAddresss[n]},
//		                    new String[]{"客户服务热线", customerServiceHotlines[n]},
//		                    new String[]{"官方网站", officialWebsites[n]},
//		                    new String[]{"主要职责", mainDutys[n]}
//		            };
//			    for (int i = 0; i < data2.length; i++) {
//			        TableRow dataRow = table2.getRows().get(i);
//			        for (int j = 0; j < data2[i].length; j++) {
//			        	if(j==0) {
//			        		dataRow.getCells().get(j).setCellWidth(new Float(80.0), CellWidthType.Point);
//			        	}
//			            TextRange range = dataRow.getCells().get(j).addParagraph().appendText(data[i][j]);
//			            range.getOwnerParagraph().getFormat().setHorizontalAlignment(HorizontalAlignment.Center);
//			            range.getCharacterFormat().setFontName("宋体");
//			            dataRow.getRowFormat().setHorizontalAlignment(RowAlignment.Center);
//			            dataRow.getCells().get(j).getCellFormat().setVerticalAlignment(VerticalAlignment.Middle);
//			        }
//			    }
//			    
//			    
//			    
//			    Table table3 = new Table(doc, true);
//			    table3.resetCells(5, 2);
//			    String[][] data3 ={
//		                    new String[]{"销售服务机构", distributors[n]},
//		                    new String[]{"住址", distributorAddresss[n]},
//		                    new String[]{"客户服务热线", customerServiceHotlines[n]},
//		                    new String[]{"官方网站", officialWebsites[n]},
//		                    new String[]{"主要职责", mainDutys[n]}
//		            };
//			    for (int i = 0; i < data2.length; i++) {
//			        TableRow dataRow = table3.getRows().get(i);
//			        for (int j = 0; j < data3[i].length; j++) {
//			        	if(j==0) {
//			        		dataRow.getCells().get(j).setCellWidth(new Float(80.0), CellWidthType.Point);
//			        	}
//			            TextRange range = dataRow.getCells().get(j).addParagraph().appendText(data3[i][j]);
//			            range.getOwnerParagraph().getFormat().setHorizontalAlignment(HorizontalAlignment.Center);
//			            range.getCharacterFormat().setFontName("宋体");
//			            dataRow.getRowFormat().setHorizontalAlignment(RowAlignment.Center);
//			            dataRow.getCells().get(j).getCellFormat().setVerticalAlignment(VerticalAlignment.Middle);
//			        }
//			    }
//			    
//			   
//			    
//			    BookmarksNavigator bookmarksNavigator = new BookmarksNavigator(doc);
//		        bookmarksNavigator.moveToBookmark("销售商");	
//		        
//			    bookmarksNavigator.insertTable(table);
//			    bookmarksNavigator.insertText("\n");
//			    bookmarksNavigator.insertTable(table2);
//			    bookmarksNavigator.insertText("\n");
//			    bookmarksNavigator.insertTable(table3);
//			    bookmarksNavigator.insertText("\n");
//			   
//			   // bookmarksNavigator.insertText(distributors.length-n+"、"+distributors[n]+"\n");
//			   
//			    
//			   
//			    doc.saveToFile("C:\\Users\\18621\\Desktop\\1.docx", FileFormat.Docx);   
//		        
//		       
//	        }
//	       
//	        try ( InputStream is = new FileInputStream("C:\\Users\\18621\\Desktop\\1.docx")){
//        	 	XWPFDocument xwpfdocument = new XWPFDocument(is);  
//		        xwpfdocument.removeBodyElement(0);
//		        OutputStream os=new FileOutputStream("C:\\Users\\18621\\Desktop\\2.docx");
//		        xwpfdocument.write(os);	
//        }catch (Exception e) {
//			throw e;
//		}
//		   
//	    }

    public static void main(String[]args) throws IOException {
        getLicense();
        //加载需要添加书签的Word文档
        Document doc = new Document();
        doc.loadFromFile("C:\\Users\\Cisnao\\Desktop\\定期报告模板-公募封闭固收 .docx");

        //获取需要添加书签的段落
        //Paragraph para = doc.getSections().get(0).getParagraphs().get(9);

        //在段落起始、末尾添加书签的开始标签和结束标签，并命名书签
        for(int i=0;i<10;i++){
            Paragraph para = doc.getSections().get(0).getParagraphs().get(11);
            //BookmarkStart start = para.appendBookmarkStart("bookmark0"+i);
            //para.getItems().insert(0,start);
            //para.appendBookmarkEnd("bookmark0"+i);
            para.appendBreak(BreakType.Line_Break);
            //para.appendText(i+"XX产品（下属分类份额A）成立以来净值表现与市场指数对比");
            //para.applyStyle(String.valueOf(StyleType.Character_Style));
            //para.getItems().insert(0,start);
            TextRange tr1 = para.appendText(i+"XX产品（下属分类份额A）成立以来净值表现与市场指数对比");
            tr1.getCharacterFormat().setTextColor(new Color(104, 25, 131));
            tr1.getCharacterFormat().setFontSize(10);
            tr1.getCharacterFormat().setBold(true);
            //tr1.getCharacterFormat.Border.BorderType = BorderStyle.DashDotStroker;
            //tr1.getCharacterFormat();
            //tr1.setTextWrappingStyle();

            para.appendBreak(BreakType.Line_Break);

            //para.appendPicture("C:\\Users\\Cisnao\\Desktop\\同步改造点.png");

              DocPicture picture =
                  doc.getSections()
                      .get(0)
                      .getParagraphs()
                      .get(11)
                      .appendPicture("C:\\Users\\Cisnao\\Desktop\\同步改造点.png");
                    //picture.TextWrappingStyle = ;
                    picture.setTextWrappingStyle(TextWrappingStyle.Inline);
                    //para.getItems().insert(0,start);

            para.appendBreak(BreakType.Line_Break);
            para.appendBreak(BreakType.Line_Break);
        }

        /*String[][] data =
                {
                        new String[]{"产品", "版本", "发布日期"},
                        new String[]{"Spire.Doc for Java", "V2.7.2", "2019-07-24"},
                };
        //创建表格
        Table table = new Table(doc, true);
        table.resetCells(2, 3);
        for (int i = 0; i < data.length; i++) {
            TableRow dataRow = table.getRows().get(i);
            for (int j = 0; j < data[i].length; j++) {
                TextRange range = dataRow.getCells().get(j).addParagraph().appendText(data[i][j]);
                range.getOwnerParagraph().getFormat().setHorizontalAlignment(HorizontalAlignment.Center);
                range.getCharacterFormat().setFontName("楷体");
                dataRow.getRowFormat().setHorizontalAlignment(RowAlignment.Center);
                dataRow.getCells().get(j).getCellFormat().setVerticalAlignment(VerticalAlignment.Middle);
            }
        }
        //定位到指定书签位置,添加表格
        BookmarksNavigator bookmarksNavigator = new BookmarksNavigator(doc);
        bookmarksNavigator.moveToBookmark("comparisonChart");

        bookmarksNavigator.insertTable(table);
        bookmarksNavigator.insertText("测试标题");*/


        String path = "C:\\Users\\Cisnao\\Desktop\\appendbookmark.docx";
        // 保存文档
        doc.saveToFile(path, FileFormat.Docx);
        doc.dispose();
        replaceStr(path);
    }

    public static void getLicense() {
        try {
            // license.xml应放在Resources路径下
            InputStream is = WordToPdfUtil.class.getClassLoader().getResourceAsStream("license.xml");
            License aposeLic = new License();
            aposeLic.setLicense(is);
        } catch (Exception e) {
            //logger.error(e.getMessage(), e);
        }
    }
    public static void replaceStr(String inputPath) throws IOException {
        String finalPath =null;
        //重新读取生成的文档
        InputStream is = null;
        is = new FileInputStream(inputPath);
        Document doc = new Document();
        XWPFDocument document = null;
        document = new XWPFDocument(is);
        //doc.loadFromFile("C:\\Users\\Cisnao\\Desktop\\定期报告模板-公募封闭固收 .docx");
        document.removeBodyElement(0);
        //String timeStr = DateUtils.getCurrentTime_yyyyMMddHHmmssSSS();
        //String outPath = basePath + "temp_final" + File.separator + timeStr + File.separator;
        String outPath = "C:\\Users\\Cisnao\\Desktop\\appendbookmark.docx";
        finalPath = outPath;
        File tempPath = new File(outPath);
        //如果输出目标文件夹不存在，则创建
        if (!tempPath.exists()) {
            tempPath.mkdirs();
        }
        OutputStream os=new FileOutputStream(finalPath);
        try {
            document.write(os);
            //log.info("生成docx文档成功！");
        } catch (Exception e) {
            e.printStackTrace();
        }
        //return finalPath;
    }

}