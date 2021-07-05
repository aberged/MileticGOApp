package com.mileticgo.app;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;

	ActivityCloser activityCloser;

	long counter=0;
	float x,y=0;

	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
		Gdx.input.setInputProcessor(new InputAdapter(){
			@Override
			public boolean touchUp(int screenX, int screenY, int pointer, int button) {
				callPlatformInvoker();
				return true;
			}
		});
	}

	@Override
	public void render () {

		if (counter%3 == 0) {
			x = (float) Math.random()*500;
			y = (float) Math.random()*500;
		}

		ScreenUtils.clear(1, 0, 0, 1);
		batch.begin();
		batch.draw(img, x, y);
		batch.end();
		counter++;
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}

	public void setActivityCloser(ActivityCloser ac){
		this.activityCloser = ac;
	}

	public void callPlatformInvoker(){
		if (this.activityCloser != null) activityCloser.closeActivity("trt");
	}

}
