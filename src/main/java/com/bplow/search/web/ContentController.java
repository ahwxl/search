/**
 * www.bplow.com
 */
package com.bplow.search.web;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bplow.search.domain.SrContent;
import com.bplow.search.service.ContentService;

/**
 * @desc 
 * @author wangxiaolei
 * @date 2016年10月29日 下午3:31:53
 */
@Controller
public class ContentController {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	
	@Autowired
	private ContentService contentService;
	
	@RequestMapping(value = "/addCnt")
	public String addContentPage(){
		
		return "content/addContent";
	}
	
	@RequestMapping(value = "/addCntAction")
	@ResponseBody
	public String addContent(@RequestParam("cnt")String cnt,SrContent content) throws Exception{
		
		logger.info("添加内容:{}",content);
		
		if(StringUtils.isNotEmpty(cnt)){
			content.setContent(cnt.getBytes("UTF-8"));
		}
		
		contentService.addContent(content);
		
		return "ok";
	}
	/**
	 * nutch使用
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "/update")
	public void updateCnt(HttpServletRequest request,HttpServletResponse response) throws Exception{
	    
	    InputStream in = request.getInputStream();
	    String xml = IOUtils.toString(in,"UTF-8");
	    
	    logger.info("请求内容url={},content={}",request.getQueryString(),xml);
	    if(StringUtils.isNotEmpty(request.getQueryString())){
	        contentService.addContent(xml);
	    }
	    
	    OutputStream os = response.getOutputStream();
        
        byte VERSION = 2;
        byte type = (byte) (6 << 5);
        os.write(VERSION);
        os.write(type);
        os.write("<add><doc boost=\"1\"><field name=\"a\"></field><doc></add>".getBytes());
        os.flush();
        os.close();
	}
	
	
}
