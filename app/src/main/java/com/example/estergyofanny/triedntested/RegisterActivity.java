package com.example.estergyofanny.triedntested;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import static com.example.estergyofanny.triedntested.MainActivity.TAG_USERNAME;

public class RegisterActivity extends AppCompatActivity {
    EditText editTextNamaRegister;
    EditText editTextPasswordRegister;
    EditText editTextConfirmPasswordRegister;
    Button buttonDaftar;
    SessionManager session;
    ConnectionManager cm;
    Boolean isInternetPresent = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Remove default title text
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        // Get access to the custom title view
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText("Registrasi");

        session = new SessionManager(getApplicationContext());

        editTextNamaRegister = (EditText) findViewById(R.id.editTextNamaRegister);
        editTextPasswordRegister = (EditText) findViewById(R.id.editTextPasswordRegister);
        editTextConfirmPasswordRegister = (EditText) findViewById(R.id.editTextConfirmPasswordRegister);


        buttonDaftar = (Button) findViewById(R.id.buttonDaftar);

        buttonDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            final String username = editTextNamaRegister.getText().toString().trim();
            final String password = editTextPasswordRegister.getText().toString().trim();
            final String confirm_password = editTextPasswordRegister.getText().toString().trim();

            if(!username.isEmpty() && !password.isEmpty() && !confirm_password.isEmpty()){
                cm = new ConnectionManager(RegisterActivity.this);
                isInternetPresent = cm.isConnectingToInternet();

                if (isInternetPresent){
                    // register user
                    registerUser(username, password, confirm_password);
                }else {
                    Toast.makeText(RegisterActivity.this, "No connectivity. Please check your internet.", Toast.LENGTH_SHORT).show();
                }
            }
            else {
                Toast.makeText(RegisterActivity.this, "Please enter all required fields.", Toast.LENGTH_SHORT).show();
            }
            }
        });
    }

    public void registerUser(final String username, final String password, final String confirm_password) {

        StringRequest request = new StringRequest(Request.Method.POST, getResources().getString(R.string.URL_REGISTER_JSON), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("response", response);
                UserCheckResponse responseModel = VolleyController.gson.fromJson(response, UserCheckResponse.class);
                //silakan diapa-apain ini responsenya :D
                if (responseModel.getSuccess() == 1) {
                    //Toast.makeText(RegisterActivity.this, responseModel.getMessage(), Toast.LENGTH_SHORT).show();
                    session.createLoginSession(username);
                    Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
                    Profile prof = new Profile(username, null, null, null, null);
                    intent.putExtra("prof_object", prof);
                    finish();
                    startActivity(intent);
                } else {
                    Toast.makeText(RegisterActivity.this, responseModel.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse != null && error.networkResponse.data != null) {
                    //kalo butuh status codenya
                    Log.e("Error code", String.valueOf(error.networkResponse.statusCode));
                    Log.e("Error", new String(error.networkResponse.data));
                }
            }
        })
        {
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", username);
                params.put("password", password);
                params.put("confirm_password", confirm_password);
                return params;

            }
        };
        VolleyController.getInstance().getRequestQueue().add(request);

    }
}
