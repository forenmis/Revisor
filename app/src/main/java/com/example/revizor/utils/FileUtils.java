package com.example.revizor.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FileUtils {
    public static File createTempFile(Context context) throws IOException {
        String imageName = generateImageName();
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageName, ".jpg", storageDir);
        return image;
    }

    private static String generateImageName() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH);
        String timeStamp = simpleDateFormat.format(new Date());
        return "JPEG" + timeStamp + "_";
    }
}
