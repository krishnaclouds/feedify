package com.koffeecuptales.feedify;

import android.os.Build;
import android.os.Environment;
import android.util.Log;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/*
*  Class that Writes Data to the Internal Storage. :)
* */

class CSVWriter {

    /*
    * @params Contents to Write into the file. (Just change the extension, if we need Text File or so)
    * @params lectureNumber -> This value is obtained from SharedPrefs. Can be changed from settings option in menu
    * @params speaker -> -do-
    * */

    public void writeToFile(String content, String lectureNumber, String speaker) {

        /*
        * Just a precautionary step to see if Internal Storage is in Writable state (May be it has all but RAM!)
        * */
        if (!isExternalStorageWritable()) {
            return;
        }

        /*
        * Creates a new file if it already doesn't exist.
        * TODO 2. Define Directory name to Constants class
        * @params -> Directory (In this case, it is stored in /<Internal Storage>/Documents/ITALKFeedbackData/lecture_<lectureNumber>
        *     _<speakerName>.csv)
        * */
        File file = new File(getFileStorageDir(), "lecure_" + lectureNumber + "_" + speaker + ".csv");
        try {
            /*
            * TODO 1.Move the File write process to background.
            * */
            FileOutputStream outputStream = new FileOutputStream(file, true);
            outputStream.write(content.getBytes());
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isExternalStorageWritable() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    private File getFileStorageDir() {

        File dir;

        /*
        * getExternalStoragePublicDirectory is only available after Android API-19, so the check.
        * It basically creates a folder in <Internal Storage>/Documents/*
        * TODO 3.Constant.DATA_SAVE_DIRECTORY refer to TODO 2
        * */
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "ITALKFeedbackData");
        } else {
            dir = new File(Environment.getExternalStorageDirectory() + "/Documents");
        }

        if (dir.exists()) {
            return dir;
        } else if (!dir.mkdirs()) {
            Log.e(Constants.LOG_TAG, "Directory cannot be Created");
        }

        /*
        * TODO 4.Save the File save location in SharedPrefs and add a button in settings
        * so that Admin can see where File is being saved in different devices.
        * */
        return dir;
    }
}
