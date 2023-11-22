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
import android.widget.LinearLayout;
import android.widget.ScrollView;
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
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class PatientChatActivity extends AppCompatActivity {
    Boolean login = LoginActivity.login;

    String patient_username;
    String counsellor_username;

    ArrayList<String> messages = new ArrayList<>();
    ArrayList<String> message_ids = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_chat);
        getSupportActionBar().hide();

        //checks if they are logging in or are registering to know which activity to get the username
        login = LoginActivity.login;
        if (login){
            patient_username = LoginActivity.username;
        }
        else{
            patient_username = RegisterActivity.username;
        }

        //gets counsellor name to display in chat
        getCounsellor(patient_username);

        //checks when the send button is pressed
        findViewById(R.id.layoutSend).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendCurrentMessage();
                displayMessages();
            }
        });
        //checks when they click the info button to take them to a new activity
        findViewById(R.id.info).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PatientChatActivity.this, ShowInfoActivity.class);
                intent.putExtra("patient_username", patient_username);
                intent.putExtra("counsellor_username", counsellor_username);
                intent.putExtra("id", "P");
                startActivity(intent);
            }
        });
    }

    public void getCounsellor(String username){
        patient_username = username;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://lamp.ms.wits.ac.za/~s2427724/messages.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        setCounsellorName(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(PatientChatActivity.this, error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("patient_username", patient_username);
                params.put("login", login.toString());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void setCounsellorName(String json){
        //displays the counsellors name on the top of the chat
        TextView counsellor_name = findViewById(R.id.textName);
        //person checks who sent the message
        String message_id;
        String message;

        if(login == true){
            try {
                JSONArray jsonArray = new JSONArray(json);

                if(jsonArray.length() == 0){
                    JSONObject jsonObject = jsonArray.getJSONObject(jsonArray.length()-1);
                    counsellor_name.setText(jsonObject.getString("Counsellor_Username"));
                    counsellor_username = jsonObject.getString("Counsellor_Username");
                }
                else{
                    JSONObject jsonObject = jsonArray.getJSONObject(jsonArray.length()-1);
                    counsellor_name.setText(jsonObject.getString("Counsellor_Username"));
                    counsellor_username = jsonObject.getString("Counsellor_Username");

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
        else{
            try {
                JSONArray jsonArray = new JSONArray(json);
                JSONObject jsonObject = jsonArray.getJSONObject(jsonArray.length()-1);
                counsellor_name.setText(jsonObject.getString("Counsellor_Username"));
                counsellor_username = jsonObject.getString("Counsellor_Username");

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void displayMessages(){
        RecyclerView recyclerView = findViewById(R.id.chatRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        AdapterPatient adapter = new AdapterPatient(messages, message_ids);
        recyclerView.setAdapter(adapter);

        adapter.notifyItemInserted(messages.size() - 1);
        recyclerView.scrollToPosition(adapter.getItemCount() - 1);

        //reloads the messages to display new messages
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                messages.clear();
                message_ids.clear();
                getCounsellor(patient_username);
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
            message_ids.add("P");
            sendMessageDatabase(currentMessage.getText().toString());
            currentMessage.setText("");
        }
    }

    //Posts the messages to the database
    public void sendMessageDatabase(String message){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://lamp.ms.wits.ac.za/~s2427724/postmessageP.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //refresh the counsellors chat
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(PatientChatActivity.this, error.getMessage(),Toast.LENGTH_LONG).show();
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