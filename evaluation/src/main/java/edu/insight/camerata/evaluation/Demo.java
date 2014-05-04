package edu.insight.camerata.evaluation;

import org.jfugue.Pattern;
import org.jfugue.Player;

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
