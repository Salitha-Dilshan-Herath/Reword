package com.iit.reword.model;

public class TranslateModel {

    private String word;

    private String languageCode;

    //optional
    private int listIndex;

    private String languageName;

    private String translatedWord;

    private int phraseId;

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

    public int getListIndex() {
        return listIndex;
    }

    public void setListIndex(int listIndex) {
        this.listIndex = listIndex;
    }

    public String getLanguageName() {
        return languageName;
    }

    public void setLanguageName(String languageName) {
        this.languageName = languageName;
    }

    public String getTranslatedWord() {
        return translatedWord;
    }

    public void setTranslatedWord(String translatedWord) {
        this.translatedWord = translatedWord;
    }

    public int getPhraseId() {
        return phraseId;
    }

    public void setPhraseId(int phraseId) {
        this.phraseId = phraseId;
    }
}
