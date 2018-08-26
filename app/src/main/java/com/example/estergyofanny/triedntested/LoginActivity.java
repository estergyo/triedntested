package com.example.estergyofanny.triedntested;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    EditText editTextEmailLogin;
    EditText editTextPasswordLogin;
    Button buttonToLogin;

    Intent intent;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        session = new SessionManager(getApplicationContext());

        editTextEmailLogin = (EditText) findViewById(R.id.editTextEmailLogin);
        editTextPasswordLogin = (EditText) findViewById(R.id.editTextPasswordLogin);

        buttonToLogin=(Button)findViewById(R.id.buttonToLogin);
        buttonToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = openOrCreateDatabase("POS", MODE_PRIVATE, null);
                db.execSQL("Create table if not exists user(store_name VARCHAR, Nama VARCHAR, email VARCHAR, password VARCHAR);");
                Cursor uname = db.rawQuery("select * from user where email = '" + editTextEmailLogin.getText() + "' ", null);
                if (uname.getCount() == 0) {
                    Toast.makeText(LoginActivity.this, "email tidak terdaftar", Toast.LENGTH_SHORT).show();
                } else {
                    Cursor passwd = db.rawQuery("select password from user where email = '" + editTextEmailLogin.getText() + "' ", null);
                    if (passwd.moveToFirst()) {
                        if (passwd.getString(0).equals(editTextPasswordLogin.getText().toString())) {

                            session.createLoginSession(editTextEmailLogin.getText().toString());

                            intent = new Intent(LoginActivity.this, HomeActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "password salah", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
    }
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
