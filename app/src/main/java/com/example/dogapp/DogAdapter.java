package com.example.dogapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dogapp.model.DogBreed;
import com.squareup.picasso.Picasso;


import java.util.List;

public  class DogAdapter extends RecyclerView.Adapter<DogAdapter.DogHolder>{
    private List<DogBreed> dogs;
    private final  String url_img = "https://raw.githubusercontent.com/DevTides/DogsApi/master/";

    private SetClickListener setClickListener;
    private Context mContext;
    public DogAdapter(List<DogBreed> dogs, SetClickListener setClickListener, Context context) {
        this.dogs = dogs;
        this.setClickListener = setClickListener;
        this.mContext = context;
    }
    @NonNull
    @Override
    public DogHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_item, parent, false);
        return new DogHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DogHolder holder, int position) {
        holder.textView.setText(dogs.get(position).getName());
        Picasso.get().
                load(dogs.get(position).getUrl()).
                into(holder.imageView);
        holder.imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                handleLongClick(dogs.get(position).getUrl(), dogs.get(position).getName(),
                        dogs.get(position).getLifeSpan(), dogs.get(position).getOrigin());
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return dogs.size();
    }



    static class DogHolder extends RecyclerView.ViewHolder {
        private TextView textView;
        private ImageView imageView;
        public DogHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tv_name);
            imageView = itemView.findViewById(R.id.iv_photo);
        }
    }
    private void handleLongClick(String url, String name, String lifeSpan, String origin) {
        setClickListener.onSetLongClick(url, name, lifeSpan, origin);
    }
    public interface SetClickListener {
        void onSetLongClick(String url, String name, String lifeSpan, String origin);
    }
    public DogBreed getItem(int position) {
        return dogs.get(position);
    }
    public void updateDogDetails(int position, String name, String lifeSpan, String origin) {
        DogBreed dog = dogs.get(position);
        dog.setName(name);
        dog.setLifeSpan(lifeSpan);
        dog.setOrigin(origin);
        notifyItemChanged(position);
    }
}