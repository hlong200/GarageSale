package dooplopper.garagesale;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class GarageSaleActivity extends AppCompatActivity {

    private boolean twoPane;

    ArrayList<Item> items = new ArrayList<Item>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garage_sale);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "You pushed the button", Snackbar.LENGTH_SHORT)
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

        Item i = new Item("1", "This is a test", "derp");
        items.add(i);

    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(items));

    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

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
            public Item item;

            public ViewHolder(View view) {
                super(view);
                this.view = view;
                this.idView = (TextView) view.findViewById(R.id.id);
                this.contentView = (TextView) view.findViewById(R.id.content);

            }

            @Override
            public String toString() {
                return super.toString() + " '" + contentView.getText() + "'";

            }

        }

    }

}
