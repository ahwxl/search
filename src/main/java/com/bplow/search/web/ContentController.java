/**
 * www.bplow.com
 */
package com.bplow.search.web;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

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
	private ContentService ContentService;
	
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
		
		ContentService.addContent(content);
		
		return "ok";
	}
	
}
