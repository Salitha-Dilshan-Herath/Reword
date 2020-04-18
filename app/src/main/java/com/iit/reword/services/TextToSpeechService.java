package com.iit.reword.services;

import android.os.AsyncTask;
import com.ibm.cloud.sdk.core.http.HttpMediaType;
import com.ibm.watson.developer_cloud.android.library.audio.StreamPlayer;
import com.ibm.watson.text_to_speech.v1.model.SynthesizeOptions;
import com.ibm.watson.text_to_speech.v1.model.Voice;
import com.ibm.watson.text_to_speech.v1.model.Voices;
import com.iit.reword.utility.interfaces.TextSpeechServiceImpl;

public class TextToSpeechService {

    private static TextToSpeechService shareInstance = new TextToSpeechService();
    private String languageCode                       = "";
    public StreamPlayer player                       = new StreamPlayer();
    public TextSpeechServiceImpl textSpeechServiceImpl;

    private TextToSpeechService() {
    }

    public static TextToSpeechService getShareInstance() {
        return shareInstance;
    }

    public void speech(String word) {
        new SynthesisTask().execute(word);
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }
}

class SynthesisTask extends AsyncTask<String, Void, Boolean> {

    @Override
    protected Boolean doInBackground(String... strings) {

        String selected_lan_code = TextToSpeechService.getShareInstance().getLanguageCode();
        String voice_type        = SynthesizeOptions.Voice.EN_US_LISAVOICE;

        Voices voices = WatsonSdk.getSharedInstance().getTextToSpeechService().listVoices().execute().getResult();

        for(Voice voice: voices.getVoices()){
            String[] code = voice.getLanguage().split("-");

            if (code.length > 0){
                if(code[0].equals(selected_lan_code)){
                    voice_type = voice.getName();
                    break;
                }
            }
        }

        System.out.println(voice_type);


        try {
            SynthesizeOptions synthesizeOptions = new SynthesizeOptions.Builder().text(strings[0])
                    .voice(voice_type)
                    .accept(HttpMediaType.AUDIO_WAV).build();
            TextToSpeechService.getShareInstance().player.playStream(WatsonSdk.getSharedInstance().getTextToSpeechService().synthesize(synthesizeOptions).execute().getResult());
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

        return true;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);

        if (TextToSpeechService.getShareInstance().textSpeechServiceImpl != null)
            TextToSpeechService.getShareInstance().textSpeechServiceImpl.isSuccessSpeech(aBoolean);
    }
}
