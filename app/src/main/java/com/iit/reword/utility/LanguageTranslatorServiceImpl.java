package com.iit.reword.utility;

import com.ibm.watson.language_translator.v3.model.IdentifiableLanguages;
import com.ibm.watson.language_translator.v3.model.TranslationResult;
import com.iit.reword.roomdb.model.Phrase;

public interface LanguageTranslatorServiceImpl {

    void getLanguageList(IdentifiableLanguages languages);

    void getTranslateResult(TranslationResult result);

}
