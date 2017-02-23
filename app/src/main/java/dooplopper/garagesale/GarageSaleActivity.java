package dooplopper.garagesale;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class GarageSaleActivity extends AppCompatActivity {

    static DBHelper dbHelper;
    static SQLiteDatabase db;
    static ContentValues values;
    Cursor dbCursor;

    String[] projection = {dbHelper._ID, dbHelper.COLUMN_NAME, dbHelper.COLUMN_NAME_PRICE};
    String selection = dbHelper.COLUMN_NAME + " = ?";
    String[] selectionArgs = {};
    String sortOrder = dbHelper.COLUMN_NAME_PRICE + " DESC";

    private boolean twoPane;

    static ArrayList<Item> items = new ArrayList<Item>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garage_sale);

        // Database stuff
        dbHelper = new DBHelper(this);
        db = dbHelper.getWritableDatabase();
        values = new ContentValues();
        dbCursor = db.query(dbHelper.TABLE_NAME, projection, selection, null, null, null, null);

        while(dbCursor.moveToNext()) {
            String name = dbCursor.getString(dbCursor.getColumnIndex("name"));
            double price = dbCursor.getDouble(dbCursor.getColumnIndex("price"));

            try {
                Item i = new Item(String.valueOf(nextId()), name, price);
                items.add(i);

            } catch(Exception e) {
                Log.e(getPackageName(), "DB Error: " + e.toString());

            }

        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ItemEditActivity.class);
                v.getContext().startActivity(intent);

                Snackbar.make(v, "Editing new item!", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null)
                        .show();

            }

        });

        View recyclerView = findViewById(R.id.item_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);

        if(findViewById(R.id.item_detail_container) != null) {
            twoPane = true;

        }

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
            holder.idView.setText(values.get(position).id);
            holder.contentView.setText(values.get(position).content);
            holder.priceView.setText("$" + String.valueOf(values.get(position).price));

            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (twoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putString(ItemDetailFragment.ITEM_ID, holder.item.id);
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
        values.put(dbHelper.COLUMN_NAME, i.content);
        values.put(dbHelper.COLUMN_NAME_PRICE, i.price);
        long newRowId = db.insert(dbHelper.TABLE_NAME, null, values);
        GarageSaleActivity.items.add(i);

    }

    public static int nextId() {
        return items.size();

    }

}
