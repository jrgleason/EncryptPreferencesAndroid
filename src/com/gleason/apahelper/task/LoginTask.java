package com.gleason.apahelper.task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.jsoup.helper.StringUtil;
import org.xml.sax.SAXException;

import com.gleason.apahelper.ActionMainActivity;
import com.gleason.apahelper.ApaLogin;
import com.gleason.apahelper.ApaRequest;
import com.gleason.apahelper.ScoreFragment;
import com.gleason.apahelper.ApaLogin.Attributes;
import com.gleason.apahelper.TeamFragment;

import android.os.AsyncTask;
import android.util.Log;

public class LoginTask extends AsyncTask<Void, Integer, Void>{

	public ApaLogin apa;
	public ApaRequest loginResp;
	public TeamFragment frag;
	private static final String USERNAME = "ctl00$cplhPublicContent$Login1$txtUserID";
	private static final String PASSWORD = "ctl00$cplhPublicContent$Login1$txtPassword";
	public String username;
	public String password;
	public boolean success = false;
	
	private String html;

	@Override
	protected Void doInBackground(Void... params) {
		html = apa.initOther(ApaLogin.APA_DEF_URL, frag, username, password);
		return null;
	} 
	
	protected void onPostExecute(Void result) {
	  frag.html = html;
  	  frag.callback2();
    }

}
