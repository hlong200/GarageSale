package dooplopper.garagesale;

/**
 * Created by Hunter on 2/21/2017.
 */

public class Item {

    public final int id;
    public final String content;
    public final double price;
    public boolean purchased;

    public Item(int id, String content, double price, boolean purchased) {
        this.id = id;
        this.content = content;
        this.price = price;
        this.purchased = purchased;

    }

}
