package com.dictionarylite.dictionary.fetchers.EngEng;

import com.dictionarylite.dictionary.objects.Definition;
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

/**
 * Created by Admin on 10/07/2018.
 */

public class EnEnDefinitionFetcher {
    private static EnEnDefinitionFetcher mInstance;
    private RequestQueue mRequestQueue;
    private FloatingWidgetService mCtx;
    private final String base_url = "https://api.wordnik.com/v4/word.json/";
    private final String definition_url = "/definitions?limit=10&sourceDictionaries=wordnet,wiktionary&useCanonical=true&includeTags=false&api_key=1d5ec5e2544999d56c00b0db3b40d0ba937ffe566af81e9bb";

    public EnEnDefinitionFetcher(FloatingWidgetService context, final String _word){

        mCtx = context;
        final ArrayList<Definition> definitions = new ArrayList<Definition>();
        mRequestQueue = getRequestQueue();

        String url = base_url+_word+definition_url;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            if(response.length() != 0) {
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject definition = response.getJSONObject(i);
                                    JSONArray example = definition.getJSONArray("exampleUses");
                                    String ex="";
                                    if(example.length()!= 0)
                                    {
                                        for(int j = 0; j<example.length();j++)
                                        {
                                            String text = example.getJSONObject(j).getString("text");
                                            if(text.contains(_word))
                                            {
                                                ex=text;
                                                break;
                                            }
                                        }
                                    }
                                    definitions.add(new Definition(definition.getString("word"), definition.getString("partOfSpeech"), definition.getString("text"),ex));
                                }
                                mCtx.addDefinition(definitions);
                                mCtx.addPreferences("EnEn",_word);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(mCtx, "Could not find", Toast.LENGTH_SHORT).show();
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

    public static synchronized EnEnDefinitionFetcher getInstance(FloatingWidgetService context, String _word) {
        if (mInstance == null) {
            mInstance = new EnEnDefinitionFetcher(context, _word);
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
