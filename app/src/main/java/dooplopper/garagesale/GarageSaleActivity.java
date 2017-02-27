package dooplopper.garagesale;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class GarageSaleActivity extends AppCompatActivity {
    static SQLiteDatabase db;
    static ContentValues cv;
    Cursor dbCursor;

    boolean twoPane;

    int idCol;
    int nameCol;
    int priceCol;
    int purchasedCol;

    static ArrayList<Item> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        twoPane = getResources().getBoolean(R.bool.twoPane);

    }

    private void init() {
        FloatingActionButton addItemFab = (FloatingActionButton) findViewById(R.id.fab);
        addItemFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ItemEditActivity.class);
                v.getContext().startActivity(intent);

            }

        });

    }

    private SQLiteDatabase initDB(Bundle savedInstanceState) {
        SQLiteDatabase db = openOrCreateDatabase("garagesale", MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS items (id PRIMARY KEY, name VARCHAR, price DOUBLE, purchased INTEGER);");
        dbCursor = db.rawQuery("SELECT * FROM items", null);
        idCol = dbCursor.getColumnIndex("id");
        nameCol = dbCursor.getColumnIndex("name");
        priceCol = dbCursor.getColumnIndex("price");
        purchasedCol = dbCursor.getColumnIndex("purchased");
        items = new ArrayList<Item>();
        populateItems(savedInstanceState != null);
        return db;

    }

    private void populateItems(boolean isLaunched) {
        if(!isLaunched) {
            try {
                while(dbCursor.moveToNext()) {
                    Item i = new Item(items.size(), dbCursor.getString(nameCol), dbCursor.getDouble(priceCol), (dbCursor.getInt(purchasedCol) == 1));
                    if(!i.purchased)
                        items.add(i);

                }

            } catch(Exception e) {
                Log.e(getPackageName(), e.getStackTrace().toString());
                items.clear();

            }

        } else {


            while(dbCursor.moveToNext()) {
                Item i = new Item(items.size(), dbCursor.getString(nameCol), dbCursor.getDouble(priceCol), (dbCursor.getInt(purchasedCol) == 1));
                if(!i.purchased)
                    items.add(i);

            }

        }

    }

}