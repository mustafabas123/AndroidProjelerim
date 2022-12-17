package com.mustafa.bitirmeprojem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class GirisActivity extends AppCompatActivity {
    EditText KullaniciAdi;
    EditText sifre;
    Button giris;
    DB Db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giris);
        KullaniciAdi=(EditText) findViewById(R.id.txtUserName);
        sifre=(EditText) findViewById(R.id.txtParola);
        Db=new DB(this);

        giris=(Button) findViewById(R.id.BtnLogin);
        giris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user=KullaniciAdi.getText().toString();
                String pass=sifre.getText().toString();

                if(user.equals(" ") || pass.equals(" ")){
                    Toast.makeText(GirisActivity.this,"Zorunlu alanları doldurun lütfen",Toast.LENGTH_SHORT).show();
                }
                else{
                    Boolean checkuserpass=Db.checkUsernameOrPassword(user,pass);
                    if(checkuserpass==true){
                        Toast.makeText(GirisActivity.this,"Giriş başarı ile gerçekleşti",Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(GirisActivity.this,AnaSayfa.class);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(GirisActivity.this,"Griş gerçekleşmedi",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }
}