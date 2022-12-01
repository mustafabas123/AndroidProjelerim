package com.mustafa.benimuygulamam;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mustafa.benimuygulamam.databinding.BookRowBinding;

import java.util.ArrayList;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookHolder> {//RecyclerView olduğunu belirtmek için RecyclerView.Adapter dahil etmemiz gerekiyor
    ArrayList<Book> bookArrayList;
    public BookAdapter(ArrayList<Book> bookArrayList){
        this.bookArrayList=bookArrayList;
    }
    @NonNull
    @Override
    public BookHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        BookRowBinding bookRowBinding=BookRowBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new BookHolder(bookRowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull BookAdapter.BookHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.binding.recyclerViewText.setText(bookArrayList.get(position).name);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(holder.itemView.getContext(),BookActivity.class);
                intent.putExtra("info","old");
                intent.putExtra("bookId",bookArrayList.get(position).id);
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return bookArrayList.size();
    }
    public class BookHolder extends RecyclerView.ViewHolder{
        private BookRowBinding binding;
        public BookHolder(BookRowBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
    }
}
