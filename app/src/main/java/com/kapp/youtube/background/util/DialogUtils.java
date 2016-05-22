package com.kapp.youtube.background.util;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

/**
 * Created by khang on 22/05/2016.
 * Email: khang.neon.1997@gmail.com
 */
public class DialogUtils {
    private static final String TAG = "DialogUtils";

    public static void ConfirmDialog(Activity activity, String title, String message,
                                     ButtonGroup buttonGroup,
                                     DialogInterface.OnClickListener onConfirm) {
        new AlertDialog.Builder(activity)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(buttonGroup.getPositiveLabel(), onConfirm)
                .setNegativeButton(buttonGroup.getNegativeLabel(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    public enum ButtonGroup {
        YES_NO("Yes", "No"),
        CANCEL_OK("OK", "Cancel");
        private String negativeLabel, positiveLabel;

        ButtonGroup(String positiveLabel, String negativeLabel) {
            this.negativeLabel = negativeLabel;
            this.positiveLabel = positiveLabel;
        }

        public String getNegativeLabel() {
            return negativeLabel;
        }

        public String getPositiveLabel() {
            return positiveLabel;
        }
    }
}
