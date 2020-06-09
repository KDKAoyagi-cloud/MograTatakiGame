package com.example.mogratatakigame;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

public class ShowDialogClass extends DialogFragment {

    public String dialogMsg = "ハイスコアをリセットしてよろしいでしょうか";

    @Override
    public Dialog onCreateDialog(Bundle bundle){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(dialogMsg);
        builder.setPositiveButton("はい", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                        //はいをタップした時の動作
                        MainActivity mainActivity = (MainActivity)getActivity();
                        mainActivity.playClickSound();
                        mainActivity.resetHighScore();
            }
        });
        builder.setNegativeButton("いいえ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //いいえをタップした時の動作
                MainActivity mainActivity = (MainActivity)getActivity();
                mainActivity.playClickSound();
                dismiss();
            }
        });

        return builder.create();
    }
}
