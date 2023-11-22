package com.example.equinox;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    public static String username;
    public static Boolean login = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
    }
    public void back(View view){
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(intent);
    }
    public void next(View view){
        EditText user = (EditText) findViewById(R.id.editText1);
        EditText pass = (EditText) findViewById(R.id.editText2);
        username = user.getText().toString();
        String password = pass.getText().toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://lamp.ms.wits.ac.za/~s2427724/login.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);

                            if(jsonObject.getString("message").equals("Successfully logged in!")){
                                if (jsonObject.getString("yn").equals("{\"Counsellor\":\"YES\"}")){
                                    if (jsonObject.getString("message").equals("Login failed")){
                                        Toast.makeText(LoginActivity.this, "Incorrect credentials",Toast.LENGTH_LONG).show();
                                    }
                                    else if(jsonObject.getString("message").equals("Successfully logged in!")){
                                        Toast.makeText(LoginActivity.this, jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
                                        Toast.makeText(LoginActivity.this, "Welcome back " + username + "!",Toast.LENGTH_LONG).show();
                                        login = true;
                                        Intent intent = new Intent(LoginActivity.this, PatientChatListActivity.class);
                                        startActivity(intent);
                                    }
                                }
                                else if (jsonObject.getString("yn").equals("{\"Counsellor\":\"NO\"}")){
                                    if (jsonObject.getString("message").equals("Login failed")){
                                        Toast.makeText(LoginActivity.this, "Incorrect credentials",Toast.LENGTH_LONG).show();
                                    }
                                    else if(jsonObject.getString("message").equals("Successfully logged in!")){
                                        Toast.makeText(LoginActivity.this, jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
                                        Toast.makeText(LoginActivity.this, "Welcome back " + username + "!",Toast.LENGTH_LONG).show();
                                        login = true;
                                        Intent intent = new Intent(LoginActivity.this, PatientChatActivity.class);
                                        startActivity(intent);
                                    }
                                }
                            }
                            else{
                                Toast.makeText(LoginActivity.this, "Incorrect login details/ not registered",Toast.LENGTH_LONG).show();
                            }
                        }
                        catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(LoginActivity.this, "An error occured, please try again",Toast.LENGTH_LONG).show();
                    }
                }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("password", password);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}