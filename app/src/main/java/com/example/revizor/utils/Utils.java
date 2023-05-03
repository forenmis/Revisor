package com.example.revizor.utils;

import android.widget.EditText;

import androidx.annotation.NonNull;

import com.example.revizor.entity.SendStatus;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utils {
    public static String BUNDLE_KEY_ADDRESS = "address";

    @NonNull
    public static String getText(EditText et) {
        if (et.getText() == null) {
            return "";
        } else {
            return et.getText().toString();
        }
    }

    public static boolean checkPass(String password, String repeatPass) {
        return password.equals(repeatPass);
    }

    public static String getStrInDate(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH);
        return simpleDateFormat.format(date);
    }

    public static Date getDateInStr(String str) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH);
        try {
            return simpleDateFormat.parse(str);
        } catch (ParseException e) {
            return null;
        }
    }

    public static String getStrStatus(SendStatus sendStatus) {
        return sendStatus.name();
    }

    public static SendStatus getStatusByStr(String str) {
        if (str.equalsIgnoreCase(SendStatus.ReadyToSend.name())) {
            return SendStatus.ReadyToSend;
        } else if (str.equalsIgnoreCase(SendStatus.Sending.name())) {
            return SendStatus.Sending;
        } else if (str.equalsIgnoreCase(SendStatus.Sent.name())) {
            return SendStatus.Sent;
        } else throw new IllegalArgumentException();
    }
}
