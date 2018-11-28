package com.synpower.util;

import java.awt.Color;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import sun.misc.BASE64Encoder;




/*****************************************************************************
 * @Package: com.synpower.util
 * ClassName: DownloadUtils
 * @Description: 下载编码处理
 * @version
 *-----------------------------------------------------------------------------
 *     VERSION           TIME                BY           CHANGE/COMMENT
 *-----------------------------------------------------------------------------
 *      1.0    2017年9月17日上午11:11:16   SP0011             create
 *-----------------------------------------------------------------------------
 * @Copyright 2017 www.synpowertech.com Inc. All rights reserved. 
 * 注意：本内容仅限于成都协能共创科技内部传阅，禁止外泄以及用于其他的商业目
 ******************************************************************************/
public class DownloadUtils {
	private static Logger logger = Logger.getLogger(DownloadUtils.class);
	/** 
	  * @Title:  base64EncodeFileName 
	  * @Description:  进行BASE64Encoder编码(用于导出弹窗浏览器兼容)
	  * @param fileName
	  * @return: String
	  * @lastEditor:  SP0011
	  * @lastEdit:  2018年2月10日上午10:20:47
	*/ 
	public static String base64EncodeFileName(String fileName) {
		
		BASE64Encoder base64Encoder = new BASE64Encoder();
		try {
			return "=?UTF-8?B?"
					+ new String(base64Encoder.encode(fileName
							.getBytes("UTF-8"))) + "?=";
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	public static void exportExcel(HttpServletRequest request, HttpServletResponse response,List<Map<String, Object>> list,
			List<String> titleList ,String fileName,List<String> paramList )
			throws ServletException, IOException {

		//临时文件存放路径
		String path = request.getSession().getServletContext().getRealPath("/");
		path=path+File.separator+"excel";
		File pathFile=new File(path);
		if (!pathFile.exists()) {
			pathFile.mkdirs();
		}
		String tempFileName=fileName+""+System.currentTimeMillis();
		File temp=new File(path+ File.separator+tempFileName+".xls");
		//火狐的编码是base64 IE的编码是URL
		String agent = request.getHeader("user-agent");
		//设置单sheet最大行数
		int maxSheetLine=65000;
		int dataSize=list.size();
		//导出excel
		

            /** **********创建工作簿************ */
			try {
			/** **********创建工作表************ */
			WritableWorkbook workbook = Workbook.createWorkbook(temp);
			int maxSheet=0;
			if (dataSize!=0) {
				if (dataSize%maxSheet!=0) {
					maxSheet=dataSize/maxSheet+1;
				}else{
					maxSheet=dataSize/maxSheet;
				}
			}
			for (int sh = 0; sh < maxSheet; sh++) {
				int startLine=sh*maxSheetLine;
				int endLine=(sh+1)*maxSheetLine;
				if (endLine>dataSize) {
					endLine=dataSize;
				}
				int sheetLine=endLine-startLine;
	            WritableSheet sheet = workbook.createSheet("Sheet"+(sh+1), sh);
	            /** **********设置纵横打印（默认为纵打）、打印纸***************** */
	            jxl.SheetSettings sheetset = sheet.getSettings();
	            sheetset.setProtected(false);
	            /** **********设置页边距(0.1d=0.26cm)  ***************** */
	            sheet.getSettings().setBottomMargin(0.7d);  
	            sheet.getSettings().setTopMargin(0.7d);  
	            sheet.getSettings().setLeftMargin(0.75d);  
	            sheet.getSettings().setRightMargin(0.75d);
	            /** **********设置是否显示行数列数编号  ***************** */
	            sheet.getSettings().setPrintHeaders(true); 
	            /** ************设置单元格字体************** */
	            WritableFont fontTable=new WritableFont(WritableFont.createFont("微软雅黑"),16,WritableFont.BOLD,false,UnderlineStyle.NO_UNDERLINE,Colour.WHITE);
	            WritableCellFormat formatTable = new WritableCellFormat(fontTable); 
	            Color color = Color.decode("#1D933A"); // 自定义的颜色
	            workbook.setColourRGB(Colour.ORANGE, color.getRed(),
	            color.getGreen(), color.getBlue());
	            formatTable.setBackground(Colour.ORANGE);
	            WritableFont font=new WritableFont(WritableFont.createFont("微软雅黑"),12,WritableFont.NO_BOLD );
	            WritableCellFormat format1 = new WritableCellFormat(font); 
	            Color colorTitle= Color.decode("#E4F7D6");
	            workbook.setColourRGB(Colour.AQUA, colorTitle.getRed(),
	            		colorTitle.getGreen(), colorTitle.getBlue());
	            format1.setBackground(Colour.AQUA);
	            WritableFont font2=new WritableFont(WritableFont.createFont("微软雅黑"),12,WritableFont.NO_BOLD );
	            WritableCellFormat format2 = new WritableCellFormat(font2); 
	            WritableFont font3=new WritableFont(WritableFont.createFont("微软雅黑"),12,WritableFont.BOLD );
	            WritableCellFormat format3 = new WritableCellFormat(font3); 
	            // 把水平对齐方式指定为居中 
	            formatTable.setAlignment(jxl.format.Alignment.CENTRE);
	            // 把垂直对齐方式指定为居中 
	            formatTable.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
	            formatTable.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
	            // 把水平对齐方式指定为居中 
	            format1.setAlignment(jxl.format.Alignment.CENTRE);
	            format1.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
	            // 把垂直对齐方式指定为居中 
	            format1.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
	            // 把水平对齐方式指定为居中 
	            format2.setAlignment(jxl.format.Alignment.CENTRE);
	            // 把垂直对齐方式指定为居中 
	            format2.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
	            format2.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
	            // 把水平对齐方式指定为居中 
	            format3.setAlignment(jxl.format.Alignment.CENTRE);
	            // 把垂直对齐方式指定为居中 
	            format3.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
	            //设置边框;  
	            format3.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
	            WritableFont NormalFont = new WritableFont(WritableFont.ARIAL, 10);
	            WritableFont BoldFont = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
	            //给sheet电子版中所有的列设置默认的列的宽度;  
				sheet.getSettings().setDefaultColumnWidth(32);
				sheet.getSettings().setDefaultRowHeight(300);
				//设置背景颜色;  
				//format1.setBackground(Colour.DARK_BLUE2);  
				//format2.setBorder(Border.ALL, BorderLineStyle.DASH_DOT);
	            /** ************以下设置三种单元格样式，灵活备用************ */
	            // 用于标题居中
	            WritableCellFormat wcf_center = new WritableCellFormat(BoldFont);
	            wcf_center.setBorder(Border.ALL, BorderLineStyle.THIN); // 线条
	            wcf_center.setVerticalAlignment(VerticalAlignment.CENTRE); // 文字垂直对齐
	            wcf_center.setAlignment(Alignment.CENTRE); // 文字水平对齐
	            wcf_center.setWrap(true); // 文字是否换行
	
	            // 用于正文居左
	            WritableCellFormat wcf_left = new WritableCellFormat(NormalFont);
	            wcf_left.setBorder(jxl.format.Border.NONE, BorderLineStyle.MEDIUM); // 线条
	            wcf_left.setVerticalAlignment(VerticalAlignment.CENTRE); // 文字垂直对齐
	            wcf_left.setWrap(false); // 文字是否换行
	            wcf_left.setAlignment(Alignment.LEFT);
	            List<Label>labelList=new ArrayList<Label>();
	            //表头
	            // 第一个参数代表列 ，第二个参数代表行
				sheet.mergeCells(0, 0, titleList.size()-1 ,0);   
				labelList.add(new Label(0,0, fileName,formatTable));
				sheet.setRowView( 0 , 800 );
				sheet.setRowView( 1 , 500 );
				for (int i = 0 ,len=titleList.size(); i < len; i++) {
					 Label label= new Label(i, 1, titleList.get(i)+"",format1);
					 labelList.add(label);
				}
				for (int i = 0,len=labelList.size(); i <len ; i++) {
					sheet.addCell(labelList.get(i));
				}
				if(!list.isEmpty() && list != null){
				// 第4行第2列
				int line=startLine;
				for (int i = 0; i < sheetLine; i++) {
					Map<String, Object> map=list.get(line);
					for (int j = 0; j <paramList.size(); j++) {
						Label label=new Label(j, i+2, map.get(paramList.get(j))+"",format2);
						sheet.addCell(label);
						
					}
					sheet.setRowView( i+2 , 450 );
					line++;
				}
				for (int i = 0,len=paramList.size(); i < len; i++) {
					sheet.setColumnView( i , 30 );
				}
				}
			}
				// 将格子放入工作簿中
			workbook.write();
			workbook.close();
			} catch (Exception e) {
				e.printStackTrace();
			} 
			// -------------------
			
			
			String fileName2= null;
			if(agent.contains("Firefox")){
				//火狐浏览器  base64的编码处理
				fileName2 = DownloadUtils.base64EncodeFileName(fileName+".xls");
			}else{
				//ie 或者谷歌浏览器URL编码处理
				fileName2=URLEncoder.encode(fileName+".xls","utf-8");
			}
			
			InputStream input = new BufferedInputStream(new FileInputStream(path+File.separator+tempFileName+".xls"));  
		    response.reset();// 清空输出流
		    response.addHeader("Content-Disposition", "attachment;filename="  
                    + new String(fileName2.getBytes()));  
            response.addHeader("Content-Length", "" + new File(path+File.separator+tempFileName+".xls").length());  
            OutputStream out = new BufferedOutputStream(  
                    response.getOutputStream());  
            response.setContentType("application/vnd.ms-excel;charset=gb2312"); 
			int flag = 0;
			while ((flag = input.read()) != -1) {
				out.write(flag);
			}
			out.flush();
			out.close();
			input.close();
			if (temp.exists()&&temp.isFile()) {
				temp.delete();
			}
	}
	public static void exportPlantExcel(HttpServletRequest request, HttpServletResponse response,List<Map<String, Object>> list,
			List<String> titleList ,String fileName,List<String> paramList )
			throws ServletException, IOException {

		//临时文件存放路径
		String path = request.getSession().getServletContext().getRealPath("/");
		path=path+File.separator+"excel";
		File pathFile=new File(path);
		if (!pathFile.exists()) {
			pathFile.mkdirs();
		}
		String tempFileName=fileName+""+System.currentTimeMillis();
		File temp=new File(path+ File.separator+tempFileName+".xls");
		String tempFilePath=path+ File.separator+tempFileName+".xls";
		//火狐的编码是base64 IE的编码是URL
		String agent = request.getHeader("user-agent");
		//需要导出的数据
		int dataSize=list.size();
		int maxSheetLine=50000;
		

		try {
			/** **********创建工作簿************ */
		WritableWorkbook workbook = Workbook.createWorkbook(temp);
		int maxSheet=0;
		//只渲染表头数据
		if (dataSize==1) {
			maxSheet=1;
		}else {
			if ((dataSize-1)%maxSheetLine!=0) {
				maxSheet=(dataSize-1)/maxSheetLine+1;
			}else{
				maxSheet=(dataSize-1)/maxSheetLine;
			}
		}
		for (int sh = 0; sh < maxSheet; sh++) {
			int startLine=sh*maxSheetLine+1;
			int endLine=(sh+1)*maxSheetLine+1;
			if (endLine>dataSize) {
				endLine=dataSize;
			}
			if (startLine>dataSize) {
				startLine=dataSize;
			}
			int sheetLine=endLine-startLine;
			if (sh!=0) {
				InputStream in=new FileInputStream(tempFilePath);
				Workbook wb=Workbook.getWorkbook(in);
	            workbook=wb.createWorkbook(new File(tempFilePath),wb);
			}
			/** **********创建工作表************ */
            WritableSheet sheet = workbook.createSheet("Sheet"+(sh+1), sh);
            //29 147 58   228 247 214
            /** **********设置纵横打印（默认为纵打）、打印纸***************** */
            jxl.SheetSettings sheetset = sheet.getSettings();
            sheetset.setProtected(false);
            /** **********设置页边距(0.1d=0.26cm)  ***************** */
            sheet.getSettings().setBottomMargin(0.7d);  
            sheet.getSettings().setTopMargin(0.7d);  
            sheet.getSettings().setLeftMargin(0.75d);  
            sheet.getSettings().setRightMargin(0.75d);
            /** **********设置是否显示行数列数编号  ***************** */
            sheet.getSettings().setPrintHeaders(true); 
            /** ************设置单元格字体************** */
            WritableFont fontTable=new WritableFont(WritableFont.createFont("微软雅黑"),16,WritableFont.BOLD,false,UnderlineStyle.NO_UNDERLINE,Colour.WHITE);
            WritableCellFormat formatTable = new WritableCellFormat(fontTable); 
            Color color = Color.decode("#1D933A"); // 自定义的颜色
            workbook.setColourRGB(Colour.ORANGE, color.getRed(),
            color.getGreen(), color.getBlue());
            formatTable.setBackground(Colour.ORANGE);
            WritableFont font=new WritableFont(WritableFont.createFont("微软雅黑"),12,WritableFont.NO_BOLD );
            WritableCellFormat format1 = new WritableCellFormat(font); 
            Color colorTitle= Color.decode("#E4F7D6");
            workbook.setColourRGB(Colour.AQUA, colorTitle.getRed(),
            		colorTitle.getGreen(), colorTitle.getBlue());
            format1.setBackground(Colour.AQUA);
            WritableFont font2=new WritableFont(WritableFont.createFont("微软雅黑"),12,WritableFont.NO_BOLD );
            WritableCellFormat format2 = new WritableCellFormat(font2); 
            WritableFont font3=new WritableFont(WritableFont.createFont("微软雅黑"),12,WritableFont.BOLD );
            WritableCellFormat format3 = new WritableCellFormat(font3); 
            // 把水平对齐方式指定为居中 
            formatTable.setAlignment(jxl.format.Alignment.CENTRE);
            // 把垂直对齐方式指定为居中 
            formatTable.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
            formatTable.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
            // 把水平对齐方式指定为居中 
            format1.setAlignment(jxl.format.Alignment.CENTRE);
            format1.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
            // 把垂直对齐方式指定为居中 
            format1.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
            // 把水平对齐方式指定为居中 
            format2.setAlignment(jxl.format.Alignment.CENTRE);
            // 把垂直对齐方式指定为居中 
            format2.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
            format2.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
            // 把水平对齐方式指定为居中 
            format3.setAlignment(jxl.format.Alignment.CENTRE);
            // 把垂直对齐方式指定为居中 
            format3.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
            //设置边框;  
            format3.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
            WritableFont NormalFont = new WritableFont(WritableFont.ARIAL, 10);
            WritableFont BoldFont = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
            //给sheet电子版中所有的列设置默认的列的宽度;  
			sheet.getSettings().setDefaultColumnWidth(32);
			sheet.getSettings().setDefaultRowHeight(300);
			//设置背景颜色;  
			//format1.setBackground(Colour.DARK_BLUE2);  
			//format2.setBorder(Border.ALL, BorderLineStyle.DASH_DOT);
            /** ************以下设置三种单元格样式，灵活备用************ */
            // 用于标题居中
            WritableCellFormat wcf_center = new WritableCellFormat(BoldFont);
            wcf_center.setBorder(Border.ALL, BorderLineStyle.THIN); // 线条
            wcf_center.setVerticalAlignment(VerticalAlignment.CENTRE); // 文字垂直对齐
            wcf_center.setAlignment(Alignment.CENTRE); // 文字水平对齐
            wcf_center.setWrap(true); // 文字是否换行

            // 用于正文居左
            WritableCellFormat wcf_left = new WritableCellFormat(NormalFont);
            wcf_left.setBorder(jxl.format.Border.NONE, BorderLineStyle.MEDIUM); // 线条
            wcf_left.setVerticalAlignment(VerticalAlignment.CENTRE); // 文字垂直对齐
            wcf_left.setWrap(false); // 文字是否换行
            wcf_left.setAlignment(Alignment.LEFT);
            List<Label>labelList=new ArrayList<Label>();
            //表头
            // 第一个参数代表列 ，第二个参数代表行
			sheet.mergeCells(0, 0, titleList.size()-1 ,0);   
			labelList.add(new Label(0,0, fileName,formatTable));
			Map<String, Object> dataMap=list.get(0);
			
			Label labelTable=new Label(0, 1, dataMap.get(paramList.get(0))+"",format3);
			labelList.add(labelTable);
			sheet.mergeCells(1, 1, 2 ,1);   
			Label labelTable2=new Label(1, 1, dataMap.get(paramList.get(1))+"",format2);
			labelList.add(labelTable2);
			
			Label labelTable3=new Label(3, 1, dataMap.get(paramList.get(2))+"",format3);
			labelList.add(labelTable3);
			sheet.mergeCells(4, 1, 5 ,1);   
			Label labelTable4=new Label(4, 1, dataMap.get(paramList.get(3))+"",format2);
			labelList.add(labelTable4);
			
			Label labelTable5=new Label(6, 1, dataMap.get(paramList.get(4))+"",format3);
			labelList.add(labelTable5);
			sheet.mergeCells(7, 1, 8 ,1);
			Label labelTable6=new Label(7, 1, dataMap.get(paramList.get(5))+"",format2);
			labelList.add(labelTable6);
			sheet.setRowView( 0 , 800 );
			sheet.setRowView( 1 , 450 );
			sheet.setRowView( 2 , 500 );
			for (int i = 0,len=titleList.size(); i < len; i++) {
				 Label label= new Label(i, 2, titleList.get(i)+"",format1);
				 labelList.add(label);
			}
			for (int i = 0,len=labelList.size(); i < len; i++) {
				sheet.addCell(labelList.get(i));
			}
			if(!list.isEmpty() && list != null){
			// 第4行第2列
			int line=startLine;
			for (int i = 0; i < sheetLine; i++) {
				Map<String, Object> map=list.get(line);
				for (int j = 0; j <paramList.size(); j++) {
					Label label=new Label(j, i+3, map.get(paramList.get(j))+"",format2);
					sheet.addCell(label);
					
				}
				sheet.setRowView( i+3 , 450 );
				line++;
			}
			for (int i = 0; i < paramList.size(); i++) {
				sheet.setColumnView( i , 30 );
			}
			}
			// 将格子放入工作簿中
			workbook.write();
			workbook.close();
		   }
			} catch (Exception e) {
				e.printStackTrace();
			} 
			// -------------------
			
			
			String fileName2= null;
			if(agent.contains("Firefox")){
				//火狐浏览器  base64的编码处理
				fileName2 = DownloadUtils.base64EncodeFileName(fileName+".xls");
			}else{
				//ie 或者谷歌浏览器URL编码处理
				fileName2=URLEncoder.encode(fileName+".xls","utf-8");
			}
			
			InputStream input = new BufferedInputStream(new FileInputStream(path+File.separator+tempFileName+".xls"));  
		    response.reset();// 清空输出流
		    response.addHeader("Content-Disposition", "attachment;filename="  
                    + new String(fileName2.getBytes()));  
            response.addHeader("Content-Length", "" + new File(path+ File.separator+tempFileName+".xls").length());  
            OutputStream out = new BufferedOutputStream(  
                    response.getOutputStream());  
            response.setContentType("application/vnd.ms-excel;charset=gb2312"); 
			int flag = 0;
			while ((flag = input.read()) != -1) {
				out.write(flag);
			}
			out.flush();
			out.close();
			input.close();
			if (temp.exists()&&temp.isFile()) {
				temp.delete();
			}
	}
	
	public static void exportPlantExcelForElectric(HttpServletRequest request, HttpServletResponse response,List<Map<String, Object>> list,
			List<String> titleList ,String fileName,List<String> paramList )
			throws ServletException, IOException {

		//临时文件存放路径
		String path = request.getSession().getServletContext().getRealPath("/");
		path=path+File.separator+"excel";
		File pathFile=new File(path);
		if (!pathFile.exists()) {
			pathFile.mkdirs();
		}
		String tempFileName=fileName+""+System.currentTimeMillis();
		File temp=new File(path+ File.separator+tempFileName+".xls");
		//火狐的编码是base64 IE的编码是URL
		String agent = request.getHeader("user-agent");
		//需要导出的数据
		//导出excel
		

            /** **********创建工作簿************ */
			try {
			/** **********创建工作表************ */
			WritableWorkbook workbook = Workbook.createWorkbook(temp);


            WritableSheet sheet = workbook.createSheet("Sheet1", 0);
            //29 147 58   228 247 214
            /** **********设置纵横打印（默认为纵打）、打印纸***************** */
            jxl.SheetSettings sheetset = sheet.getSettings();
            sheetset.setProtected(false);
            /** **********设置页边距(0.1d=0.26cm)  ***************** */
            sheet.getSettings().setBottomMargin(0.7d);  
            sheet.getSettings().setTopMargin(0.7d);  
            sheet.getSettings().setLeftMargin(0.75d);  
            sheet.getSettings().setRightMargin(0.75d);
            /** **********设置是否显示行数列数编号  ***************** */
            sheet.getSettings().setPrintHeaders(true); 
            /** ************设置单元格字体************** */
            WritableFont fontTable=new WritableFont(WritableFont.createFont("微软雅黑"),16,WritableFont.BOLD,false,UnderlineStyle.NO_UNDERLINE,Colour.WHITE);
            WritableCellFormat formatTable = new WritableCellFormat(fontTable); 
            Color color = Color.decode("#1D933A"); // 自定义的颜色
            workbook.setColourRGB(Colour.ORANGE, color.getRed(),
            color.getGreen(), color.getBlue());
            formatTable.setBackground(Colour.ORANGE);
            WritableFont font=new WritableFont(WritableFont.createFont("微软雅黑"),12,WritableFont.NO_BOLD );
            WritableCellFormat format1 = new WritableCellFormat(font); 
            Color colorTitle= Color.decode("#E4F7D6");
            workbook.setColourRGB(Colour.AQUA, colorTitle.getRed(),
            		colorTitle.getGreen(), colorTitle.getBlue());
            format1.setBackground(Colour.AQUA);
            WritableFont font2=new WritableFont(WritableFont.createFont("微软雅黑"),12,WritableFont.NO_BOLD );
            WritableCellFormat format2 = new WritableCellFormat(font2); 
            WritableFont font3=new WritableFont(WritableFont.createFont("微软雅黑"),12,WritableFont.BOLD );
            WritableCellFormat format3 = new WritableCellFormat(font3); 
            // 把水平对齐方式指定为居中 
            formatTable.setAlignment(jxl.format.Alignment.CENTRE);
            // 把垂直对齐方式指定为居中 
            formatTable.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
            formatTable.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
            // 把水平对齐方式指定为居中 
            format1.setAlignment(jxl.format.Alignment.CENTRE);
            format1.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
            // 把垂直对齐方式指定为居中 
            format1.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
            // 把水平对齐方式指定为居中 
            format2.setAlignment(jxl.format.Alignment.CENTRE);
            // 把垂直对齐方式指定为居中 
            format2.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
            format2.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
            // 把水平对齐方式指定为居中 
            format3.setAlignment(jxl.format.Alignment.CENTRE);
            // 把垂直对齐方式指定为居中 
            format3.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
            //设置边框;  
            format3.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
            WritableFont NormalFont = new WritableFont(WritableFont.ARIAL, 10);
            WritableFont BoldFont = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
            //给sheet电子版中所有的列设置默认的列的宽度;  
			sheet.getSettings().setDefaultColumnWidth(32);
			sheet.getSettings().setDefaultRowHeight(300);
			//设置背景颜色;  
			//format1.setBackground(Colour.DARK_BLUE2);  
			//format2.setBorder(Border.ALL, BorderLineStyle.DASH_DOT);
            /** ************以下设置三种单元格样式，灵活备用************ */
            // 用于标题居中
            WritableCellFormat wcf_center = new WritableCellFormat(BoldFont);
            wcf_center.setBorder(Border.ALL, BorderLineStyle.THIN); // 线条
            wcf_center.setVerticalAlignment(VerticalAlignment.CENTRE); // 文字垂直对齐
            wcf_center.setAlignment(Alignment.CENTRE); // 文字水平对齐
            wcf_center.setWrap(true); // 文字是否换行

            // 用于正文居左
            WritableCellFormat wcf_left = new WritableCellFormat(NormalFont);
            wcf_left.setBorder(jxl.format.Border.NONE, BorderLineStyle.MEDIUM); // 线条
            wcf_left.setVerticalAlignment(VerticalAlignment.CENTRE); // 文字垂直对齐
            wcf_left.setWrap(false); // 文字是否换行
            wcf_left.setAlignment(Alignment.LEFT);
            List<Label>labelList=new ArrayList<Label>();
            //表头
            // 第一个参数代表列 ，第二个参数代表行
			sheet.mergeCells(0, 0, titleList.size()-1 ,0);   
			labelList.add(new Label(0,0, fileName,formatTable));
			Map<String, Object> dataMap=list.get(0);
			
			Label labelTable=new Label(0, 1, dataMap.get(paramList.get(0))+"",format3);
			labelList.add(labelTable);
			sheet.mergeCells(1, 1, 2 ,1);   
			Label labelTable2=new Label(1, 1, dataMap.get(paramList.get(2))+"",format2);
			labelList.add(labelTable2);
			
			Label labelTable3=new Label(3, 1, dataMap.get(paramList.get(3))+"",format3);
			labelList.add(labelTable3);
			Label labelTable4=new Label(4, 1, dataMap.get(paramList.get(4))+"",format2);
			labelList.add(labelTable4);
			
			Label labelTable5=new Label(5, 1, dataMap.get(paramList.get(5))+"",format3);
			labelList.add(labelTable5);
			Label labelTable6=new Label(6, 1, dataMap.get(paramList.get(6))+"",format2);
			labelList.add(labelTable6);
			sheet.setRowView( 0 , 800 );
			sheet.setRowView( 1 , 450 );
			sheet.setRowView( 2 , 500 );
			for (int i = 0; i < titleList.size(); i++) {
				 Label label= new Label(i, 2, titleList.get(i)+"",format1);
				 labelList.add(label);
			}
			for (int i = 0; i < labelList.size(); i++) {
				sheet.addCell(labelList.get(i));
			}
			if(!list.isEmpty() && list != null){
			// 第4行第2列
			for (int i = 0; i < list.size()-1; i++) {
				Map<String, Object> map=list.get(i+1);
				for (int j = 0; j <paramList.size(); j++) {
					Label label=new Label(j, i+3, map.get(paramList.get(j))+"",format2);
					sheet.addCell(label);
					
				}
				sheet.setRowView( i+3 , 450 );
			}
			for (int i = 0; i < paramList.size(); i++) {
				sheet.setColumnView( i , 30 );
			}
			}
				// 将格子放入工作簿中
			workbook.write();
			workbook.close();
			} catch (Exception e) {
				e.printStackTrace();
			} 
			// -------------------
			
			
			String fileName2= null;
			if(agent.contains("Firefox")){
				//火狐浏览器  base64的编码处理
				fileName2 = DownloadUtils.base64EncodeFileName(fileName+".xls");
			}else{
				//ie 或者谷歌浏览器URL编码处理
				fileName2=URLEncoder.encode(fileName+".xls","utf-8");
			}
			
			InputStream input = new BufferedInputStream(new FileInputStream(path+File.separator+tempFileName+".xls"));  
		    response.reset();// 清空输出流
		    response.addHeader("Content-Disposition", "attachment;filename="  
                    + new String(fileName2.getBytes()));  
            response.addHeader("Content-Length", "" + new File(path+ File.separator+tempFileName+".xls").length());  
            OutputStream out = new BufferedOutputStream(  
                    response.getOutputStream());  
            response.setContentType("application/vnd.ms-excel;charset=gb2312"); 
			int flag = 0;
			while ((flag = input.read()) != -1) {
				out.write(flag);
			}
			out.flush();
			out.close();
			input.close();
			if (temp.exists()&&temp.isFile()) {
				temp.delete();
			}
	}
	public static void exportPlantExcelStoredOnly(HttpServletRequest request, HttpServletResponse response,List<Map<String, Object>> list,
			List<String> titleList ,String fileName,List<String> paramList )
					throws ServletException, IOException {
		
		//临时文件存放路径
		String path = request.getSession().getServletContext().getRealPath("/");
		path=path+File.separator+"excel";
		File pathFile=new File(path);
		if (!pathFile.exists()) {
			pathFile.mkdirs();
		}
		String tempFileName=fileName+""+System.currentTimeMillis();
		File temp=new File(path+ File.separator+tempFileName+".xls");
		//火狐的编码是base64 IE的编码是URL
		String agent = request.getHeader("user-agent");
		//需要导出的数据
		//导出excel
		
		
		/** **********创建工作簿************ */
		try {
			/** **********创建工作表************ */
			WritableWorkbook workbook = Workbook.createWorkbook(temp);
			
			
			WritableSheet sheet = workbook.createSheet("Sheet1", 0);
			//29 147 58   228 247 214
			/** **********设置纵横打印（默认为纵打）、打印纸***************** */
			jxl.SheetSettings sheetset = sheet.getSettings();
			sheetset.setProtected(false);
			/** **********设置页边距(0.1d=0.26cm)  ***************** */
			sheet.getSettings().setBottomMargin(0.7d);  
			sheet.getSettings().setTopMargin(0.7d);  
			sheet.getSettings().setLeftMargin(0.75d);  
			sheet.getSettings().setRightMargin(0.75d);
			/** **********设置是否显示行数列数编号  ***************** */
			sheet.getSettings().setPrintHeaders(true); 
			/** ************设置单元格字体************** */
			WritableFont fontTable=new WritableFont(WritableFont.createFont("微软雅黑"),16,WritableFont.BOLD,false,UnderlineStyle.NO_UNDERLINE,Colour.WHITE);
			WritableCellFormat formatTable = new WritableCellFormat(fontTable); 
			Color color = Color.decode("#1D933A"); // 自定义的颜色
			workbook.setColourRGB(Colour.ORANGE, color.getRed(),
					color.getGreen(), color.getBlue());
			formatTable.setBackground(Colour.ORANGE);
			WritableFont font=new WritableFont(WritableFont.createFont("微软雅黑"),12,WritableFont.NO_BOLD );
			WritableCellFormat format1 = new WritableCellFormat(font); 
			Color colorTitle= Color.decode("#E4F7D6");
			workbook.setColourRGB(Colour.AQUA, colorTitle.getRed(),
					colorTitle.getGreen(), colorTitle.getBlue());
			format1.setBackground(Colour.AQUA);
			WritableFont font2=new WritableFont(WritableFont.createFont("微软雅黑"),12,WritableFont.NO_BOLD );
			WritableCellFormat format2 = new WritableCellFormat(font2); 
			WritableFont font3=new WritableFont(WritableFont.createFont("微软雅黑"),12,WritableFont.BOLD );
			WritableCellFormat format3 = new WritableCellFormat(font3); 
			// 把水平对齐方式指定为居中 
			formatTable.setAlignment(jxl.format.Alignment.CENTRE);
			// 把垂直对齐方式指定为居中 
			formatTable.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
			formatTable.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
			// 把水平对齐方式指定为居中 
			format1.setAlignment(jxl.format.Alignment.CENTRE);
			format1.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
			// 把垂直对齐方式指定为居中 
			format1.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
			// 把水平对齐方式指定为居中 
			format2.setAlignment(jxl.format.Alignment.CENTRE);
			// 把垂直对齐方式指定为居中 
			format2.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
			format2.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
			// 把水平对齐方式指定为居中 
			format3.setAlignment(jxl.format.Alignment.CENTRE);
			// 把垂直对齐方式指定为居中 
			format3.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
			//设置边框;  
			format3.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
			WritableFont NormalFont = new WritableFont(WritableFont.ARIAL, 10);
			WritableFont BoldFont = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
			//给sheet电子版中所有的列设置默认的列的宽度;  
			sheet.getSettings().setDefaultColumnWidth(32);
			sheet.getSettings().setDefaultRowHeight(300);
			//设置背景颜色;  
			//format1.setBackground(Colour.DARK_BLUE2);  
			//format2.setBorder(Border.ALL, BorderLineStyle.DASH_DOT);
			/** ************以下设置三种单元格样式，灵活备用************ */
			// 用于标题居中
			WritableCellFormat wcf_center = new WritableCellFormat(BoldFont);
			wcf_center.setBorder(Border.ALL, BorderLineStyle.THIN); // 线条
			wcf_center.setVerticalAlignment(VerticalAlignment.CENTRE); // 文字垂直对齐
			wcf_center.setAlignment(Alignment.CENTRE); // 文字水平对齐
			wcf_center.setWrap(true); // 文字是否换行
			
			// 用于正文居左
			WritableCellFormat wcf_left = new WritableCellFormat(NormalFont);
			wcf_left.setBorder(jxl.format.Border.NONE, BorderLineStyle.MEDIUM); // 线条
			wcf_left.setVerticalAlignment(VerticalAlignment.CENTRE); // 文字垂直对齐
			wcf_left.setWrap(false); // 文字是否换行
			wcf_left.setAlignment(Alignment.LEFT);
			List<Label>labelList=new ArrayList<Label>();
			//表头
			// 第一个参数代表列 ，第二个参数代表行
			sheet.mergeCells(0, 0, titleList.size()-1 ,0);   
			labelList.add(new Label(0,0, fileName,formatTable));
			Map<String, Object> dataMap=list.get(0);
			
			Label labelTable=new Label(0, 1, dataMap.get(paramList.get(0))+"",format3);
			labelList.add(labelTable);
			Label labelTable2=new Label(1, 1, dataMap.get(paramList.get(1))+"",format2);
			labelList.add(labelTable2);
			
			Label labelTable3=new Label(2, 1, dataMap.get(paramList.get(2))+"",format3);
			labelList.add(labelTable3);
			Label labelTable4=new Label(3, 1, dataMap.get(paramList.get(3))+"",format2);
			labelList.add(labelTable4);
			
			Label labelTable5=new Label(4, 1, dataMap.get(paramList.get(4))+"",format3);
			labelList.add(labelTable5);
			//sheet.mergeCells(4, 2, 5 ,paramList.size()-1);   
			sheet.setRowView( 0 , 800 );
			sheet.setRowView( 1 , 450 );
			sheet.setRowView( 2 , 500 );
			for (int i = 0; i < titleList.size(); i++) {
				Label label= new Label(i, 2, titleList.get(i)+"",format1);
				labelList.add(label);
			}
			for (int i = 0; i < labelList.size(); i++) {
				sheet.addCell(labelList.get(i));
			}
			if(!list.isEmpty() && list != null){
				// 第4行第2列
				for (int i = 0; i < list.size()-1; i++) {
					Map<String, Object> map=list.get(i+1);
					for (int j = 0; j <paramList.size(); j++) {
						Label label=new Label(j, i+3, map.get(paramList.get(j))+"",format2);
						sheet.addCell(label);
						
					}
					sheet.setRowView( i+3 , 450 );
				}
				for (int i = 0; i < paramList.size(); i++) {
					sheet.setColumnView( i , 30 );
				}
			}
			// 将格子放入工作簿中
			workbook.write();
			workbook.close();
		} catch (Exception e) {
			e.printStackTrace();
		} 
		// -------------------
		
		
		String fileName2= null;
		if(agent.contains("Firefox")){
			//火狐浏览器  base64的编码处理
			fileName2 = DownloadUtils.base64EncodeFileName(fileName+".xls");
		}else{
			//ie 或者谷歌浏览器URL编码处理
			fileName2=URLEncoder.encode(fileName+".xls","utf-8");
		}
		
		InputStream input = new BufferedInputStream(new FileInputStream(path+File.separator+tempFileName+".xls"));  
		response.reset();// 清空输出流
		response.addHeader("Content-Disposition", "attachment;filename="  
				+ new String(fileName2.getBytes()));  
		response.addHeader("Content-Length", "" + new File(path+ File.separator+tempFileName+".xls").length());  
		OutputStream out = new BufferedOutputStream(  
				response.getOutputStream());  
		response.setContentType("application/vnd.ms-excel;charset=gb2312"); 
		int flag = 0;
		while ((flag = input.read()) != -1) {
			out.write(flag);
		}
		out.flush();
		out.close();
		input.close();
		if (temp.exists()&&temp.isFile()) {
			temp.delete();
		}
	}
	/** 
	  * @Title:  uploadPhoto 
	  * @Description:  图片上传工具类 
	  * @param request
	  * @return: String 返回图片的上传路径
	  * 		                        如果返回为空则上传失败
	  * @lastEditor:  SP0011
	  * @lastEdit:  2018年1月10日上午9:34:08
	*/ 
	public  static List<String> uploadPhoto(HttpServletRequest request,String uploadPath,String requestURL){
		File file=null;
		List<String>resultList=new ArrayList<>(1);
	    try {  
	        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;  
	
	        //MultipartFile multipartFile = multipartRequest.getFile("file");   
	        List<MultipartFile> multipartFiles=multipartRequest.getFiles("file");
	        int index = 0;
	       if (Util.isNotBlank(multipartFiles)) {
	    	   for (MultipartFile multipartFile : multipartFiles) {
	    		    index ++;
	    		   	String logoRealPathDir = uploadPath;   
			        File logoSaveFile = new File(logoRealPathDir);       
			        if(!logoSaveFile.exists()) {      
			        	logoSaveFile.mkdirs();
			        }
			        /**获取文件的后缀**/
			        String suffix = multipartFile.getOriginalFilename().substring    
			        (multipartFile.getOriginalFilename().lastIndexOf("."));   
			        /**拼成完整的文件保存路径加文件**/
			        String tempName = System.currentTimeMillis()+"";
			        String name =  tempName+index+suffix;  
			        String fileName = logoRealPathDir +"/"+name;      
			        file = new File(fileName);   
			
			        String data = requestURL+"/"+ tempName+String.valueOf(index)+suffix;  
			    	multipartFile.transferTo(file);  
			    	logger.info(" upload photo path:  "+data+"  file name "+file.getName());
			    	if (file!=null) {
						resultList.add(data);
					}
	    	   }
       	  }
        } catch (Exception e) { 
        	logger.error(" upload photo failed ",e);
        } 
	    
	    return resultList;
	}

	public static List<String> uploadExcel(HttpServletRequest request, String uploadPath) {
		File file = null;
		List<String> resultList = new ArrayList<>(1);
		try {
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			List<MultipartFile> multipartFiles = multipartRequest.getFiles("file");
			if (Util.isNotBlank(multipartFiles)) {
				for (MultipartFile multipartFile : multipartFiles) {
					String logoRealPathDir = uploadPath;
					File logoSaveFile = new File(logoRealPathDir);
					if (!logoSaveFile.exists()) {
						logoSaveFile.mkdirs();
					}
					/** 获取文件的后缀 **/
					String suffix = multipartFile.getOriginalFilename()
							.substring(multipartFile.getOriginalFilename().lastIndexOf("."));
					/** 拼成完整的文件保存路径加文件 **/
					String name = +System.currentTimeMillis() + suffix;
					String fileName = logoRealPathDir + "/" + name;
					file = new File(fileName);
					multipartFile.transferTo(file);
					logger.info(" upload excel path:  " + fileName + "  file name " + file.getName());
					if (file != null) {
						resultList.add(fileName);
					}
				}
			}
		} catch (Exception e) {
			logger.error(" upload excel failed ", e);
		}
		return resultList;
	}
	/** 
	  * @Title:  download 
	  * @Description:  http下载文件 
	  * @param urlString
	  * @param filename
	  * @param savePath
	  * @throws Exception: void
	  * @lastEditor:  SP0011
	  * @lastEdit:  2018年4月17日上午9:56:52
	*/ 
	public static void downloadHTTPFile(String urlString, String filename,String savePath) throws Exception {    
        // 构造URL    
        URL url = new URL(urlString);    
        // 打开连接    
        URLConnection con = url.openConnection();    
        //设置请求超时为5s    
        con.setConnectTimeout(5*1000);    
        // 输入流    
        InputStream is = con.getInputStream();    
        
        // 1K的数据缓冲    
        byte[] bs = new byte[1024];    
        // 读取到的数据长度    
        int len;    
        // 输出的文件流    
       File sf=new File(savePath);    
       if(!sf.exists()){    
           sf.mkdirs();    
       }    
       OutputStream os = new FileOutputStream(sf.getPath()+File.separator+filename);    
        // 开始读取    
        while ((len = is.read(bs)) != -1) {    
          os.write(bs, 0, len);    
        }    
        // 完毕，关闭所有链接    
        os.close();    
        is.close();    
    }    
}
