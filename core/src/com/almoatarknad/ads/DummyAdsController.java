package com.almoatarknad.ads;

public class DummyAdsController implements AdsController {

	@Override
	public void showBannerAd() {
		System.out.println("SHOWING AD");

	}

	@Override
	public void hideBannerAd() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isWifiConnected() {
		// TODO Auto-generated method stub
		return true;
	}

}
