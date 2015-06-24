package edu.insight.camerata.evaluation.rough;

import org.jfugue.pattern.Pattern;
import org.jfugue.player.Player;


public class Demo {
	
	  public static void main( String[] args )
	    {
	        System.out.println( "Hello World!" );
			Player player = new Player();
			Pattern pattern = new Pattern("C D E F G A B  C D E F G A B C D E F G A B C D E F G A B");
			player.play(pattern);
			System.exit(0);
	    }

}
