package com.mustafa.benimuygulamam;

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

import com.mustafa.benimuygulamam.databinding.ActivityMainBinding;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    SQLiteDatabase database;
    ArrayList<Book> bookArrayList;
    BookAdapter bookAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding=ActivityMainBinding.inflate(getLayoutInflater());
        View view=binding.getRoot();
        setContentView(view);

        bookArrayList=new ArrayList<>();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        bookAdapter=new BookAdapter(bookArrayList);
        binding.recyclerView.setAdapter(bookAdapter);
        getData();
    }
    public void getData(){
        try {
            database=this.openOrCreateDatabase("Kutuphane",MODE_PRIVATE,null);//yoksa bir dataBase yarat Kutuphane adında
            Cursor cursor=database.rawQuery("select * from kitaplik",null); //kitaplik tablosundaki verileri al ve cursor değişkeninw atadık
            int AdIndex=cursor.getColumnIndex("KitapAd");
            int IdIndex=cursor.getColumnIndex("id");
            while(cursor.moveToNext()){
                String Ad=cursor.getString(AdIndex);
                int id=cursor.getInt(IdIndex);
                Book book=new Book(Ad,id);
                bookArrayList.add(book);
            }
            bookAdapter.notifyDataSetChanged();//önemli
            cursor.close();//cursor kappatık önemli
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        //menu ile main activiteyi bağladık bu kod bloğunda
        MenuInflater menuInflater =getMenuInflater();
        menuInflater.inflate(R.menu.book_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    //bu kod ise menuye tıklandığında ne olması gerektiğini sağlıyor
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() ==R.id.add_book){// Menümüzde 1 tane seçenek var onun id sini kontrol ettik
            Intent intent=new Intent(this,BookActivity.class);
            intent.putExtra("info","new");//yeni bir kitap eklenicek
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

}