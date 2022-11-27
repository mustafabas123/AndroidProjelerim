package com.mustafa.kursodev2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    EditText kenar1,kenar2,kenar3;
    Button hesapla;
    TextView sonuc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        kenar1=(EditText) findViewById(R.id.txtKenar1);
        kenar2=(EditText) findViewById(R.id.txtKenar2);
        kenar3=(EditText) findViewById(R.id.txtKenar3);
        sonuc=(TextView) findViewById(R.id.txtSonuc);
        hesapla=(Button) findViewById(R.id.btnHesapla);

        hesapla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int k1=Integer.parseInt(kenar1.getText().toString());
                int k2=Integer.parseInt(kenar2.getText().toString());
                int k3=Integer.parseInt(kenar3.getText().toString());
                double u=(k1+k2+k3)/2.0;
                double alan=Math.sqrt(u*(u-k1)*(u-k2)*(u-k3));
                sonuc.setText("Cevap:"+alan);
            }
        });
    }
}