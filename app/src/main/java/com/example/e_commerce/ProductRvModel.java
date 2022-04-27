package com.example.e_commerce;

public class ProductRvModel {

    private String imageUrl;
    private String price;
    private String title ;
    private String id;
    private int total = 0;

    public ProductRvModel(String imageUrl, String price, String title, String id) {
        this.imageUrl = imageUrl;
        this.price = price;
        this.title = title;
        this.id = id;
    }

    public void changePrice()
    {
        price = "Price Changed";
    }

    public String getImage() {
        return imageUrl;
    }

    public String getPrice() {
        return price;
    }

    public String getTitle() {
        return title;
    }

    public String getID() {
        return id;
    }
}
