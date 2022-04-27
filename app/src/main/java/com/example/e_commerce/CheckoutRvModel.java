package com.example.e_commerce;

public class CheckoutRvModel {

    private int image;
    private float price;
    private String title ;
    private int quantity;

    public CheckoutRvModel(int image, float price, String title, int quantity) {
        this.image = image;
        this.price = price;
        this.title = title;
        this.quantity = quantity;
    }

    public int getImage() {
        return image;
    }

    public float getPrice() {
        return price;
    }

    public String getTitle() {
        return title;
    }

    public int getQuantity() {
        return quantity;
    }
}
