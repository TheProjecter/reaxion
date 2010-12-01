package com.googlecode.reaxion.game.state;

import java.util.logging.Logger;

import com.googlecode.reaxion.game.Reaxion;
import com.googlecode.reaxion.game.audio.AudioPlayer;
import com.googlecode.reaxion.game.audio.BgmPlayer;
import com.googlecode.reaxion.game.mission.MissionManager;
import com.googlecode.reaxion.game.overlay.ResultsOverlay;
import com.jme.app.AbstractGame;
import com.jme.image.Texture;
import com.jme.input.AbsoluteMouse;
import com.jme.input.InputHandler;
import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;
import com.jme.input.MouseInput;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.scene.shape.Quad;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;
import com.jmex.game.state.CameraGameState;
import com.jmex.game.state.GameStateManager;

/**
 * {@code ResultsGameState} is a simple GameState that has a camera and an
 * overlay displaying the results of the previous {@code BattleGameState}.
 * 
 * @author Khoa
 */
public class ResultsGameState extends CameraGameState {

	public static final String NAME = "resultsGameState";

	private static final Logger logger = Logger
			.getLogger(ResultsGameState.class.getName());

	public float tpf;

	private BattleGameState battle;

	private ResultsOverlay resultsNode;

	protected InputHandler input;

	protected AbstractGame game = null;

	private AbsoluteMouse mouse;

	public ResultsGameState(BattleGameState b) {
		super(NAME);
		battle = b;
		init();
	}

	private void init() {
		rootNode = new Node("RootNode");

		// Prepare results node
		resultsNode = new ResultsOverlay();
		resultsNode.setCombatants(battle.getPlayer(), battle.getPartner(),
				battle.getOpponents());
		resultsNode.setStats(battle.targetTime, battle.getTotalTime(), battle
				.getPlayer(), battle.getPartner(), battle.expYield);
		rootNode.attachChild(resultsNode);

		// Fix up the camera, will not be needed for final camera controls
		Vector3f loc = new Vector3f(0.0f, 2.5f, 10.0f);
		Vector3f left = new Vector3f(-1.0f, 0.0f, 0.0f);
		Vector3f up = new Vector3f(0.0f, 1.0f, 0.0f);
		Vector3f dir = new Vector3f(0.0f, 0f, -1.0f);
		cam.setFrame(loc, left, up, dir);
		cam.setFrustumPerspective(45f, (float) DisplaySystem.getDisplaySystem()
				.getWidth()
				/ DisplaySystem.getDisplaySystem().getHeight(), .01f, 1000);
		cam.update();

		// Initial InputHandler
		// input = new FirstPersonHandler(cam, 15.0f, 0.5f);
		input = new InputHandler();
		initKeyBindings();

		// Setup software mouse
		/*
		mouse = new AbsoluteMouse("Mouse Input", DisplaySystem
				.getDisplaySystem().getWidth(), DisplaySystem
				.getDisplaySystem().getHeight());
		mouse.registerWithInputHandler(input);
		TextureState cursor = DisplaySystem.getDisplaySystem().getRenderer()
				.createTextureState();
		cursor.setTexture(TextureManager.loadTexture(Reaxion.class
				.getClassLoader().getResource(
						"com/googlecode/reaxion/resources/cursors/cursor.png"),
				Texture.MinificationFilter.NearestNeighborNoMipMaps,
				Texture.MagnificationFilter.NearestNeighbor));
		mouse.setRenderState(cursor);
		BlendState as1 = DisplaySystem.getDisplaySystem().getRenderer()
				.createBlendState();
		as1.setBlendEnabled(true);
		as1.setSourceFunction(BlendState.SourceFunction.SourceAlpha);
		as1
				.setDestinationFunction(BlendState.DestinationFunction.OneMinusSourceAlpha);
		as1.setTestEnabled(true);
		as1.setTestFunction(BlendState.TestFunction.GreaterThan);
		mouse.setRenderState(as1);
		rootNode.attachChild(mouse);
		*/
		
		// increment exp
		resultsNode.allotExp(battle.getPlayer(), battle.getPartner());

		// Finish up
		rootNode.updateRenderState();
		rootNode.updateWorldBound();
		rootNode.updateGeometricState(0.0f, true);
	}

	// duplicate the functionality of DebugGameState
	// Most of this can be commented out during finalization
	private void initKeyBindings() {
		KeyBindingManager.getKeyBindingManager().set("screen_shot",
				KeyInput.KEY_F1);
		KeyBindingManager.getKeyBindingManager().set("exit",
				KeyInput.KEY_ESCAPE);
		KeyBindingManager.getKeyBindingManager().set("mem_report",
				KeyInput.KEY_R);
		KeyBindingManager.getKeyBindingManager().set("toggle_mouse",
				KeyInput.KEY_M);
		KeyBindingManager.getKeyBindingManager().set("return",
				KeyInput.KEY_RETURN);
	}

	@Override
	public void stateUpdate(float _tpf) {
		tpf = _tpf;

		// Update the InputHandler
		if (input != null) {
			input.update(tpf);

			/** If exit is a valid command (via key Esc), exit game */
			if (KeyBindingManager.getKeyBindingManager().isValidCommand("exit",
					false)) {
				if (game != null) {
					game.finish();
				} else {
					Reaxion.terminate();
				}
			}
		}

		// Update the overlay
		resultsNode.update(this);

		// Update the geometric state of the rootNode
		rootNode.updateGeometricState(tpf, true);

		if (input != null) {
			if (KeyBindingManager.getKeyBindingManager().isValidCommand(
					"return", false)) {
				returnToCharSelectState();
			}
			if (KeyBindingManager.getKeyBindingManager().isValidCommand(
					"screen_shot", false)) {
				DisplaySystem.getDisplaySystem().getRenderer().takeScreenShot(
						"SimpleGameScreenShot");
			}
			if (KeyBindingManager.getKeyBindingManager().isValidCommand(
					"mem_report", false)) {
				long totMem = Runtime.getRuntime().totalMemory();
				long freeMem = Runtime.getRuntime().freeMemory();
				long maxMem = Runtime.getRuntime().maxMemory();

				logger.info("|*|*|  Memory Stats  |*|*|");
				logger.info("Total memory: " + (totMem >> 10) + " kb");
				logger.info("Free memory: " + (freeMem >> 10) + " kb");
				logger.info("Max memory: " + (maxMem >> 10) + " kb");
			}
			if (KeyBindingManager.getKeyBindingManager().isValidCommand(
					"toggle_mouse", false)) {
				MouseInput.get().setCursorVisible(
						!MouseInput.get().isCursorVisible());
				logger.info("Cursor Visibility set to "
						+ MouseInput.get().isCursorVisible());
			}
		}
	}

	private void returnToCharSelectState() {
		setActive(false);
		GameStateManager.getInstance().detachChild(this);
		
		if (MissionManager.hasCurrentMission())
			MissionManager.startNext();
		else
			GameStateManager.getInstance().getChild(CharacterSelectionState.NAME).setActive(true);
	}
	
	public void cleanup() {
	}
}
