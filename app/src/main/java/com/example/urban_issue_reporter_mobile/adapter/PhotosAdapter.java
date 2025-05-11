package com.example.urban_issue_reporter_mobile.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.urban_issue_reporter_mobile.R;
import com.example.urban_issue_reporter_mobile.model.Photo;

import java.util.List;

public class PhotosAdapter extends RecyclerView.Adapter<PhotosAdapter.PhotoViewHolder> {

    private Context context;
    private List<Photo> photos;

    // Options Glide optimisées
    private static RequestOptions glideOptions = new RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.ALL) // Mise en cache sur disque
            .priority(Priority.HIGH) // Priorité élevée
            .dontTransform() // Évite les transformations qui ralentissent
            .placeholder(R.drawable.placeholder_image);

    public PhotosAdapter(Context context, List<Photo> photos) {
        this.context = context;
        this.photos = photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_photo, parent, false);
        return new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {
        Photo photo = photos.get(position);

        // Afficher le chargement
        holder.progressBar.setVisibility(View.VISIBLE);

        // Nettoyer les requêtes précédentes
        Glide.with(context).clear(holder.imageView);

        // Charger l'image avec Glide avec des options optimisées
        Glide.with(context)
                .load(photo.getUrl())
                .apply(glideOptions)
                .transition(DrawableTransitionOptions.withCrossFade(100)) // Transition plus rapide
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        holder.progressBar.setVisibility(View.GONE);
                        Log.e("PhotosAdapter", "Erreur de chargement de l'image: " + (e != null ? e.getMessage() : "inconnu"));
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        holder.progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return photos != null ? photos.size() : 0;
    }

    static class PhotoViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        ProgressBar progressBar;

        public PhotoViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.photoImageView);
            progressBar = itemView.findViewById(R.id.photoProgressBar);
        }
    }
}