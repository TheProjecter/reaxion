package com.googlecode.reaxion.game;

import java.io.IOException;
import java.util.concurrent.Callable;

import javax.swing.JOptionPane;

import com.googlecode.reaxion.game.audio.AudioPlayer;
import com.googlecode.reaxion.game.mission.MissionManager;
import com.googlecode.reaxion.game.state.BattleGameState;
import com.googlecode.reaxion.game.state.CharacterSelectionState;
import com.googlecode.reaxion.game.state.StageSelectionState;
import com.googlecode.reaxion.game.util.FontUtils;
import com.googlecode.reaxion.game.util.PlayerInfoManager;
import com.jme.input.MouseInput;
import com.jme.util.GameTaskQueueManager;
import com.jmex.editors.swing.settings.GameSettingsPanel;
import com.jmex.game.StandardGame;
import com.jmex.game.state.GameStateManager;
import com.jmex.game.state.load.LoadingGameState;

/**
 * The main game. This should run everything, but for now it's just a luncher
 * for {@code BattleGameState}.
 * 
 * @author Nilay, Khoa
 */
public class Reaxion {
	private static final long SERVER_OBJECT = 1;
	private static final long CLIENT_OBJECT = 2;

	private static final String GAME_VERSION = "0.5a";

	/**
	 * Multithreaded game system that shows the state of GameStates
	 */
	private StandardGame game;

	private StageSelectionState stageState;

	/**
	 * GameState that shows progress of resource loading
	 */
	private LoadingGameState loadState;

	/**
	 * GameState that allows basic WASD movement, mouse camera rotation, and
	 * placement of objects, lights, etc.
	 */
	private BattleGameState battleState;

	/**
	 * GameState that allows character selection.
	 */
	private CharacterSelectionState charState;

	// private StageSelectionState stageState;

	/**
	 * Initialize the system
	 */
	public Reaxion() {
		/* Allow collection and viewing of scene statistics */
		System.setProperty("jme.stats", "set");
		/* Create a new StandardGame object with the given title in the window */
		game = new StandardGame("Reaxion v" + GAME_VERSION);
	}

	public static void main(String[] args) {
		try {
			Reaxion main = new Reaxion();
			main.start();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Start up the system
	 * 
	 * @throws IOException
	 *             when StandardGame's settings are unavailable (?)
	 * @throws InterruptedException
	 */
	public void start() throws InterruptedException {
		if (GameSettingsPanel.prompt(game.getSettings()))
			game.start();
		GameTaskQueueManager.getManager().update(new GameInit());
	}

	/**
	 * Performs necessary cleanup, then closes application.
	 */
	public static void terminate() {
		AudioPlayer.cleanup();
		System.exit(0);
	}

	/**
	 * Initializes the system.
	 * 
	 * @author Nilay
	 * 
	 */
	private class GameInit implements Callable<Void> {

		// @Override
		public Void call() throws Exception {
			MouseInput.get().setCursorVisible(true);
			AudioPlayer.prepare();
			// SoundEffectManager.initialize();
			FontUtils.loadFonts();
			MissionManager.createMissions();

			int sv = JOptionPane.showConfirmDialog(null, "Be server?");
			
			

			switch (sv) {
			case 0:
				NetworkingObjects.setUpServer();
				break;
			case 1:
				NetworkingObjects.setUpClient();
				break;
			case 2:
			default:
				terminate();
			}

			PlayerInfoManager.init();

			charState = new CharacterSelectionState();
			GameStateManager.getInstance().attachChild(charState);
			charState.setActive(true);

			return null;
		}


	}
}