package dooplopper.garagesale;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

/**
 * Created by Hunter on 2/21/2017.
 */

public class ItemDetailActivity extends AppCompatActivity implements View.OnClickListener {

    ItemDetailFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        FloatingActionButton commentFab = (FloatingActionButton) findViewById(R.id.comment_fab);
        commentFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "Comment button", Snackbar.LENGTH_SHORT).setAction("Action", null).show();

            }

        });

        FloatingActionButton deleteFab = (FloatingActionButton) findViewById(R.id.delete_fab);
        deleteFab.setOnClickListener(this);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);

        }

        // Transfer data to ItemDetailFragment and display it
        if(savedInstanceState == null) {
            Bundle arguments = new Bundle();
            arguments.putInt(ItemDetailFragment.ITEM_ID, getIntent().getIntExtra(ItemDetailFragment.ITEM_ID, 0));
            fragment = new ItemDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction().add(R.id.item_detail_container, fragment).commit();

        }

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.delete_fab) {
            getParentActivityIntent().putExtra("purchased", fragment.getArguments().getInt(ItemDetailFragment.ITEM_ID));
            NavUtils.navigateUpFromSameTask(this);

        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int id = menuItem.getItemId();
        if(id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;

        }
        return super.onOptionsItemSelected(menuItem);

    }

}
