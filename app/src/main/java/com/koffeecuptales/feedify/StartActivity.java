package com.koffeecuptales.feedify;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

/*
* The App Basically starts in here!!
* */

/*
* APP level to-do's remaining.
* TODO 8.Implement settings page proerly, so that Admin has access to certain things specifically - Admin Login
* TODO 9.Add a Splash Screen
* TODO 10.Fill and complete About i-talk page.
* TODO 11.Implement 'Create Questionary' feature
* TODO 12.Implement 'Select Questionary' feature -> so that, Admin can select a specific QuestionSet before lecture
* TODO 13.Implement Read and Write Questionary to and from Excel Sheet, so that, it is easy to upload them and reuse them
* */

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        /*
        * Since, The App title is set from Firt Activity App bar androdi:name, to make them different,
        * we have to rewrite the app bar title, here manually
        * */
        this.setTitle(getResources().getString(R.string.italkFeedback));

        /*
        * Asking the user permission to read-write in memory card, if he hasn't already granted them during app install.
        * */
        boolean hasPermission = (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
        if (!hasPermission) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, Constants.REQUEST_EXTERNAL_STORAGE);
        }
    }

    /*
    * The next tow functions, basically over-writes the Back button and prompts for user conifirmation to exit the app.
    * */

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


    /*
    * Creating the main_menu to the StartActivity.
    * It has two options now, Settings and About.
    * Selecting on any of them, takes user to the concerned Activity
    * */
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

    /*
    * OnClick Listener for 'Give Feedback' button.
    * I prefer this way of declaring onClick functions rather then <buttonId>.addOnClickListener() style,
    * beacuse, it gives us the flexibility to reuse the Method!!
    * */
    public void startFeedback(View view) {

        /*
        * Get the Employee Code from user and then, send it to the Next Activity -> FeedbackForm,
        * so that we can include it in the Data to be saved
        * */
        String employeeCode;
        EditText editText = findViewById(R.id.employeeCode);
        if(editText.getText().toString().equalsIgnoreCase("")){
            /*
            * This is snackbar, try it; Its cool!
            * */
            Snackbar.make(findViewById(R.id.activity_start), "Please Provide your Employee Code!", Snackbar.LENGTH_LONG).show();
        } else {
            employeeCode = editText.getText().toString();

            /*
            * Erasing the Employee code from EditView after saving it
            * */
            editText.setText("");

            /*
            * Starting the New Intent, along with the extra Parameter -> EmployeeCode
            * key -> Constants.EMPLOYEE_CODE value -> employeeCode.
            * */
            Intent intent = new Intent(this, FeedbackForm.class);
            intent.putExtra(Constants.EMPLOYEE_CODE, employeeCode);
            startActivity(intent);
        }
    }
}
