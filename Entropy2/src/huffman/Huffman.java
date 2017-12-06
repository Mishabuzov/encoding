package huffman;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

import static huffman.Constants.ENCODED_FILE;

public class Huffman {

    private final StringBuilder text;

    private final PriorityQueue<Node> nodes;

    private final TreeMap<Character, String> codes;

    private StringBuilder encoded;

    private final int allSymbols[];

    private final int maxSymbol;

    public Huffman(final String stringText) {
        text = new StringBuilder(stringText);
        nodes = new PriorityQueue<>(getNodesComparator());
        codes = new TreeMap<>();
        maxSymbol = 20_000;
        allSymbols = new int[maxSymbol];
        encoded = new StringBuilder();
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
        saveEncodedTextIntoFile();
        System.out.println("encoded length for Huffman1 - " + encoded.length());
    }

    private void encodeText() {
        char c;
        for (int i = 0; i < text.length(); i++) {
            c = text.charAt(i);
            if (isCorrectSymbol(c)) {
                encoded.append(codes.get(c));
            }
        }
    }

    private boolean isCorrectSymbol(char c) {
        return c < maxSymbol;
    }

    private void saveEncodedTextIntoFile() {
        try {
           /* FileOutputStream fos = new FileOutputStream(ENCODED_FILE);
            fos.write(new String(encoded).getBytes());
            fos.close();*/
//            String s = "0100100001100101011011000110110001101111"; // ASCII "Hello"
            byte[] data = decodeBinary(new String(encoded));
            java.nio.file.Files.write(new File(ENCODED_FILE).toPath(), data);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    private byte[] decodeBinary(String s) {
        if (s.length() % 8 != 0) throw new IllegalArgumentException(
                "Binary data length must be multiple of 8");
        byte[] data = new byte[s.length() / 8];
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '1') {
                data[i >> 3] |= 0x80 >> (i & 0x7);
            } else if (c != '0') {
                throw new IllegalArgumentException("Invalid char in binary string");
            }
        }
        return data;
    }

    private Node buildTreeAndGetRoot() {
        while (nodes.size() > 1) {
            nodes.add(new Node(nodes.poll(), nodes.poll()));
        }
        return nodes.peek();
    }

    public void printCodes() {
        System.out.println("--- codes Huffman1 ---");
        codes.forEach((k, v) -> System.out.println("'" + k + "' : " + v));
        System.out.println();
    }

    private void calculateCharProbability() {
//        System.out.println("-- probability --");
        char c;
        for (int i = 0; i < text.length(); i++) {
            c = text.charAt(i);
            createProbabilityForOneSymbol(c);
        }
        for (int i = 0; i < allSymbols.length; i++) {
            if (allSymbols[i] > 0) {
                nodes.add(new Node(allSymbols[i] / (text.length() * 1.0), String.valueOf((char) i)));
//                System.out.println("'" + ((char) i) + "' : " + allSymbols[i] / (text.length() * 1.0));
            }
        }
        System.out.println("Codes count for Huffman1 - " + nodes.size());
    }

    private void createProbabilityForOneSymbol(char c) {
        if (isCorrectSymbol(c)) {
            allSymbols[c]++;
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
                codes.put(node.getCharacter().charAt(0), code);
            }
        }
    }
}

