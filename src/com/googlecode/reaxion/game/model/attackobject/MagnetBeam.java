package com.googlecode.reaxion.game.model.attackobject;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.state.StageGameState;

public class MagnetBeam extends AttackObject {
	public static final String filename = "laser";
	protected static final int span = 40;
	protected static final float dpf = .5f;
	
	public MagnetBeam(Model m) {
		super(filename, dpf, m);
		flinch = true;
		lifespan = span;
	}
	
	public MagnetBeam(Model[] m) {
		super(filename, dpf, m);
		flinch = true;
		lifespan = span;
	}
	
	@Override
	public void act(StageGameState b) {
		//check lifespan
        if (lifeCount == lifespan)
        	b.removeModel(this);
        lifeCount++;
	}
	
}
