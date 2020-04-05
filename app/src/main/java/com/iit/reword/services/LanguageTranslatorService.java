package com.iit.reword.services;

import android.os.AsyncTask;

import com.ibm.watson.language_translator.v3.model.IdentifiableLanguages;
import com.iit.reword.utility.LanguageTranslatorServiceImpl;

public class LanguageTranslatorService {

    private static LanguageTranslatorService shareInstance = new LanguageTranslatorService();

    public LanguageTranslatorServiceImpl languageTranslatorServiceImpl;

    private LanguageTranslatorService() {
    }

    public static LanguageTranslatorService getShareInstance() {
        return shareInstance;
    }

    public void getAllLanguages() {
        new LanguageDownloadTask().execute();
    }
}


class LanguageDownloadTask extends AsyncTask<Void, Integer, IdentifiableLanguages> {

    @Override
    protected IdentifiableLanguages doInBackground(Void... voids) {

        IdentifiableLanguages identifiableLanguage = null;
        try {
            identifiableLanguage = WatsonSdk.getSharedInstance().getLanguageTranslatorService()
                    .listIdentifiableLanguages().execute().getResult();
        } catch (Exception e) {
            System.out.println(e.toString());
        }

        return identifiableLanguage;
    }

    @Override
    protected void onPostExecute(IdentifiableLanguages languages) {
        super.onPostExecute(languages);


        LanguageTranslatorService.getShareInstance().languageTranslatorServiceImpl.getLanguageList(languages);
        //System.out.println(languages.getLanguages().size());
    }

}