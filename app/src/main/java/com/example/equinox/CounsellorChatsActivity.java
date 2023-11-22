package com.example.equinox;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
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

public class CounsellorChatsActivity extends AppCompatActivity {
    Boolean login = LoginActivity.login;

    String patient_username;
    String counsellor_username;

    ArrayList<String> messages = new ArrayList<>();
    ArrayList<String> message_ids = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counsellor_chats);
        getSupportActionBar().hide();

        patient_username =  PatientChatListActivity.Patient_username_display;
        //checks if they are logging in or are registering to know which activity to get the username
        login = LoginActivity.login;
        if (login){
            counsellor_username = LoginActivity.username;
        }
        else{
            counsellor_username = RegisterActivity.username;
        }
        //gets patients name to display in chat
        getPatient();

        //checks when the send button is pressed
        findViewById(R.id.layoutSend).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendCurrentMessage();
                displayMessages();
            }
        });
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CounsellorChatsActivity.this,PatientChatListActivity.class);
                startActivity(intent);
            }
        });
        //checks when they click the info button to take them to a new activity
        findViewById(R.id.info).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CounsellorChatsActivity.this, ShowInfoActivity.class);
                intent.putExtra("patient_username", patient_username);
                intent.putExtra("counsellor_username", counsellor_username);
                intent.putExtra("id", "C");
                startActivity(intent);
            }
        });
    }

    public void getPatient(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://lamp.ms.wits.ac.za/~s2427724/messages1.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        setPatientName(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(CounsellorChatsActivity.this, error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("counsellor_username", counsellor_username);
                params.put("patient_username", patient_username);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void setPatientName(String json){
        //displays the counsellors name on the top of the chat
        TextView patient_username_display = findViewById(R.id.textName);
        patient_username_display.setText(patient_username);
        //person checks who sent the message
        String message_id;
        String message;

        if(login == true){
            try {
                JSONArray jsonArray = new JSONArray(json);

                if(jsonArray.length() == 0){
                    //do nothing
                }
                else{
                    JSONObject jsonObject = jsonArray.getJSONObject(jsonArray.length()-1);

                    for (int i = 0; i <jsonArray.length(); i++){
                        jsonObject = jsonArray.getJSONObject(i);
                        message_id = jsonObject.getString("Person");
                        message = jsonObject.getString("Message");
                        messages.add(message);
                        message_ids.add(message_id);
                    }
                    displayMessages();
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void displayMessages(){
        RecyclerView recyclerView = findViewById(R.id.chatRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        AdapterCounsellor adapter = new AdapterCounsellor(messages, message_ids);
        recyclerView.setAdapter(adapter);

        adapter.notifyItemInserted(messages.size() - 1);
        recyclerView.scrollToPosition(adapter.getItemCount() - 1);

        //reloads the messages to display new messages
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                messages.clear();
                message_ids.clear();
                getPatient();
            }
        }, 5000);
    }

    public void sendCurrentMessage(){
        EditText currentMessage = findViewById(R.id.typeMessage);

        //checks if there is a message to send
        if(currentMessage.getText().toString().equals("")){
            //don't send the empty message
        }
        else{
            messages.add(currentMessage.getText().toString());
            message_ids.add("C");
            sendMessageDatabase(currentMessage.getText().toString());

            currentMessage.setText("");
        }
    }

    //Posts the messages to the database
    public void sendMessageDatabase(String message){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://lamp.ms.wits.ac.za/~s2427724/postmessageC.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //refresh the patients chat
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(CounsellorChatsActivity.this, error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("patient_username", patient_username);
                params.put("counsellor_username", counsellor_username);
                params.put("message", message);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}