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

import static android.support.design.R.styleable.FloatingActionButton;

public class GarageSaleActivity extends AppCompatActivity {

    static DBHelper dbHelper;
    static SQLiteDatabase db;
    static ContentValues values;
    Cursor dbCursor;

    private boolean twoPane;
    private boolean isLaunched = false;

    int idCol;
    int nameCol;
    int priceCol;
    int purchasedCol;

    static ArrayList<Item> items = new ArrayList<Item>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(savedInstanceState);

    }

    private void init(Bundle savedInstanceState) {
        setContentView(R.layout.activity_garage_sale);

        Log.v(getPackageName(), "Reloading data");

        if(savedInstanceState == null) {
            try {
                db = this.openOrCreateDatabase("garagesale", MODE_PRIVATE, null);
                db.execSQL("CREATE TABLE IF NOT EXISTS " + "items (id PRIMARY KEY, name VARCHAR, price DOUBLE, purchased INTEGER);");
                dbCursor = db.rawQuery("SELECT * FROM items", null);
                idCol = dbCursor.getColumnIndex("id");
                nameCol = dbCursor.getColumnIndex("name");
                priceCol = dbCursor.getColumnIndex("price");
                purchasedCol = dbCursor.getColumnIndex("purchased");

                while(dbCursor.moveToNext()) {
                    Item i = new Item(items.size(), dbCursor.getString(nameCol), dbCursor.getDouble(priceCol), (dbCursor.getInt(purchasedCol) == 1));
                    if(items.contains(i) || i.purchased) {
                        Log.v(getPackageName(), "Already contains " + i.content);

                    } else {
                        items.add(i);

                    }

                }

            } catch(Exception e) {
                Log.e(getPackageName(), e.getStackTrace().toString());
                items.clear();

                while(dbCursor.moveToNext()) {
                    Item i = new Item(items.size(), dbCursor.getString(nameCol), dbCursor.getDouble(priceCol), (dbCursor.getInt(purchasedCol) == 1));
                    if(items.contains(i) || i.purchased) {
                        Log.v(getPackageName(), "Already contains " + i.content);

                    } else {
                        items.add(i);

                    }

                }

            }

            values = new ContentValues();

        } else {
            values.put("purchased", 1);
            db.update("items", values, "id = " + String.valueOf(getIntent().getIntExtra("purchased", 0)), null);

        }

        Log.v(getPackageName(), String.valueOf(items.size()));

        android.support.design.widget.FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ItemEditActivity.class);
                v.getContext().startActivity(intent);

            }

        });

        View recyclerView = findViewById(R.id.item_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);

        if(findViewById(R.id.item_detail_container) != null) {
            twoPane = true;

        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outSave) {
        outSave.putBoolean("isLaunched", true);

    }

    /*@Override
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();

    }*/

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(items));

    }

    public class SimpleItemRecyclerViewAdapter extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<Item> values;

        public SimpleItemRecyclerViewAdapter(List<Item> items) {
            values = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.item = values.get(position);
            holder.idView.setText(String.valueOf(values.get(position).id));
            holder.contentView.setText(values.get(position).content);
            holder.priceView.setText("$" + String.valueOf(values.get(position).price));

            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (twoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putInt(ItemDetailFragment.ITEM_ID, holder.item.id);
                        ItemDetailFragment fragment = new ItemDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.item_detail_container, fragment)
                                .commit();

                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, ItemDetailActivity.class);
                        intent.putExtra(ItemDetailFragment.ITEM_ID, holder.item.id);
                        intent.putExtra(ItemDetailFragment.ITEM_PRICE, holder.item.price);
                        context.startActivity(intent);

                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return values.size();

        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            public final View view;
            public final TextView idView;
            public final TextView contentView;
            public final TextView priceView;
            public Item item;

            public ViewHolder(View view) {
                super(view);
                this.view = view;
                this.idView = (TextView) view.findViewById(R.id.id);
                this.contentView = (TextView) view.findViewById(R.id.content);
                this.priceView = (TextView) view.findViewById(R.id.price);

            }

            @Override
            public String toString() {
                return super.toString() + " '" + contentView.getText() + "'";

            }

        }

    }

    public static void publishItem(Item i) {
        db.execSQL("INSERT INTO items (name, price) VALUES ('" + i.content + "', " + i.price + ");");
        Log.v("potato", "Inserted item: " + i.content);
        GarageSaleActivity.items.add(i);

    }

    public static int nextId() {
        return items.size();

    }

}
