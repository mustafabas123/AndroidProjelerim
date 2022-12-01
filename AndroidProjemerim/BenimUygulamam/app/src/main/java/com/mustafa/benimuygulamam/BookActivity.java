package com.mustafa.benimuygulamam;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.mustafa.benimuygulamam.databinding.ActivityBookBinding;

import java.io.ByteArrayOutputStream;

public class BookActivity extends AppCompatActivity {
    private ActivityBookBinding binding;
    ActivityResultLauncher<Intent> activityResultLauncher;
    ActivityResultLauncher<String> permissonLauncher;
    Bitmap selectedImage;
    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding=ActivityBookBinding.inflate(getLayoutInflater());
        View view=binding.getRoot();
        setContentView(view);
        registerLauncher();//izin isteme ve resim getirme launchlarını burda tanımlamamız gerekiyor aksi takdirde hata verir
        database=this.openOrCreateDatabase("Kutuphane",MODE_PRIVATE,null);

        Intent intent=getIntent();
        String info=intent.getStringExtra("info");
        if(info.equals("new")){
            //yeni kitap
            binding.txtKitapAd.setText("");
            binding.txtKitapYazar.setText("");
            binding.txtSayfaSayisi2.setText("");
            binding.txtYayinEvi.setText("");
            binding.button.setVisibility(View.VISIBLE);//Yeni bir kitap ekleniceği zaman butonu göster kaydet butonunu
            binding.imageView.setImageResource(R.drawable.selectimage);
        }
        else{
            int bookId=intent.getIntExtra("bookId",0);
            binding.button.setVisibility(View.INVISIBLE);//Yeni bir kitap eklenmiyceği zaman butonu gösterme

            try {
                Cursor cursor=database.rawQuery("select * from kitaplik where id= ?",new String[] {String.valueOf(bookId)});
                int KitapAdIndex=cursor.getColumnIndex("KitapAd");
                int yazarAdIndex=cursor.getColumnIndex("YazarAd");
                int sayfaSayisiIndex=cursor.getColumnIndex("sayfaSayisi");
                int yayinEviIndex=cursor.getColumnIndex("yayinEvi");
                int imageIndex=cursor.getColumnIndex("resim");

                while(cursor.moveToNext())
                {
                    binding.txtKitapAd.setText(cursor.getString(KitapAdIndex));
                    binding.txtKitapYazar.setText(cursor.getString(yazarAdIndex));
                    binding.txtSayfaSayisi2.setText(cursor.getString(sayfaSayisiIndex));
                    binding.txtYayinEvi.setText(cursor.getString(yayinEviIndex));

                    byte[] bytes=cursor.getBlob(imageIndex);
                    Bitmap bitmap= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                    binding.imageView.setImageBitmap(bitmap);
                }
                cursor.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    public void Save(View view){
        String kitapAd=binding.txtKitapAd.getText().toString();
        String yazarAd=binding.txtKitapYazar.getText().toString();
        String sayfaSayisi=binding.txtSayfaSayisi2.getText().toString();
        String yayinEvi=binding.txtYayinEvi.getText().toString();

        //görseli byte dizisine çevirdik ve küçük bir hale getirdik
        Bitmap image=smallMakeImage(selectedImage,300);
        ByteArrayOutputStream outputStream=new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG,50,outputStream);
        byte[] byteArray=outputStream.toByteArray();

        try {

            database.execSQL("create table if not exists kitaplik(id Integer primary key,KitapAd varchar,YazarAd varchar,sayfaSayisi varchar,yayinEvi varchar,resim blob)");

            String sqlString="insert into kitaplik(KitapAd,YazarAd,sayfaSayisi,yayinEvi,resim) values(?,?,?,?,?)";
            SQLiteStatement sqLiteStatement=database.compileStatement(sqlString);
            //Bu kod bloğunda app ten aldığımız verileri veri tabanına atıyoruz ama bağlama yaparak burdaki soru işaretleri tek tek atama yapıyoruz
            sqLiteStatement.bindString(1,kitapAd);
            sqLiteStatement.bindString(2,yazarAd);
            sqLiteStatement.bindString(3,sayfaSayisi);
            sqLiteStatement.bindString(4,yayinEvi);
            sqLiteStatement.bindBlob(5,byteArray);
            sqLiteStatement.execute();

        }catch (Exception e){
            //veriler dataBase aktarılırken bir hata oluşursa uygulama çökmesin diye bu kod bloğunu kullandık.
            //uygulama çökmesin consola hata mesajı dönsün.
            e.printStackTrace();
        }
        Intent intent=new Intent(BookActivity.this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//Bütün activiteleri kapat sadece bu aktiviteyi aç
        startActivity(intent);
    }
    public Bitmap smallMakeImage(Bitmap image,int maximumSize){
        int width=image.getWidth();
        int height=image.getHeight();

        float bitMapOrani=(float)width/height;

        if(bitMapOrani>1){
            //yatay bir görsel
            width=maximumSize;
            height=(int)(width/bitMapOrani);
        }else{
            //dikey bir görsel
            height=maximumSize;
            width=(int)(height*bitMapOrani);
        }
        Bitmap smallBitmap=Bitmap.createScaledBitmap(image,width,height,true);
        return smallBitmap;
    }
    public void SelectedImage(View view){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE)){
                Snackbar.make(view,"Galeriye girmek için izne ihtiyacım var",Snackbar.LENGTH_INDEFINITE).setAction("izin ver", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //izin isteme
                        permissonLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
                    }
                }).show();
            }
            else{
                //izin isteme
                permissonLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
        }
        else{
            //galeriy girmek için izin verilmiş demektir bu kod bloğuna giriyorsa java
            Intent intenToGallery=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            activityResultLauncher.launch(intenToGallery);

        }

    }
    private void registerLauncher(){
        activityResultLauncher=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if(result.getResultCode()==RESULT_OK){
                    //kullanıcı bir şey seçti demek
                    Intent intentFromResult=result.getData();
                    if(intentFromResult !=null){
                        //kullanıcının galeriden bir resime basıp basmadığını kotrol ediyoruz seçmışse bu kod bloğuna giriyor
                        Uri imageData=intentFromResult.getData();
                        try {
                            if (Build.VERSION.SDK_INT >= 28) {//appmiz sdkyi 28 veya 28 den büyük ise seçtiğimiz resim bu kod bloğuda göre bitmap cevrilip paneldeki imageView gelir
                                ImageDecoder.Source source=ImageDecoder.createSource(getContentResolver(),imageData);
                                selectedImage= ImageDecoder.decodeBitmap(source);
                                binding.imageView.setImageBitmap(selectedImage);
                            }else{
                                //appmiz sdkyi 28 veya 28 den küçük ise seçtiğimiz resim bu kod bloğuda göre bitmap cevrilip paneldeki imageView gelir
                                selectedImage=MediaStore.Images.Media.getBitmap(BookActivity.this.getContentResolver(),imageData);
                                binding.imageView.setImageBitmap(selectedImage);
                            }



                            //resim yüklenirken bir hata oluşursa uygulamanın çökmememisi sağlıycak ve console bir hata mesajı atıcak
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }

            }
        });
        permissonLauncher=registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
            @Override
            public void onActivityResult(Boolean result) {
                if(result){
                    //izin verildi
                    Intent intenToGallery=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    activityResultLauncher.launch(intenToGallery);
                }
                else{
                    //izin verilmedi
                    Toast.makeText(BookActivity.this,"Galeriye girmen için izne ihtiyacım var",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}