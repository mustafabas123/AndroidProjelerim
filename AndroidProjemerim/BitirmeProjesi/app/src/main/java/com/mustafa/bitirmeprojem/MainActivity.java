package com.mustafa.bitirmeprojem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button HesapVar,YeniHesap;

    public void init(){
        HesapVar=(Button) findViewById(R.id.BtnHesap);
        YeniHesap=(Button) findViewById(R.id.BtnYeniHesap);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        HesapVar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentGiris=new Intent(MainActivity.this,GirisActivity.class);
                startActivity(intentGiris);
            }
        });
        YeniHesap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentKayit=new Intent(MainActivity.this,KayitEkraniActivity.class);
                startActivity(intentKayit);
            }
        });
    }
}