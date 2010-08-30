package com.googlecode.reaxion.game;

import com.jme.math.FastMath;
import com.jme.math.Vector3f;

/**
 * Fires an energy bullet towards the target
 */
public class ShieldBarrier extends Attack {
	
	private static final int duration = 180;
	private Barrier barrier;
	
	public ShieldBarrier() {
		name = "Barrier";
	}
	
	public ShieldBarrier(AttackData ad) {
		super(ad, 2);
		name = "Barrier";
		if (character.model.getWorldTranslation().y > 0)
			finish();
	}
	
	@Override
	public void load() {
		LoadingQueue.push(new Model(Barrier.filename));
	}
	
	@Override
	public void firstFrame(BattleGameState b) {
		character.moveLock = true;
		character.jumpLock = true;
		character.animationLock = true;
		
		character.play("guard");
		character.velocity = new Vector3f(0, 0, 0);
		
		// calculate transformations
		Vector3f rotation = character.rotationVector;
		float angle = FastMath.atan2(rotation.x, rotation.z);
		Vector3f translation = new Vector3f(1.5f*FastMath.sin(angle), 0, 1.5f*FastMath.cos(angle));
		
		barrier = (Barrier)LoadingQueue.quickLoad(new Barrier(character), b);
		
		barrier.rotate(rotation);
		barrier.model.setLocalTranslation(character.model.getWorldTranslation().add(translation));
		
		b.getRootNode().updateRenderState();
	}
	
	@Override
	public void nextFrame(BattleGameState b) {
		if (frameCount >= duration) {
			finish();
		}
	}
	
	@Override
	public void interrupt(BattleGameState b, Model other) {
		// check if the interrupting object passed through the barrier
        Model[] collisions = other.getLinearModelCollisions(b, other.velocity.normalize().mult(-1.5f), .02f);
        for (Model c : collisions) {
        	if (c == barrier) {
        		// third the damage and no flinch!
            	character.hp -= other.damagePerFrame/3;
            	//System.out.println(character.model+" hit by "+other+": "+(character.hp+other.damagePerFrame/2)+" -> "+character.hp);
            	return;
        	}
        }
        character.reactHit(b, other);
	}
	
	@Override
	public void finish() {
		super.finish();
		barrier.cancel();
		character.moveLock = false;
		character.jumpLock = false;
		character.animationLock = false;
	}
	
}