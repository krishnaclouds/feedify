package com.koffeecuptales.feedify;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class feedback_form_new extends AppCompatActivity {

    private final QuestionSet questionSet = new QuestionSet();
    private final CSVWriter cw = new CSVWriter();

    private RadioGroup a1;
    private RadioGroup a2;
    private RadioGroup a3;
    private RadioGroup a4;
    private RadioGroup a5;

    private TextView q1;
    private TextView q2;
    private TextView q3;
    private TextView q4;
    private TextView q5;

    String employeeCode;
    String dataToSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_form_new);

        Intent intent = getIntent();
        employeeCode = intent.getStringExtra(Constants.EMPLOYEE_CODE);

        a1 =  findViewById(R.id.a1);
        a2 =  findViewById(R.id.a2);
        a3 =  findViewById(R.id.a3);
        a4 =  findViewById(R.id.a4);
        a5 =  findViewById(R.id.a5);

        a1.check(R.id.a11);
        a2.check(R.id.a22);
        a3.check(R.id.a33);
        a4.check(R.id.a44);
        a5.check(R.id.a55);

        q1 = (TextView) findViewById(R.id.q1);
        q2 = (TextView) findViewById(R.id.q2);
        q3 = (TextView) findViewById(R.id.q3);
        q4 = (TextView) findViewById(R.id.q4);
        q5 = (TextView) findViewById(R.id.q5);

    }

    public void submit_new(View view){
        int optionSelectedId1 = a1.getCheckedRadioButtonId();
        RadioButton r1 = findViewById(optionSelectedId1);
        String res1 = r1.getText().toString();
        String qes1 = q1.getText().toString();

        int optionSelectedId2 = a2.getCheckedRadioButtonId();
        RadioButton r2 = findViewById(optionSelectedId2);
        String res2 = r2.getText().toString();
        String qes2 = q2.getText().toString();

        int optionSelectedId3 = a3.getCheckedRadioButtonId();
        RadioButton r3 = findViewById(optionSelectedId3);
        String res3 = r3.getText().toString();
        String qes3 = q3.getText().toString();

        int optionSelectedId4 = a4.getCheckedRadioButtonId();
        RadioButton r4 = findViewById(optionSelectedId4);
        String res4 = r4.getText().toString();
        String qes4 = q4.getText().toString();

        int optionSelectedId5 = a5.getCheckedRadioButtonId();
        RadioButton r5 = findViewById(optionSelectedId5);
        String res5 = r5.getText().toString();
        String qes5 = q5.getText().toString();

        String dataToSave = qes1 + "," + res1 + "," + qes2 + "," + res2 + "," + qes3 + "," + res3 + "," + qes4 + "," + res4 + "," + qes5 + "," + res5 + "\n";

        dataToSave = employeeCode + "," + dataToSave;

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(getPermissionToWriteIfRequired()){
                cw.writeToFile(dataToSave, getLecutureNubmer(), getSpeakerName());
                Toast.makeText(this, "Thank you for your valuble feedback", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            cw.writeToFile(dataToSave, getLecutureNubmer(), getSpeakerName());
            Toast.makeText(this, "Thank you for your valuble feedback", Toast.LENGTH_SHORT).show();
            finish();
        }

     }

    private boolean getPermissionToWriteIfRequired(){
        boolean hasPermission = (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
        if (!hasPermission) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, Constants.REQUEST_EXTERNAL_STORAGE);
        }
        return hasPermission;
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case Constants.REQUEST_EXTERNAL_STORAGE:{
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                    SharedPreferences sharedPref = this.getSharedPreferences(Constants.SHARED_PREFERENCES, MODE_PRIVATE);
                    String lectureNumber = sharedPref.getString(Constants.LECTURE_NUMBER, "1");
                    String speaker = sharedPref.getString(Constants.SPEAKER, "speaker");
                    cw.writeToFile(dataToSave, lectureNumber, speaker);

                    Toast.makeText(this, "Thankyou for your valuble feedback", Toast.LENGTH_SHORT).show();
                    finish();

                } else {

                    Toast.makeText(this, "Failed to save the feedback", Toast.LENGTH_SHORT).show();
                    finish();

                }
            }
        }
    }
}
