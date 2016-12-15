package com.bplow.search.authority;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

public class MyHostnameVerifier implements HostnameVerifier{

    public boolean verify(String s, SSLSession sslsession) {
        return true;
    }
}
