package com.googlecode.reaxion.game.burstgrid.info;

/**
 * This class represents Austin's statistics. Reasonable base stats are yet to
 * be decided.
 * 
 * @author Cycofactory
 * 
 */
public class AustinInfo extends PlayerInfo {

	@Override
	public void init() {
		setAbilities(new String[] { "RapidGauge" });
		setAttacks(new String[] { "BombingMagnet", "ShootFireball",
				"SpawnBubble", "LanceGuard" });
		createBurstGrid("");
	}
	
}