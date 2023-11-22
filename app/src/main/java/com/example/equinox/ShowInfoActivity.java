package com.example.equinox;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ShowInfoActivity extends AppCompatActivity {

    private String patient_username;
    private String counsellor_username;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_info);
        getSupportActionBar().hide();

        //gets all the relevant info to send a Post request to obtain the users respective problems
        Bundle info = getIntent().getExtras();
        patient_username = info.getString("patient_username");
        counsellor_username = info.getString("counsellor_username");
        id = info.getString("id");
        showInfo();
        TextView textView = findViewById(R.id.text1);
        if(id.equals("P")){
            textView.setText("This counsellor specializes in:");
        }
        else{
            textView.setText("This patient needs help with:");
        }

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                if(id.equals("P")){
                    intent = new Intent(ShowInfoActivity.this, PatientChatActivity.class);
                }
                else{
                    intent = new Intent(ShowInfoActivity.this, CounsellorChatsActivity.class);
                }
                startActivity(intent);
            }
        });
    }

    public void showInfo(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://lamp.ms.wits.ac.za/~s2427724/showinfo.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        displayProblems(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ShowInfoActivity.this, error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("patient_username", patient_username);
                params.put("counsellor_username", counsellor_username);
                params.put("id", id);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void displayProblems(String response){
        TextView textView = findViewById(R.id.problems);
        try {
            JSONArray jsonArray = new JSONArray(response);
            JSONObject jsonObject = jsonArray.getJSONObject(jsonArray.length()-1);

            String[] problems = jsonObject.getString("Problem").split(",");
            String issues = "";
            for (int i = 0; i<problems.length; i++){
                if (i == problems.length - 1){
                    issues = issues + problems[i] ;
                }
                else{
                    issues = issues + problems[i] + " | ";
                }
            }
            textView.setText(issues);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        //JSONObject jsonObject = jsonArray.getJSONObject(jsonArray.length()-1);

    }
}