package com.example.android.sunshine.app;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment {


    public DetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // important: set attachToRoot to false
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        TextView textView = (TextView)rootView.findViewById(R.id.detail_textView);

        Intent intent = getActivity().getIntent();
        textView.setText(intent.getStringExtra("extra"));

        return rootView;
    }

}
