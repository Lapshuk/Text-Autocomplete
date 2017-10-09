import ucb.junit.textui;
import org.junit.Test;

import java.util.HashMap;
import java.util.LinkedList;

import static org.junit.Assert.*;

/**
 * The suite of all JUnit tests for the Autocomplete class.
 *
 * @author
 */
public class TestAutocomplete {


    @Test
    public void constructorCreatesTreeSimpleTest() {
        String[] words = {"hello", "hi", "bye"};
        double[] weights = {1.0, 2.0, 3.0};

        Autocomplete a = new Autocomplete(words, weights);

        HashMap<Character, Trie.Node> actual = a.getDictionary().getRoot().getLinks();
        HashMap<Character, Trie.Node> expected = new HashMap<>();
        expected.put('h', null);
        expected.put('b', null);

        assertEquals(actual.keySet(), expected.keySet());
    }

    @Test
    public void constructorErrorCasesTest() {
        String[] words = {"hello", "hi", "bye"};

        double[] weights = {1.0, 2.0, 3.0, 4.0};
        try {
            Autocomplete a = new Autocomplete(words, weights);
            assertFalse(true);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }

        double[] weights2 = {1.0, 2.0};
        try {
            Autocomplete a = new Autocomplete(words, weights2);
            assertFalse(true);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }

        double[] weights3 = {1.0, 2.0, -3.0};
        try {
            Autocomplete a = new Autocomplete(words, weights3);
            assertFalse(true);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }

        double[] weights4 = {1.0, 2.0, 3.0};
        String[] words2 = {"hello", "hi", "hello"};
        try {
            Autocomplete a = new Autocomplete(words2, weights4);
            assertFalse(true);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    @Test
    public void topMatchSimpleTest() {
        String[] words = {"automobile", "automatic"};
        double[] weights = {2.0, 1.0};

        Autocomplete a = new Autocomplete(words, weights);

        assertEquals("automobile", a.topMatch("auto"));
    }

    @Test
    public void topMatchSimple2Test() {
        String[] words = {"Al Mahallah al Kubra", "Al Mansurah", "Al Mubarraz, Saudi Arabia"};
        double[] weights = {431052.0, 420195.0, 290802.0};

        Autocomplete a = new Autocomplete(words, weights);

        assertEquals("Al Mahallah al Kubra", a.topMatch("Al M"));
    }

    @Test
    public void topMatchEmptyPrefixTest() {
        String[] words = {"Al Mahallah al Kubra", "Al Mansurah", "Al Mubarraz, Saudi Arabia"};
        double[] weights = {431052.0, 420195.0, 290802.0};

        Autocomplete a = new Autocomplete(words, weights);

        assertEquals("Al Mahallah al Kubra", a.topMatch(""));
    }

    @Test
    public void topMatchSpacePrefixTest() {
        String[] words = {"smog", "buck", "sad", "spite", "spit", " spy"};
        double[] weights = {5, 10, 12, 20, 15, 7};

        Autocomplete a = new Autocomplete(words, weights);

        assertTrue(" spy".equals(a.topMatch(" ")));
    }

    @Test
    public void topMatchesSimpleTest() {
        String[] words = {"Al Mahallah al Kubra", "Al Mansurah", "Al Mubarraz, Saudi Arabia"};
        double[] weights = {431052.0, 420195.0, 290802.0};

        Autocomplete a = new Autocomplete(words, weights);

        LinkedList<String> expected = new LinkedList<>();
        expected.add("Al Mahallah al Kubra");
        expected.add("Al Mansurah");

        assertEquals(expected, a.topMatches("Al M", 2));
    }

    @Test
    public void topMatchesReturnAllMatchesTest() {
        String[] words = {"Al Mahallah al Kubra", "Al Mansurah", "Al Mubarraz, Saudi Arabia"};
        double[] weights = {431052.0, 420195.0, 290802.0};

        Autocomplete a = new Autocomplete(words, weights);

        LinkedList<String> expected = new LinkedList<>();
        expected.add("Al Mahallah al Kubra");
        expected.add("Al Mansurah");

        assertEquals(expected, a.topMatches("Al Ma", 10));
    }

    @Test
    public void topMatchesSimple2Test() {

        String[] words = {"Mumbai, India", "Mexico City, Distrito Federal, Mexico", "Manila, "
                + "Philippines", "Moscow, Russia", "Melbourne, Victoria, Australia", "Montreal, "
                + "Quebec, Canada", "Madrid, Spain"};
        double[] weights = {1, 2, 3, 4, 5, 6, 7};

        Autocomplete a = new Autocomplete(words, weights);

        LinkedList<String> tester = new LinkedList<>();
        tester.add("Madrid, Spain");
        tester.add("Montreal, Quebec, Canada");
        tester.add("Melbourne, Victoria, Australia");

        assertEquals(tester, a.topMatches("M", 3));

    }

    @Test
    public void topMatchesSimple3Test() {
        String[] words = {"cat", "car", "cab", "cars"};
        double[] weights = {1, 2, 3, 4};

        Autocomplete a = new Autocomplete(words, weights);

        LinkedList<String> expected = new LinkedList<>();
        expected.add("cars");
        expected.add("cab");

        assertEquals(expected, a.topMatches("ca", 2));
    }

    @Test
    public void topMatchesEmptyPrefixTest() {
        String[] words = {"cat", "car", "cab", "cars"};
        double[] weights = {1, 2, 3, 4};

        Autocomplete a = new Autocomplete(words, weights);

        LinkedList<String> expected = new LinkedList<>();
        expected.add("cars");
        expected.add("cab");

        assertEquals(expected, a.topMatches("", 2));
    }

    @Test
    public void topMatchesSmallFringeTest() {
        String[] words = {"city", "car", "cell", "cars"};
        double[] weights = {2, 4, 3, 1};

        Autocomplete a = new Autocomplete(words, weights);

        LinkedList<String> expected = new LinkedList<>();
        expected.add("car");
        expected.add("cell");

        assertEquals(expected, a.topMatches("c", 2));
    }


    /**
     * Run the JUnit tests above.
     */
    public static void main(String[] ignored) {
        textui.runClasses(TestAutocomplete.class);
    }
}
