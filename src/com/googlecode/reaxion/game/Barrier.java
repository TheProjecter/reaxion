package com.googlecode.reaxion.game;

import com.jme.math.Vector3f;

public class Barrier extends AttackObject {
	
	protected static final String filename = "barrier";
	protected static final int span = 180;
	protected static final int fadePoint = 168;
	protected static final float dpf = 0;
	
	public Barrier(Model m) {
    	super(filename, dpf, m);
    	lifespan = span;
    	allowYaw = false;
    	allowPitch = false;
    }
	
	public Barrier(Model[] m) {
    	super(filename, dpf, m);
    	lifespan = span;
    	allowYaw = false;
    	allowPitch = false;
    }
	
	@Override
	public void act(BattleGameState b) {
		
		if (lifeCount >= fadePoint) {
			float factor = ((float)lifespan - lifeCount)/(float)(lifespan - fadePoint);
			model.setLocalScale(new Vector3f(factor, factor, factor));
			model.setLocalTranslation(model.getLocalTranslation().add(new Vector3f(0, 2.75f/(float)(lifespan - fadePoint), 0)));
		}
        
        //check lifespan
        if (lifeCount == lifespan)
        	b.removeModel(this);
        lifeCount++;
    }
	
	// ends attack
	public void cancel() {
		lifeCount = lifespan;
	}
	
}