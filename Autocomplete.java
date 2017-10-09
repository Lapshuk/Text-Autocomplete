import java.util.*;

/**
 * Implements autocomplete on prefixes for a given dictionary of terms and weights.
 *
 * @author
 */
public class Autocomplete {

    private Trie dictionary;

    /**
     * Initializes required data structures from parallel arrays.
     *
     * @param terms   Array of terms.
     * @param weights Array of weights.
     */
    public Autocomplete(String[] terms, double[] weights) {
        if (terms.length != weights.length) {
            throw new IllegalArgumentException();
        }

        HashSet<String> testSet = new HashSet<>(terms.length);

        dictionary = new Trie();
        //dictionary.setWordMapCap(terms.length);


        for (int i = 0; i < terms.length; i++) {
            if (weights[i] < 0) {
                throw new IllegalArgumentException();
            }

            if (testSet.contains(terms[i])) {
                throw new IllegalArgumentException();
            }
            dictionary.insert(terms[i], weights[i]);
            testSet.add(terms[i]);

        }
    }

    public Trie getDictionary() {
        return dictionary;
    }

    /**
     * Find the weight of a given term. If it is not in the dictionary, return 0.0
     *
     * @param term
     * @return
     */
    public double weightOf(String term) {
        Trie.Node node = dictionary.getLast(term);
        if (node != null) {
            return node.getWeight();
        }
        return 0.0;
    }

    /**
     * Return the top match for given prefix, or null if there is no matching term.
     *
     * @param prefix Input prefix to match against.
     * @return Best (highest weight) matching string in the dictionary.
     */
    public String topMatch(String prefix) {

        Trie.Node head;
        String result = "";

        head = prefix.equals("") ? dictionary.getRoot() : dictionary.getRoot().getLast(prefix);
        if (head == null) {
            return null;
        }
        result = prefix;

        // if head is null, just getLInks from dictionary.
        while (head.getWeight() != head.getMaxWeight()) {
            HashMap<Character, Trie.Node> s = head.getLinks();
            Character k;
            k = s.keySet().stream()
                    .max((a, b) -> Double.compare(s.get(a).getMaxWeight(), s.get(b).getMaxWeight()))
                    .get();
            result += k;
            head = s.get(k);
        }

        return result;
    }

    /**
     * Returns the top k matching terms (in descending order of weight) as an iterable.
     * If there are less than k matches, return all the matching terms.
     *
     * @param prefix
     * @param k
     * @return
     */
    public Iterable<String> topMatches(String prefix, int k) {
        if (k < 0) {
            throw new IllegalArgumentException();
        }

        Trie.Node head = prefix.equals("") ? dictionary.getRoot()
                : dictionary.getRoot().getLast(prefix); //last node in prefix
        if (head == null) {
            return new ArrayList<String>();
        }

        PriorityQueue<Trie.Node> fringe = new PriorityQueue<>((a, b) ->
                -1 * Double.compare(a.getMaxWeight(), b.getMaxWeight()));

        PriorityQueue<Trie.Node> bestSoFar = new PriorityQueue<>((a, b) ->
                Double.compare(a.getWeight(), b.getWeight()));

        PriorityQueue<Double> weights = new PriorityQueue<>();

        if (head.getIsWord()) {
            bestSoFar.add(head);
        }
        // add links to PQ
        double currMax = 0;
        double smallestWeight = 0.0;
        Trie.Node n;
        for (Map.Entry<Character, Trie.Node> entry : head.getLinks().entrySet()) {
            n = entry.getValue();
            currMax = n.getMaxWeight();

            if (fringe.size() < k || currMax > smallestWeight) {
                fringe.add(n);
                weights.add(n.getMaxWeight());
                smallestWeight = weights.peek();
                if (weights.size() > k) {
                    weights.poll();
                }
            }
        }

        while (!fringe.isEmpty() && (bestSoFar.size() <= k
                || fringe.peek().getMaxWeight() > bestSoFar.peek().getMaxWeight())) {
            n = fringe.poll();

            if (n.getIsWord()) {
                if (bestSoFar.size() < k || bestSoFar.peek().getWeight() < n.getWeight()) {
                    bestSoFar.add(n);
                }
                //remove element if it is the smallest one and we reached k
                if (bestSoFar.size() > k) {
                    bestSoFar.poll();
                }
            }

            for (Map.Entry<Character, Trie.Node> entry : n.getLinks().entrySet()) {
                n = entry.getValue();
                currMax = n.getMaxWeight();

                if (fringe.size() < k || currMax >= smallestWeight) {
//                    smallestWeight = currMax < smallestWeight
//                            ? currMax : smallestWeight;
                    fringe.add(n);

                    if (!weights.contains(currMax)) {
                        weights.add(currMax);
                    }
                    smallestWeight = weights.peek();
                    if (weights.size() > k) {
                        weights.poll();
                    }
                }
            }
        }

        LinkedList<String> iterable = new LinkedList<>();
        while (!bestSoFar.isEmpty()) {
            iterable.addFirst(getDictionary().getWord(bestSoFar.poll()));
        }
        return iterable;
    }

    /**
     * Test client. Reads the data from the file, then repeatedly reads autocomplete
     * queries from standard input and prints out the top k matching terms.
     *
     * @param args takes the name of an input file and an integer k as
     *             command-line arguments
     */
    public static void main(String[] args) {
        // initialize autocomplete data structure
        In in = new In(args[0]);
        int N = in.readInt();
        String[] terms = new String[N];
        double[] weights = new double[N];
        for (int i = 0; i < N; i++) {
            weights[i] = in.readDouble();   // read the next weight
            in.readChar();                  // scan past the tab
            terms[i] = in.readLine();       // read the next term
        }


        Autocomplete autocomplete = new Autocomplete(terms, weights);
        // process queries from standard input
        int k = Integer.parseInt(args[1]);
        while (StdIn.hasNextLine()) {
            String prefix = StdIn.readLine();
            //double start = System.currentTimeMillis();
            for (String term : autocomplete.topMatches(prefix, k)) {

                StdOut.printf("%14.1f  %s\n", autocomplete.weightOf(term), term);

            }
            //double end = System.currentTimeMillis();
            //System.out.println(end - start);
            //StdOut.printf(autocomplete.topMatch(prefix));

        }
    }
}
