package com.example.a5lites;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends AppCompatActivity {
    Button mButton;
    EditText mnum,mmac;
    private SharedPreferences prefs;
    private String PHONE_NO = "Phone";
    private String MAC = "Mac_add";
    static final String LOG_CHECK_KEY = "logged";
    static final String CREDENTIALS = "Credentials";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        prefs = getSharedPreferences(CREDENTIALS,Context.MODE_PRIVATE);
        if(prefs.getBoolean(LOG_CHECK_KEY,false)){
            Intent intent = new Intent(Login.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        mButton = findViewById(R.id.save);
        mnum = findViewById(R.id.num);
        mmac = findViewById(R.id.umac);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String num = mnum.getText().toString();
                String macadd = mmac.getText().toString();
                if(num.length() == 10 && !macadd.equals("")){
                   prefs = getSharedPreferences(CREDENTIALS,Context.MODE_PRIVATE);
                    prefs.edit().putBoolean(LOG_CHECK_KEY, true).apply();
                    prefs.edit().putString(PHONE_NO,num).apply();
                    prefs.edit().putString(MAC,macadd).apply();
                    Toast.makeText(getApplicationContext(),"number saved",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}
