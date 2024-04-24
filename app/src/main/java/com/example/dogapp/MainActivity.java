package com.example.dogapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.SearchView;

import com.example.dogapp.databinding.ActivityMainBinding;
import com.example.dogapp.model.DogBreed;
import com.example.dogapp.viewmodel.DoggDetail;
import com.example.dogapp.viewmodel.DogsApiService;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements DogAdapter.SetClickListener, ItemTouchHelperListener{
    private ActivityMainBinding binding;
    private DogAdapter dogAdapter;
    private DogsApiService dogsApiService;
    private List<DogBreed> allDogBreeds;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View viewRoot = binding.getRoot();
        setContentView(viewRoot);
        dogsApiService = new DogsApiService();
        binding.mainSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchSets(newText);
                return false;
            }
        });
        setupRV();

    }
    private void setupRV(){
        dogsApiService.getDogs()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<List<DogBreed>>() {
                    @Override
                    public void onSuccess(@NonNull List<DogBreed> dogBreeds) {
                        allDogBreeds = dogBreeds;
                        displayDogBreeds(allDogBreeds);
//                        dogAdapter.notifyDataSetChanged();
                    }
                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });

    }
    private void displayDogBreeds(List<DogBreed> dogBreeds) {
        dogAdapter = new DogAdapter(dogBreeds, MainActivity.this, this);
        binding.rvImages.setAdapter(dogAdapter);
        binding.rvImages.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
        // dogAdapter.notifyDataSetChanged(); // No need to call notifyDataSetChanged here
    }
    private void searchSets(String query) {
        if (query.isEmpty()) {
            displayDogBreeds(allDogBreeds); // Display all dog breeds if the query is empty
        } else {
            List<DogBreed> filteredList = new ArrayList<>();
            for (DogBreed dogBreed : allDogBreeds) {
                // Perform a case-insensitive search based on the dog breed's name
                if (dogBreed.getName().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(dogBreed);
                }
            }
            displayDogBreeds(filteredList); // Display filtered dog breeds
        }
    }

    @Override
    public void onSetLongClick(String url, String name, String lifeSpan, String origin) {
        Intent intent = new Intent(MainActivity.this, DoggDetail.class);
        intent.putExtra("url", url);
        intent.putExtra("name", name);
        intent.putExtra("lifeSpan", lifeSpan);
        intent.putExtra("origin", origin);
        startActivity(intent);
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder) {
        int position = viewHolder.getAdapterPosition();
        DogBreed dogBreed = dogAdapter.getItem(position); // Lấy thông tin của con chó từ adapter

        if (dogBreed != null) {
            // Update thông tin của con chó trong adapter hoặc view holder tương ứng
            dogAdapter.updateDogDetails(position, dogBreed.getName(), dogBreed.getLifeSpan(), dogBreed.getOrigin()); // Cập nhật thông tin chi tiết của con chó

            // Áp dụng animation để mở rộng item
            Animation animation = AnimationUtils.loadAnimation(this, R.anim.expand_animation);
            viewHolder.itemView.startAnimation(animation);

            // Gọi notifyDataSetChanged() để cập nhật giao diện
            dogAdapter.notifyDataSetChanged();
        }
    }
}