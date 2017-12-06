package entropy;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public class Calculator {

    private Map<String, Double> probabilityOf2Letters;

    private Map<String, Double> conditionalProbabilityOf2Letters;

    private Map<String, Double> probabilityOf3Letters;

    private Map<String, Double> conditionalProbabilityOf3Letters;

    private Map<Character, Double> alphabet;

    private String text;

    private String name;

    public Calculator() {
        alphabet = new HashMap<>();
        probabilityOf2Letters = new HashMap<>();
        conditionalProbabilityOf2Letters = new HashMap<>();
        probabilityOf3Letters = new HashMap<>();
        conditionalProbabilityOf3Letters = new HashMap<>();
    }

    public void calculateEntropyOfTextAndPrint(String text, String name) {
        this.text = text;
        this.name = name;
        createAlphabetAndSyllablesMatrix();
        calculateProbabilitiesOf2Letters();
        calculateAndPrintEntropy();
        clearData();
    }

    private void clearData(){
        text = "";
        alphabet.clear();
        probabilityOf2Letters.clear();
        conditionalProbabilityOf2Letters.clear();
        probabilityOf3Letters.clear();
        conditionalProbabilityOf3Letters.clear();
    }

    private void createAlphabetAndSyllablesMatrix() {
        String substringKey;
        for (int i = 0; i < text.length() - 1; i += 2) {
            if (i >= text.length()) {
                break;
            }
            alphabet.merge(text.charAt(i), 1d, new BiFunction<Double, Double, Double>() {
                @Override
                public Double apply(Double a, Double b) {
                    return a + b;
                }
            });
            alphabet.merge(text.charAt(i + 1), 1d, (a, b) -> a + b);
            substringKey = text.substring(i, i + 2);
            probabilityOf2Letters.merge(substringKey, 1d, (a, b) -> a + b);
        }
        create3LettersSyllableMatrix();
    }

    private void create3LettersSyllableMatrix() {
        String substringKey;
        for (int i = 0; i < text.length() - 1; i += 3) {
            if (i >= text.length()) {
                break;
            }
            substringKey = text.substring(i, i + 3);
            probabilityOf3Letters.merge(substringKey, 1d, (a, b) -> a + b);
        }
    }

    private void calculateProbabilityOfOneLetters() {
        for (Map.Entry<Character, Double> entry : alphabet.entrySet()) {
            entry.setValue(entry.getValue() / text.length());
        }
    }

    private void calculateProbabilitiesOf2Letters() {
        calculateProbabilityOfOneLetters();
        int countSyllablesWith2Letters = text.length() / 2;
        String key;
        double letterProbability;
        for (Map.Entry<String, Double> entry : probabilityOf2Letters.entrySet()) {
            entry.setValue(entry.getValue() / countSyllablesWith2Letters);
            key = entry.getKey();
            letterProbability = alphabet.get(key.charAt(key.length() - 1)); //Вероятность последней буквы слога в тексте
            conditionalProbabilityOf2Letters.put(entry.getKey(), entry.getValue() / letterProbability);
        }
        calculateProbabilityOf3Letters();
    }

    private void calculateProbabilityOf3Letters() {
        int countSyllablesWith3Letters = text.length() / 3;
        String key;
        String keyFor2Syllables;
        double conditionalProbabilityValueOf3LetterSyllable;
        for (Map.Entry<String, Double> entry : probabilityOf3Letters.entrySet()) {
            entry.setValue(entry.getValue() / countSyllablesWith3Letters);
            key = entry.getKey();
            keyFor2Syllables = key.substring(1, key.length());
            if(probabilityOf2Letters.get(keyFor2Syllables) == null){
                conditionalProbabilityValueOf3LetterSyllable = 1;
            } else {
                conditionalProbabilityValueOf3LetterSyllable =
                        entry.getValue() / probabilityOf2Letters.get(keyFor2Syllables);
            }
            conditionalProbabilityOf3Letters.put(entry.getKey(), conditionalProbabilityValueOf3LetterSyllable);
        }
    }

    private void calculateAndPrintEntropy() {
        System.out.println(name + "\n"
                + "Количество символов: " + String.valueOf(text.length()) + "\n"
                + "Мощность алфавита: " + String.valueOf(alphabet.size()) + "\n"
                + "H(A): " + String.valueOf(calculateSimpleEntropy()) + "\n"
                + "H(A|B): " + String.valueOf(calculate2letterSyllableEntropy()) + "\n"
                + "H(A|BC): " + String.valueOf(calculate3letterSyllableEntropy()) + "\n");
        System.out.println();
    }

    private double calculateSimpleEntropy() {
        double entropyOfText = 0;
        char key;
        for (Map.Entry<Character, Double> entry : alphabet.entrySet()) {
            key = entry.getKey();
            entropyOfText += entry.getValue() * (Math.log(entry.getValue()) / Math.log(2));
        }
        return entropyOfText * (-1);
    }

    private double calculate2letterSyllableEntropy() {
        double entropyOfText = 0;
        String key;
        for (Map.Entry<String, Double> entry : probabilityOf2Letters.entrySet()) {
            key = entry.getKey();
            entropyOfText += entry.getValue() * (Math.log(conditionalProbabilityOf2Letters.get(key)) / Math.log(2));
        }
        return entropyOfText * (-1);
    }

    private double calculate3letterSyllableEntropy() {
        double entropyOfText = 0;
        String key;
        for (Map.Entry<String, Double> entry : probabilityOf3Letters.entrySet()) {
            key = entry.getKey();
            entropyOfText += entry.getValue() * (Math.log(conditionalProbabilityOf3Letters.get(key)) / Math.log(2));
        }
        return entropyOfText * (-1);
    }

}