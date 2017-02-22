package dooplopper.garagesale;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Currency;
import java.util.Locale;

/**
 * Created by Hunter on 2/21/2017.
 */

public class ItemDetailFragment extends Fragment {

    public static final String ITEM_ID = "item_id";
    public static final String ITEM_PRICE = "item_price";
    Item item;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        if(getArguments().containsKey(ITEM_ID)) {
            item = GarageSaleActivity.items.get(Integer.valueOf(getArguments().getString(ITEM_ID)));
            getActivity().setTitle(item.content);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.item_detail, container, false);

        if(item != null) {
            ((TextView) rootView.findViewById(R.id.item_detail)).setText("$" + String.valueOf(item.price));
        }

        return rootView;
    }

}
