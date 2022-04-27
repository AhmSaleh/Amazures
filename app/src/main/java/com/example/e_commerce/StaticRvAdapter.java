package com.example.e_commerce;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class StaticRvAdapter extends RecyclerView.Adapter<StaticRvAdapter.StaticRvViewHolder>{

    private ArrayList<StaticRvModel> items;
    private Context context;
    int row_index = -1;

    public OnItemClickListener mListener;
    public interface OnItemClickListener {
        void onItemClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public StaticRvAdapter(ArrayList<StaticRvModel> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public StaticRvViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.static_rv_item, parent, false);
        StaticRvViewHolder staticRvViewHolder = new StaticRvViewHolder(view);
        context = parent.getContext();
        return staticRvViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull StaticRvViewHolder holder, int position) {

        StaticRvModel currentItem = items.get(position);
        //Glide.with(context).load("https://firebasestorage.googleapis.com/v0/b/e-commerce-c8404.appspot.com/o/mobiles%20and%20tablets%2Fiphone11.jpg?alt=media&token=31a02932-73a3-4773-a613-a29e5dfaf3f7").into( holder.imageView);
        holder.imageView.setImageResource(currentItem.getImage());
        holder.textView.setText(currentItem.getText());

     /*   holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                row_index = position;
                notifyDataSetChanged();
            }
        });

        if(row_index == position)
            holder.linearLayout.setBackgroundResource(R.drawable.static_rv_selected);
        else
            holder.linearLayout.setBackgroundResource(R.drawable.static_rv_bg);
*/
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class StaticRvViewHolder extends RecyclerView.ViewHolder {


        ImageView imageView;
        TextView textView;
        LinearLayout linearLayout;
        public StaticRvViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.itemImageID);
            textView = itemView.findViewById(R.id.itemTextID);
            linearLayout = itemView.findViewById(R.id.linearLayout);
            int lastIndex = 0;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mListener.onItemClick(position);
                        }
                    }

                }
            });
        }
    }
}