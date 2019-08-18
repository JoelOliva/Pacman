package com.pacman;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import static com.pacman.TitleScreen.windowHeight;
import static com.pacman.TitleScreen.windowWidth;

/**
 * Created by Joel on 6/28/17.
 */
public class GameOverScreen implements Screen
{
	private Stage stage;
	private Game game;
	private boolean won;

	public GameOverScreen(Game game, boolean won)
	{
		this.game = game;
		this.won = won;
	}

	/**
	 * Called when this screen becomes the current screen for a {@link Game}.
	 */
	@Override
	public void show()
	{
		stage = new Stage(new ScreenViewport());

		Label.LabelStyle labelStyle = new Label.LabelStyle(AssetsUtils.font32, Color.WHITE);
		Label gameOverText;
		if (won)
			gameOverText = new Label("  Winner!\n\nScore: " + Level.score, labelStyle);
		else
			gameOverText = new Label("Game Over\n\nScore: " + Level.score, labelStyle);

		gameOverText.setX(windowWidth / 2 - gameOverText.getWidth() / 2);
		gameOverText.setY(windowHeight - 150);

		float playAgainButtonX = windowWidth / 2 - AssetsUtils.playAgain.getWidth() / 2;
		float playAgainButtonY = 200;

		// Play again button
		TextureRegionDrawable playAgainDrawable = new TextureRegionDrawable(new TextureRegion(AssetsUtils.playAgain));
		TextureRegionDrawable playAgainBrightDrawable = new TextureRegionDrawable(new TextureRegion(AssetsUtils.playAgainBright));
		ImageButton playAgainButton = new ImageButton(playAgainDrawable);
		playAgainButton.setX(playAgainButtonX);
		playAgainButton.setY(playAgainButtonY);
		playAgainButton.getStyle().imageUp = playAgainDrawable;
		playAgainButton.getStyle().imageOver = playAgainBrightDrawable;
		playAgainButton.addListener(new InputListener(){
			@Override
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				game.setScreen(new GameScreen(game));
				return true;
			}
		});

		stage.addActor(gameOverText);
		stage.addActor(playAgainButton);

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
	}
}
