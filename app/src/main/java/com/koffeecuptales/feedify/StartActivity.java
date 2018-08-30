package com.koffeecuptales.feedify;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

//        this.setTitle(getResources().getString(R.string.italkFeedback));
        this.setTitle("MCF i-talk (" + getLectureDate() + ")\n");

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            boolean hasPermission = (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
            if (!hasPermission) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, Constants.REQUEST_EXTERNAL_STORAGE);
            }
        }

        TextView lectureBy = (TextView) findViewById(R.id.lectureby);
        lectureBy.setText(String.format("%s %s", "by: ", getSpeakerName()));
        TextView lectureNo = (TextView) findViewById(R.id.lectureNo);
        lectureNo.setText(String.format("%s %s", getString(R.string.lectureNo), getLecutureNubmer()));
        TextView tv_lectuteTitle = findViewById(R.id.tv_lectureTitle);
        tv_lectuteTitle.setText(getLectureTitle());
        TextView tv_lectureDate = findViewById(R.id.tv_lectureDate);
        tv_lectureDate.setText(getLectureDate());


        AutoCompleteTextView textView = (AutoCompleteTextView) findViewById(R.id.employeeCode);
        String[] employees = getResources().getStringArray(R.array.employeeIdArray);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_selectable_list_item, employees);
        textView.setAdapter(adapter);

    }

    public void updateDetails(){
        TextView lectureBy = findViewById(R.id.lectureby);
        lectureBy.setText(String.format("%s %s", getString(R.string.lectureby), getSpeakerName()));
        TextView lectureNo = findViewById(R.id.lectureNo);
        lectureNo.setText(String.format("%s %s", getString(R.string.lectureNo), getLecutureNubmer()));
        TextView tv_lectuteTitle = findViewById(R.id.tv_lectureTitle);
        tv_lectuteTitle.setText(getLectureTitle());
        TextView tv_lectureDate = findViewById(R.id.tv_lectureDate);
        tv_lectureDate.setText(getLectureDate());
    }

    @Override
    public void onBackPressed() {
        AlertDialog diaBox = AskOption();
        diaBox.show();
    }

    private AlertDialog AskOption()
    {
        return new AlertDialog.Builder(this)
                .setTitle("Exit")
                .setMessage("Are you sure you want to exit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menu_settings){
            Intent intent = new Intent(this, Settings.class);
            startActivity(intent);
        } else if(item.getItemId() == R.id.menu_about){
            Intent intent = new Intent(this, About.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    public void startFeedback(View view) {

        String employeeCode;

        AutoCompleteTextView editText = (AutoCompleteTextView) findViewById(R.id.employeeCode);
        if(editText.getText().toString().equalsIgnoreCase("")){

            Snackbar.make(findViewById(R.id.activity_start), "Please Provide your Employee Code!", Snackbar.LENGTH_LONG).show();
        } else {
            employeeCode = editText.getText().toString();

            editText.setText("");

            // Intent intent = new Intent(this, FeedbackForm.class);
            Intent intent = new Intent(this, feedback_form_new.class);
            intent.putExtra(Constants.EMPLOYEE_CODE, employeeCode);
            startActivity(intent);
        }
    }

    private String getSpeakerName(){
        SharedPreferences sharedPref = this.getSharedPreferences(Constants.SHARED_PREFERENCES, MODE_PRIVATE);
        return sharedPref.getString(Constants.SPEAKER, "MCF - ISRO");
    }

    private String getLecutureNubmer(){
        SharedPreferences sharedPref = this.getSharedPreferences(Constants.SHARED_PREFERENCES, MODE_PRIVATE);
        return sharedPref.getString(Constants.LECTURE_NUMBER, "1");
    }

    private String getLectureTitle(){
        SharedPreferences sharedPref = this.getSharedPreferences(Constants.SHARED_PREFERENCES, MODE_PRIVATE);
        return sharedPref.getString(Constants.LECTURE_TITLE, "Welcome to MCF i-talk");
    }

    private String getLectureDate(){

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Calendar c = Calendar.getInstance();
        String date = sdf.format(c.getTime());

        SharedPreferences sharedPref = this.getSharedPreferences(Constants.SHARED_PREFERENCES, MODE_PRIVATE);
        return sharedPref.getString(Constants.LECTURE_DATE, date);
    }
}
