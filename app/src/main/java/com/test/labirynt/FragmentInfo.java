package com.test.labirynt;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class FragmentInfo extends Fragment {
    View view;

    MainActivity mainActivity;

    public FragmentInfo() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.info_fragment,container,false);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mainActivity = (MainActivity)getActivity();
        Button resetBtn = mainActivity.findViewById(R.id.resetTimebutton);
        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mainActivity.getApplicationContext(),"Hold longer to reset best time",Toast.LENGTH_SHORT).show();
            }
        });
        resetBtn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mainActivity.setBestTimeMilis(0);
                mainActivity.saveBestTimeInMilis();
                mainActivity.setBestTimeTv();
                Toast.makeText(mainActivity.getApplicationContext(),"Best time has been reset",Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }
}
