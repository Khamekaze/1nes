package com.almoatarknad.android;

import com.almoatarknad.MainGame;
import com.almoatarknad.googleplay.IGoogleServices;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.games.Games;
import com.google.example.games.basegameutils.GameHelper.GameHelperListener;

import android.R;
import android.content.Intent;
import android.os.Bundle;
import bin.classes.com.google.example.games.basegameutils.GameHelper;



public class AndroidLauncher extends AndroidApplication implements IGoogleServices {
	
	private final static int REQUEST_CODE_UNUSED = 9002;

	
	private GameHelper _gameHelper;
	
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		_gameHelper = new GameHelper(this, GameHelper.CLIENT_GAMES);
		_gameHelper.enableDebugLog(false);
		
		GameHelperListener gameHelperListener = new GameHelper.GameHelperListener() {
			
			@Override
			public void onSignInSucceeded() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onSignInFailed() {
				// TODO Auto-generated method stub
				
			}
		};
		
		_gameHelper.setup(gameHelperListener);
		
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new MainGame(this), config);
	}
	
	@Override
	protected void onStart()
	{
	super.onStart();
	_gameHelper.onStart(this);
	}

	@Override
	protected void onStop()
	{
	super.onStop();
	_gameHelper.onStop();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
	super.onActivityResult(requestCode, resultCode, data);
	_gameHelper.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void signIn() {
		try {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					_gameHelper.beginUserInitiatedSignIn();
				}
			});
		} catch (Exception e) {
			Gdx.app.log("AndroidLauncher", "Login failed: " + e.getMessage());
		}
		
	}

	@Override
	public void signOut() {
		try {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					_gameHelper.signOut();
				}
			});
		} catch (Exception e) {
			Gdx.app.log("AndroidLauncher", "Logout failed :" + e.getMessage());
		}
		
	}

	@Override
	public void rateGame() {
		
	}

	@Override
	public void submitScore(int score) {
		if(isSignedIn() == true) {
//			Games.Leaderboards.submitScore(_gameHelper.getApiClient(), getString(R.string.leaderboard_id), score);
//			startActivityForResult(Games.Leaderboards.getLeaderboardIntent(_gameHelper.getApiClient(), getString(R.string.leaderboard_id)), REQUEST_CODE_UNUSED);
		} else {
			
		}
		
	}

	@Override
	public void showScores() {
		if(isSignedIn() == true) {
//			startActivityForResult(Games.Leaderboards.getLeaderboardIntent(_gameHelper.getApiClient(), getString(R.string.leaderboard_id)), REQUEST_CODE_UNUSED);
		} else {
			
		}
		
	}

	@Override
	public boolean isSignedIn() {
		return _gameHelper.isSignedIn();
	}
}
