package kin.olivescript.com.kin;

/**
 * Created by sudaraka on 9/3/16.
 */
public class Item {
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    private String item;
    private int id;

    public Item(String item, int id) {
        this.item = item;
        this.id = id;
    }
}
