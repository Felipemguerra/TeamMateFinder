package edu.fsu.cs.mobile.teammatefinder;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class mainMenu extends android.app.Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main_menu, container, false);
        TextView t1 = v.findViewById(R.id.makeAPost);
        t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).setFragToForm();
            }
        });

        TextView t2 = v.findViewById(R.id.FindAMate);
        t2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).setFragToPosts();
            }
        });

        TextView t3 = v.findViewById(R.id.MyRequests);
        t3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).setFragToMyPosts();
            }
        });

        TextView t4 = v.findViewById(R.id.GoBack);
        t4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).setFragToLogin();
            }
        });

        TextView t5 = v.findViewById(R.id.checkOnReqest);
        t5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).setFragToConversations();
            }
        });

        return v;
    }
}
