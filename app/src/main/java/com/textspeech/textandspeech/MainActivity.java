package com.textspeech.textandspeech;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    //Declaring variables
    EditText editText;
    Button button;
    ImageView micView;
    TextToSpeech t1;
    private final int REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialization for variables

        editText = findViewById(R.id.showText);
        button = findViewById(R.id.submit_btn);
        micView = findViewById(R.id.mic_id);
        t1 = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.ENGLISH);
                }
            }
        });


        //setting action to element

        settingAction();


    }

    private void settingAction() {

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String findTheText = editText.getText().toString();

                if (findTheText.equals("")) {
                    editText.setError("Field  should not be empty!");
                    Toast.makeText(MainActivity.this, "Please enter your text", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, findTheText, Toast.LENGTH_SHORT).show();
                    t1.speak(findTheText, TextToSpeech.QUEUE_FLUSH, null);
                }

            }
        });

        micView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say something");

                try {
                    startActivityForResult(intent, REQUEST_CODE);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Sorry, Your Device does not support voice input!", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_CODE:
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> res = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);




                    editText.setText(res.get(0));
                    Toast.makeText(getApplicationContext(), res.get(0), Toast.LENGTH_SHORT).show();
                    t1.speak(res.get(0), TextToSpeech.QUEUE_FLUSH, null);
                }
        }


    }


    @Override
    protected void onDestroy() {
        //shouting down

        if (t1 != null) {
            t1.stop();
            t1.shutdown();
        }

        super.onDestroy();

    }

}