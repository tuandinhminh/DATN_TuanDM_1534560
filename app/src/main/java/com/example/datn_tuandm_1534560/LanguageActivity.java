package com.example.datn_tuandm_1534560;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.Locale;

public class LanguageActivity extends AppCompatActivity {
    RadioGroup rg;
    RadioButton vi,en;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.language);
        rg = findViewById(R.id.radioGroup);
        vi = findViewById(R.id.Vietnamese);
        en = findViewById(R.id.English);
        //kiem tra ngon ngu ban dau
        final Locale current = getResources().getConfiguration().locale;
        if (current.toString().contains("vi")){
            vi.setChecked(true);
        }
        if (current.toString().contains("en")){
            en.setChecked(true);
        }
        //thay doi ngon ngu
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.Vietnamese){
                    changeLanguage("vi");
                }
                if (i == R.id.English){
                    changeLanguage("en");
                    Toast.makeText(LanguageActivity.this, current.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    //ham thay doi ngon ngu
    public void changeLanguage(String language){
        Locale locale = new Locale(language);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getBaseContext().getResources().updateConfiguration(configuration,getBaseContext().getResources().getDisplayMetrics());
        Intent intent = new Intent(this,LanguageActivity.class);
        startActivity(intent);
    }
}
