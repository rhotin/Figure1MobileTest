package com.appdeveloper.rh.figure1mobiletest;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by Roman on 2/18/2018.
 */

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ImageViewHolder> {

    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }


    public class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView photo;
        TextView title;

        public ImageViewHolder(final View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(itemView, position);
                        }
                    }
                }
            });
        }


    }

    private ArrayList<ImageObject> mImageObjects;
    private Context mContext;

    public GalleryAdapter(Context context, ArrayList<ImageObject> gifObjects) {
        mImageObjects = gifObjects;
        mContext = context;
    }

    private Context getContext() {
        return mContext;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View trendingView = inflater.inflate(R.layout.gallery_row, parent, false);
        ImageViewHolder viewHolder = new ImageViewHolder(trendingView);
        viewHolder.photo = (ImageView) viewHolder.itemView.findViewById(R.id.photo);
        viewHolder.title = (TextView) viewHolder.itemView.findViewById(R.id.title);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        Glide.with(getContext()).load("https://i.imgur.com/" +
                mImageObjects.get(position).id + ".jpg").into(holder.photo);
        holder.title.setText(mImageObjects.get(position).title);

    }

    @Override
    public int getItemCount() {
        return mImageObjects.size();
    }
}
