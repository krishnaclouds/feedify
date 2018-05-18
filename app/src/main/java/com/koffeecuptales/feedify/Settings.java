package com.koffeecuptales.feedify;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


/*
* Settings Activity, accessed from the side Menu
* TODO 7.Implement Admin Login, so that he can only do specific tasks.
* */

public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    /*
    * onClick Listener for Update Button.
    * It updates, the speaker name and lecture number.
    *
    * TODO 5.Show the current user and lectureNumber in a textView.
    * */
    public void updateSessionDetails(View view){

        EditText speakerName = findViewById(R.id.speakerName);
        EditText lectureNumber = findViewById(R.id.lectureNumber);

        if(speakerName.getText().toString().equals("") || lectureNumber.getText().toString().equals("")){
            Toast.makeText(this, "It wouldn't be great to save nothing :(", Toast.LENGTH_SHORT).show();
        } else{

            /*
            * Saving Speaker and lectureNumber in global SharedPref, so that we can access them anywhere.
            * */
            SharedPreferences sharedPref = this.getSharedPreferences(Constants.SHARED_PREFERENCES, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(Constants.SPEAKER, speakerName.getText().toString());
            editor.putString(Constants.LECTURE_NUMBER, lectureNumber.getText().toString());
            editor.apply();

            //TODO remove this later. - Updating speaker and lecture on the start page.
//            StartActivity startActivity = new StartActivity();
//            startActivity.updateDetails();

            /*
            * TODO 6.change editor.apply() to editor.commit()
            * */

            speakerName.setText("");
            lectureNumber.setText("");
            Toast.makeText(this, "Saved the details", Toast.LENGTH_SHORT).show();
        }
    }

    private String getSpeakerName(){
        SharedPreferences sharedPref = this.getSharedPreferences(Constants.SHARED_PREFERENCES, MODE_PRIVATE);
        return sharedPref.getString(Constants.SPEAKER, "speaker");
    }

    private String getLecutureNubmer(){
        SharedPreferences sharedPref = this.getSharedPreferences(Constants.SHARED_PREFERENCES, MODE_PRIVATE);
        return sharedPref.getString(Constants.LECTURE_NUMBER, "1");
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
