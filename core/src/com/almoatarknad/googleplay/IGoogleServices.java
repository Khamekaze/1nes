package com.almoatarknad.googleplay;

public interface IGoogleServices {
	public void signIn();
	public void signOut();
	public void rateGame();
	public void submitScore(int score);
	public void showScores();
	public boolean isSignedIn();

}
