package com.kayak.dps.valtabimp.util;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 解析zip文件
 * @author Lenovo
 *
 */
public class ZipUtil {

	 
	 
	 public static void main(String args[]) {
//			String file = "c://ssi.zip";
//			String saveRootDirectory = "c://test/";
//			new ZipUtil().zipFileRead(file, saveRootDirectory);
			
			String s="23123123.1234567891011121617";
			Double sd=Double.valueOf(s);
			
			
			
			System.out.println("-----------"+new BigDecimal(s));
			System.out.println("-----------"+new BigDecimal(1.001000));
			System.out.println("-----------"+BigDecimal.valueOf(1.001000));
			System.out.println("-----------"+new BigDecimal("1.001000"));
			
			System.out.println("-----------"+new BigDecimal("1.001000").divide(new BigDecimal(1.001),18,BigDecimal.ROUND_HALF_UP));
			
			System.out.println("-----------"+new BigDecimal("1.001000").divide(BigDecimal.valueOf(1.001),18,BigDecimal.ROUND_HALF_UP));
			System.out.println("-----------"+new BigDecimal("1.001000").divide(new BigDecimal("1.001"),18,BigDecimal.ROUND_HALF_UP));
		}
	 

	 
		
		
		  /**
		    * @Author：
		    * @Description：获取某个目录下所有直接下级文件，包括目录下的子目录的下的文件，所以用递归获取
		    * @Date：
		    */
		    public static List<File> getFiles(String path) {
		    	List<File> files = new ArrayList<>();
		        File file = new File(path);
		        if(!file.exists()) {//文件夹不存在
		        	return files;
		        }
		        if(file.isDirectory()) {
			        files=getFls(file);
		        }else {
		        	files.add(file);
		        }

		        return files;
		    }
		    
		    private static List<File> getFls(File file) {
		    	List<File> filels=new ArrayList<>();
		    	File[] tempList =file.listFiles();
		        for (int i = 0; i < tempList.length; i++) {
		            if (tempList[i].isDirectory()) {
		            	List<File> filesz=getFls(tempList[i]);
		            	filels.addAll(filesz);
		            }else {
		            	filels.add(tempList[i]);
		            }
		        }
		    	return filels;
		    }

		

		
		 public static String zipExportFiles(String outputFile, String files[]) throws IOException {
			  
			          File f = new File(outputFile);
			          File parent = f.getParentFile();
			          if (!parent.exists()) {
			              parent.mkdirs();
			          }
			  
			          // Create a buffer for reading the files
			          byte[] buf = new byte[1024];
			  
			          ZipOutputStream out = new ZipOutputStream(new FileOutputStream(f));
			          // Compress the files
			          for (int i = 0, j = files.length; i < j; i++) {
			              File entryfile = new File(files[i]);
			              FileInputStream in = new FileInputStream(entryfile);
			             // Add ZIP entry to output stream.
			  
			              out.putNextEntry(new ZipEntry(entryfile.getName()));
			              // Transfer bytes from the file to the ZIP file
			              int len = 0;
			              while ((len = in.read(buf)) > 0) {
			                  out.write(buf, 0, len);
			              }
			              // Complete the entry
			              out.closeEntry();
			              in.close();
			          }
			  
			          out.close();
			          return f.getAbsolutePath();
			      }

}
