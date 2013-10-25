package com.gleason.apahelper;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.EditText;

public class WebViewFragment extends Fragment {
	String html;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup rootView = (ViewGroup) inflater.inflate(
				R.layout.web_view_fragment, container, false);
		WebView v = (WebView)rootView.findViewById(R.id.webView1);
		v.loadDataWithBaseURL("file:///android_asset/",html, "text/html", "UTF-8", null);
		return rootView;
	}
}
