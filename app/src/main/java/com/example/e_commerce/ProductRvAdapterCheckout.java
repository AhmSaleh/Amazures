package com.example.e_commerce;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ProductRvAdapterCheckout extends RecyclerView.Adapter<ProductRvAdapterCheckout.ProductViewHolder>{

    private ArrayList<ProductRvModel> items;
    private Context context;
    int row_index = -1;
    private String EAN ;


    private OnItemClickListener mListener;
    public interface  OnItemClickListener{
        void addItemClick(int position);
        void removeItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView textTitleView, priceTitleView, textTitleViewID, quantity, quantity1;
        ConstraintLayout constraintLayout;
        ImageView imageView, btn_addition, btn_subtraction;
        Button addButton;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.productImage);
            textTitleView   = itemView.findViewById(R.id.titleText);
            quantity   = itemView.findViewById(R.id.quantity2);
            quantity1   = itemView.findViewById(R.id.quantity4);
            priceTitleView  = itemView.findViewById(R.id.priceText);
            btn_addition = itemView.findViewById(R.id.btn_addition);
            btn_subtraction = itemView.findViewById(R.id.btn_subtraction);

            constraintLayout = itemView.findViewById(R.id.constraintLayout);

            btn_addition.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            mListener.addItemClick(position);
                            quantity.setText(String.valueOf(Integer.parseInt(quantity.getText().toString())+1));
                        }
                    }
                }
            });

            btn_subtraction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            mListener.removeItemClick(position);
                            if(quantity.getText().toString()!="0")
                                quantity.setText(String.valueOf(Integer.parseInt(quantity.getText().toString())-1));
                        }
                    }
                }
            });

        }
    }
    public ProductRvAdapterCheckout(ArrayList<ProductRvModel> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.checkout_rv_item, parent, false);
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
        holder.quantity1.setText(currentItem.getID());

        for(int i=0 ; i<StaticVariables.products.size(); i++){
            if(StaticVariables.products.get(i).getProductID().equals(currentItem.getID())){
                holder.quantity.setText(String.valueOf(StaticVariables.products.get(i).quantity));
                break;
            }
        }
        //Toast.makeText(context, currentItem.getID(), Toast.LENGTH_SHORT).show();
        EAN = currentItem.getID();

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

}
