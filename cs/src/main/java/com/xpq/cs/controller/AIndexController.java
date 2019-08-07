package com.xpq.cs.controller;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import com.xpq.cs.model.common.ApiResult;
import com.xpq.cs.util.ApiResultUtils;

import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 人员表 前端控制器
 * </p>
 *
 * @author x
 * @since 2019-08-07
 */
@RestController
@RequestMapping("/index")
public class AIndexController {

    @GetMapping(value = "download-excel")
    @ApiOperation("下载人员模板")
    public ApiResult personExcel(HttpServletRequest request,  HttpServletResponse response) throws IOException{
		Resource resource = new DefaultResourceLoader().getResource("/static/excelTemplate/人员模板表.xlsx");
		InputStream stream = resource.getInputStream();
		String fileName = "批量注册人员模板";
		Workbook workbook = WorkbookFactory.create(stream);
		if (stream!=null) {
			stream.close();
		}
		if (workbook==null) {
			return ApiResultUtils.failed("批量注册人员模板.xlsx 这文件不存在");
		}
		String agent = request.getHeader("USER-AGENT");  
		if (workbook instanceof HSSFWorkbook) {//这个是处理不同浏览器导出乱码的
			if (null != agent && -1 != agent.indexOf("MSIE") || null != agent  && -1 != agent.indexOf("Trident")) {// ie  
				fileName= java.net.URLEncoder.encode(fileName+".xls", "UTF8");  
			} else if (null != agent && -1 != agent.indexOf("Mozilla")) {// 火狐,chrome等  
				fileName = new String((fileName+".xls").getBytes("UTF-8"), "iso-8859-1");  
			}  
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition", "attachment; filename="+fileName);
		} else {
			if (null != agent && -1 != agent.indexOf("MSIE") || null != agent  && -1 != agent.indexOf("Trident")) {// ie  
				fileName= java.net.URLEncoder.encode(fileName+".xlsx", "UTF8");  
			} else if (null != agent && -1 != agent.indexOf("Mozilla")) {// 火狐,chrome等  
				fileName = new String((fileName+".xlsx").getBytes("UTF-8"), "iso-8859-1");  
			}  
			response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			response.setHeader("Content-Disposition", "attachment; filename="+ fileName );
		}
		OutputStream out = response.getOutputStream();
		workbook.write(out);
		workbook.close();
		out.close();
 	   return ApiResultUtils.success();
    }
}

