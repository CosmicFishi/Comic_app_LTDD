package com.example.comic_app;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.net.URL;
import java.util.regex.Pattern;

public class Utils {
    public Bitmap getImage(String url) {
        try {
            return BitmapFactory.decodeStream(new URL(url).openConnection().getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static boolean checkValidRegex(String string, String pattern) {
        return Pattern.matches(pattern, string);
    }

    /*
        hàm này chỉ trả về đối tượng alert box thôi.
        cần phải thêm hai nút yes, no sẽ xử lý việc nào.
        sau đó thì phải hiển thị ra
    */
    public static AlertDialog.Builder getAlertBox(Context context, String title, String msg) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        alertDialogBuilder.setTitle(title).setMessage(msg);

        return alertDialogBuilder;
    }
}
