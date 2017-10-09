import ucb.junit.textui;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.*;

/**
 * The suite of all JUnit tests for the Trie class.
 *
 * @author
 */
public class TestTrie {

    @Test
    public void insertSimpleTest() {
        Trie t = new Trie();
        t.insert("ab");
        assertTrue(t.getRoot().getLinks().containsKey('a'));
        assertTrue(t.getRoot().getLinks().size() == 1);
        assertTrue(t.getRoot().getLinks().get('a').getLinks().containsKey('b'));
        assertTrue(t.getRoot().getLinks().get('a').getLinks().size() == 1);
    }

    @Test
    public void insertEmptyStringTest() throws IllegalArgumentException {
        Trie t = new Trie();
        try {
            t.insert("");
            assertFalse(true);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
            assertTrue(t.getRoot().getLinks().isEmpty());
        }
    }

    @Test
    public void insertNullTest() throws IllegalArgumentException {
        Trie t = new Trie();
        try {
            t.insert(null);
            assertFalse(true);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
            assertTrue(t.getRoot().getLinks().isEmpty());
        }
    }

    @Test
    public void insertTwoWordsTest() {
        Trie t = new Trie();
        Trie.Node n = t.new Node();
        t.insert("aa");
        t.insert("ab");
        t.insert("ac");

        HashMap<Character, Trie.Node> actual = t.getRoot().getLinks().get('a').getLinks();
        HashMap<Character, Trie.Node> expected = new HashMap<>();
        expected.put('a', n);
        expected.put('b', n);
        expected.put('c', n);

        assertEquals(actual.keySet(), expected.keySet());
    }

    @Test
    public void insertWeightSimpleTest() {
        Trie t = new Trie();
        Trie.Node n = t.new Node();
        t.insert("cat", 1.0);
        t.insert("car", 2.0);

        Trie.Node c = t.getRoot().getLinks().get('c');
        assertTrue(c.getMaxWeight() == 2);
        assertTrue(c.getWeight() == 0);

        assertTrue(c.getLinks().get('a').getMaxWeight() == 2);
        assertTrue(c.getLinks().get('a').getWeight() == 0);

        assertTrue(c.getLinks().get('a').getLinks().get('t').getMaxWeight() == 1);
        assertTrue(c.getLinks().get('a').getLinks().get('t').getWeight() == 1);

        assertTrue(c.getLinks().get('a').getLinks().get('r').getMaxWeight() == 2);
        assertTrue(c.getLinks().get('a').getLinks().get('r').getWeight() == 2);
    }

    @Test
    public void insertWeightSimple2Test() {
        Trie t = new Trie();
        Trie.Node n = t.new Node();
        t.insert("cat", 1.0);
        t.insert("car", 2.0);
        t.insert("cia", 4.0);

        Trie.Node c = t.getRoot().getLinks().get('c');
        assertTrue(c.getMaxWeight() == 4);
        assertTrue(c.getWeight() == 0);

        assertTrue(c.getLinks().get('i').getMaxWeight() == 4);
        assertTrue(c.getLinks().get('i').getWeight() == 0);

        assertTrue(c.getLinks().get('i').getLinks().get('a').getMaxWeight() == 4);
        assertTrue(c.getLinks().get('i').getLinks().get('a').getWeight() == 4);

        assertTrue(c.getLinks().get('a').getLinks().get('r').getMaxWeight() == 2);
        assertTrue(c.getLinks().get('a').getLinks().get('r').getWeight() == 2);
    }

    @Test
    public void findTest() {
        Trie t = new Trie();
        t.insert("hello");
        t.insert("hey");
        t.insert("goodbye");
        t.insert("world");
        t.insert("go");

        assertTrue(t.find("go", false));
        assertTrue(t.find("go", true));

        assertTrue(t.find("hell", false));
        assertTrue(t.find("hello", true));
        assertTrue(t.find("good", false));
        assertTrue(t.find("world", false));
        assertTrue(t.find("world", true));

        assertFalse(t.find("bye", false));
        assertFalse(t.find("heyy", false));
        assertFalse(t.find("hell", true));


        //asserting exception for "" or null
        try {
            t.find("", false);
            assertFalse(true);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }


        try {
            t.find(null, false);
            assertFalse(true);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }

    }

    @Test
    public void orderedWordsSimpleTest() {
        Trie t = new Trie();
        t.insert("hello");
        t.insert("goodbye");
        t.insert("goodday");
        t.insert("death");
        ArrayList<String> expected = new ArrayList<>();
        expected.add("goodday");
        expected.add("goodbye");
        expected.add("death");
        expected.add("hello");

        ArrayList<String> actual = t.orderedWords("agdbecfhijklmnopqrsty");

        assertTrue(expected.equals(actual));
    }

    @Test
    public void orderedWordsInvertTest() {
        Trie t = new Trie();
        t.insert("anxiety");
        t.insert("anxieties");
        ArrayList<String> actual = t.orderedWords("agdbecfhyijklmnopqrstuvwxz");
        ArrayList<String> expected = new ArrayList<>();
        expected.add("anxiety");
        expected.add("anxieties");
        assertTrue(expected.equals(actual));
    }

    @Test
    public void orderedWordsSkipWords() {
        Trie t = new Trie();
        t.insert("apple");
        t.insert("anxiety");
        t.insert("anxieties");
        t.insert("dog");
        t.insert("yoga");
        t.insert("yo");

        ArrayList<String> actual = t.orderedWords("abcdefghijklmnpqrstuvwxz");
        ArrayList<String> expected = new ArrayList<>();
        expected.add("anxieties");
        expected.add("apple");

        assertTrue(expected.equals(actual));
    }

    @Test
    public void orderedWordsInvert2Test() {
        Trie t = new Trie();
        t.insert("bobs");
        t.insert("bobys");
        ArrayList<String> actual = t.orderedWords("agdbecfhijklmnopqrstuvwxyz");
        ArrayList<String> expected = new ArrayList<>();
        expected.add("bobs");
        expected.add("bobys");
        assertTrue(expected.equals(actual));
    }

    @Test
    public void getLastTest() {
        Trie t = new Trie();
        Trie.Node n = t.new Node();
        t.insert("cat", 1.0);
        t.insert("car", 2.0);
        t.insert("cia", 4.0);

        assertTrue(t.getLast("cia").getWeight() == 4);
    }

    @Test
    public void orderedWordsErrorsTest() {
        Trie t = new Trie();

        String order = "";
        try {
            t.orderedWords(order);
            assertFalse(true);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }

        order = "abca";
        try {
            t.orderedWords(order);
            assertFalse(true);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }

        order = "abc";
        try {
            t.orderedWords(order);
            assertFalse(true);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }

    }

    /**
     * Run the JUnit tests above.
     */
    public static void main(String[] ignored) {
        textui.runClasses(TestTrie.class);
    }
}
