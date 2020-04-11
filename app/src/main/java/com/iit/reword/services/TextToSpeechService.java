package com.iit.reword.services;

import android.os.AsyncTask;

import com.ibm.cloud.sdk.core.http.HttpMediaType;
import com.ibm.watson.developer_cloud.android.library.audio.StreamPlayer;
import com.ibm.watson.text_to_speech.v1.model.SynthesizeOptions;
import com.iit.reword.utility.interfaces.TextSpeechServiceImpl;

public class TextToSpeechService {

    private static TextToSpeechService shareInstance = new TextToSpeechService();
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

}

class SynthesisTask extends AsyncTask<String, Void, Boolean> {

    @Override
    protected Boolean doInBackground(String... strings) {

        SynthesizeOptions synthesizeOptions = new SynthesizeOptions.Builder().text(strings[0])
                                                                                  .voice(SynthesizeOptions.Voice.EN_US_LISAVOICE)
                                                                                  .accept(HttpMediaType.AUDIO_WAV).build();
        try {
            TextToSpeechService.getShareInstance().player.playStream(WatsonSdk.getSharedInstance().getTextToSpeechService().synthesize(synthesizeOptions).execute().getResult());
        }catch (Exception e){
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
