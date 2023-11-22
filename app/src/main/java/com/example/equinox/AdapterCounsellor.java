package com.example.equinox;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterCounsellor extends RecyclerView.Adapter<viewHolderCounsellor> {

    ArrayList<String> message;
    ArrayList<String> people;

    public static final int VIEW_TYPE_SENT = 1;
    public static final int VIEW_TYPE_RECEIVED = 2;

    //A constructor to collect the message being sent
    public AdapterCounsellor(ArrayList<String> messages, ArrayList<String> people) {
        this.message = messages;
        this.people = people;
    }

    @NonNull
    @Override
    public viewHolderCounsellor onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == VIEW_TYPE_SENT){
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.message_sent, parent, false);
            return new viewHolderCounsellor(view).linkAdapter(this);
        }
        else{
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.message_received, parent, false);
            return new viewHolderCounsellor(view).linkAdapter(this);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolderCounsellor holder, int position) {
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
        if(people.get(position).equals("C")){
            return VIEW_TYPE_SENT;
        }
        else{
            return VIEW_TYPE_RECEIVED;
        }
    }
}

class viewHolderCounsellor extends RecyclerView.ViewHolder{
    //this finds the textView the sent message is going to be stored in and sends it to the viewHolder method
    TextView textView;
    TextView textView1;
    private AdapterCounsellor adapter;

    public viewHolderCounsellor(@NonNull View itemView) {
        super(itemView);

        textView = itemView.findViewById(R.id.sent_message);
        textView1 = itemView.findViewById(R.id.received_message);
    }

    public viewHolderCounsellor linkAdapter(AdapterCounsellor adapter){
        this.adapter = adapter;
        return this;
    }
}


