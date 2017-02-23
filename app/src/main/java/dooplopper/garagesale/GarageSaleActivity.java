package dooplopper.garagesale;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class GarageSaleActivity extends AppCompatActivity implements View.OnClickListener{

    private boolean twoPane;
    private boolean isLaunched = false;
    static SQLiteDatabase db;
    Cursor dbCursor;

    int idCol;
    int nameCol;
    int priceCol;

    static ArrayList<Item> items = new ArrayList<Item>();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == RESULT_CANCELED) {
            Log.v(getPackageName(), "Returning to home");
            isLaunched = true;

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(!isLaunched) {
            setContentView(R.layout.activity_garage_sale);

            try {
                db = this.openOrCreateDatabase("garagesale", MODE_PRIVATE, null);
                db.execSQL("CREATE TABLE IF NOT EXISTS " + "items (id PRIMARY KEY, name VARCHAR, price DOUBLE);");
                dbCursor = db.rawQuery("SELECT * FROM items", null);
                idCol = dbCursor.getColumnIndex("id");
                nameCol = dbCursor.getColumnIndex("name");
                priceCol = dbCursor.getColumnIndex("price");

                while(dbCursor.moveToNext()) {
                    Item i = new Item(items.size(), dbCursor.getString(nameCol), dbCursor.getDouble(priceCol));
                    if(items.contains(i)) {
                        Log.v(getPackageName(), "Already contains " + i.content);

                    } else {
                        items.add(i);

                    }

                }

                for(Item i : items) {
                    Log.v(getPackageName(), i.content);

                }

            } catch(Exception e) {
                Log.e(getPackageName(), e.getStackTrace().toString());

            }

            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
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

    }

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
        Log.v("potato", "Inserted more data");
        //GarageSaleActivity.items.add(i);

    }

    public static int nextId() {
        return items.size();

    }

}
