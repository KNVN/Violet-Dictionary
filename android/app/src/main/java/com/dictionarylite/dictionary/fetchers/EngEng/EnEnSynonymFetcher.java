package com.dictionarylite.dictionary.fetchers.EngEng;

import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.dictionarylite.FloatingWidgetService;
import com.dictionarylite.dictionary.objects.Synonym;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Admin on 24/07/2018.
 */

public class EnEnSynonymFetcher {
    private static EnEnSynonymFetcher mInstance;
    private RequestQueue mRequestQueue;
    private FloatingWidgetService mCtx;
    private final String base_url = "https://api.wordnik.com/v4/word.json/";
    private final String synonym_url = "/relatedWords?useCanonical=true&relationshipTypes=synonym&limitPerRelationshipType=4&api_key=1d5ec5e2544999d56c00b0db3b40d0ba937ffe566af81e9bb";

    public EnEnSynonymFetcher(FloatingWidgetService context, String _word){

        mCtx = context;
        final ArrayList<Synonym> synonyms = new ArrayList<Synonym>();
        mRequestQueue = getRequestQueue();

        String url = base_url+_word+synonym_url;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                                JSONArray sym = response.getJSONObject(0).getJSONArray("words");
                                for(int i=0; i<sym.length();i++) {
                                    synonyms.add(new Synonym(sym.getString(i)));
                                }
                            mCtx.addSynonym(synonyms);
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

    public static synchronized EnEnSynonymFetcher getInstance(FloatingWidgetService context, String _word) {
        if (mInstance == null) {
            mInstance = new EnEnSynonymFetcher(context, _word);
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
