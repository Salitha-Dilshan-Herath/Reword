package com.iit.reword.services;

import android.os.AsyncTask;
import android.view.View;
import android.widget.Toast;

import com.ibm.watson.language_translator.v3.model.IdentifiableLanguages;
import com.ibm.watson.language_translator.v3.model.TranslateOptions;
import com.ibm.watson.language_translator.v3.model.Translation;
import com.ibm.watson.language_translator.v3.model.TranslationResult;
import com.iit.reword.activities.TranslateActivity;
import com.iit.reword.model.TranslateModel;
import com.iit.reword.utility.interfaces.LanguageTranslatorServiceImpl;

public class LanguageTranslatorService  {

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

    public void translate(TranslateModel model) {
        new LanguageTranslateTask().execute(model);
    }

    public void translateList(TranslateModel model) { new LanguageTranslateListTask().execute(model); }
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
    }

}

class LanguageTranslateTask extends AsyncTask<TranslateModel, Integer, TranslationResult> {

    @Override
    protected TranslationResult doInBackground(TranslateModel... translateModels) {

        TranslationResult translationResult   = null;


        System.out.println("Code real   " + translateModels[0].getLanguageCode());
        System.out.println("word real   " + translateModels[0].getWord());

        TranslateOptions translateOptions = new TranslateOptions.Builder()
                                                .addText(translateModels[0].getWord())
                                                .source("en").target(translateModels[0].getLanguageCode())
                                                .build();
        try {

            translationResult = WatsonSdk.getSharedInstance().getLanguageTranslatorService().translate(translateOptions).execute().getResult();
        } catch (Exception e) {
            System.out.println(e.toString());
        }

        return translationResult;
    }

    @Override
    protected void onPostExecute(TranslationResult result) {
        super.onPostExecute(result);


        System.out.println(result);
        LanguageTranslatorService.getShareInstance().languageTranslatorServiceImpl.getTranslateResult(result);
    }
}

class LanguageTranslateListTask extends AsyncTask<TranslateModel, Integer, TranslateModel> {

    @Override
    protected TranslateModel doInBackground(TranslateModel... translateModels) {

        TranslationResult translationResult   = null;

        System.out.println("Code real   " + translateModels[0].getLanguageCode());
        System.out.println("word real   " + translateModels[0].getWord());

        TranslateOptions translateOptions = new TranslateOptions.Builder()
                .addText(translateModels[0].getWord())
                .source("en").target(translateModels[0].getLanguageCode())
                .build();
        try {

            translationResult = WatsonSdk.getSharedInstance().getLanguageTranslatorService().translate(translateOptions).execute().getResult();
            if (translationResult != null) {

                if (translationResult.getWordCount() > 0) {

                    String word = "";
                    for (Translation translation : translationResult.getTranslations()) {

                        word += translation.getTranslation();
                    }

                    translateModels[0].setTranslatedWord(word);

                }
            }

        } catch (Exception e) {
            System.out.println(e.toString());
        }

        return translateModels[0];
    }

    @Override
    protected void onPostExecute(TranslateModel result) {
        super.onPostExecute(result);

        System.out.println(result);
        LanguageTranslatorService.getShareInstance().languageTranslatorServiceImpl.getTranslateListResult(result);
    }
}