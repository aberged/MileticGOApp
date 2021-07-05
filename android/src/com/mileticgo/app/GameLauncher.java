package com.mileticgo.app;

import android.os.Bundle;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class GameLauncher extends AndroidApplication implements ActivityCloser {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		MyGdxGame game = new MyGdxGame();
		game.setActivityCloser(this);
		initialize(game, config);
	}

	@Override
	public void closeActivity(String s) {
		this.finish();
	}
}
