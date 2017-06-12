package com.bplow.search.service;

import java.util.Iterator;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;

public class HtmlUnitTest {

    @Test
    public void homePage() throws Exception {
        final WebClient webClient = new WebClient(BrowserVersion.INTERNET_EXPLORER);
        final HtmlPage page = webClient.getPage("http://item.yhd.com/item/62762115");
        //Assert.assertEquals("杨尚川的博客 - ITeye技术网站", page.getTitleText());
        final String pageAsXml = page.asXml();
        final String pageAsText = page.asText();
        Assert.assertTrue(pageAsText.contains("[置顶] 国内首套免费的《Nutch相关框架视频教程》(1-20)"));
        webClient.close();
    }

    @Test
    public void homePage_Firefox() throws Exception {
        final WebClient webClient = new WebClient(BrowserVersion.FIREFOX_52);
        final HtmlPage page = webClient.getPage("http://search.yhd.com/c0-0/k%25E5%2592%2596%25E5%2595%25A1/?tp=1.1.12.0.3.LmRxP8k-10-5FBcb");
        //Assert.assertEquals("雀巢咖啡 1+2原味1.5kg/盒(100条x15g)-1号店", page.getTitleText());
        System.out.println(page.asText());
        webClient.close();
    }

    @Test
    public void getElements() throws Exception {
        final WebClient webClient = new WebClient(BrowserVersion.CHROME);
        final HtmlPage page = webClient.getPage("http://yangshangchuan.iteye.com");
        final HtmlDivision div = page.getHtmlElementById("blog_actions");
        //获取子元素
        Iterator<DomElement> iter = div.getChildElements().iterator();
        while (iter.hasNext()) {
            System.out.println(iter.next().getTextContent());
        }
        //获取所有输出链接
        for (HtmlAnchor anchor : page.getAnchors()) {
            System.out.println(anchor.getTextContent() + " : " + anchor.getAttribute("href"));
        }
        webClient.close();
    }

    @Test
    public void xpath() throws Exception {
        final WebClient webClient = new WebClient();
        final HtmlPage page = webClient.getPage("http://yangshangchuan.iteye.com");
        //获取所有博文标题
        final List<HtmlAnchor> titles = page
            .getByXPath("/html/body/div[2]/div[2]/div/div[16]/div/h3/a");
        for (HtmlAnchor title : titles) {
            System.out.println(title.getTextContent() + " : " + title.getAttribute("href"));
        }
        //获取博主信息
        final HtmlDivision div = (HtmlDivision) page.getByXPath("//div[@id='blog_owner_name']")
            .get(0);
        System.out.println(div.getTextContent());
        webClient.close();
    }

    @Test
    public void submittingForm() throws Exception {
        final WebClient webClient = new WebClient(BrowserVersion.FIREFOX_52);
        final HtmlPage page = webClient.getPage("http://www.oschina.net");
        // Form没有name和id属性
        final HtmlForm form = page.getForms().get(0);
        final HtmlTextInput textField = form.getInputByName("q");
        final HtmlButton button = form.getButtonByName("");
        textField.setValueAttribute("APDPlat");
        final HtmlPage resultPage = button.click();
        final String pageAsText = resultPage.asText();
        Assert.assertTrue(pageAsText.contains("找到约"));
        Assert.assertTrue(pageAsText.contains("条结果"));
        webClient.close();
    }

}
