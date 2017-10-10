package com.example.foodoncampus;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by GonzaloConcepciónMeg on 27/09/2017.
 */

public class Item  implements Serializable{
    private static final long serialVersionUID = -1213949467658913456L;

    private static ArrayList<Item> items = new ArrayList<Item>();
    private String title;
    private String body;

    public Item(String title, String body) {
        this.title = title;
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    @Override
    public String toString() {
        return getTitle();
    }

    public static ArrayList<Item> getItems() {
        return items;
    }

    public static void addItem(String day, String content){
        Item item = new Item(day, content);
        items.add(item);
    }
}