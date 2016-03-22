package com.bplow.search.service;

import java.io.IOException;
import java.util.Date;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;


@ContextConfiguration(locations = { "/applicationContext.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
public class SearchTest {
    
    @Autowired
    private Search search;
    
    @Test
    public void searchTest() throws Exception{
        search.search();
        
    }
    
    
    public void addDocTest() throws IOException{
        /*id*/
        FieldType fieldType = new FieldType();
        fieldType.setStored(true);
        fieldType.setTokenized(false);
        Field field = new Field("id","1234567890",fieldType);
        /*日期*/
        Date date = new Date();
        LongField longField = new LongField("date",date.getTime(),LongField.TYPE_STORED);
        
        /*url*/
        Field stringField = new Field("url","http://agc.com",StringField.TYPE_STORED);
        
        Field textField = new Field("content","在水一方，大的撒飒飒的第三方的订单达到了",TextField.TYPE_STORED);
        
        Document doc = new Document();
        doc.add(field);
        doc.add(longField);
        doc.add(stringField);
        doc.add(textField);
        
        search.addDocToIndex(doc);
    }
    
    

}
