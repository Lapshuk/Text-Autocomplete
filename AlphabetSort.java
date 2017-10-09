import java.util.*;

/**
 * AlphabetSort takes input from stdin and prints to stdout.
 * The first line of input is the alphabet permutation.
 * The the remaining lines are the words to be sorted.
 * <p>
 * The output should be the sorted words, each on its own line,
 * printed to std out.
 */
public class AlphabetSort {

    /**
     * Reads input from standard input and prints out the input words in
     * alphabetical order.
     *
     * @param args ignored
     */
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        String order = sc.nextLine();
        Trie words = new Trie();
        String result = "";

        while (sc.hasNextLine()) {
            String next = sc.nextLine();
            words.insert(next);
        }
        ArrayList<String> wordsArr = words.orderedWords(order);
        for (int i = 0; i < wordsArr.size(); i++) {
            if (i != wordsArr.size() - 1) {
                System.out.println(wordsArr.get(i));
            } else {
                System.out.print(wordsArr.get(i));
            }
        }
    }
}
