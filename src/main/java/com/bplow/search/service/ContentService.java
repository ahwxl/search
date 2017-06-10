/**
 * www.bplow.com
 */
package com.bplow.search.service;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bplow.search.common.DateHelper;
import com.bplow.search.common.SecurityUtil;
import com.bplow.search.domain.SearchBo;
import com.bplow.search.domain.SrContent;
import com.bplow.search.domain.TOrder1;
import com.bplow.search.mapper.SrContentMapper;
import com.bplow.search.mapper.TOrder1Mapper;

/**
 * @desc 
 * @author wangxiaolei
 * @date 2016年10月29日 下午3:28:56
 */
@Service
public class ContentService {
	
	private Logger log = LoggerFactory.getLogger(ContentService.class);
	
	@Autowired
	private SrContentMapper srContentMapper;
	
	@Autowired
	private TOrder1Mapper orderMapper;
	
	@Autowired
	private Search search;
	
	@Autowired
	private ContentAnalyzerService contentAnalyzerService;
	
	
	public void addContent(SrContent content) throws Exception{
		
		content.setId(UUID.randomUUID().toString().replace("-", ""));
		content.setGmtModify(new Date());
		srContentMapper.insert(content);
		
		SearchBo bo = new SearchBo();
		bo.setId(content.getId());
		bo.setName(content.getCntCaption());
		bo.setCnt(contentAnalyzerService.getHtmlContent(content.getContent()));
		
		log.info("添加内容到索引中:{}",bo.getId());
		search.addDocToIndex(bo);
		
	}
	
	public void addContent(String xml) throws Exception{
	    
	    SAXReader reader = new SAXReader();
	    Document document = reader.read(new ByteArrayInputStream(xml.getBytes("UTF-8")));
	    
	    Iterator orderIt = document.selectNodes("/add/doc").iterator();
        while (orderIt.hasNext()) {
           Element elem = (Element) orderIt.next();
           SearchBo bo = new SearchBo();
           
           Iterator emIt = elem.elementIterator();
           
           while(emIt.hasNext()){
               Element fieldem = (Element) emIt.next();
               System.out.println(fieldem.attributeValue("name")+"==="+fieldem.getText());
               
               if("url".equals(fieldem.attributeValue("name"))){
                   bo.setUrl(fieldem.getText());
                   bo.setId(SecurityUtil.hash(fieldem.getText()));
               }else if("content".equals(fieldem.attributeValue("name"))){
                   bo.setCnt(fieldem.getText());
               }else if("digest".equals(fieldem.attributeValue("name"))){
                   bo.setId(fieldem.getText());
               }else if("host".equals(fieldem.attributeValue("name"))){
                   bo.setName(fieldem.getText());
               }
           }
           log.info("添加内容到索引中:{}",bo.getCnt());
           search.updateDoc(bo);
           
        }
	    
	}
	
	public List<SrContent> querySrContent(SrContent content){
		
		List <SrContent> list = srContentMapper.queryForPage(content);
		
		return list;
	}
	
	
	public void createOrder(TOrder1 order){
	    
	    orderMapper.insert(order);
	    
	}

}
