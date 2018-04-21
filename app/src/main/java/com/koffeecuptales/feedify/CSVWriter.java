package com.koffeecuptales.feedify;


import android.os.Build;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

class CSVWriter {

    public void writeToFile(String content, String lectureNumber, String speaker) {

        if (!isExternalStorageWritable()) {
            return;
        }

        File file = new File(getFileStorageDir(), "lecure_" + lectureNumber + "_" + speaker + ".csv");
        try {
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
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "ISROFeedBackData");
        } else {
            dir = new File(Environment.getExternalStorageDirectory() + "/Documents");
        }

        Log.d("FILES", "" + dir);

        if (dir.exists()) {
            return dir;
        } else if (!dir.mkdirs()) {
            Log.e(Constants.LOG_TAG, "Directory cannot be Created");
        }

        return dir;
    }
}
