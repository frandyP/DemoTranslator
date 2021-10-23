package com.example.itlavision;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

public interface GoogleCloudService {

    String GoogleAccessKeyTranslate = "https://translation.googleapis.com/language/translate/v2?key=AIzaSyAP3GItLN5jESTdL3M8tWuad9JsBpFUj60";
    String GooogleAccessKeylanguages = "https://translation.googleapis.com/language/translate/v2/languages?key=AIzaSyAP3GItLN5jESTdL3M8tWuad9JsBpFUj60";
    String GooogleAccessKeyDetect  = "https://translation.googleapis.com/language/translate/v2/detect?key=AIzaSyAP3GItLN5jESTdL3M8tWuad9JsBpFUj60";

    String languageOrigin = null;
    String languageOriginCode = null;

    String languageDestiny = null;
    String languageDestinyCode = null;



    void TranslateThisText(String languageOrigin, String languageDestiny, String textToTranlate, Context context, final VolleyCallback callback);
    void POSTRequest(JSONObject jsonObjectToTranslate);

}
