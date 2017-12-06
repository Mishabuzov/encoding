package shannon_fano;

import huffman.BitOutputStream;

import java.io.*;
import java.util.*;

import static huffman.Constants.ENCODED_FILE;

public class ShannonFanoTwoChars {

    private String text;
    private StringBuilder encoded;
    private int originalStringLength;
    private Map<String, String> codes;
    private Map<String, Double> characterFrequency;

    public ShannonFanoTwoChars(String str) {
        super();
        text = str;
        originalStringLength = str.length();
        characterFrequency = new HashMap<>();
        codes = new HashMap<>();
        encoded = new StringBuilder();
        calculateFrequencyAndSortMap();
        compressString();
        encodeText();
        print();
    }

    public void printCodes() {
        System.out.println("--- codes Shannon-Fano 2---");
        codes.forEach((k, v) -> System.out.println("'" + k + "' : " + v));
        System.out.println();
    }

    private void print() {
        System.out.println("codes count for Shannon-Fano2 - " + codes.size());
        System.out.println("encoded length for Shannon-Fano2 - " + encoded.length());
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
                encoded.append(codes.get(c + "" + nextSymbol));
                writeEncodingToFile(codes.get(c + "" + nextSymbol), b_out);
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
        List<String> codesList = new ArrayList<>();
        for (Map.Entry<String, Double> entry : characterFrequency.entrySet()) {
            codesList.add(entry.getKey());
        }
        appendBit(codes, codesList, true);
    }

    private void appendBit(Map<String, String> result, List<String> signList, boolean up) {
        String bit = "";
        if (!result.isEmpty()) {
            bit = (up) ? "0" : "1";
        }

        for (String sign : signList) {
            String code = (result.get(sign) == null) ? "" : result.get(sign);
            result.put(sign, code + bit);
        }
        if (signList.size() > 1) {
            int separator = findSeparatorPosition(signList);
            List<String> upList = signList.subList(0, separator);
            appendBit(result, upList, false);
            List<String> downList = signList.subList(separator, signList.size());
            appendBit(result, downList, true);
        }
    }

    private int findSeparatorPosition(List<String> signList) {
        if (signList.size() == 2) {
            return 1;
        }
        double currentFrequencySum = 0d;
        for (String sign : signList) {
            currentFrequencySum += characterFrequency.get(sign);
        }
        double halfFrequencySum = 0d;
        int separator = 1;
        for (String sign : signList) {
            halfFrequencySum += characterFrequency.get(sign);
            if (halfFrequencySum >= currentFrequencySum / 2) {
                separator += signList.indexOf(sign);
                if(separator == signList.size()){
                    separator = signList.size() - 1;
                }
                break;
            }
        }
        return separator;
    }

    private void calculateFrequencyAndSortMap() {
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (i + 1 >= text.length()) {
                characterFrequency.merge(c + "", 1d / originalStringLength, (a, b) -> a + b);
                return;
            }
            char nextChar = text.charAt(i + 1);
            characterFrequency.merge(c + "" + nextChar, 1d / originalStringLength, (a, b) -> a + b);
        }
        characterFrequency = MapUtil.sortByValue(characterFrequency);
    }
}
