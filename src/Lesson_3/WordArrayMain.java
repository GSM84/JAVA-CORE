package Lesson_3;

import java.util.HashMap;
import java.util.Map;

public class WordArrayMain {
    public static void main(String[] args) {
        String[] wordArray = ("У этой кнИги совсем иная задача. " +
                "Она поможет вам научиться не только разговаривать, но и " +
                "у этой книги совсем инаЯ задача. " +
                "размышлять по-русски. Книга, которую вы держите в руках, составлена " +
                "У этой книги совсем иная задача. "
                ).split("(\\s|\\.\\s|,\\s)");

        HashMap<String, Integer> wordMap = new HashMap<>();
        String tempValue;
        for (int i = 0; i < wordArray.length; i++) {
            tempValue = wordArray[i].toUpperCase();
            wordMap.put(tempValue, wordMap.get(tempValue)== null ? 1 : wordMap.get(tempValue) + 1);
        }

        for (Map.Entry<String, Integer> pair : wordMap.entrySet()) {
            System.out.println(pair.getKey() + " " + pair.getValue());
        }
    }
}
