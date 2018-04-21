package com.koffeecuptales.feedify;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    public void updateSessionDetails(View view){
        EditText speakerName = findViewById(R.id.speakerName);
        EditText lectureNumber = findViewById(R.id.lectureNumber);

        if(speakerName.getText().toString().equals("") || lectureNumber.getText().toString().equals("")){
            Toast.makeText(this, "It wouldn't be great to save nothing :(", Toast.LENGTH_SHORT).show();
        } else{
            SharedPreferences sharedPref = this.getSharedPreferences(Constants.SHARED_PREFERENCES, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(Constants.SPEAKER, speakerName.getText().toString());
            editor.putString(Constants.LECTURE_NUMBER, lectureNumber.getText().toString());
            editor.apply();

            speakerName.setText("");
            lectureNumber.setText("");
            Toast.makeText(this, "Saved the details", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
