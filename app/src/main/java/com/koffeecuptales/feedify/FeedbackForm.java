package com.koffeecuptales.feedify;
import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
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

            SharedPreferences sharedPref = this.getSharedPreferences(Constants.SHARED_PREFERENCES, MODE_PRIVATE);
            String lectureNumber = sharedPref.getString(Constants.LECTURE_NUMBER, "1");
            String speaker = sharedPref.getString(Constants.SPEAKER, "speaker");

            getPermissionToWriteIfRequired();
            cw.writeToFile(dataToSave, lectureNumber, speaker);

            Toast.makeText(this, "Thankyou for your valuble feedback", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            dataToSave = dataToSave + question + "," + response + ",";
            updateQuestion(questionNumber);
            questionNumber = questionNumber - 1;
        }
    }

    private void updateQuestion(int question) {
        questionTextView.setText(questionSet.getQuestion(question));
    }

    private void getPermissionToWriteIfRequired(){
        boolean hasPermission = (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
        if (!hasPermission) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constants.REQUEST_EXTERNAL_STORAGE);
        }
    }

}
