package com.googlecode.reaxion.game;

public class Nilay extends MajorCharacter {
	
	private static final String filename = "i_nilay4";
	
    public Nilay() {
    	// Load model
    	super(filename);
    }
    
    public Nilay(Boolean _trackable) {
    	// Load model
    	super(filename, _trackable);
    }
    
    @ Override
    public void type() {
    	maxHp = 100;
    	maxGauge = 100;
    	speed = .5f;
    }
    
}
