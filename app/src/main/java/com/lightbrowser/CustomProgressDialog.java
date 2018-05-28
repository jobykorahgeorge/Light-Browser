package com.lightbrowser;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.RelativeLayout;


/**
 * Created by JOBY KORAH GEORGE on 14-Mar-18.
 */

public class CustomProgressDialog {
    public static ProgressDialog createProgressDialog(Context mContext, boolean cancelable) {

        ProgressDialog progressDialog = new ProgressDialog(mContext,R.style.MyTheme);
        try {
            progressDialog.show();
        } catch (WindowManager.BadTokenException e) {
            Log.d("progress_error", e.getMessage());
        }
        progressDialog.setCancelable(cancelable);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(mContext.getResources().getColor(R.color.transparent)));
        progressDialog.setContentView(R.layout.progress_dialog);
        return progressDialog;
    }
}
