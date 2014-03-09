package ca.pluszero.emotive.models;

public final class Option {

//	<item>Search</item> <!-- Google query in user's web browser; not in web view -->
//  <item>Find</item> <!-- Location search with GMaps-->
//  <item>Listen</item> <!-- Play music  -->
//  <item>Watch a movie</item> <!-- Watch a movie in some theatre -->

    public static enum Verb {
        FOOD(1),
        MUSIC(2),
        LEARN(3),
        WATCH(4);
        public final int val;

        Verb(int val) {
            this.val = val;
        }
    }

//    <item>Anything</item>
//    <item>Web</item>
//    <item>Images</item>
}
