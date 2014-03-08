package ca.pluszero.emotive.models;

public final class Option {
	
//	<item>Search</item> <!-- Google query in user's web browser; not in web view -->
//  <item>Find</item> <!-- Location search with GMaps-->
//  <item>Listen</item> <!-- Play music  -->
//  <item>Watch a movie</item> <!-- Watch a movie in some theatre -->

	public static enum Verb {
		SEARCH(0),
		FIND_ME(1),
		LISTEN_TO(2),
		WATCH_A_MOVIE(3);
		public final int val;
		Verb(int val) { this.val = val; }
	}
	
//    <item>Anything</item>
//    <item>Web</item>
//    <item>Images</item>
	public static enum Search {
		ANYTHING(0),
		WEB(1),
		IMAGES(2);
		public final int val;
		Search(int val) { this.val = val; }
	}
	
//    <item>On my device</item>
//    <item>On YouTube</item>
	
//    <string-array name="find_me_options">
//        <item>Anything</item>
//        <item>Restaurant</item>
//        <item>Movie Theatre</item>

	public static enum FindMe {
		ANYTHING(0),
		FOOD(1),
		THEATRE(2);
		public final int val;
		FindMe(int val) { this.val = val; }
	}
	
	public static enum ListenTo {
		ON_MY_DEVICE(0),
		ON_YOUTUBE(1),
		ON_GROOVESHARK(2);
		public final int val;
		ListenTo(int val) { this.val = val; }
	}
	
	public static enum WatchAMovie {
		NEAR_ME(0),
		NEAR(1),
		WATCH_A_TRAILER(2);
		public final int val;
		WatchAMovie(int val) { this.val = val; }
	}
}
