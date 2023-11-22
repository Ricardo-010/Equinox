package com.example.equinox;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class CProblemsActivity extends AppCompatActivity {
    public static String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cproblems);
        getSupportActionBar().hide();

        findViewById(R.id.imageButton3).setOnClickListener(view -> {
            next_problemsC();
        });
    }

    public void next_problemsC(){
        CheckBox checkBox1 = (CheckBox) findViewById(R.id.checkBox1);
        CheckBox checkBox2 = (CheckBox) findViewById(R.id.checkBox2);
        CheckBox checkBox3 = (CheckBox) findViewById(R.id.checkBox3);
        CheckBox checkBox4 = (CheckBox) findViewById(R.id.checkBox4);
        CheckBox checkBox5 = (CheckBox) findViewById(R.id.checkBox5);
        CheckBox checkBox6 = (CheckBox) findViewById(R.id.checkBox6);

        String problems = "";
        username = RegisterActivity.username;

        if (checkBox1.isChecked() || checkBox2.isChecked() || checkBox3.isChecked() || checkBox4.isChecked() || checkBox5.isChecked() || checkBox6.isChecked()){
            if(checkBox1.isChecked()){
                problems = problems + "," + checkBox1.getText().toString();
            }
            if(checkBox2.isChecked()){
                problems = problems + "," + checkBox2.getText().toString();
            }
            if(checkBox3.isChecked()){
                problems = problems + "," + checkBox3.getText().toString();
            }
            if(checkBox4.isChecked()){
                problems = problems + "," + checkBox4.getText().toString();
            }
            if(checkBox5.isChecked()){
                problems = problems + "," + checkBox5.getText().toString();
            }
            if(checkBox6.isChecked()){
                problems = problems + "," + checkBox6.getText().toString();
            }
            problems = problems.substring(1,problems.length());
            String finalProblems = problems;

            StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://lamp.ms.wits.ac.za/~s2427724/problems.php",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Intent intent = new Intent(CProblemsActivity.this, PatientChatListActivity.class);
                            startActivity(intent);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(CProblemsActivity.this, error.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }){
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("username", username);
                    params.put("problems", finalProblems);
                    params.put("yn", "YES");
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        }
        else{
            Toast.makeText(CProblemsActivity.this, "Please select at least one option",Toast.LENGTH_LONG);
        }
    }
}