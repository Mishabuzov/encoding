import entropy.Calculator;
import huffman.Huffman;
import huffman.HuffmanTwoChars;
import huffman.Names;
import shannon_fano.ShannonFano;
import shannon_fano.ShannonFanoTwoChars;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static huffman.Constants.TEXT_SOURCE;

public class Starter2 {

    public static void main(String[] args) {
        String text = scanFile(TEXT_SOURCE);
        // 2) task
        Calculator calculator = new Calculator();
        calculator.calculateEntropyOfTextAndPrint(text, Names.NORMAL.getName());
        //
      /*  huffman1(text);
        huffman2(text);*/
        shannonFano(text);
//        shannonFano2(text);
    }

    private static void shannonFano(String text){
        ShannonFano shannonFano = new ShannonFano(text);
        long codedFileSize = getSizeOfFIle(Names.ENCODED.getWay());
        System.out.println("encoded file size / text length (Shannon-Fano1) - " + (double) codedFileSize * 8 / text.length());
        System.out.println();
//        shannonFano.printCodes();
    }

    private static void shannonFano2(String text){
        ShannonFanoTwoChars shannonFano2 = new ShannonFanoTwoChars(text);
        long codedFileSize = getSizeOfFIle(Names.ENCODED.getWay());
        System.out.println("encoded file size / text length (Shannon-Fano2) - " + (double) codedFileSize * 8 / text.length());
        System.out.println();
//        shannonFano2.printCodes();
    }

    private static void huffman1(String text){
        // 1)
        Huffman huffman = new Huffman(text);
        huffman.processText();
        // 3)
//        long normalFileSize = getSizeOfFIle(Names.NORMAL.getWay());
        long codedFileSize = getSizeOfFIle(Names.ENCODED.getWay());
        System.out.println("encoded file size / text length (Huffman1) - " + (double) codedFileSize * 8 / text.length());
        System.out.println();
//        huffman.printCodes();
    }

    private static void huffman2(String text){
        // 1)
        HuffmanTwoChars huffman = new HuffmanTwoChars(text);
        huffman.processText();
        // 3)
//        long normalFileSize = getSizeOfFIle(Names.NORMAL.getWay());
        long codedFileSize = getSizeOfFIle(Names.ENCODED.getWay());
        System.out.println("encoded file size / text length (Huffman2) - " + (double) codedFileSize * 8 / text.length());
        System.out.println();
//        huffman.printCodes();
    }

    private static long getSizeOfFIle(String pathName){
        File file = new File(pathName);
        return file.length();
    }

    private static String scanFile(String source) {
        byte[] text = new byte[0];
        try {
            text = Files.readAllBytes(Paths.get(source));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String(text);
    }

}