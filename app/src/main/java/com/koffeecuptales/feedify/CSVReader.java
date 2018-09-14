package com.koffeecuptales.feedify;

import android.os.Environment;

public class CSVReader {

    public void readFromFile(String fileName){
        if(!isExternalStorageReadable()){
            return;
        }

    }

    private boolean isExternalStorageReadable() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

}
