package com.gleason.apahelper.task;

import java.util.Map;

import com.gleason.apahelper.ActionMainActivity;
import com.gleason.apahelper.ApaLogin;
import com.gleason.apahelper.TeamFragment;

import android.os.AsyncTask;

public class GetParamsTask extends AsyncTask<Void, Integer, Void> {

	public Map<String, String> vars;
	public TeamFragment act;
	public ApaLogin a;


	protected void onPostExecute(Void result) {
//		act.a = a;
    	act.callback();
    }

	@Override
	protected Void doInBackground(Void... params) {
		// TODO Auto-generated method stub
		a = new ApaLogin();
		a.init(ApaLogin.APA_DEF_URL);
		return null;
	}

}
