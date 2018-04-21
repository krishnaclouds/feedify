package com.koffeecuptales.feedify;
import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class FeedbackForm extends AppCompatActivity {

    private final QuestionSet questionSet = new QuestionSet();
    private final CSVWriter cw = new CSVWriter();
    private String dataToSave = "";
    private TextView questionTextView;
    private RadioGroup optionsGroup;
    private int questionNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_form);

        // Retrieve the Employee Code from StartActivity.activity and save for Saving Purposes!!
        Intent intent = getIntent();
        String employeeCode = intent.getStringExtra(Constants.EMPLOYEE_CODE);
        dataToSave = employeeCode + ",";

        // Reducing the Number by 1, so as to use it directly in the call. QUestions are used in Reverse Order.
        questionNumber = questionSet.getNumberofQuestions() - 1;
        questionTextView = findViewById(R.id.questionTextView);
        updateQuestion(questionNumber);

        // Update the Question Index.
        questionNumber = questionNumber - 1;

        optionsGroup = findViewById(R.id.optionsGroup);
    }

    @Override
    public void onBackPressed() {
    }

    // The logic is to save the outpu of radio button in a variable and use them to create csv file.
    public void submitOptionClicked(View view) {

        // Save all the responses and Go back to the StartActivity.
        int selectedOptionId = optionsGroup.getCheckedRadioButtonId();
        RadioButton optionSelected = findViewById(selectedOptionId);

        String response = optionSelected.getText().toString();
        String question = questionSet.getQuestion(questionNumber + 1);

        optionsGroup.check(R.id.option1);

        if (questionNumber < 0) {

            dataToSave = dataToSave + question + "," + response + "\n";
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if(getPermissionToWriteIfRequired()){
                    cw.writeToFile(dataToSave, getLecutureNumer(), getSpeakerName());
                    Toast.makeText(this, "Thankyou for your valuble feedback", Toast.LENGTH_SHORT).show();
                    finish();
                }
            } else {
                cw.writeToFile(dataToSave, getLecutureNumer(), getSpeakerName());
                Toast.makeText(this, "Thankyou for your valuble feedback", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            Log.d(Constants.LOG_TAG, "" + questionNumber);
            dataToSave = dataToSave + question + "," + response + ",";
            updateQuestion(questionNumber);
            questionNumber = questionNumber - 1;
        }
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

    private void updateQuestion(int question) {
        questionTextView.setText(questionSet.getQuestion(question));
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

    private String getLecutureNumer(){
        SharedPreferences sharedPref = this.getSharedPreferences(Constants.SHARED_PREFERENCES, MODE_PRIVATE);
        return sharedPref.getString(Constants.LECTURE_NUMBER, "1");
    }
}