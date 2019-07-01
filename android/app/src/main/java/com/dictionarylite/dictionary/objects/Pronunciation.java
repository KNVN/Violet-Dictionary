package com.dictionarylite.dictionary.objects;


public class Pronunciation {
    // In English, we have two kinds of pronunciation, UK and US
    // But, how about other languages?
    public String FirstType;
    public String SecondType;

    public Pronunciation(String FirstType, String SecondType) {

        this.FirstType = FirstType;
        this.SecondType = SecondType;
    }
}
