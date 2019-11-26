package Lesson_3;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class PhoneBook {

    static HashMap<String, HashSet<String>> phoneBook;

    public static void main(String[] args) {
        phoneBook = new HashMap<>();

        add("Ivanov", "12345");
        add("Ivanov", "54321");
        add("Petrov", "54321");
        add("Sidorov", "(55)555");

        get("Ivanov");
        get("Sidorov");

        //printBook();
    }

    public static void add(String _lastName, String _phoneNumber){
        HashSet<String> hs;
        String lastName = _lastName.toUpperCase();
        hs = (phoneBook.get(lastName) == null) ?  new HashSet<>(): phoneBook.get(lastName);
        hs.add(_phoneNumber);
        phoneBook.put(lastName, hs);
    }

    static void get(String _last_name){
        if(phoneBook.get(_last_name.toUpperCase()) == null)
            System.err.println("Абонент " + _last_name + " отсутствует");
        else {
            System.out.println(_last_name +": " + phoneBook.get(_last_name.toUpperCase()));
        }
    }

    public static void printBook(){
        System.out.println("Весь справочник:");
        for (Map.Entry<String, HashSet<String>> pair : phoneBook.entrySet()) {
            System.out.println(pair.getKey() + ": " + pair.getValue());
        }
    }
}
