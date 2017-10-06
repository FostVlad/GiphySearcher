package com.goloveschenko.gifsearcher.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.goloveschenko.gifsearcher.R;

public class GifDialog extends DialogFragment {
    private static final String ARG_KEY = "url";

    public static GifDialog getInstance(String url) {
        GifDialog gifDialog = new GifDialog();
        Bundle args = new Bundle();
        args.putString(ARG_KEY, url);
        gifDialog.setArguments(args);
        return gifDialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_gif, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.dialog_gif_image);
        Glide.with(getActivity())
                .asGif()
                .load(getArguments().getString(ARG_KEY))
                .into(imageView);

        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .create();
    }
}
