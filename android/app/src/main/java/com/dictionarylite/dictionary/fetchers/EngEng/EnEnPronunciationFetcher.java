package com.dictionarylite.dictionary.fetchers.EngEng;

import com.dictionarylite.dictionary.objects.Definition;
import com.dictionarylite.dictionary.objects.Pronunciation;
import android.content.Context;
import android.widget.Toast;

import com.dictionarylite.FloatingWidgetService;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by Admin on 10/07/2018.
 */

public class EnEnPronunciationFetcher {
    private static EnEnPronunciationFetcher mInstance;
    private RequestQueue mRequestQueue;
    private FloatingWidgetService mCtx;

    private final String base_url = "https://api.datamuse.com/words?sp=";
    private final String pronun_url = "&qe=sp&md=r&ipa=1&max=1";
    public EnEnPronunciationFetcher(FloatingWidgetService context, String _word){

        mCtx = context;
        mRequestQueue = getRequestQueue();

        String url = base_url+_word+pronun_url;
        final ArrayList<Pronunciation> pronuns = new ArrayList<Pronunciation>();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            String pronun = response.getJSONObject(0).getJSONArray("tags").getString(2);
                            String[] IPA = pronun.split(":");
                            if(IPA.length == 2) { //!null
                                pronuns.add(new Pronunciation("/ " + IPA[1] + " /", null));
                                mCtx.addPronunciation(pronuns);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(mCtx, "Error", Toast.LENGTH_SHORT).show();

                    }
                });

// Access the RequestQueue through your singleton class.
        mRequestQueue.add(jsonArrayRequest);

    }

    public static synchronized EnEnPronunciationFetcher getInstance(FloatingWidgetService context, String _word) {
        if (mInstance == null) {
            mInstance = new EnEnPronunciationFetcher(context, _word);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }
}

