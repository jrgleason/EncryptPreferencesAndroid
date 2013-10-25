package com.gleason.apahelper;

import java.io.IOException;
import org.jsoup.Connection;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import android.support.v4.app.Fragment;

public class ApaLogin {
	
	public static final String APA_DEF_URL = "https://members.poolplayers.com/Default.aspx";
	public static final String APA_MAIN_URL = "https://members.poolplayers.com/Main/Main.aspx";

	public enum Attributes {
		VIEWSTATE,
		EVENTVALIDATION,
		COOKIE
	}
	
	private ApaRequest ar;
	public void init(String url){
		ar = new ApaRequest();
        Document doc;
		
		try {
			Connection c = Jsoup.connect(url);
			doc=c.get();
			Response r = c.response();
			ar.parseResponse(r, doc);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static final String USERNAME = "ctl00$cplhPublicContent$Login1$txtUserID";
	private static final String PASSWORD = "ctl00$cplhPublicContent$Login1$txtPassword";
	
	public String initOther(String url, Fragment frag, String username, String password){
        Document doc;
		if(username == null || password == null){
			//TODO: Error message
			return null;
		}
		try {
			ar.getParams().put(USERNAME,username);
			ar.getParams().put(PASSWORD,password);
			Connection c = Jsoup.connect(url);
			c.header("Content-Type","application/x-www-form-urlencoded");
			c.data(ar.getParams());
			doc=c.post();
			Response r = c.response();
			ar.parseResponse(r, doc);
			return doc.html();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * @return the ar
	 */
	public ApaRequest getAr() {
		return ar;
	}

	/**
	 * @param ar the ar to set
	 */
	public void setAr(ApaRequest ar) {
		this.ar = ar;
	}
}
