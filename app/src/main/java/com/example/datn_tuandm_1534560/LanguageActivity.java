package com.example.datn_tuandm_1534560;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import static com.example.datn_tuandm_1534560.ConstantVariables.LAN_EN;
import static com.example.datn_tuandm_1534560.ConstantVariables.LAN_VI;
import static com.example.datn_tuandm_1534560.MainActivity.LANGUAGE;

import java.util.Locale;

public class LanguageActivity extends AppCompatActivity {
    RadioGroup rg;
    RadioButton vi,en;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (LANGUAGE.equals(LAN_VI)){
            changeLanguage1(LAN_VI);
        }
        if (LANGUAGE.equals(LAN_EN)){
            changeLanguage1(LAN_EN);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.language);
        rg = findViewById(R.id.radioGroup);
        vi = findViewById(R.id.Vietnamese);
        en = findViewById(R.id.English);
        //kiem tra ngon ngu ban dau
        final Locale current = getResources().getConfiguration().locale;
        if (LANGUAGE.equals(LAN_VI)){
            vi.setChecked(true);
        }
        if (LANGUAGE.equals(LAN_EN)){
            en.setChecked(true);
        }
        //thay doi ngon ngu
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.Vietnamese){
                    changeLanguage(LAN_VI);
                    LANGUAGE = LAN_VI;
                }
                if (i == R.id.English){
                    changeLanguage(LAN_EN);
                    LANGUAGE = LAN_EN;
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
    public void changeLanguage1(String language){
        Locale locale = new Locale(language);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getBaseContext().getResources().updateConfiguration(configuration,getBaseContext().getResources().getDisplayMetrics());
    }
}
