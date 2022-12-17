package com.mustafa.bitirmeprojem;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.mustafa.bitirmeprojem.databinding.ActivityFoodBinding;

import java.io.ByteArrayOutputStream;

public class FoodActivity extends AppCompatActivity {
    private ActivityFoodBinding binding;
    ActivityResultLauncher<Intent> activityResultLauncher;
    ActivityResultLauncher<String> permissionResultLauncher;
    Bitmap selectedImage;
    DB Db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityFoodBinding.inflate(getLayoutInflater());
        View view=binding.getRoot();
        setContentView(view);
        registerLauncher();
        Db=new DB(this);
        Intent intent=getIntent();
        String info=intent.getStringExtra("info");
        if(info.equals("new")){
            //
            //binding.txtYemekAd.setText(" ");
            //binding.txtYemekIcindekiler.setText(" ");
            //binding.txtYemekTarifi.setText(" ");
            binding.button.setVisibility(View.VISIBLE);
            binding.imageView.setImageResource(R.drawable.selectimage);
        }else{
            int foodId=intent.getIntExtra("foodId",0);
            binding.button.setVisibility(View.INVISIBLE);
            try{
                SQLiteDatabase database=Db.getWritableDatabase();
                Cursor cursor=database.rawQuery("select * from foods where id=?",new String[] {String.valueOf(foodId)});
                int FoodNameIndex=cursor.getColumnIndex("foodName");
                int FoodRecipe=cursor.getColumnIndex("foodRecipe");
                int Foodingredients=cursor.getColumnIndex("foodingredients");
                int image=cursor.getColumnIndex("foodImage");

                while(cursor.moveToNext()){
                    binding.txtYemekAd.setText(cursor.getString(FoodNameIndex));
                        binding.txtYemekTarifi.setText(cursor.getString(FoodRecipe));
                    binding.txtYemekIcindekiler.setText(cursor.getString(Foodingredients));
                    byte[] bytes=cursor.getBlob(image);
                    Bitmap bitmap= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                    binding.imageView.setImageBitmap(bitmap);
                }
                cursor.close();

            }catch (Exception e){

            }
        }
    }
    public Bitmap makeSmallerImage(Bitmap image,int maximumSize){
        int width=image.getWidth();
        int height=image.getHeight();

        float bitMapRatio=(float) width/ (float) height;
        if(bitMapRatio>1){
            //yatay bir görsel
            width=maximumSize;
            height=(int) (width/bitMapRatio);
        }
        else {
            //dikey bir görsel
            height=maximumSize;
            width=(int)(height*bitMapRatio);
        }
        Bitmap smallImage=Bitmap.createScaledBitmap(image,width,height,true);
        return smallImage;
    }
    public void save(View view){
        String YemekAd=binding.txtYemekAd.getText().toString();
        String YemekTarif=binding.txtYemekTarifi.getText().toString();
        String YemekIcindekiler=binding.txtYemekIcindekiler.getText().toString();

        Bitmap kucukResim=makeSmallerImage(selectedImage,300);
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        kucukResim.compress(Bitmap.CompressFormat.PNG,50,byteArrayOutputStream);
        byte[] array= byteArrayOutputStream.toByteArray();

        if(YemekAd.equals(" ") || YemekTarif.equals(" ") || YemekIcindekiler.equals(" ")){
            Toast.makeText(FoodActivity.this,"Bu alanlar boş gecilemez",Toast.LENGTH_SHORT).show();
        }
        else{
            Boolean insertData=Db.insertData2(YemekAd,YemekTarif,YemekIcindekiler,array);
            if(insertData==true){
                Toast.makeText(FoodActivity.this,"Yemek bilgileri eklendi",Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(FoodActivity.this,AnaSayfa.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
            else{
                Toast.makeText(FoodActivity.this,"Yemek Eklenirken bir hata oluştu",Toast.LENGTH_SHORT).show();
            }
        }

    }
    public void selectedImage(View view){
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE)){
                Snackbar.make(view,"İzne ihtiyacımız var",Snackbar.LENGTH_INDEFINITE).setAction("İzin ver", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //izin isteme
                        permissionResultLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
                    }
                }).show();
            }
            else{
                //izin isteme
                permissionResultLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
        }
        else{
            //galeriye git
            Intent intentToGallery=new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            activityResultLauncher.launch(intentToGallery);
        }

    }
    private void registerLauncher(){
        activityResultLauncher=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if(result.getResultCode()==RESULT_OK){
                    Intent intentToFromResult=result.getData();
                    if(intentToFromResult !=null){
                        Uri imageData=intentToFromResult.getData();
                        try {
                            if(Build.VERSION.SDK_INT>=28){
                                ImageDecoder.Source source=ImageDecoder.createSource(getContentResolver(),imageData);
                                selectedImage=ImageDecoder.decodeBitmap(source);
                                binding.imageView.setImageBitmap(selectedImage);
                            }
                            else{
                                selectedImage= MediaStore.Images.Media.getBitmap(FoodActivity.this.getContentResolver(),imageData);
                                binding.imageView.setImageBitmap(selectedImage);
                            }
                        }catch (Exception e){
                            e.printStackTrace();

                        }
                    }
                }
            }
        });
        permissionResultLauncher=registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
            @Override
            public void onActivityResult(Boolean result) {
                if(result){
                    Intent intentToGallery=new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    activityResultLauncher.launch(intentToGallery);
                }
                else{
                    Toast.makeText(FoodActivity.this,"Galeriye gitmek için izne ihtiyacım var",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}