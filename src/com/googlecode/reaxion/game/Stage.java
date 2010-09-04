package com.googlecode.reaxion.game;

import java.awt.geom.Point2D;
import java.util.Arrays;

import com.jme.math.FastMath;
import com.jme.math.Vector3f;

/**
 * Stages used during the {@code BattleGameState} should extend this class.
 * @author Khoa
 */
public class Stage extends Model {
    
    public Stage() {
    	init();
    }
    
    public Stage(String fn) {
    	filename = fn;
    	init();
    }

    /**
     * Repositions character to ensure that it remains within the bounds of
     * the stage.
     */
    public void contain(Character c) {
    	Vector3f v = c.model.getWorldTranslation();
    	Point2D.Float p = new Point2D.Float(v.x + c.velocity.x, v.z + c.velocity.z);
    	
    	// snap to boundaries if outside
    	Point2D.Float offset = snapToBounds(p);
    	c.velocity = c.velocity.subtract(new Vector3f(offset.x, 0, offset.y));
    	
    	Point2D.Float[] hit = bound(c);
    	
    	// if hits occur at two or more points along the circumference
    	if (hit.length >= 2) {
    		
    		Point2D.Float midpoint = new Point2D.Float();
    		
    		// since hits penetrate in sets of two, the true midpoint is the midpoint of the midpoints
    		for (int i=0; i<hit.length - hit.length%2; i+=2) {
    			midpoint.x += hit[i+1].x+hit[i].x;
    			midpoint.y += hit[i+1].y+hit[i].y;
    		}
    		midpoint.x /= hit.length - hit.length%2;
    		midpoint.y /= hit.length - hit.length%2; 
    		
    		
    		//Point2D.Float midpoint = new Point2D.Float((hit[1].x+hit[0].x)/2, (hit[1].y+hit[0].y)/2);
    		
    		// find magnitude of displacement
    		float magnitude = c.boundRadius - FastMath.sqrt(FastMath.pow(midpoint.x-p.x, 2) + FastMath.pow(midpoint.y-p.y, 2));
    		
    		// find angle to midpoint from center
    		float angle = FastMath.atan2(midpoint.y - p.y, midpoint.x - p.x);
    		
    		// find vector of penetration
    		Vector3f vector = new Vector3f(magnitude*FastMath.cos(angle), 0, magnitude*FastMath.sin(angle));
    		
    		//System.out.println(Arrays.toString(hit) +" -> "+ midpoint +": "+ v +" + "+ vector);
    		
    		// fix the velocity vector so that the collision doesn't occur
			c.velocity = c.velocity.subtract(vector);
			
			if (offset.x != 0 && offset.y != 0)
				System.out.println("Out of bounds correction -> "+v.add(c.velocity));
    	}
    }
    
    /**
     * Return the center's offset out of bounds and return it to bound boundaries.
     * Override to add functionality.
     */
    public Point2D.Float snapToBounds(Point2D.Float center) {
    	return (new Point2D.Float());
    }
    
    /**
     * Calls {@code bound()} using a {@code Character}'s next position and
     * radius
     */
    public Point2D.Float[] bound(Character c) {
    	Vector3f t = c.model.getWorldTranslation();
    	return bound(new Point2D.Float(t.x+c.velocity.x, t.z+c.velocity.z), c.boundRadius);
    }
    
    /**
     * Returns up to two points of collision between the stage bounds and a
     * circle with center {@code center} and radius {@code radius}. Override
     * to add actual bounding function.
     */
    public Point2D.Float[] bound(Point2D.Float center, float radius) {
    	return (new Point2D.Float[0]);
    }
}