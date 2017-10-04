package com.goloveschenko.gifsearcher.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.goloveschenko.gifsearcher.R;
import com.goloveschenko.gifsearcher.data.entity.Gif;

import java.util.List;

public class GifsAdapter extends RecyclerView.Adapter<GifsAdapter.ViewHolder> {
    private List<Gif> gifList;
    private Context context;

    public GifsAdapter(List<Gif> gifList, Context context) {
        this.gifList = gifList;
        this.context = context;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        ProgressBar progressBar;

        ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.gif_item_image);
            progressBar = (ProgressBar) itemView.findViewById(R.id.gif_item_progress);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gif_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Gif gif = gifList.get(position);
        Glide.with(context)
                .asGif()
                .load(gif.getNormalSizeUrl())
                .listener(new RequestListener<GifDrawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<GifDrawable> target, boolean isFirstResource) {
                        holder.progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GifDrawable resource, Object model, Target<GifDrawable> target, DataSource dataSource, boolean isFirstResource) {
                        holder.progressBar.setVisibility(View.GONE);
                        holder.image.setVisibility(View.VISIBLE);
                        return false;
                    }
                })
                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        return gifList.size();
    }
}
