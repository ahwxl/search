/**
 * www.bplow.com
 */
package com.bplow.search.web;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;

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
	
	@RequestMapping(value = "/update")
	@ResponseBody
	public String updateCnt(HttpServletRequest request) throws Exception{
	    
	    InputStream in = request.getInputStream();
	    String xml = IOUtils.toString(in,"UTF-8");
	    
	    logger.info("请求内容url={},content={}",request.getQueryString(),xml);
	    
	    contentService.addContent(xml);
	    
	    
	    
	    return "ok";
	}
	
	
}
