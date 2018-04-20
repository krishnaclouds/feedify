package com.koffeecuptales.feedify;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        this.setTitle(getResources().getString(R.string.italkFeedback));
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

    // OnClickEventListener for the Button -> 'Give Feedback'
    public void startFeedback(View view) {

        //Get Employee Code and send it to FeedbackForm Activity
        String employeeCode;
        EditText editText = findViewById(R.id.employeeCode);
        if(editText.getText().toString().equalsIgnoreCase("")){
            Snackbar.make(findViewById(R.id.activity_feedbacklanding), "Please Provide your Employee Code", Snackbar.LENGTH_LONG).show();
        } else {
            employeeCode = editText.getText().toString();
            editText.setText("");

            Intent intent = new Intent(this, FeedbackForm.class);
            intent.putExtra(Constants.EMPLOYEE_CODE, employeeCode);
            startActivity(intent);
        }
    }
}
