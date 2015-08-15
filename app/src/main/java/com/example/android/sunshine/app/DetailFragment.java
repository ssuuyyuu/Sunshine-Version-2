package com.example.android.sunshine.app;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment {

    private static final String FORECAST_SHARE_HASHTAG = " #SunshineApp";
    private String mForecastStr;

    public DetailFragment() {
        // Required empty public constructor
    }

    // Do not need to override onOptionsItemSelected. Once we set SharedIntent for shareActionProvider, it takes care of the action.

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_detail, menu);

        // find the share item
        MenuItem menuItem = menu.findItem(R.id.action_share);

        // do not use menuItem.getActionProvider()
        ShareActionProvider shareActionProvider =
                (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);

        if (shareActionProvider != null) {
            shareActionProvider.setShareIntent(createShareForecastIntent());
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Important: must set this to true, otherwise the inflated menu will not show !!!!!
        setHasOptionsMenu(true);

        // important: set attachToRoot to false
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        TextView textView = (TextView)rootView.findViewById(R.id.detail_textView);

        Intent intent = getActivity().getIntent();
        mForecastStr = intent.getStringExtra("extra");
        textView.setText(mForecastStr);

        return rootView;
    }

    private Intent createShareForecastIntent() {
        Intent intent = new Intent(Intent.ACTION_SEND);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT,
                mForecastStr + FORECAST_SHARE_HASHTAG);
        return intent;
    }

}
