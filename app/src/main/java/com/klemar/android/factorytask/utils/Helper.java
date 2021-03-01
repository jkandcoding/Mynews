package com.klemar.android.factorytask.utils;

import android.content.Context;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;

public class Helper {

    public static void showAlertDialog(Context context) {
        AlertDialog dialog = new AlertDialog.Builder(context).create();
                dialog.setTitle("Greška");
        dialog.setMessage("Ups, došlo je do pogreške.");
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, "U redu", (dialogInterface, i) -> dialog.dismiss());
                dialog.show();
    }
}
