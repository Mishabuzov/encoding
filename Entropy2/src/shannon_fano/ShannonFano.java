package shannon_fano;

import huffman.BitOutputStream;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;

import static huffman.Constants.ENCODED_FILE;

public class ShannonFano {

    private String text;
    private StringBuilder encoded;
    private int originalStringLength;
    private Map<Character, String> codes;
    private Map<Character, Double> characterFrequency;

    public ShannonFano(String str) {
        super();
        text = str;
        originalStringLength = str.length();
        characterFrequency = new HashMap<>();
        codes = new HashMap<>();
        encoded = new StringBuilder();
        this.calculateFrequencyAndSortMap();
        this.compressString();
        encodeText();
        print();
    }

    public void printCodes() {
        System.out.println("--- codes Shannon-Fano 1---");
        codes.forEach((k, v) -> System.out.println("'" + k + "' : " + v));
        System.out.println();
    }

    private void print() {
        System.out.println("codes count for Shannon-Fano1 - " + codes.size());
        System.out.println("encoded length for Shannon-Fano1 - " + encoded.length());
    }

    private void encodeText() {
        char c;
        try (OutputStream out = new BufferedOutputStream(new FileOutputStream(new File(ENCODED_FILE)))) {
            BitOutputStream b_out = new BitOutputStream(out);
            for (int i = 0; i < text.length(); i++) {
                c = text.charAt(i);
                encoded.append(codes.get(c));
                writeEncodingToFile(codes.get(c), b_out);
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


    private void compressString() {
        List<Character> charList = new ArrayList<>();

        Iterator<Entry<Character, Double>> entries = characterFrequency.entrySet().iterator();
        while (entries.hasNext()) {
            Entry<Character, Double> entry = entries.next();
            charList.add(entry.getKey());
        }

        appendBit(codes, charList, true);
    }

    private void appendBit(Map<Character, String> result, List<Character> charList, boolean up) {
        String bit = "";
        if (!result.isEmpty()) {
            bit = (up) ? "0" : "1";
        }

        for (Character c : charList) {
            String s = (result.get(c) == null) ? "" : result.get(c);
            result.put(c, s + bit);
        }
        if (charList.size() > 1) {
            int separator = findSeparatorPosition(charList);
            List<Character> upList = charList.subList(0, separator);
            appendBit(result, upList, false);
            List<Character> downList = charList.subList(separator, charList.size());
            appendBit(result, downList, true);
        }
    }

    private int findSeparatorPosition(List<Character> charList) {
        if (charList.size() == 2) {
            return 1;
        }
        double currentFrequencySum = 0d;
        for (char c : charList) {
            currentFrequencySum += characterFrequency.get(c);
        }
        double halfFrequencySum = 0d;
        int separator = 1;
        for (char c : charList) {
            halfFrequencySum += characterFrequency.get(c);
            if (halfFrequencySum >= currentFrequencySum / 2) {
                separator += charList.indexOf(c);
                break;
            }
        }
        return separator;
    }

    private void calculateFrequencyAndSortMap() {
        for (Character c : text.toCharArray()) {
            characterFrequency.merge(c, 1d / originalStringLength, (a, b) -> a + b);
        }
        characterFrequency = MapUtil.sortByValue(characterFrequency);
    }

}