package com.koffeecuptales.feedify;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

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
