package com.googlecode.reaxion.game.mission;

/**
 * {@code MissionID} is a set of mission names each containing an {@code int} id.
 * This {@code enum} is used in lieu of an {@code int} in the {@code MissionManager} 
 * {@code startMission} method for programmer clarity.
 * 
 * @author Brian Clanton
 *
 */

public enum MissionID {
	
	OPEN_HUBGAMESTATE(-1),
	DEFEAT_LIGHT_USER(1),
	VS_TOYBOX (2),
	VS_DORIRUZU (3),
	VS_MONICA_1 (4),
	VS_REMNANT (5),
	VS_SKYTANK (6),
	VS_PYROCLAST(7);
	
	public int id;
	
	private MissionID(int id) {
		this.id = id;
	}
	
}
