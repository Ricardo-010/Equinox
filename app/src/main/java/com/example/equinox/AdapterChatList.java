package com.example.equinox;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterChatList extends RecyclerView.Adapter<viewHolderChatList> {
    ArrayList<String> patient_names;
    viewHolderChatList.OnChatListener mOnChatListener;
    //A constructor to collect the message being sent
    public AdapterChatList(ArrayList<String> patient_names, viewHolderChatList.OnChatListener onChatListener) {
        this.patient_names = patient_names;
        this.mOnChatListener = onChatListener;
    }

    @NonNull
    @Override
    public viewHolderChatList onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.users, parent, false);
        return new viewHolderChatList(view, mOnChatListener);//.linkAdapter(this);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolderChatList holder, int position) {
        holder.textView.setText(patient_names.get(position));
    }

    @Override
    public int getItemCount() {
        return patient_names.size();
    }
}

class viewHolderChatList extends RecyclerView.ViewHolder implements  View.OnClickListener{
    TextView textView;
    OnChatListener onChatListener;

    //private AdapterChatList adapter;

    public viewHolderChatList(@NonNull View itemView, OnChatListener onChatListener) {
        super(itemView);
        textView = itemView.findViewById(R.id.username);
        this.onChatListener = onChatListener;

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        onChatListener.onChatClick(getAdapterPosition());
    }

    /*
    public viewHolderChatList linkAdapter(AdapterChatList adapter){
        this.adapter = adapter;
        return this;
    }
     */
    public interface OnChatListener{
        void onChatClick(int position);
    }

}
