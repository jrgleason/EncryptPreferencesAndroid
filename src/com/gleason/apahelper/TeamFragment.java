package com.gleason.apahelper;

import java.util.Map;

import com.gleason.apahelper.task.GetParamsTask;
import com.gleason.apahelper.task.LoginTask;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class TeamFragment extends Fragment implements OnClickListener {

	private Toast loading;
	private EditText username;
	private EditText password;
	private CheckBox cb;

	public static ScoreFragment newInstance(int position) {
		Log.i("Pager", "ScoreFragment.newInstance()");

		ScoreFragment fragment = new ScoreFragment();

		Bundle args = new Bundle();
		args.putInt("position", position);
		fragment.setArguments(args);

		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.team,
				container, false);
		View v = rootView.findViewById(R.id.btn_login);
		username = (EditText) rootView.findViewById(R.id.username);
		password = (EditText) rootView.findViewById(R.id.password);
		cb = (CheckBox) rootView.findViewById(R.id.savePwd);
		cb.setOnClickListener(this);
		synchronized (ActionMainActivity.class) {
			if (ActionMainActivity.isStored()) {
				username.setText(ActionMainActivity.getUsername());
//				username.setVisibility(View.INVISIBLE);
				password.setText(ActionMainActivity.getPassword());
				password.setVisibility(View.INVISIBLE);
				cb.setChecked(true);
			}
		}

		if (v != null)
			v.setOnClickListener(this);
		return rootView;
	}

	private GetParamsTask task;
	private boolean addToPref = false;
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.savePwd:
			CheckBox cb = (CheckBox)v;
			if(cb.isChecked()){
				addToPref = true;
			}
			else{
				addToPref = false;
				password = (EditText) getActivity().findViewById(R.id.password);
				password.setVisibility(View.VISIBLE);
				password.setText("");
			}
			break;
		case R.id.btn_login:
			loading = Toast.makeText(this.getActivity(), "Initializing login", Toast.LENGTH_LONG);
			ActionMainActivity ama = (ActionMainActivity) getActivity();
			if(addToPref){
				try {
					ActionMainActivity.setUserNamePassword(username.getText().toString(), password.getText().toString());
					ActionMainActivity.setStored(true);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			loading.show();
			task = new GetParamsTask();
			task.act = this;
			task.execute();
			break;
		}
	}

	private LoginTask lt;

	// public ApaLogin a;
	public void callback() {
		lt = new LoginTask();
		lt.apa = task.a;
		lt.username = username.getText().toString();
		lt.password = password.getText().toString();
		lt.frag = this;
		lt.execute();

	}

	public String html;

	public void callback2() {
		ActionMainActivity a = (ActionMainActivity) this.getActivity();
		int i = html.indexOf("17732 - APA of Central Florida");
		if (i > 0) {
			((TextView) a.findViewById(R.id.message_login))
					.setText("Logged in");
			WebViewFragment f = new WebViewFragment();
			f.html = html;
			a.changeFragment(f);
		} else {
			((TextView) a.findViewById(R.id.message_login))
					.setText("Problems logging in please try again");
		}

	}
}
