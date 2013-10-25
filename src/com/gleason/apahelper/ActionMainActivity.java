package com.gleason.apahelper;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import com.gleason.apahelper.security.CryptoTranslator;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.ViewGroup;

public class ActionMainActivity extends FragmentActivity {

	private static final String SECURITY_SETTINGS = "Security";
	private static final String SEC_KEY = "secret";
	private static final String IV_KEY = "iv";
	private static final String CRED_STATUS = "credsSaved";
	private static final String USERNAME_KEY = "username";
	private static final String PASSWORD_KEY = "password";
	private static final boolean CRED_STATUS_DEF = false;
	private ScreenSlidePagerAdapter mPagerAdapter;
	private static ViewPager mPager;
	private ActionBar actionBar;
	private Context context;
	private FragmentManager fm;
	private static SharedPreferences settings;
	private static boolean isStored;
	private static String username;
	private static String password;

	private static void isUsernamePasswordValid() {
		if (password.equals("") || username.equals("")) {
			// there is an issue so is stored is set back to false
			settings.edit().putBoolean(CRED_STATUS, CRED_STATUS_DEF).commit();
		}
	}

	private static void getUserNamePassword() throws Exception {
		isStored = settings.getBoolean(CRED_STATUS, CRED_STATUS_DEF);
		String iv = settings.getString(IV_KEY, null);
		if (isStored && iv != null) {
			if (settings.contains(USERNAME_KEY))
				username = settings.getString(USERNAME_KEY, "");
			if (settings.contains(PASSWORD_KEY))
				password = settings.getString(PASSWORD_KEY, "");
		    isUsernamePasswordValid();
			String password2 = CryptoTranslator.decrypt(password, iv);
			password = password2;
			Log.d("Security", "Password encrypted");
		}
	}

	public static void setUserNamePassword(String username, String password)
			throws Exception {
		
		//String username2 = CryptoTranslator.encrypt(username);
		Log.d("Security", "Username encrypted");
		String iv = CryptoTranslator.getIv();
		String password2 = CryptoTranslator.encrypt(password, iv);
		
		ActionMainActivity.username = username;
		ActionMainActivity.password = password;
		Log.d("Security", "Password encrypted");
		settings.edit().putString(USERNAME_KEY, username)
				.putString(PASSWORD_KEY, password2)
				.putString(IV_KEY, iv)
				.commit();
		isUsernamePasswordValid();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (settings == null)
			settings = getSharedPreferences(SECURITY_SETTINGS, MODE_PRIVATE);
		String secString = settings.getString(SEC_KEY, null);
		if (secString == null) {
			try {
				CryptoTranslator.generateKey();
				settings.edit()
						.putString(SEC_KEY,
								CryptoTranslator.getSEC_KEY_String()).commit();
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			CryptoTranslator.setSEC_KEY_STRING(secString);
		}
		try {
			getUserNamePassword();
		} catch (Exception ex) {
			Log.i("Preferences",
					"There was an issue getting username and password");
			isStored = CRED_STATUS_DEF;
		}
		fm = getSupportFragmentManager();
		context = this;
		setContentView(R.layout.ab_activity_main);
		actionBar = getActionBar();
		actionBar.setSubtitle("THE APA Android App");
		actionBar.setTitle("APA Helper");
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		mPager = (ViewPager) findViewById(R.id.pager);
		mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
		mPager.setAdapter(mPagerAdapter);
		// mPager.setAdapter(new MyAdapter(getSupportFragmentManager()));

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	static final class TabInfo {
		private final Class<?> clss;
		private final Bundle args;

		TabInfo(Class<?> _class, Bundle _args) {
			clss = _class;
			args = _args;
		}
	}

	public void changeFragment(Fragment f) {
		mPagerAdapter.setF(f);
		ViewGroup g = (ViewGroup) findViewById(R.id.pager);
		// WebViewFragment v = (WebViewFragment)mPagerAdapter.instantiateItem(g,
		// 1);

		// String s = v.html;
	}

	/**
	 * @return the username
	 */
	public static String getUsername() {
		return username;
	}

	/**
	 * @param username
	 *            the username to set
	 */
	public static void setUsername(String username) {
		ActionMainActivity.username = username;
	}

	/**
	 * @return the password
	 */
	public static String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public static void setPassword(String password) {
		ActionMainActivity.password = password;
	}

	/**
	 * @return the isStored
	 */
	public static boolean isStored() {
		return isStored;
	}

	/**
	 * @param isStored
	 *            the isStored to set
	 */
	public static void setStored(boolean isStored) {
		ActionMainActivity.isStored = isStored;
		settings.edit().putBoolean(CRED_STATUS, isStored).commit();
	}

	private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter
			implements ActionBar.TabListener {

		private ArrayList<TabInfo> mTabs = new ArrayList<TabInfo>();
		private List<Fragment> fragments = new ArrayList<Fragment>();

		public ScreenSlidePagerAdapter(FragmentManager fm) {
			super(fm);
			fragments.add(new ScoreFragment());
			fragments.add(new TeamFragment());
			this.addTab(actionBar.newTab().setText("Score"),
					ScoreFragment.class, null);
			this.addTab(actionBar.newTab().setText("Team"), TeamFragment.class,
					null);
		}

		@Override
		public Fragment getItem(int position) {
			return fragments.get(position);
		}

		@Override
		public int getCount() {
			return fragments.size();
		}

		public void addTab(ActionBar.Tab tab, Class<?> clss, Bundle args) {
			TabInfo info = new TabInfo(clss, args);
			tab.setTag(info);
			tab.setTabListener(this);
			mTabs.add(info);
			actionBar.addTab(tab);
			notifyDataSetChanged();
		}

		public void replaceTab(ActionBar.Tab tab, Class<?> clss, Bundle args,
				TabInfo ti) {
			TabInfo info = new TabInfo(clss, args);
			tab.setTag(info);
			tab.setTabListener(this);
			mTabs.set(mTabs.indexOf(ti), info);
			actionBar.removeTab(tab);
			notifyDataSetChanged();
			addTab(actionBar.newTab().setText(tab.getText()), clss, args);
		}

		@Override
		public void onTabReselected(Tab arg0,
				android.app.FragmentTransaction arg1) {
		}

		@Override
		public void onTabSelected(Tab tab, android.app.FragmentTransaction arg1) {
			Object tag = tab.getTag();
			for (int i = 0; i < mTabs.size(); i++) {
				if (mTabs.get(i) == tag) {
					mPager.setCurrentItem(i);
				}
			}
		}

		@Override
		public void onTabUnselected(Tab arg0,
				android.app.FragmentTransaction arg1) {
			// TODO Auto-generated method stub

		}

		/**
		 * @param f
		 *            the f to set
		 */
		public void setF(Fragment f) {
			int loc = actionBar.getSelectedTab().getPosition();
			fragments.set(loc, f);
			notifyDataSetChanged();
		}

		@Override
		public int getItemPosition(Object object) {
			if (object instanceof Fragment) {
				if (fragments.contains(object))
					return POSITION_UNCHANGED;

			}
			return POSITION_NONE;
		}
	}
}
