package com.example.koray.capstoneproject.Utils;

import android.app.ProgressDialog;
import android.content.Context;

import com.example.koray.capstoneproject.R;

/**
 * Created by Koray on 22.12.2017.
 */

public class ProgDialog {
    private ProgressDialog dialog;

    public ProgDialog(Context context){
        dialog = new ProgressDialog(context);
        dialog.setMessage("Yükleniyor");
        dialog.setCancelable(false);
    }

    public void show(){
        dialog.show();
    }

    public void stop(){
        dialog.dismiss();
    }
}
