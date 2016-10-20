package com.bplow.search.service;

import java.io.IOException;
import java.util.Date;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.bplow.search.common.DateHelper;
import com.bplow.search.domain.SearchBo;


@ContextConfiguration(locations = { "/applicationContext.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
public class SearchTest {
    
    private Logger log = LoggerFactory.getLogger(SearchTest.class);
    
    @Autowired
    private Search search;
    
    @Test
    public void addDoc4Index()throws Exception{
        
        SearchBo bo = new SearchBo();
        bo.setId(DateHelper.getCurrentDate());
        bo.setName("GBK的文字编码是双字节来表示的，即不论中、英文字符均使用双字节来表示，只不过为区分中文，将其最高位都定成1。");
        bo.setCnt("这是内容，你看下汪小");
        
        search.addDocToIndex(bo);
        
    }
    
    
    @Test
    public void searchTest() throws Exception{
        search.search("GBK");
    }
    
    /**
     *测试日期 
     */
    @Test
    public void addDocTest() throws IOException{
        for(int i =0;i<30;i++){
            /*id*/
            FieldType fieldType = new FieldType();
            fieldType.setStored(true);
            fieldType.setTokenized(false);
            Field field = new Field("id","A"+i,fieldType);
            /*日期*/
            Date date = new Date();
            LongField longField = new LongField("date",date.getTime(),LongField.TYPE_STORED);
            
            /*url*/
            Field stringField = new Field("url","http://agc.com",StringField.TYPE_STORED);
            
            /*content*/
            Field textField = new Field("content","在水一方，大的撒飒飒的第三方的订单达到了",TextField.TYPE_STORED);
            
            Document doc = new Document();
            doc.add(field);
            doc.add(longField);
            doc.add(stringField);
            doc.add(textField);
            
            search.addDocToIndex(doc);
        }
        
    }
    
    @Test
    public void searchDate() throws IOException, ParseException, InvalidTokenOffsetsException{
        ScoreDoc[] hits = search.search("content", "订单");
        /*for (int i = 0; i < hits.length; i++) {
            Document hitDoc = isearcher.doc(hits[i].doc);
            log.info(hitDoc.get("fieldname"));
        }*/
    }
    
    /**
     * 范围测试
     * 
     * @throws IOException
     * @throws ParseException
     * @throws InvalidTokenOffsetsException 
     */
    @Test
    public void searchRange() throws IOException, ParseException, InvalidTokenOffsetsException{
        ScoreDoc[] hits = search.search("content", "订单");
    }
    
    /**
     * 分页测试
     * 
     * @throws IOException
     * @throws ParseException
     */
    @Test
    public void searchPage() throws IOException, ParseException{
        search.searchPage("content", "订单");
        
    }
    
    

}
