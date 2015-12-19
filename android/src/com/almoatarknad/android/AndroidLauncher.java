package com.almoatarknad.android;

import com.almoatarknad.MainGame;
import com.almoatarknad.ads.AdsController;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;



public class AndroidLauncher extends AndroidApplication implements AdsController {
	private final int SHOW_ADS = 1;
	private final int HIDE_ADS = 0;
	private static final String AD_UNIT_ID_BANNER = "ca-app-pub-4335249035736245/697384081";
	private static final String GOOGLE_PLAY_URL = "https://play.google.com/store/apps/developer?id=TheInvader360";
	private static final String GITHUB_URL = "https://github.com/TheInvader360";
	private static final String BLOG_URL = "http://theinvader360.blogspot.co.uk/";
	public static AdView adView;
	protected View gameView;
	private InterstitialAd interstitialAd;
	AdRequest.Builder adRequestBuilder;
	private String android_id;
	
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.useAccelerometer = false;
		config.useCompass = false;
		config.useGLSurfaceView20API18 = true;
		android_id = Secure.getString(getContext().getContentResolver(),
	            Secure.ANDROID_ID);
		
		System.out.println("ANDROID ID: " + android_id);
		
		RelativeLayout layout = new RelativeLayout(this);
		
		gameView = initializeForView(new MainGame(this), config);
		setupAds();
		
		layout.addView(gameView, ViewGroup.LayoutParams.MATCH_PARENT,
					   ViewGroup.LayoutParams.MATCH_PARENT);
		
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		layout.addView(adView, params);
		layout.setPadding(0, 1, 0, 0);
	    setContentView(layout);
	}
	
	public void setupAds() {
		adView = new AdView(this);
		adView.setVisibility(View.INVISIBLE);
		adView.setBackgroundColor(0xff000000);
		adView.setAdUnitId(AD_UNIT_ID_BANNER);
		adView.setAdSize(AdSize.SMART_BANNER);
	}

	@Override
	public void showBannerAd() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				adView.setVisibility(View.VISIBLE);
				AdRequest.Builder builder = new AdRequest.Builder();
				builder.addTestDevice("AB3F394AF7CBCDF23DFB28D8536A5896");
				AdRequest ad = builder.build();
				adView.loadAd(ad);
			}
		});
		
	}

	@Override
	public void hideBannerAd() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				adView.setVisibility(View.INVISIBLE);
			}
		});
		
	}

	@Override
	public boolean isWifiConnected() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		
		return (ni != null && ni.isConnected());
		
	}
}
