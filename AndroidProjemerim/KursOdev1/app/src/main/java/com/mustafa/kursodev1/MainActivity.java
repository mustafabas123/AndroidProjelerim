package com.mustafa.kursodev1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    EditText sayi1,sayi2;
    Button btnTopla,btnCikarma,btnCarpma,btnBolme;
    TextView txtSonuc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sayi1=(EditText)findViewById(R.id.textSayi1);
        sayi2=(EditText) findViewById(R.id.textSayi2);

        btnTopla=(Button)findViewById(R.id.btnTopla);
        btnCikarma=(Button) findViewById(R.id.btnCikarma);
        btnBolme=(Button) findViewById(R.id.btnBolme);
        btnCarpma=(Button) findViewById(R.id.btnCarpma);

        txtSonuc=(TextView)findViewById(R.id.textSonuc);


        btnTopla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int s1=Integer.parseInt(sayi1.getText().toString());
                int s2=Integer.parseInt(sayi2.getText().toString());

                int sonuc=s1+s2;

                txtSonuc.setText("Sonuc:"+sonuc);
            }
        });
        btnCikarma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int s1=Integer.parseInt(sayi1.getText().toString());
                int s2=Integer.parseInt(sayi2.getText().toString());
                int sonuc=s1-s2;
                txtSonuc.setText("Sonuc:"+sonuc);
            }
        });
        btnCarpma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int s1=Integer.parseInt(sayi1.getText().toString());
                int s2=Integer.parseInt(sayi2.getText().toString());
                int sonuc=s1*s2;
                txtSonuc.setText("Sonuc:"+sonuc);
            }
        });
        btnBolme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int s1=Integer.parseInt(sayi1.getText().toString());
                int s2=Integer.parseInt(sayi2.getText().toString());
                double sonuc=s1/s2;
                txtSonuc.setText("Sonuc:"+sonuc);
            }
        });

    }

}