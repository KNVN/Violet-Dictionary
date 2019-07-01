package com.dictionarylite;

import android.content.SharedPreferences;

import android.graphics.Typeface;
import android.os.Build;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.app.Service;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.Gravity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dictionarylite.dictionary.objects.Definition;
import com.dictionarylite.dictionary.objects.Pronunciation;
import com.dictionarylite.dictionary.objects.Synonym;
import com.dictionarylite.dictionary.fetchers.EngEng.EnEnDefinitionFetcher;
import com.dictionarylite.dictionary.fetchers.EngEng.EnEnPronunciationFetcher;
import com.dictionarylite.dictionary.fetchers.EngEng.EnEnSynonymFetcher;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FloatingWidgetService extends Service {
    private WindowManager mWindowManager;
    private View mFloatingView;
    WindowManager.LayoutParams params;

    View Screen;
    SharedPreferences sharedPreferences;
    //

    ArrayList<Definition> mDefinitions = new ArrayList<Definition>();
    ArrayList<Pronunciation> mPronun = new ArrayList<Pronunciation>();
    ArrayList<Synonym> mSynonyms = new ArrayList<Synonym>();
    //Adapter List
    TextView word;
    TextView definition;
    TextView pronun;
    TextView synonym;
    View adaplist;
    //
    List<String> items;
    ListView listview;
    ArrayAdapter adapter;



    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //Inflate the floating view layout we created
        mFloatingView = LayoutInflater.from(this).inflate(R.layout.floating_widget, null);

        //Add the view to the window.
         params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        //Specify the view position
        params.gravity = Gravity.TOP | Gravity.LEFT;        //Initially view will be added to top-left corner
        params.x = 0;
        params.y = 100;

        //Add the view to the window
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        mWindowManager.addView(mFloatingView, params);
        //The root element of the collapsed view layout
        final View collapsedView = mFloatingView.findViewById(R.id.collapse_view);
//The root element of the expanded view layout
        final View expandedView = mFloatingView.findViewById(R.id.expanded_container);
        Screen = mFloatingView.findViewById(R.id.screen);

         adaplist = mFloatingView.findViewById(R.id.Adapter);


//Set the close button
        ImageView closeButtonCollapsed = (ImageView) mFloatingView.findViewById(R.id.close_btn);
        closeButtonCollapsed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //close the service and remove the from from the window
                stopSelf();
            }
        });


//Set the view while floating view is expanded.
        final EditText editText = (EditText) mFloatingView.findViewById(R.id.editText);
        //get words list from memory
        sharedPreferences = getSharedPreferences("wit_player_shared_preferences", MODE_PRIVATE);
        String input = sharedPreferences.getString("InputLanguage","English");
        //output? it's english
        if(input == "Vietnamese") {
            Toast.makeText(FloatingWidgetService.this, "Chinese", Toast.LENGTH_LONG).show();
        }
        getPreferences("EnEn");

        listview = (ListView) mFloatingView.findViewById(R.id.listview);
            listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item = (String)adapterView.getItemAtPosition(i);
                SearchForWord(item);
            }
        });



//Set the search button.
        Button searchBtn = (Button) mFloatingView.findViewById(R.id.search_btn);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isEditTextExpanded()){
                    editText.setVisibility(View.VISIBLE);
                    collapsedView.setVisibility(View.GONE);
                    listview.setVisibility(View.GONE);
                    enableKeyPadInput(true);
                    Screen.setAlpha((float)0.5);
                }else{
                    String text = editText.getText().toString();

                    //add word to memory
                    editText.setVisibility(View.GONE);
                    collapsedView.setVisibility(View.VISIBLE);
                    listview.setVisibility(View.VISIBLE);
                    SearchForWord(text);
                    enableKeyPadInput(false);
                    Screen.setAlpha((float)0);
                }
            }
        });
        //AdapterList
        word = (TextView) mFloatingView.findViewById(R.id.word);
        pronun = (TextView) mFloatingView.findViewById(R.id.pronun);
        definition = (TextView) mFloatingView.findViewById(R.id.definition);
        definition.setMovementMethod(new ScrollingMovementMethod());
        synonym = (TextView) mFloatingView.findViewById(R.id.synonym);
        synonym.setMovementMethod(new ScrollingMovementMethod());

        collapsedView.setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;


            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:


                        //remember the initial position.
                        initialX = params.x;
                        initialY = params.y;


                        //get the touch location
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        return true;
                    case MotionEvent.ACTION_UP:
                        int Xdiff = (int) (event.getRawX() - initialTouchX);
                        int Ydiff = (int) (event.getRawY() - initialTouchY);


                        //The check for Xdiff <10 && YDiff< 10 because sometime elements moves a little while clicking.
                        //So that is click event.
                        if (Xdiff < 10 && Ydiff < 10) {
                            if (isViewExpanded()) {
                                //When user clicks on the image view of the collapsed layout,
                                //and expanded view will become gone.
                                expandedView.setVisibility(View.GONE);
                                adaplist.setVisibility(View.GONE);
                                listview.setVisibility(View.GONE);

                            }else{
                                //or visible
                                expandedView.setVisibility(View.VISIBLE);
                                listview.setVisibility(View.VISIBLE);
                            }
                        }
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        //Calculate the X and Y coordinates of the view.
                        params.x = initialX + (int) (event.getRawX() - initialTouchX);
                        params.y = initialY + (int) (event.getRawY() - initialTouchY);


                        //Update the layout with new X & Y coordinate
                        mWindowManager.updateViewLayout(mFloatingView, params);
                        return true;
                }
                return false;
            }
        });
        //….
        //….
    }

    /**
     * Detect if the floating view is collapsed or expanded.
     *
     * @return true if the floating view is expanded.
     */
    private boolean isViewExpanded() {
        return mFloatingView == null || mFloatingView.findViewById(R.id.expanded_container).getVisibility() == View.VISIBLE;
    }
    private boolean isEditTextExpanded() {
        return mFloatingView == null || mFloatingView.findViewById(R.id.editText).getVisibility() == View.VISIBLE;
    }
    private void enableKeyPadInput(Boolean enable) {
        int initialX = params.x;
        int initialY = params.y;
        mWindowManager.removeViewImmediate(mFloatingView);
        if (enable) {
            params = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.TYPE_PHONE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                    PixelFormat.TRANSLUCENT);
        } else {
            params = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_PHONE,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT);
        }
        params.gravity = Gravity.TOP | Gravity.LEFT;        //Initially view will be added to top-left corner
        params.x = initialX;
        params.y = initialY;
        mWindowManager.addView(mFloatingView, params);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mFloatingView != null) mWindowManager.removeView(mFloatingView);
    }
    public void getPreferences(String key){
        String wordsString = sharedPreferences.getString(key,"");

        String[] itemsWords = wordsString.split(",");
        items = new ArrayList<String>();
        if (itemsWords.length >= 10) {
            for (int i = 0; i < 10; i++) {
                items.add(itemsWords[i]);
            }
        } else if (itemsWords.length > 0){
            for (int i = 0; i<itemsWords.length; i++) {
                items.add(itemsWords[i]);
            }
        } else{items.add("NONE");}

        adapter = new ArrayAdapter(FloatingWidgetService.this, R.layout.list,android.R.id.text1, items);
    }
    public void addPreferences(String key, String text){
        if(!Objects.equals(items.get(0), text) && !Objects.equals(text, "")) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            //descend order
            items.add(0, text);
//
            StringBuilder stringBuilder = new StringBuilder();
            for (String s : items) {
                stringBuilder.append(s);
                stringBuilder.append(",");
            }
            editor.putString(key, stringBuilder.toString());
            editor.commit();
            //to list
            adapter.notifyDataSetChanged();
        }
    }
    public void SearchForWord(String text){
        adaplist.setVisibility(View.GONE);

        new EnEnDefinitionFetcher(FloatingWidgetService.this, text);
        pronun.setText("");
        new EnEnPronunciationFetcher(FloatingWidgetService.this, text);
        synonym.setText("");
        new EnEnSynonymFetcher(FloatingWidgetService.this,text);
    }
    public void addAdapter(){
        word.setText(this.mDefinitions.get(0).word);
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<mDefinitions.size();i++){
                String def = "<i>("+this.mDefinitions.get(i).type+")</i> "+this.mDefinitions.get(i).definition+"<br><br>";
                sb.append(def);
                if(!Objects.equals(this.mDefinitions.get(i).example, ""))
                {
                   String ex = "<i><font color='#C17EDA'>"+this.mDefinitions.get(i).example+"</font></i><br>";
                   sb.append(ex);
                }
        }
        definition.setText(Html.fromHtml(sb.toString()));
        ((View)mFloatingView.findViewById(R.id.Adapter)).setVisibility(View.VISIBLE);
        Toast.makeText(FloatingWidgetService.this, "Success", Toast.LENGTH_SHORT).show();
    }
    public void addDefinition(ArrayList<Definition> newItems)
    {
       if(mDefinitions.size()>0) {
            mDefinitions.clear();
      }
      mDefinitions.addAll(newItems);
            this.addAdapter();
    }
    public void addPronunciation(ArrayList<Pronunciation> newItems)
    {
      if(mPronun.size()>0) {
           mPronun.clear();
      }
        mPronun.addAll(newItems);
        pronun.setText(this.mPronun.get(0).FirstType);
    }
    public void addSynonym(ArrayList<Synonym> newItems)
    {
        if(mSynonyms.size()>0) {
            mSynonyms.clear();
        }
        mSynonyms.addAll(newItems);
        for(int i=0;i<mSynonyms.size();i++){
            if(i==0) {
                synonym.setText("synonym: "+this.mSynonyms.get(0).synonym);
            }else {
                synonym.setText(synonym.getText()+", "+this.mSynonyms.get(i).synonym);
            }
        }
    }

}