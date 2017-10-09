import java.util.*;

/**
 * Prefix-Trie. Supports linear time find() and insert().
 * Should support determining whether a word is a full word in the
 * Trie or a prefix.
 *
 * @author
 */
public class Trie {

    private Node root;
    private HashMap<Node, String> wordMap = new HashMap<>(800);
    private TreeSet<Double> weightSet =
            new TreeSet<>((a, b) -> -1 * Double.compare(a, b));

    public Trie() {
        root = new Node();
    }

    public Trie(boolean isWord, double weight) {
        root = new Node(isWord, weight);
    }

    public Node getRoot() {
        return root;
    }

    public String getWord(Node n) {
        return wordMap.get(n);
    }


    public void setWordMapCap(int capacity) {
        wordMap = new HashMap<>(capacity);
    }


    public class Node {

        private boolean isWord;
        private double weight;
        private double maxWeight;
        private HashMap<Character, Node> links;

        public Node() {
            isWord = false;
            weight = 0.0;
            maxWeight = 0.0;
            links = new HashMap<>();
        }

        public Node(boolean isWord, double weight) {
            this.isWord = isWord;
            this.weight = weight;
            this.maxWeight = 0.0;
            this.links = new HashMap<>();
        }

        public HashMap<Character, Node> getLinks() {
            return links;
        }

        public void setIsWord(boolean isWord) {
            this.isWord = isWord;
        }

        public boolean getIsWord() {
            return isWord;
        }

        public void setWeight(double weight) {
            this.weight = weight;
        }

        public double getWeight() {
            return weight;
        }

        public void setMaxWeight(double maxWeight) {
            this.maxWeight = maxWeight;
        }

        public double getMaxWeight() {
            return maxWeight;
        }

        public String print() {
            Node currNode = this;
            String result = "";
            if (!currNode.links.isEmpty()) {
                for (Character c : currNode.links.keySet()) {
                    result += (c);
                    result += currNode.links.get(c).print();
                    result += "\n";
                }
            }

            return result;
        }

        public Node getLast(String term) {
            Trie.Node currNode = root;
            for (int i = 0; i < term.length(); i++) {
                Character currChar = term.charAt(i);
                if (!currNode.getLinks().containsKey(currChar)) {
                    return null;
                }
                currNode = currNode.links.get(currChar);
            }
            return currNode;
        }

        public boolean find(String s, boolean isFullWord) {
            if (s == null || s.isEmpty()) {
                throw new IllegalArgumentException("Empty string.");
            }
            Node currNode = this;
            for (int i = 0; i < s.length(); i++) {
                Character currChar = s.charAt(i);
                if (currNode.links.containsKey(currChar)) {
                    currNode = currNode.links.get(currChar);
                } else {
                    return false;
                }
            }

            if (!currNode.isWord == !isFullWord) {
                return true;
            } else {
                return currNode.isWord;
            }

        }

        public ArrayList<String> orderedWords(char[] order, String prefix,
                                              ArrayList<String> result) {
            Node curr = this;
            if (!curr.links.isEmpty()) {

                ArrayList<Character> orderedLinks = new ArrayList<>();
                for (int i = 0; i < order.length; i++) {
                    if (curr.links.containsKey(order[i])) {
                        orderedLinks.add(order[i]);
                    }
                }

                for (Character c : orderedLinks) {
                    Node currChild = curr.links.get(c);
                    prefix += c;

                    if (!currChild.links.isEmpty()) {
                        if (currChild.isWord) {
                            result.add(prefix);
                        }

                        currChild.orderedWords(order, prefix, result);
                        if (curr.links.size() > 1) {
                            prefix = prefix.substring(0, prefix.length() - 1);
                        }

                    } else {
                        result.add(prefix);
                        prefix = prefix.substring(0, prefix.length() - 1);

                    }
                }
            }
            return result;
        }

    }

    public ArrayList<String> orderedWords(String alphabetOrder) {
        //storing into set to compare teh size
        HashSet<Character> orderSet = new HashSet<>();
        for (int i = 0; i < alphabetOrder.length(); i++) {
            orderSet.add(alphabetOrder.charAt(i));
        }

        char[] order = alphabetOrder.toCharArray();

        if (order.length == 0 || root.getLinks().isEmpty()
                || orderSet.size() != alphabetOrder.length()) {
            throw new IllegalArgumentException();
        }
        return root.orderedWords(order, "", new ArrayList<String>());
    }


    public String print() {
        return root.print();
    }

    public Node getLast(String term) {
        return root.getLast(term);
    }

    public boolean find(String s, boolean isFullWord) {
        return root.find(s, isFullWord);
    }


    public void insert(String s) {
        if (s == null || s.isEmpty()) {
            throw new IllegalArgumentException("Empty string.");
        }
        Node currNode = root;

        for (int i = 0; i < s.length(); i++) {
            Character currChar = s.charAt(i);
            if (!currNode.links.containsKey(currChar)) {
                currNode.links.put(currChar, new Node());
            }
            currNode = currNode.links.get(currChar);
        }
        currNode.isWord = true;
    }

    public void insert(String s, double weight) {
        if (s == null || s.isEmpty()) {
            throw new IllegalArgumentException("Empty string.");
        }
        Node currNode = root;

        for (int i = 0; i < s.length(); i++) {
            Character currChar = s.charAt(i);
            if (!currNode.links.containsKey(currChar)) {
                Node newNode = new Node();
                newNode.maxWeight = weight;
                currNode.links.put(currChar, newNode);
            }
            currNode = currNode.links.get(currChar);

            if (currNode.maxWeight < weight) {
                currNode.maxWeight = weight;
            }
        }
        currNode.isWord = true;
        currNode.weight = weight;
        if (root.maxWeight < weight) {
            root.maxWeight = weight;
        }
        wordMap.put(currNode, s);
    }

}
