package com.example.equinox;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ProblemsActivity extends AppCompatActivity {
    public static String patient_username;

    String[] patient_problems;
    ArrayList<String> counsellors = new ArrayList<>();
    ArrayList<String> counsellor_problems = new ArrayList<>();
    ArrayList<Integer> no_patients = new ArrayList<>();
    ArrayList<Integer> no_patients_temp = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problems);
        getSupportActionBar().hide();

    }

    public void next_problems(View view){
        assignPatientProblems();
    }

    public void assignPatientProblems(){
        CheckBox checkBox1 = findViewById(R.id.checkBox1);
        CheckBox checkBox2 = findViewById(R.id.checkBox2);
        CheckBox checkBox3 = findViewById(R.id.checkBox3);
        CheckBox checkBox4 = findViewById(R.id.checkBox4);
        CheckBox checkBox5 = findViewById(R.id.checkBox5);
        CheckBox checkBox6 = findViewById(R.id.checkBox6);

        String problems = "";
        patient_username = RegisterActivity.username;

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
                            firstRequest();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(ProblemsActivity.this, error.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }){
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("username", patient_username);
                    params.put("problems", finalProblems);
                    params.put("yn", "NO");
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        }
        else{
            Toast.makeText(ProblemsActivity.this, "Please select at least one option",Toast.LENGTH_LONG).show();
        }
    }

    public void firstRequest(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://lamp.ms.wits.ac.za/~s2427724/assignCounsellor.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        collectCounsellorDetails(response);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ProblemsActivity.this, "An error occurred",Toast.LENGTH_LONG).show();
                    }
                }){
            @Nullable
            @Override

            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username", patient_username);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
        secondRequest();
    }

    public void secondRequest(){
        StringRequest stringRequest1 = new StringRequest(Request.Method.POST, "https://lamp.ms.wits.ac.za/~s2427724/assignCounsellor1.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        collectPatientProblems(response);

                        checkCriteria();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ProblemsActivity.this, "An error occurred",Toast.LENGTH_LONG).show();
                    }
                }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username", patient_username);
                return params;
            }
        };
        RequestQueue requestQueue1 = Volley.newRequestQueue(this);
        requestQueue1.add(stringRequest1);
    }

    public void collectCounsellorDetails(String json){
        try {
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i<jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                String coun_username = jsonObject.getString("Counsellor_Username");
                String problems = jsonObject.getString("Problem");
                String no = jsonObject.getString("No_patients");

                counsellors.add(coun_username);
                counsellor_problems.add(problems);
                no_patients.add(Integer.parseInt(no));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void collectPatientProblems(String json){
        try {
            JSONArray jsonArray = new JSONArray(json);
            JSONObject jsonObject = jsonArray.getJSONObject(jsonArray.length()-1);
            patient_problems = jsonObject.getString("Problem").split(",");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void checkCriteria(){
        ArrayList<Boolean> truth_values = new ArrayList<>();
        ArrayList<Boolean> temp = new ArrayList<>();
        for(int i = 0; i<counsellor_problems.size(); i++){
            for(int j = 0; j<patient_problems.length; j++){
                temp.add(counsellor_problems.get(i).contains(patient_problems[j]));
            }
            Boolean truth_value = true;
            for(int z = 0; z<patient_problems.length; z++){
                if(temp.get(z) == false){
                    truth_value = false;
                }
            }
            truth_values.add(truth_value);
            temp.clear();
        }

        for (int i = 0; i < no_patients.size(); i++){
            if (truth_values.get(i) == true){
                no_patients_temp.add(no_patients.get(i));
            }
        }

        Collections.sort(no_patients_temp);
        int no = no_patients_temp.get(0);
        String num = String.valueOf(no+1);

        for(int i = 0; i<no_patients.size(); i++){
            if(no_patients.get(i) == no && truth_values.get(i) == true){
                assignCounsellor(counsellors.get(i), num);
                break;
            }
        }
    }

    public void assignCounsellor(String counsellor, String number){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://lamp.ms.wits.ac.za/~s2427724/assignCounsellor2.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Intent intent = new Intent(ProblemsActivity.this, PatientChatActivity.class);
                        startActivity(intent);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ProblemsActivity.this, "An error occurred",Toast.LENGTH_LONG).show();
                    }
                }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("counsellor_username", counsellor);
                params.put("patient_username", patient_username);
                params.put("no_patients", number);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}