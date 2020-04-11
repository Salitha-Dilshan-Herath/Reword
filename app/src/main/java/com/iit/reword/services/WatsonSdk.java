package com.iit.reword.services;

import com.ibm.cloud.sdk.core.security.Authenticator;
import com.ibm.cloud.sdk.core.security.IamAuthenticator;
import com.ibm.watson.language_translator.v3.LanguageTranslator;
import com.ibm.watson.text_to_speech.v1.TextToSpeech;
import com.iit.reword.App;
import com.iit.reword.R;

class WatsonSdk {

    private static WatsonSdk sharedInstance = new WatsonSdk();

    //MARK: Services
    private LanguageTranslator languageTranslatorService;
    private TextToSpeech textToSpeechService;

    private WatsonSdk() {
        //initialization services
        setLanguageTranslatorService();
        setTextToSpeechService();

    }


    public static WatsonSdk getSharedInstance() {
        return sharedInstance;
    }

    //MARK: Translate Service
    protected LanguageTranslator getLanguageTranslatorService() {
        return languageTranslatorService;
    }

    private void setLanguageTranslatorService() {

        Authenticator authenticator = new IamAuthenticator(App.getInstance().getResources().getString(R.string.language_translator_apikey));
        LanguageTranslator service   = new LanguageTranslator("2018-05-01", authenticator);
        service.setServiceUrl(App.getInstance().getResources().getString(R.string.language_translator_url));

        this.languageTranslatorService = service;
    }

    //MARK: Text to Speech Service

    public TextToSpeech getTextToSpeechService() {
        return textToSpeechService;
    }

    private void setTextToSpeechService() {

        Authenticator authenticator = new IamAuthenticator(App.getInstance().getResources().getString(R.string.text_speech_iam_apikey));
        TextToSpeech  service       = new TextToSpeech(authenticator);
        service.setServiceUrl(App.getInstance().getResources().getString(R.string.text_speech_url));
        this.textToSpeechService = service;
    }
}
