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
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;



public class AndroidLauncher extends AndroidApplication implements AdsController {
	private final int SHOW_ADS = 1;
	private final int HIDE_ADS = 0;
	private static final String AD_UNIT_ID_BANNER = "ca-app-pub-4335249035736245/6973840813";
	protected AdView adView;
	protected View gameView;
	AdRequest.Builder adRequestBuilder;
	
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.useAccelerometer = false;
		config.useCompass = false;
		config.useGLSurfaceView20API18 = true;
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
		
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
		layout.setPadding(0, 0, 0, 0);
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
				//Plattan
//				builder.addTestDevice("AB3F394AF7CBCDF23DFB28D8536A5896");
				
				//Mobilen
//				builder.addTestDevice("C24EE5AFF55DC9227921D27018F6FED9");
//				AdRequest ad = new AdRequest.Builder().addTestDevice("C24EE5AFF55DC9227921D27018F6FED9").build();
				AdRequest ad = new AdRequest.Builder().build();
				adView.loadAd(ad);
				adView.setVisibility(View.VISIBLE);
				System.out.println("SHOW AD");
			}
		});
	}
	
	@Override
	public void loadBannerAd() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				//Plattan
//				builder.addTestDevice("AB3F394AF7CBCDF23DFB28D8536A5896");
				
				//Mobilen
//				builder.addTestDevice("C24EE5AFF55DC9227921D27018F6FED9");
//				AdRequest ad = new AdRequest.Builder().addTestDevice("C24EE5AFF55DC9227921D27018F6FED9").build();
				AdRequest ad = new AdRequest.Builder().build();
				adView.loadAd(ad);
				System.out.println("LOAD AD");
			}
		});
	}

	@Override
	public void hideBannerAd() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				adView.setVisibility(View.INVISIBLE);
				System.out.println("HIDE AD");
			}
		});
	}

	@Override
	public boolean isWifiConnected() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		return (ni != null && ni.isConnected());
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if(adView != null) {
			adView.resume();
		}
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		if(adView != null) {
			adView.pause();
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(adView != null) {
			adView.destroy();
		}
	}
}
