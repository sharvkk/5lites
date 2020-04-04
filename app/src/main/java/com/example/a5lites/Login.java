package com.example.a5lites;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.dynamodbv2.document.datatype.Document;

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
                    try {
                        prefs = getSharedPreferences(CREDENTIALS, Context.MODE_PRIVATE);
                        prefs.edit().putBoolean(LOG_CHECK_KEY, true).apply();
                        prefs.edit().putString(PHONE_NO, num).apply();
                        prefs.edit().putString(MAC, macadd).apply();
                        Document document = new Document();
                        document.put("MACid", macadd);
                        document.put("status", "negative");
                        document.put("phone", num);
                        Integer temp = new PutDataAsyncTask().execute(document).get();
                        System.out.println("O/P: " + temp);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                        Toast.makeText(getApplicationContext(),"number saved",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(intent);
                        finish();
                }
            }
        });
    }
    private class GetAllItemsAsyncTask extends AsyncTask<String, Void, com.amazonaws.mobileconnectors.dynamodbv2.document.datatype.Document> {
        @Override
        protected com.amazonaws.mobileconnectors.dynamodbv2.document.datatype.Document doInBackground(String... params) {
            DatabaseAccess databaseAccess = DatabaseAccess.getInstance(Login.this);
            return databaseAccess.getItem(params[0]);
        }

        @Override
        protected void onPostExecute(com.amazonaws.mobileconnectors.dynamodbv2.document.datatype.Document documents) {
        }

    }

    private class PutDataAsyncTask extends AsyncTask<Document, Void, Integer> {
        @Override
        protected Integer doInBackground(Document... params) {
            DatabaseAccess databaseAccess = DatabaseAccess.getInstance(Login.this);
            databaseAccess.putItem(params[0]);
            System.out.println("In background!!!");
            return 1;
        }

        @Override
        protected void onPostExecute(Integer integer) {
        }

    }
}
