package com.mustafa.bitirmeprojem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.mustafa.bitirmeprojem.databinding.ActivityAnaSayfaBinding;

import java.util.ArrayList;

public class AnaSayfa extends AppCompatActivity {
    private ActivityAnaSayfaBinding binding;
    ArrayList<Food> foodArrayList;
    FoodAdapter foodAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityAnaSayfaBinding.inflate(getLayoutInflater());
        View view=binding.getRoot();
        setContentView(view);
        foodArrayList=new ArrayList<>();
        binding.recylerView.setLayoutManager(new LinearLayoutManager(this));
        foodAdapter=new FoodAdapter(foodArrayList);
        binding.recylerView.setAdapter(foodAdapter);

        getData();
    }
    public void getData(){
        try {
            DB Db=new DB(this);
            SQLiteDatabase sqLiteDatabase=Db.getWritableDatabase();
            Cursor cursor=sqLiteDatabase.rawQuery("select * from foods",null);
            int nameIndex=cursor.getColumnIndex("foodName");
            int IdIndex=cursor.getColumnIndex("id");
            while(cursor.moveToNext()){
                String name=cursor.getString(nameIndex);
                int id=cursor.getInt(IdIndex);
                Food food=new Food(name,id);
                foodArrayList.add(food);
            }
            foodAdapter.notifyDataSetChanged();
            cursor.close();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.food_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.add_food){
            Intent intent=new Intent(this,FoodActivity.class);
            intent.putExtra("info","new");
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}