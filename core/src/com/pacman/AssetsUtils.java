package com.pacman;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * A helper class for the show method in GameScreen. The only purpose of this class
 * is to help increase readability.
 *
 * Created by Joel on 5/24/17.
 */
public class AssetsUtils
{
	// Music and Sounds
	public static Sound intro;
	public static Sound chomp;
	public static Sound itemCollected;
	public static Sound extraLife;
	public static Sound pacmanDeathSound;
	public static Sound ghostDeath;
	public static Sound sirenLow;
	public static Sound sirenHigh;
	public static Sound ghostFleeing;
	public static Sound powerUpMode;

	public static Texture spritesheet;					// The spritesheet containing all sprites
	public static Texture pacmanLogo;					// The big pacman logo in the title screen
	public static Texture play;							// The play button image
	public static Texture playBright;					// Modified version of the play button image
	public static Texture playAgain;					// Modified version of the play button image
	public static Texture playAgainBright;					// Modified version of the play button image
	public static TextureRegion[] sprites;				// A collection of all the sprites
	public static TextureRegion[] scoreTextures;				// A collection of all the sprites
	public static Animation<TextureRegion> pacmanDeath;	// Pacman death animation
	public static BitmapFont font;						// The 8 bit style font
	public static BitmapFont font32;						// The 8 bit style font
	public static TextureRegion[] itemTexture;
	public static TextureRegion[] entityCurrentTexture;

	/**
	 *
	 */
	public static TextureRegion[][] entityDefaultFrame;
	/**
	 *
	 */
	public static Animation<TextureRegion>[][] entityFrames;

	private AssetsUtils() {}

	public static void initSetup()
	{
		loadAssets();
		createTextures();
		createAnimations();

		itemTexture = new TextureRegion[8];
		itemTexture[0] = sprites[52];
		itemTexture[1] = sprites[53];
		itemTexture[2] = sprites[54];
		itemTexture[3] = sprites[55];
		itemTexture[4] = sprites[56];
		itemTexture[5] = sprites[57];
		itemTexture[6] = sprites[58];
		itemTexture[7] = sprites[59];

		entityCurrentTexture = new TextureRegion[5];
		entityCurrentTexture[0] = sprites[40];
		entityCurrentTexture[4] = entityDefaultFrame[4][0];
		entityCurrentTexture[1] = entityDefaultFrame[1][1];
		entityCurrentTexture[2] = entityDefaultFrame[2][0];
		entityCurrentTexture[3] = entityDefaultFrame[3][0];
	}

	/**
	 * Load all the assests for the game
	 */
	private static void loadAssets()
	{
		// 8 bit style font
		font = new BitmapFont(Gdx.files.internal("Press_Start.fnt"));
		font32 = new BitmapFont(Gdx.files.internal("Press_Start-32.fnt"));

		// Sounds
		intro = Gdx.audio.newSound(Gdx.files.internal("Sounds/intro.ogg"));
		chomp = Gdx.audio.newSound(Gdx.files.internal("Sounds/chomp.ogg"));
		itemCollected = Gdx.audio.newSound(Gdx.files.internal("Sounds/item_collected.ogg"));
		extraLife = Gdx.audio.newSound(Gdx.files.internal("Sounds/extra_life.ogg"));
		pacmanDeathSound = Gdx.audio.newSound(Gdx.files.internal("Sounds/pacman_death.ogg"));
		ghostDeath = Gdx.audio.newSound(Gdx.files.internal("Sounds/ghost_death.ogg"));
		sirenLow = Gdx.audio.newSound(Gdx.files.internal("Sounds/siren_low.ogg"));
		sirenHigh = Gdx.audio.newSound(Gdx.files.internal("Sounds/almost_over.ogg"));
		ghostFleeing = Gdx.audio.newSound(Gdx.files.internal("Sounds/ghost_fleeing.ogg"));
		powerUpMode = Gdx.audio.newSound(Gdx.files.internal("Sounds/power_up_mode.ogg"));

		// Load the spritesheet containing all 16 x 16 sprites
		spritesheet = new Texture("spritesheet.png");
		pacmanLogo = new Texture("logo.png");
		play = new Texture("play.png");
		playBright = new Texture("playBright.png");
		playAgain = new Texture("playAgain.png");
		playAgainBright = new Texture("playAgainBright.png");
	}

	/**
	 * Isolate or "extract" the sprites from the spritesheet
	 */
	private static void createTextures()
	{
		sprites = new TextureRegion[68];

		// Pacman right animation images
		sprites[0] = new TextureRegion(spritesheet, 0, 0, 16, 16);
		sprites[1] = new TextureRegion(spritesheet, 16, 0, 16, 16);
		// Pacman left animation images
		sprites[2] = new TextureRegion(spritesheet, 0, 16, 16, 16);
		sprites[3] = new TextureRegion(spritesheet, 16, 16, 16, 16);
		// Pacman up animation images
		sprites[4] = new TextureRegion(spritesheet, 0, 32, 16, 16);
		sprites[5] = new TextureRegion(spritesheet, 16, 32, 16, 16);
		// Pacman down animation images
		sprites[6] = new TextureRegion(spritesheet, 0, 48, 16, 16);
		sprites[7] = new TextureRegion(spritesheet, 16, 48, 16, 16);

		// Blinky Right animation images
		sprites[8] = new TextureRegion(spritesheet, 0, 64, 16, 16);
		sprites[9] = new TextureRegion(spritesheet, 16, 64, 16, 16);
		// Blinky left animation images
		sprites[10] = new TextureRegion(spritesheet, 32, 64, 16, 16);
		sprites[11] = new TextureRegion(spritesheet, 48, 64, 16, 16);
		// Blinky up animation images
		sprites[12] = new TextureRegion(spritesheet, 64, 64, 16, 16);
		sprites[13] = new TextureRegion(spritesheet, 80, 64, 16, 16);
		// Blinky down animation images
		sprites[14] = new TextureRegion(spritesheet, 96, 64, 16, 16);
		sprites[15] = new TextureRegion(spritesheet, 112, 64, 16, 16);

		// Pinky right animation images
		sprites[16] = new TextureRegion(spritesheet, 0, 80, 16, 16);
		sprites[17] = new TextureRegion(spritesheet, 16, 80, 16, 16);
		// Pinky left animation images
		sprites[18] = new TextureRegion(spritesheet, 32, 80, 16, 16);
		sprites[19] = new TextureRegion(spritesheet, 48, 80, 16, 16);
		// Pinky up animation images
		sprites[20] = new TextureRegion(spritesheet, 64, 80, 16, 16);
		sprites[21] = new TextureRegion(spritesheet, 80, 80, 16, 16);
		// Pinky down animation images
		sprites[22] = new TextureRegion(spritesheet, 96, 80, 16, 16);
		sprites[23] = new TextureRegion(spritesheet, 112, 80, 16, 16);

		// Inky right animation images
		sprites[24] = new TextureRegion(spritesheet, 0, 96, 16, 16);
		sprites[25] = new TextureRegion(spritesheet, 16, 96, 16, 16);
		// Inky left animation images
		sprites[26] = new TextureRegion(spritesheet, 32, 96, 16, 16);
		sprites[27] = new TextureRegion(spritesheet, 48, 96, 16, 16);
		// Inky up animation images
		sprites[28] = new TextureRegion(spritesheet, 64, 96, 16, 16);
		sprites[29] = new TextureRegion(spritesheet, 80, 96, 16, 16);
		// Inky down animation images
		sprites[30] = new TextureRegion(spritesheet, 96, 96, 16, 16);
		sprites[31] = new TextureRegion(spritesheet, 112, 96, 16, 16);

		// Clyde right animation images
		sprites[32] = new TextureRegion(spritesheet, 0, 112, 16, 16);
		sprites[33] = new TextureRegion(spritesheet, 16, 112, 16, 16);
		// Clyde left animation images
		sprites[34] = new TextureRegion(spritesheet, 32, 112, 16, 16);
		sprites[35] = new TextureRegion(spritesheet, 48, 112, 16, 16);
		// Clyde up animation images
		sprites[36] = new TextureRegion(spritesheet, 64, 112, 16, 16);
		sprites[37] = new TextureRegion(spritesheet, 80, 112, 16, 16);
		// Clyde down animation images
		sprites[38] = new TextureRegion(spritesheet, 96, 112, 16, 16);
		sprites[39] = new TextureRegion(spritesheet, 112, 112, 16, 16);

		// Pacman full circle
		sprites[40] = new TextureRegion(spritesheet, 32, 0, 16, 16);

		// Pacman death sequence
		sprites[41] = new TextureRegion(spritesheet, 48, 0, 16, 16);
		sprites[42] = new TextureRegion(spritesheet, 64, 0, 16, 16);
		sprites[43] = new TextureRegion(spritesheet, 80, 0, 16, 16);
		sprites[44] = new TextureRegion(spritesheet, 96, 0, 16, 16);
		sprites[45] = new TextureRegion(spritesheet, 112, 0, 16, 16);
		sprites[46] = new TextureRegion(spritesheet, 128, 0, 16, 16);
		sprites[47] = new TextureRegion(spritesheet, 144, 0, 16, 16);
		sprites[48] = new TextureRegion(spritesheet, 160, 0, 16, 16);
		sprites[49] = new TextureRegion(spritesheet, 176, 0, 16, 16);
		sprites[50] = new TextureRegion(spritesheet, 192, 0, 16, 16);
		sprites[51] = new TextureRegion(spritesheet, 208, 0, 16, 16);

		// Item textures
		sprites[52] = new TextureRegion(spritesheet, 32, 48, 16, 16);
		sprites[53] = new TextureRegion(spritesheet, 48, 48, 16, 16);
		sprites[54] = new TextureRegion(spritesheet, 64, 48, 16, 16);
		sprites[55] = new TextureRegion(spritesheet, 80, 48, 16, 16);
		sprites[56] = new TextureRegion(spritesheet, 96, 48, 16, 16);
		sprites[57] = new TextureRegion(spritesheet, 112, 48, 16, 16);
		sprites[58] = new TextureRegion(spritesheet, 128, 48, 16, 16);
		sprites[59] = new TextureRegion(spritesheet, 144, 48, 16, 16);

		// Ghost death
		sprites[60] = new TextureRegion(spritesheet, 128, 64, 16, 16);
		sprites[61] = new TextureRegion(spritesheet, 144, 64, 16, 16);
		sprites[62] = new TextureRegion(spritesheet, 160, 64, 16, 16);
		sprites[63] = new TextureRegion(spritesheet, 174, 64, 16, 16);

		// Ghost eyes
		sprites[64] = new TextureRegion(spritesheet, 128, 80, 16, 16);
		sprites[65] = new TextureRegion(spritesheet, 144, 80, 16, 16);
		sprites[66] = new TextureRegion(spritesheet, 160, 80, 16, 16);
		sprites[67] = new TextureRegion(spritesheet, 174, 80, 16, 16);

		// Score textures
		scoreTextures = new TextureRegion[4];
		scoreTextures[0] = new TextureRegion(spritesheet, 0, 128, 16, 16);
		scoreTextures[1] = new TextureRegion(spritesheet, 16, 128, 16, 16);
		scoreTextures[2] = new TextureRegion(spritesheet, 32, 128, 16, 16);
		scoreTextures[3] = new TextureRegion(spritesheet, 48, 128, 16, 16);
	}

	/**
	 * Create all the animations for all characters in the game
	 */
	private static void createAnimations()
	{
		Animation<TextureRegion> pacmanRight = new Animation<>(0.10f, new TextureRegion[] {sprites[0], sprites[1]});
		Animation<TextureRegion> pacmanLeft = new Animation<>(0.10f, new TextureRegion[] {sprites[2], sprites[3]});
		Animation<TextureRegion> pacmanUp = new Animation<>(0.10f, new TextureRegion[] {sprites[4], sprites[5]});
		Animation<TextureRegion> pacmanDown = new Animation<>(0.10f, new TextureRegion[] {sprites[6], sprites[7]});

		Animation<TextureRegion> blinkyRight = new Animation<>(0.25f, new TextureRegion[] {sprites[8], sprites[9]});
		Animation<TextureRegion> blinkyLeft = new Animation<>(0.25f, new TextureRegion[] {sprites[10], sprites[11]});
		Animation<TextureRegion> blinkyUp = new Animation<>(0.25f, new TextureRegion[] {sprites[12], sprites[13]});
		Animation<TextureRegion> blinkyDown = new Animation<>(0.25f, new TextureRegion[] {sprites[14], sprites[15]});

		Animation<TextureRegion> pinkyRight = new Animation<>(0.25f, new TextureRegion[] {sprites[16], sprites[17]});
		Animation<TextureRegion> pinkyLeft = new Animation<>(0.25f, new TextureRegion[] {sprites[18], sprites[19]});
		Animation<TextureRegion> pinkyUp = new Animation<>(0.25f, new TextureRegion[] {sprites[20], sprites[21]});
		Animation<TextureRegion> pinkyDown = new Animation<>(0.25f, new TextureRegion[] {sprites[22], sprites[23]});

		Animation<TextureRegion> inkyRight = new Animation<>(0.25f, new TextureRegion[] {sprites[24], sprites[25]});
		Animation<TextureRegion> inkyLeft = new Animation<>(0.25f, new TextureRegion[] {sprites[26], sprites[27]});
		Animation<TextureRegion> inkyUp = new Animation<>(0.25f, new TextureRegion[] {sprites[28], sprites[29]});
		Animation<TextureRegion> inkyDown = new Animation<>(0.25f, new TextureRegion[] {sprites[30], sprites[31]});

		Animation<TextureRegion> clydeRight = new Animation<>(0.25f, new TextureRegion[] {sprites[32], sprites[33]});
		Animation<TextureRegion> clydeLeft = new Animation<>(0.25f, new TextureRegion[] {sprites[34], sprites[35]});
		Animation<TextureRegion> clydeUp = new Animation<>(0.25f, new TextureRegion[] {sprites[36], sprites[37]});
		Animation<TextureRegion> clydeDown = new Animation<>(0.25f, new TextureRegion[] {sprites[38], sprites[39]});

		Animation<TextureRegion> ghostDeathBlue = new Animation<>(0.25f, new TextureRegion[] {sprites[60], sprites[61]});
		Animation<TextureRegion> ghostDeathWhite = new Animation<>(0.25f, new TextureRegion[] {sprites[62], sprites[63]});

		pacmanDeath = new Animation<>(0.13f, new TextureRegion[] {sprites[41], sprites[42], sprites[43], sprites[44], sprites[45], sprites[46], sprites[47], sprites[48], sprites[49], sprites[50], sprites[51]});

		entityFrames = new Animation[6][4];
		entityDefaultFrame = new TextureRegion[5][4];

		// Pacman animations
		entityFrames[0][0] = pacmanLeft;
		entityFrames[0][1] = pacmanRight;
		entityFrames[0][2] = pacmanUp;
		entityFrames[0][3] = pacmanDown;

		// Pacman default frames
		entityDefaultFrame[0][0] = sprites[3];
		entityDefaultFrame[0][1] = sprites[1];
		entityDefaultFrame[0][2] = sprites[5];
		entityDefaultFrame[0][3] = sprites[7];

		// Blinky animations
		entityFrames[4][0] = blinkyLeft;
		entityFrames[4][1] = blinkyRight;
		entityFrames[4][2] = blinkyUp;
		entityFrames[4][3] = blinkyDown;

		// Blinky default frames
		entityDefaultFrame[4][0] = sprites[11];
		entityDefaultFrame[4][1] = sprites[9];
		entityDefaultFrame[4][2] = sprites[13];
		entityDefaultFrame[4][3] = sprites[15];

		// Pinky animations
		entityFrames[1][0] = pinkyLeft;
		entityFrames[1][1] = pinkyRight;
		entityFrames[1][2] = pinkyUp;
		entityFrames[1][3] = pinkyDown;

		// Pinky default frames
		entityDefaultFrame[1][0] = sprites[19];
		entityDefaultFrame[1][1] = sprites[17];
		entityDefaultFrame[1][2] = sprites[21];
		entityDefaultFrame[1][3] = sprites[23];

		// Inky animations
		entityFrames[2][0] = inkyLeft;
		entityFrames[2][1] = inkyRight;
		entityFrames[2][2] = inkyUp;
		entityFrames[2][3] = inkyDown;

		// Inky default frames
		entityDefaultFrame[2][0] = sprites[27];
		entityDefaultFrame[2][1] = sprites[25];
		entityDefaultFrame[2][2] = sprites[29];
		entityDefaultFrame[2][3] = sprites[31];

		// Inky animations
		entityFrames[3][0] = clydeLeft;
		entityFrames[3][1] = clydeRight;
		entityFrames[3][2] = clydeUp;
		entityFrames[3][3] = clydeDown;

		// Inky default frames
		entityDefaultFrame[3][0] = sprites[35];
		entityDefaultFrame[3][1] = sprites[33];
		entityDefaultFrame[3][2] = sprites[37];
		entityDefaultFrame[3][3] = sprites[39];

		// Inky animations
		entityFrames[5][0] = ghostDeathBlue;
		entityFrames[5][1] = ghostDeathWhite;
	}
}
