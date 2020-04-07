package com.iit.reword.model;

public class TranslateModel {

    private String word;

    private String languageCode;

    public TranslateModel(String word, String languageCode) {
        this.word = word;
        this.languageCode = languageCode;
    }

    public String getWord() {
        return word;
    }

    public String getLanguageCode() {
        return languageCode;
    }
}
