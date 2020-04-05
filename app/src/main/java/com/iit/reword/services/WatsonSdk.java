package com.iit.reword.services;

import android.content.res.Resources;

import com.ibm.cloud.sdk.core.security.Authenticator;
import com.ibm.cloud.sdk.core.security.IamAuthenticator;
import com.ibm.watson.language_translator.v3.LanguageTranslator;
import com.iit.reword.App;
import com.iit.reword.R;

class WatsonSdk {

    private static WatsonSdk sharedInstance = new WatsonSdk();

    //MARK: Services
    private LanguageTranslator languageTranslatorService;

    private WatsonSdk() {
        //initialization services
        setLanguageTranslatorService();

    }


    public static WatsonSdk getSharedInstance() {
        return sharedInstance;
    }

    protected LanguageTranslator getLanguageTranslatorService() {
        return languageTranslatorService;
    }

    private void setLanguageTranslatorService() {

        Authenticator authenticator = new IamAuthenticator(App.getInstance().getResources().getString(R.string.language_translator_apikey));
        LanguageTranslator service   = new LanguageTranslator("2018-05-01", authenticator);
        service.setServiceUrl(App.getInstance().getResources().getString(R.string.language_translator_url));

        this.languageTranslatorService = service;
    }
}
