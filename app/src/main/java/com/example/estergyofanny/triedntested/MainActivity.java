package com.example.estergyofanny.triedntested;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.util.Linkify;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    EditText editTextEmailLogin;
    EditText editTextPasswordLogin;
    Button buttonToLogin;
    TextView textViewSignUp;

    Intent intent;
    SessionManager session;
    ConnectionManager cm;
    Boolean isInternetPresent = false;
    Profile prof;

    private String url = "http://sasmitoh.nitarahmawati.my.id/login_json.php";

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    public final static String TAG_USERNAME = "username";
    public final static String TAG_ID ="id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        session = new SessionManager(getApplicationContext());

        if (session.isLoggedIn()) {
            intent = new Intent(MainActivity.this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

        editTextEmailLogin = (EditText) findViewById(R.id.editTextEmailLogin);
        editTextPasswordLogin = (EditText) findViewById(R.id.editTextPasswordLogin);
        textViewSignUp = (TextView) findViewById(R.id.textViewSignUp);

        textViewSignUp.setPaintFlags(textViewSignUp.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        Linkify.addLinks(textViewSignUp, Linkify.ALL);

        buttonToLogin=(Button)findViewById(R.id.buttonToLogin);
        buttonToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = editTextEmailLogin.getText().toString().trim();
                String password = editTextPasswordLogin.getText().toString().trim();

                if(!username.isEmpty() && !password.isEmpty()){
                    cm = new ConnectionManager(MainActivity.this);
                    isInternetPresent = cm.isConnectingToInternet();

                    if (isInternetPresent){
                        // login user
                        loginUser(username, password);
                    }else {
                        Toast.makeText(MainActivity.this, "No connectivity. Please check your internet.", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(MainActivity.this, "Please enter all required fields.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        textViewSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("LoginActivity", "Sign Up Activity activated.");
                // this is where you should start the signup Activity
                MainActivity.this.startActivity(new Intent(MainActivity.this, RegisterActivity.class));
            }
        });
    }
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    private void loginUser(final String username, final String password){
        StringRequest request = new StringRequest(Request.Method.POST, getResources().getString(R.string.URL_LOGIN_JSON), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("response", response);
                UserCheckResponse responseModel = VolleyController.gson.fromJson(response, UserCheckResponse.class);
                //silakan diapa-apain ini responsenya :D
                if (responseModel.getSuccess() == 1) {
                    //Toast.makeText(MainActivity.this, responseModel.getMessage(), Toast.LENGTH_SHORT).show();
                    session.createLoginSession(username);
                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                    prof = new Profile(username, null, null, null, null);
                    intent.putExtra("prof_object", prof);
                    //intent.putExtra("prof_object", username);
                    finish();
                    startActivity(intent);
                }
                else{
                    Toast.makeText(MainActivity.this, responseModel.getMessage(), Toast.LENGTH_SHORT).show();
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
                return params;
            }
        };
        VolleyController.getInstance().getRequestQueue().add(request);
    }
}
