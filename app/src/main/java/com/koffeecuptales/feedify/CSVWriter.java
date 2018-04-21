package com.koffeecuptales.feedify;


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

        File file = new File(getFileStorageDir("ISROFeedBackData"), "lecure_" + lectureNumber + "_" + speaker + ".csv");

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


    private File getFileStorageDir(String dirName) {

        File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), dirName);

        if (dir.exists()) {
            return dir;
        } else if (!dir.mkdirs()) {
            Log.e(Constants.LOG_TAG, "Directory cannot be Created");
        }

        return dir;
    }
}
