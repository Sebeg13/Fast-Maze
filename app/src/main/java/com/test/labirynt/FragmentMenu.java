package com.test.labirynt;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentMenu extends Fragment {


    onZdarzenieListener aListener;
    MainActivity mainActivity;

    public FragmentMenu() {
        // Required empty public constructor
    }


    public interface onZdarzenieListener {
        public void onZdarzenie();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.menu_fragment, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            AppCompatActivity activity = (AppCompatActivity) context;
            aListener = (onZdarzenieListener) activity;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mainActivity = (MainActivity) getActivity();
        mainActivity.setBestTimeTv(mainActivity.findViewById(R.id.bestTimeValTv));
        mainActivity.restoreBestTimeInMilis();
        mainActivity.setBestTimeTv();



    }
}
