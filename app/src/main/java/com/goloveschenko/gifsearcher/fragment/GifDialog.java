package com.goloveschenko.gifsearcher.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
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
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_gif, null);
        dialog.setContentView(view);
        ImageView imageView = (ImageView) view.findViewById(R.id.dialog_gif_image);
        ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.dialog_gif_progress);
        Glide.with(getActivity())
                .asGif()
                .load(getArguments().getString(ARG_KEY))
                .listener(new RequestListener<GifDrawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<GifDrawable> target, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GifDrawable resource, Object model, Target<GifDrawable> target, DataSource dataSource, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(imageView);
        return dialog;
    }
}
