package com.example.equinox;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
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

public class PatientChatListActivity extends AppCompatActivity implements viewHolderChatList.OnChatListener {
    Boolean login = LoginActivity.login;
    String counsellor_username;
    ArrayList<String> patient_names = new ArrayList<>();
    static String Patient_username_display;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_chat_list);
        getSupportActionBar().hide();

        login = LoginActivity.login;
        if (login){
            counsellor_username = LoginActivity.username;
        }
        else{
            counsellor_username= RegisterActivity.username;
        }
        getPatientNames();
    }

    public void getPatientNames(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://lamp.ms.wits.ac.za/~s2427724/getpatientnames.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject = jsonArray.getJSONObject(jsonArray.length()-1);

                            for (int i = 0; i <jsonArray.length(); i++){
                                jsonObject = jsonArray.getJSONObject(i);
                                patient_names.add(jsonObject.getString("Patient_Username"));
                            }

                            displayUsers(patient_names);
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(PatientChatListActivity.this, error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("counsellor_username", counsellor_username);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void displayUsers(ArrayList<String> patient_names){
        RecyclerView recyclerView = findViewById(R.id.chatListRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        AdapterChatList adapter = new AdapterChatList(patient_names, this);
        recyclerView.setAdapter(adapter);

        adapter.notifyItemInserted(patient_names.size() - 1);
    }

    @Override
    public void onChatClick(int position) {
        patient_names.get(position);
        Patient_username_display = patient_names.get(position);
        Intent intent = new Intent(this, CounsellorChatsActivity.class);
        startActivity(intent);
    }
}