package com.dictionarylite.dictionary.objects;



public class Definition {
    public String word;
    public String type;
    public String definition;
    public String example;

    public Definition(String word, String type, String definition, String example) {
        this.word = word;
        this.type = type;
        this.definition = definition;
        this.example = example;
    }
}
