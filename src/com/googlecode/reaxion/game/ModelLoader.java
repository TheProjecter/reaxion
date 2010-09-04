package com.googlecode.reaxion.game;

import com.radakan.jme.mxml.*;
import com.googlecode.reaxion.test.ModelTest;
import com.jme.scene.Node;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.event.EventListenerList;

/**
 * Facilitates the importing and loading of files into a {@code Model}
 * @author Khoa
 */
public class ModelLoader {
	
	private static final String baseURL = "com/googlecode/reaxion/resources/";
    
	/**
	 * Loads a new Model based on {@code filename} and returns the resulting {@code Model}
	 * @param filename shared name between mesh, skeleton, and material XML files
	 * @return Model with loaded model
	 * @author Khoa
	 */
	public static Model load(String filename) {
    	Model chr = new Model();
    	
    	return load(chr, filename);
    }
	
	/**
	 * Loads {@code filename} into the provided {@code Model} and returns the resulting {@code Model}
	 * @param chr - Model to be loaded into
	 * @param filename shared name between mesh, skeleton, and material XML files
	 * @return Model with loaded model
	 * @author Khoa
	 */
    public static Model load(Model chr, String filename) {
    	try {
			chr.model = ResourceLibrary.get(filename);
			chr.initialize();
	    	LoadingQueue.pop(chr);
		} catch (Exception e) {
			System.out.println("Error loading model.");
		}
        
        return chr;
    }
}