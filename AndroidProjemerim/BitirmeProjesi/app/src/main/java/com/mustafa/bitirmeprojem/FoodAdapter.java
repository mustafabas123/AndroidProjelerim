package com.mustafa.bitirmeprojem;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mustafa.bitirmeprojem.databinding.RecylerRowBinding;

import java.util.ArrayList;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodHolder> {

    ArrayList<Food> foodArrayList;
    public FoodAdapter (ArrayList<Food> foodArrayList){
        this.foodArrayList=foodArrayList;
    }
    @NonNull
    @Override
    public FoodHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecylerRowBinding recylerRowBinding=RecylerRowBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new FoodHolder(recylerRowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.binding.recylerViewTextView.setText(foodArrayList.get(position).name);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(holder.itemView.getContext(),FoodActivity.class);
                intent.putExtra("info","old");
                intent.putExtra("foodId",foodArrayList.get(position).id);
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return foodArrayList.size();
    }

    public class FoodHolder extends RecyclerView.ViewHolder{
        private RecylerRowBinding binding;
        public FoodHolder(RecylerRowBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
    }
}
