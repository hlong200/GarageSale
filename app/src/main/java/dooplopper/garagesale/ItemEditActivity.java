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

    EditText editName;
    EditText editPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_edit);

        editName = (EditText) findViewById(R.id.edit_name);
        editPrice = (EditText) findViewById(R.id.edit_price);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GarageSaleActivity.publishItem(new Item(String.valueOf(GarageSaleActivity.nextId()), editName.getText().toString(), Double.valueOf(editPrice.getText().toString())));
                navigateUpTo(getParentActivityIntent());
                Snackbar.make(v, "Publish", Snackbar.LENGTH_SHORT).setAction("Action", null).show();

            }

        });

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);

        }

    }

}
