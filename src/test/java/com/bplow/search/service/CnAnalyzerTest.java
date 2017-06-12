package com.bplow.search.service;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ContextConfiguration(locations = { "/applicationContext.xml","/applicationContext-import.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
public class CnAnalyzerTest {

    @Autowired
    CnAnalyzer cnAnalyzer;
    
    @Test
    public void cnTest() throws IOException{
        cnAnalyzer.cnAnalyzer();
    }
    
}
