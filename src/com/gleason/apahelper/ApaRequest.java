package com.gleason.apahelper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ApaRequest {
	private static final String VALUE = "value";
	private Response response;
	private Map<String, String> params = new HashMap<String, String>();
	private String cookies;
	private Map<String, String> cookieMap= new HashMap<String, String>();

	/**
	 * @return the response
	 */
	public Response getResponse() {
		return response;
	}

	/**
	 * @param response
	 *            the response to set
	 */
	public void setResponse(Response response) {
		this.response = response;
	}

	/**
	 * @return the params
	 */
	public Map<String, String> getParams() {
		return params;
	}

	/**
	 * @param params
	 *            the params to set
	 */
	public void setParams(Map<String, String> params) {
		this.params = params;
	}

	/**
	 * @return the cookies
	 */
	public String getCookies() {
		return cookies;
	}

	/**
	 * @param cookies
	 *            the cookies to set
	 */
	public void setCookies(String cookies) {
		this.cookies = cookies;
	}

	public void parseResponse(Response res, Document doc) {
		cookieMap = res.cookies();
		makeCookie(cookieMap);

		Elements e = doc.getElementsByTag("input");
		for (Element element : e) {
			getParams().put(element.attr("name"), element.attr(VALUE));
		}
	}

	/**
	 * @return the cookieMap
	 */
	public Map<String, String> getCookieMap() {
		return cookieMap;
	}

	/**
	 * @param cookieMap the cookieMap to set
	 */
	public void setCookieMap(Map<String, String> cookieMap) {
		this.cookieMap = cookieMap;
	}

	public void parseResponse(HttpResponse res) throws IllegalStateException,
			SAXException, IOException, ParserConfigurationException {
		Header[] head = res.getHeaders("Cookie");
		if (head.length == 1) {
			cookies = head[0].getValue();
		}
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory
				.newInstance();
		DocumentBuilder builder = builderFactory.newDocumentBuilder();
		org.w3c.dom.Document doc = builder.parse(res.getEntity().getContent());
		NodeList nl = doc.getElementsByTagName("input");
		for (int i = 0; i < nl.getLength(); i++) {
			org.w3c.dom.Element n = (org.w3c.dom.Element) nl.item(i);
			getParams().put(n.getAttribute("name"), n.getAttribute(VALUE));
		}
	}

	private void makeCookie(Map<String, String> cMap) {
		StringBuilder sb = new StringBuilder();
		for (String i : cMap.keySet()) {
			sb.append(i);
			sb.append("=");
			sb.append(cMap.get(i));
			sb.append(";");
		}

		cookies = sb.toString();
		cookies = cookies.substring(0, cookies.length() - 1);
	}

}
