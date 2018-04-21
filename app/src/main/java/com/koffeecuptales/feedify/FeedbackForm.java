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
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class FeedbackForm extends AppCompatActivity {

    /*
    * Objects to access QuestionSet and File Writer Utils
    * TODO 8.Separate Utils.
    * */
    private final QuestionSet questionSet = new QuestionSet();
    private final CSVWriter cw = new CSVWriter();

    /*
    * dataToSave variable is used to save the Data during the run time of the app
    * and once user answers all the feedback questions, the contents of dataToSave are written to disk.
    * */

    /*
    * HOW dataToSave IS MADE?
    * 1. retrieve the employee code from StartActivity extras and append to dataToSave
    * 2. When even, user submits an answer, retrieve the question and answer and append them along with a
    * ',' to the dataToSave
    * 3. When user is done with all the answers, append a '\n' instead of ',' so that new line is created.
    *
    * Note: Since, we are using a simple global variable, We have disabled backbuton once the feedback starts to keep the
    * logic simple.
    *
    * TODO 8.Improve the dataToSave logic => Improve the program logic itself, so that user can go back and edit his response.
    *
    * */
    private String dataToSave = "";

    /*
    * TextView to hold the Question!! This is updated using the method -> you guessed it updateQuestion :P
    * */
    private TextView questionTextView;
    private RadioGroup optionsGroup;
    private int questionNumber;

    /*
    * Shared Preferences object. Used to retrieve the lectureNumber and Speaker as and when needed!!
    * */
    private final SharedPreferences sharedPref = this.getSharedPreferences(Constants.SHARED_PREFERENCES, MODE_PRIVATE);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_form);

        /*
        * Retrieve the Employee Code from StartActivity.activity and save for Saving Purpose!!
        */
        Intent intent = getIntent();
        String employeeCode = intent.getStringExtra(Constants.EMPLOYEE_CODE);
        dataToSave = employeeCode + ",";

        /*
        * Reducing the Number of Questions by 1 (array.lenght() - 1 => last index of array), so as to use it directly in the call.
        * Questions are used in Reverse Order just to keep logic simple
        */
        questionNumber = questionSet.getNumberofQuestions() - 1;
        questionTextView = findViewById(R.id.questionTextView);

        /*
        * Call this method to set the First Question, just while laughing the Activity onCreate.
        * And after that, update the Question, whenever user submits an answer.
        * */
        updateQuestion(questionNumber);
        questionNumber = questionNumber - 1;

        optionsGroup = findViewById(R.id.optionsGroup);
    }

    /*
    * Helper Methods -> Name should help identify, what they do. Just go with the flow.
    * */
    private String getSpeakerName(){
        return sharedPref.getString(Constants.SPEAKER, "speaker");
    }

    private String getLecutureNubmer(){
        return sharedPref.getString(Constants.LECTURE_NUMBER, "1");
    }

    private void updateQuestion(int question) {
        questionTextView.setText(questionSet.getQuestion(question));
    }

    /*
    * Overriding the Back Button!! Leave it blank it just does nothing :P
    * */
    @Override
    public void onBackPressed() {
    }

    /*
    * onClick Listener for button 'submit'
    * Everything happens in here, so lets go take in slow.
    * */
    public void submitOptionClicked(View view) {

        /*
        * Retrieve the option selected by the user and the corresponding question
        * */
        int selectedOptionId = optionsGroup.getCheckedRadioButtonId();
        RadioButton optionSelected = findViewById(selectedOptionId);

        String response = optionSelected.getText().toString();
        String question = questionSet.getQuestion(questionNumber + 1);

        /*
        * Reset the value, so that on next question, he will have excellent selected by default.
        * */
        optionsGroup.check(R.id.option1);

        /*
        * If questionNumber is less than 0 => we have reached the end
        * So, just append '\n' instead of ',' and save the file
        * */
        if (questionNumber < 0) {
            dataToSave = dataToSave + question + "," + response + "\n";

            /*
            * Since, we only need read-write permissions in SDK version greater than M, as for them
            * */
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                /*
                * Here we are calling getPermissionToWrite, which return TRUE if we already have permission,
                * so all happy save the data and call finish() to return back to StartActivity
                *
                * If getPermissionToWrite file returns FALSE, we have nothing to do.
                * Since, the method getPermissionToWrite itself, has code to ask for permission, which opens a prompt box
                * Based on what ever user chooses to do, 'onRequestPermissionsResult' method is fired automatically
                * by Android. So just over-write it to save data to file, if user gives YES and
                * a failed to save toast, if things go wrong.
                * */
                if(getPermissionToWriteIfRequired()){
                    cw.writeToFile(dataToSave, getLecutureNubmer(), getSpeakerName());
                    Toast.makeText(this, "Thank you for your valuble feedback", Toast.LENGTH_SHORT).show();
                    finish();
                }
            } else {
                /*
                * SDK is lower, so just simply save, as Android by default gives the permission
                * */
                cw.writeToFile(dataToSave, getLecutureNubmer(), getSpeakerName());
                Toast.makeText(this, "Thank you for your valuble feedback", Toast.LENGTH_SHORT).show();
                finish();
            }

        } else {
            /*
            * Append the question and response to dataToSave and populate next question.
            * */
            dataToSave = dataToSave + question + "," + response + ",";
            updateQuestion(questionNumber);
            questionNumber = questionNumber - 1;
        }
    }

    /*
    * This method, checks if user has read-write permissions or not.
    * if YES, it returns true.
    * else , It requests of the Permissions and returns False.
    * User will get a prompt requesting the permission.
    * Based on his reaction -> onRequestPermissionsResult Method is fired, which we over-wrote to do something cool.
    * */
    private boolean getPermissionToWriteIfRequired(){
        boolean hasPermission = (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
        if (!hasPermission) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, Constants.REQUEST_EXTERNAL_STORAGE);
        }
        return hasPermission;
    }

    /*
    * Basically, we can assign an value to each of the permission we need, here we said Android to link permission related
    * to Storage read write in Constants.REQUEST_EXTERNAL_STORAGE. so, we go to it using switch case, which is actually not
    * need in our case as we are dealing with just one permission.
    *
    * This is method is called, when ever user reponds to permission request.
    * */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case Constants.REQUEST_EXTERNAL_STORAGE:{
                /*
                * If Permission is granted, Cool, just call writeToFile. else, Toast the failure
                * */
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

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