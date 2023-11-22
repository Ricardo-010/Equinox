package com.example.equinox;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.telecom.Call;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class RegisterActivity extends AppCompatActivity {
    public static String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();
    }

    public void back(View view){
        Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
        startActivity(intent);
    }

    public void next_register(View view){
        RadioButton patient_button = (RadioButton) findViewById(R.id.patient);
        RadioButton counsellor_button = (RadioButton) findViewById(R.id.counsellor);

        EditText usernameEditText = (EditText) findViewById(R.id.editTextTextPersonName5);
        EditText password = (EditText) findViewById(R.id.editTextTextPassword3);
        EditText confirm_password = (EditText) findViewById(R.id.editTextTextPassword4);
        username = usernameEditText.getText().toString();
        String password1 = password.getText().toString();
        String confirm_password1 = confirm_password.getText().toString();

        String counsellor = "";
        if (patient_button.isChecked()){
            counsellor = "NO";
        }
        if (counsellor_button.isChecked()){
            counsellor = "YES";
        }

        if(password1.equals(confirm_password1) && password1.length() != 0 && confirm_password1.length() != 0 && username.length() != 0 && (counsellor_button.isChecked() || patient_button.isChecked())){
            registerUser(username, password1, counsellor, patient_button, counsellor_button);
        }
        else{
            Toast.makeText(RegisterActivity.this, "Please fill in all of the above fields correctly", Toast.LENGTH_LONG).show();
        }
    }

    private void registerUser(String username, String password1, String counsellor, RadioButton patient_button, RadioButton counsellor_button){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://lamp.ms.wits.ac.za/~s2427724/register.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            Toast.makeText(RegisterActivity.this, jsonObject.getString("message"),Toast.LENGTH_LONG).show();

                            if (jsonObject.getString("message").equals("This username is already in use")){

                            }
                            if(jsonObject.getString("message").equals("You have successfully registered!")){
                                if(patient_button.isChecked()){
                                    Intent intent = new Intent(RegisterActivity.this, ProblemsActivity.class);
                                    startActivity(intent);
                                }
                                if(counsellor_button.isChecked()){
                                    Intent intent = new Intent(RegisterActivity.this, CProblemsActivity.class);
                                    startActivity(intent);
                                }
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
                        Toast.makeText(RegisterActivity.this, error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("password", password1);
                params.put("yn", counsellor);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}