package com.mustafa.kursodev4;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private RadioGroup rG;
    private RadioButton rB1;
    private RadioButton rB2;
    private RadioButton rB3;
    private RadioButton rB4;
    private RadioButton bt;
    private Button Gonder;
    private TextView sonuc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rG=(RadioGroup) findViewById(R.id.RadioGroup);
        Gonder=(Button) findViewById(R.id.button);
        sonuc=(TextView) findViewById(R.id.txtSonuc);

        Gonder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int secilenId=rG.getCheckedRadioButtonId();
                bt=(RadioButton) findViewById(secilenId);
                switch (secilenId){
                    case R.id.radioButton1:
                        sonuc.setText("Yanlış cevap verdiniz");
                        break;
                    case R.id.radioButton2:
                        sonuc.setText("Yanlış cevap verdiniz");
                        break;
                    case R.id.radioButton3:
                        sonuc.setText("Doğru cevap 10 puan kazandınız");
                        break;
                    case R.id.radioButton4:
                        sonuc.setText("Yanlış cevap verdiniz");
                        break;
                }
            }
        });

    }
}