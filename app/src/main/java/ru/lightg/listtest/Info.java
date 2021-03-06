package ru.lightg.listtest;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;


public class Info extends DialogFragment {
    private String text;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        View v = inflater.inflate(R.layout.info, null);
        TextView textView = (TextView) v.findViewById(R.id.textView);
        textView.setText(text);
        return v;
    }
    public void setText(String text) {
        this.text = text;
    }
}
