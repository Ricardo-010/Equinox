package com.example.equinox;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterPatient extends RecyclerView.Adapter<viewHolder> {

    ArrayList<String> message;
    ArrayList<String> people;

    public static final int VIEW_TYPE_SENT = 1;
    public static final int VIEW_TYPE_RECEIVED = 2;

    //A constructor to collect the message being sent
    public AdapterPatient(ArrayList<String> messages, ArrayList<String> people) {
        this.message = messages;
        this.people = people;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == VIEW_TYPE_SENT){
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.message_sent, parent, false);
            return new viewHolder(view).linkAdapter(this);
        }
        else{
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.message_received, parent, false);
            return new viewHolder(view).linkAdapter(this);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        if(getItemViewType(position) == VIEW_TYPE_SENT){
            holder.textView.setText(message.get(position));
        }
        else{
            holder.textView1.setText(message.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return message.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(people.get(position).equals("P")){
            return VIEW_TYPE_SENT;
        }
        else{
            return VIEW_TYPE_RECEIVED;
        }
    }
}

class viewHolder extends RecyclerView.ViewHolder{
    //this finds the textView the sent message is going to be stored in and sends it to the viewHolder method
    TextView textView;
    TextView textView1;
    private AdapterPatient adapter;

    public viewHolder(@NonNull View itemView) {
        super(itemView);

        textView = itemView.findViewById(R.id.sent_message);
        textView1 = itemView.findViewById(R.id.received_message);
    }

    public viewHolder linkAdapter(AdapterPatient adapter){
        this.adapter = adapter;
        return this;
    }
}

