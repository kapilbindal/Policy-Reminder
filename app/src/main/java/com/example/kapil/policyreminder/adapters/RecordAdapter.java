package com.example.kapil.policyreminder.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kapil.policyreminder.AddNewActivity;
import com.example.kapil.policyreminder.R;
import com.example.kapil.policyreminder.model.Record;

import java.util.ArrayList;

/**
 * Created by KAPIL on 10-01-2018.
 */

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.RecordViewHolder>{

    private ArrayList<Record> records;
    Context context;

    public RecordAdapter(ArrayList<Record> records, Context context) {
        this.records = records;
        this.context = context;
    }

    public void setRecords(ArrayList<Record> records) {
        this.records = records;
        notifyDataSetChanged();
    }

    @Override
    public RecordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater li = LayoutInflater.from(parent.getContext());
        return new RecordViewHolder(li.inflate(R.layout.list_item_record,parent,false));
    }

    @Override
    public void onBindViewHolder(RecordViewHolder holder, final int position) {
        holder.bindView(records.get(position));

        holder.tvPolNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, AddNewActivity.class));
            }
        });

    }

    @Override
    public int getItemCount() {
        return records.size();
    }

    class RecordViewHolder extends RecyclerView.ViewHolder{
        TextView tvName,tvPolNum,tvExpDate,tvCompany,tvPolType,tvMobNum,tvEmail;

        public RecordViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvPolNum = itemView.findViewById(R.id.tvPolNum);
            tvExpDate = itemView.findViewById(R.id.tvExpDate);
            //tvCompany = itemView.findViewById(R.id.tvCompany);
            tvPolType = itemView.findViewById(R.id.tvPolType);
            //tvMobNum = itemView.findViewById(R.id.tvMobNum);
            //tvEmail = itemView.findViewById(R.id.tvEmail);
        }
        void bindView(Record record){
            tvName.setText(record.getName());
            tvPolNum.setText(record.getPolicyNum());
            tvExpDate.setText(record.getExpiryDate());
            //tvCompany.setText(record.getCompany());
            tvPolType.setText(record.getType());
            //tvMobNum.setText(record.getMobileNum());
            //tvEmail.setText(record.getEmail());
        }
    }
}
