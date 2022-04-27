package com.example.e_commerce;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ProductRvAdapter extends RecyclerView.Adapter<ProductRvAdapter.ProductViewHolder>{

    private ArrayList<ProductRvModel> items;
    private Context context;
    int row_index = -1;
    private String EAN ;


    private OnItemClickListener mListener;
    public interface  OnItemClickListener{
        void addItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView textTitleView, priceTitleView, textTitleViewID;
        ConstraintLayout constraintLayout;
        ImageView imageView;
        Button addButton;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.productImage);
            textTitleView   = itemView.findViewById(R.id.titleText);
            priceTitleView  = itemView.findViewById(R.id.priceText);
            textTitleViewID   = itemView.findViewById(R.id.productId);
            addButton = itemView.findViewById(R.id.add_button);
            constraintLayout = itemView.findViewById(R.id.constraintLayout1);

//            int a = getAdapterPosition();


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            mListener.addItemClick(position);
                        }
                    }
                }
            });

            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            mListener.addItemClick(position);
                           // Toast.makeText(context, EAN, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }
    }
    public ProductRvAdapter(ArrayList<ProductRvModel> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_rv_item, parent, false);
        ProductViewHolder ProductViewHolder = new ProductViewHolder(view);
        context = parent.getContext();
        return ProductViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {

        ProductRvModel currentItem = items.get(position);
        Glide.with(context).load(currentItem.getImage()).into(holder.imageView);
        holder.textTitleView.setText(currentItem.getTitle());
        holder.priceTitleView.setText(String.valueOf(currentItem.getPrice())+" LE");
        holder.textTitleViewID.setText(String.valueOf("EAN: "+currentItem.getID()));
        EAN = currentItem.getID();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

}
