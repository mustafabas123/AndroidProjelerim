package com.mustafa.bitirmeprojem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class KayitEkraniActivity extends AppCompatActivity {

    EditText userName,parola,reparola;
    Button Btn;

    DB Db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kayit_ekrani);

        userName=findViewById(R.id.txtUserName);
        parola=findViewById(R.id.txtParola);
        reparola=findViewById(R.id.txtRepassword);
        Btn=findViewById(R.id.BtnYeniHesapEkle);
        Db =new DB(this);

        Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user=userName.getText().toString();
                String pass=parola.getText().toString();
                String repass=reparola.getText().toString();
                if(user.equals(" ") || pass.equals(" ") || reparola.equals(" ")){
                    Toast.makeText(KayitEkraniActivity.this,"Bu alanlar boş gecilemez",Toast.LENGTH_SHORT).show();
                }
                else{
                    if(pass.equals(repass)){
                        Boolean checkUser=Db.checkusername(user);
                        if(checkUser==false){
                            Boolean insert=Db.insertData(user,pass);
                            if(insert==true){
                                Toast.makeText(KayitEkraniActivity.this,"Kayıt başarı ile gerçekleşti",Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(KayitEkraniActivity.this,GirisActivity.class);
                                startActivity(intent);
                            }else{
                                Toast.makeText(KayitEkraniActivity.this,"Kayıt gercekleşmedi ",Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(KayitEkraniActivity.this,"Kullanıcı zaten var",Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(KayitEkraniActivity.this,"Girdiğiniz parolalar uyuşmuyor",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}