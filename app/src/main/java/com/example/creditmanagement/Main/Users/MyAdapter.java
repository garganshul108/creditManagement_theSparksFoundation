package com.example.creditmanagement.Main.Users;

import android.app.Dialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.creditmanagement.Main.AnimationUtil;
import com.example.creditmanagement.R;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private ArrayList<Model> mDatasets = new ArrayList<>();
    Stack<Model> sender = new Stack<>();
    private MainActivity context;
    int previousPosition = 0;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextView name , credit;
        public MyViewHolder(View v) {
            super(v);
            name = v.findViewById(R.id.name);
            credit = v.findViewById(R.id.credit);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(ArrayList<Model> myDatasets , MainActivity context) {
        this.mDatasets = myDatasets;
        this.context = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        View v =  LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);

        return new MyViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        if(mDatasets.get(position).getName() != null)
            holder.name.setText(mDatasets.get(position).getName());
        if(mDatasets.get(position).getCredit() != null){
            String c = context.getResources().getString(R.string.rs)+mDatasets.get(position).getCredit();
            holder.credit.setText(c);
        }

        if(position > previousPosition){ //we are scrolling down..
            AnimationUtil.animate(holder , true);
        }
        else {//we are scrolling up
            AnimationUtil.animate(holder , false);
        }

        previousPosition = position;

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sender.isEmpty()){
                    sender.push(mDatasets.remove(position));
                    context.stack_empty = false;
                    context.adapter.notifyDataSetChanged();
                    context.dynamic_tv.setVisibility(View.VISIBLE);
                    AnimationUtil.animateView(context.dynamic_tv , true);
                }
                else{
                    final Dialog dialog = new Dialog(context);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.amount_box);
                    dialog.show();
                    final EditText credit = dialog.findViewById(R.id.credit);
                    Button confirm_btn = dialog.findViewById(R.id.confirm_btn);
                    confirm_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Double amount = Double.parseDouble(credit.getText().toString());
                            if(Double.parseDouble(sender.peek().getCredit() ) >= amount){
                                Double receivers_total_amount = Double.parseDouble(mDatasets.get(position).getCredit())
                                        + amount;
                                Double sender_total_amount = Double.parseDouble(sender.peek().getCredit())
                                        - amount;
                                Model so = sender.pop();
                                FirebaseDatabase.getInstance().getReference("users").child(so.getName())
                                        .setValue(String.valueOf(sender_total_amount));
                                FirebaseDatabase.getInstance().getReference("users").child(mDatasets.get(position).getName())
                                        .setValue(String.valueOf(receivers_total_amount));
                                mDatasets.get(position).setCredit(String.valueOf(receivers_total_amount));
                                so.setCredit(String.valueOf(sender_total_amount));
                                mDatasets.add(so);
                                Collections.sort(mDatasets);
                                context.adapter.notifyDataSetChanged();

                            }

                            else {
                                Toast.makeText(context , "Sorry you have insufficient amount.." , Toast.LENGTH_LONG).show();
                                mDatasets.add(sender.pop());
                                context.stack_empty = true;
                                Collections.sort(mDatasets);
                                context.adapter.notifyDataSetChanged();
                            }
                            context.dynamic_tv.setVisibility(View.GONE);
                            dialog.cancel();
                        }
                    });

                }
            }
        });


    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDatasets.size();
    }
}
