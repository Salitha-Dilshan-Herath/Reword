package com.iit.reword.model;

public class OfflineTranslate {

    private String phrase;
    private String translate_phrase;

    public String getPhrase() {
        return phrase;
    }

    public void setPhrase(String phrase) {
        this.phrase = phrase;
    }

    public String getTranslate_phrase() {
        return translate_phrase;
    }

    public void setTranslate_phrase(String translate_phrase) {
        this.translate_phrase = translate_phrase;
    }

    @Override
    public String toString() {
        return "OfflineTranslate{" +
                "phrase='" + phrase + '\'' +
                ", translatePhrase='" + translate_phrase + '\'' +
                '}';
    }
}
