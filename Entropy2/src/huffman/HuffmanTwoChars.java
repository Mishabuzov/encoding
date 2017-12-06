package huffman;

import java.io.*;
import java.util.*;

import static huffman.Constants.ENCODED_FILE;

public class HuffmanTwoChars {

    private final StringBuilder text;

    private final PriorityQueue<Node> nodes;

    private final TreeMap<String, String> codes;

    private final StringBuilder encoded;

    private final Map<String, Double> twoSymbolSequences;

    private final int maxSymbol;

    public HuffmanTwoChars(final String stringText) {
        text = new StringBuilder(stringText);
        nodes = new PriorityQueue<>(getNodesComparator());
        codes = new TreeMap<>();
        maxSymbol = 20_000;
        encoded = new StringBuilder();
        twoSymbolSequences = new HashMap<>();
    }

    private Comparator<Node> getNodesComparator() {
        return (leftNode, rightNode) -> {
            if (leftNode.getFrequency() == rightNode.getFrequency()) {
                return 0;
            } else {
                return (leftNode.getFrequency() < rightNode.getFrequency()) ? -1 : 1;
            }
        };
    }

    public void processText() {
        calculateCharProbability();
        generateCodes(buildTreeAndGetRoot(), "");
//        printCodes();
        encodeText();
        System.out.println("encoded length Huffman2 - " + encoded.length());
    }

    private void encodeText() {
        char c;
        char nextSymbol;
        try (OutputStream out = new BufferedOutputStream(new FileOutputStream(new File(ENCODED_FILE)))) {
            BitOutputStream b_out = new BitOutputStream(out);
            for (int i = 0; i < text.length(); i += 2) {
                c = text.charAt(i);
                if (i + 1 >= text.length()) {
                    encoded.append(codes.get(c + ""));
                    writeEncodingToFile(codes.get(c + ""), b_out);
                    break;
                }
                nextSymbol = text.charAt(i + 1);
                if (isCorrectSymbols(c, nextSymbol)) {
                    encoded.append(codes.get(c + "" + nextSymbol));
                    writeEncodingToFile(codes.get(c + "" + nextSymbol), b_out);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeEncodingToFile(String encodedString, BitOutputStream b_out) throws IOException {
        for (int k = 0; k < encodedString.length(); k++) {
            b_out.write(Integer.parseInt(encodedString.substring(k, k + 1)));
        }
    }

    private boolean isCorrectSymbols(char c, char nextChar) {
        return c < maxSymbol && nextChar < maxSymbol;
    }

    private Node buildTreeAndGetRoot() {
        while (nodes.size() > 1) {
            nodes.add(new Node(nodes.poll(), nodes.poll()));
        }
        return nodes.peek();
    }

    public void printCodes() {
        System.out.println("--- Codes Huffman 2---");
        codes.forEach((k, v) -> System.out.println("'" + k + "' : " + v));
        System.out.println();
    }

    private void calculateCharProbability() {
        for (int i = 0; i < text.length(); i++) {
            createProbabilityForTwoSymbols(i);
        }
        for (String key : twoSymbolSequences.keySet()) {
            nodes.add(new Node(twoSymbolSequences.get(key), key));
        }
        System.out.println("codes count Huffman2 - " + twoSymbolSequences.size());
    }

    private void createProbabilityForTwoSymbols(int i) {
        char c = text.charAt(i);
        if (i + 1 >= text.length()) {
            twoSymbolSequences.merge(c + "", 1d, (a, b) -> a + b);
            return;
        }
        char nextChar = text.charAt(i + 1);
        if (isCorrectSymbols(c, nextChar)) {
            twoSymbolSequences.merge(c + "" + nextChar, 1d, (a, b) -> a + b);
        }
    }

    private void generateCodes(Node node, String code) {
        if (node != null) {
            if (node.getRightNode() != null) {
                generateCodes(node.getRightNode(), code + "1");
            }

            if (node.getLeftNode() != null) {
                generateCodes(node.getLeftNode(), code + "0");
            }

            if (node.isLeaf(node.getLeftNode(), node.getRightNode())) {
                codes.put(node.getCharacter(), code);
            }
        }
    }
}
