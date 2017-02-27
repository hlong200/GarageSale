package dooplopper.garagesale;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

/**
 * Created by Hunter on 2/22/2017.
 */

public class ItemEditActivity extends AppCompatActivity {

    // Name and price text fields
    EditText editName;
    EditText editPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_edit);

        // Instantiate name and price text fields
        editName = (EditText) findViewById(R.id.edit_name);
        editPrice = (EditText) findViewById(R.id.edit_price);

        // Floating Action Button for publishing item
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
<<<<<<< HEAD
                GarageSaleActivity.publishItem(new Item(String.valueOf(GarageSaleActivity.nextId()), editName.getText().toString(), Double.valueOf(editPrice.getText().toString())));
=======
                // Instantiate new Item with id, name, and price parameters and publish it
                GarageSaleActivity.publishItem(new Item(GarageSaleActivity.nextId(), editName.getText().toString(), Double.valueOf(editPrice.getText().toString())));
                // Return to app home activity
>>>>>>> origin/master
                navigateUpTo(getParentActivityIntent());

            }

        });

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);

        }

    }

}
