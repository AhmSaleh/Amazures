package com.example.e_commerce;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
/*
public class CheckoutRvAdapter extends RecyclerView.Adapter<CheckoutRvAdapter.CheckViewHolder>{

    private ArrayList<ProductRvModel> items;
    int row_index = -1;

    public CheckoutRvAdapter(ArrayList<CheckoutRvModel> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public CheckViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.checkout_rv_item, parent, false);
        CheckViewHolder checkViewHolder = new CheckViewHolder(view);
        return CheckViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CeckoutViewHolder holder, int position) {

        CheckoutRvModel currentItem = items.get(position);

        holder.imageView.setImageResource(currentItem.getImage());
        holder.textTitleView.setText(currentItem.getTitle());
        holder.quantity.setText(currentItem.getQuantity());
        holder.priceTitleView.setText(String.valueOf(currentItem.getPrice()));


    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class CeckoutViewHolder extends RecyclerView.ViewHolder {

        TextView textTitleView, priceTitleView, quantity;
        ConstraintLayout constraintLayout;
        ImageView imageView;

        public CeckoutViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.productImage);
            textTitleView   = itemView.findViewById(R.id.titleText);
            priceTitleView  = itemView.findViewById(R.id.priceText);
            quantity = itemView.findViewById(R.id.quantity);
            constraintLayout = itemView.findViewById(R.id.constraintLayout1);


        }
    }

}*/
/*
*
            imageView = itemView.findViewById(R.id.productImage);
            textTitleView = itemView.findViewById(R.id.titleText);
            priceTitleView = itemView.findViewById(R.id.priceText);
            constraintLayout = itemView.findViewById(R.id.constraintLayout1);*/