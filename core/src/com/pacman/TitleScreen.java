package com.pacman;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

/**
 * Created by Joel on 6/26/17.
 */
public class TitleScreen implements Screen
{
	public static float windowWidth;
	public static float windowHeight;

	private Stage stage;
	private Game game;

	public TitleScreen(Game game)
	{
		this.game = game;
	}

	/**
	 * Called when this screen becomes the current screen for a {@link Game}.
	 */
	@Override
	public void show()
	{
		AssetsUtils.initSetup();
		stage = new Stage(new ScreenViewport());
		windowWidth = Gdx.graphics.getWidth();
		windowHeight = Gdx.graphics.getHeight();

		float playButtonX = windowWidth / 2 - AssetsUtils.play.getWidth() / 2;
		float playButtonY = 25;
		float pacmanLogoX = windowWidth / 2 - AssetsUtils.pacmanLogo.getWidth() / 2;
		float pacmanLogoY = windowHeight - AssetsUtils.pacmanLogo.getHeight() - 20;

		// Play button
		TextureRegionDrawable playDrawable = new TextureRegionDrawable(new TextureRegion(AssetsUtils.play));
		TextureRegionDrawable playBrightDrawable = new TextureRegionDrawable(new TextureRegion(AssetsUtils.playBright));
		ImageButton playButton = new ImageButton(playDrawable);
		playButton.setX(playButtonX);
		playButton.setY(playButtonY);
		playButton.getStyle().imageUp = playDrawable;
		playButton.getStyle().imageOver = playBrightDrawable;
		playButton.addListener(new InputListener(){
			@Override
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				game.setScreen(new GameScreen(game));
				return true;
			}
		});

		Image pacmanImg = new Image(new TextureRegionDrawable(new TextureRegion(AssetsUtils.pacmanLogo)));
		pacmanImg.setX(pacmanLogoX);
		pacmanImg.setY(pacmanLogoY);

		stage.addActor(pacmanImg);
		stage.addActor(playButton);

		Gdx.input.setInputProcessor(stage);
	}

	/**
	 * Called when the screen should render itself.
	 *
	 * @param delta The time in seconds since the last render.
	 */
	@Override
	public void render(float delta)
	{
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		stage.act(delta);
		stage.draw();
	}

	/**
	 * @param width
	 * @param height
	 * @see ApplicationListener#resize(int, int)
	 */
	@Override
	public void resize(int width, int height)
	{

	}

	/**
	 * @see ApplicationListener#pause()
	 */
	@Override
	public void pause()
	{

	}

	/**
	 * @see ApplicationListener#resume()
	 */
	@Override
	public void resume()
	{

	}

	/**
	 * Called when this screen is no longer the current screen for a {@link Game}.
	 */
	@Override
	public void hide()
	{

	}

	/**
	 * Called when this screen should release all resources.
	 */
	@Override
	public void dispose()
	{
		AssetsUtils.pacmanLogo.dispose();
		AssetsUtils.play.dispose();
		AssetsUtils.playBright.dispose();
	}
}
