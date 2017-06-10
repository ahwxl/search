/**
 * www.bplow.com
 */
package com.bplow.search.common;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @desc
 * @author wangxiaolei
 * @date 2017年6月10日 下午9:45:37
 */
public class SecurityUtil {
	
	static Base64Coder code = new Base64Coder();

	public static String hash(String txt) {

		String result = "";
		MessageDigest md;

		try {
			md = MessageDigest.getInstance("SHA");
			md.update(txt.getBytes("GBK"));
			byte[] toChapter1Digest = md.digest();
			result = code.encode(toChapter1Digest);
		} catch (NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return result;

	}
	
	public static void main(String[] args) {
		
		String tmp = SecurityUtil.hash("htttp://wwww.baidu.com/aa/adfd.htmlasfsafafasdfasfasfsafasfasd");
		
		System.out.println(tmp);
		
		tmp = SecurityUtil.hash("htttp://wwww.baidu.com/aa/adfd.html");
		
		System.out.println(tmp);
	}

}
