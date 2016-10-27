/**
 * www.bplow.com
 */
package com.bplow.search.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bplow.search.page.Page;
import com.bplow.search.service.Search;

/**
 * @desc 
 * @author wangxiaolei
 * @date 2016年10月27日 下午9:41:07
 */
@Controller
public class SearchController {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private Search search;
	
	@RequestMapping(value = "/search")
	@ResponseBody
	public Page search(@RequestParam("w")String w) throws Exception {
		
		logger.info("接受搜索请求:{}",w);
		
		Page page = new Page();
		page.setLimit(5);
		page = search.searchForPage(w, page);
		
		return page;
	}

}
