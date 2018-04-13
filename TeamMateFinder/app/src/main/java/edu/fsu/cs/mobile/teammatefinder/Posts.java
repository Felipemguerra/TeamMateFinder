package edu.fsu.cs.mobile.teammatefinder;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;

public class Posts extends android.app.Fragment {

    private ListView lv;
    private FirebaseFirestore database;
    private Spinner spinner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_posts, container, false);

        database = FirebaseFirestore.getInstance();
        spinner = v.findViewById(R.id.pspinner);
        lv = v.findViewById(R.id.listview);

        Button back = v.findViewById(R.id.pback);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).setFragToMainMenu();
            }
        });

        database.collection("posts").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<String> list = new ArrayList<>();
                    for (DocumentSnapshot document : task.getResult()) {
                        list.add(document.getId());
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),R.layout.support_simple_spinner_dropdown_item,list);
                    spinner.setAdapter(adapter);
                }
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                database.collection("posts").document(spinner.getSelectedItem().toString()).collection("posts").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<String> list = new ArrayList<String>();
                            for (final DocumentSnapshot document : task.getResult()) {
                                list.add(document.get("date").toString() + " " + document.get("time").toString() + "        " + document.get("rank").toString());
                            }
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, list);
                            lv.setAdapter(adapter);
                        }
                    }});
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final int pos = i;
                database.collection("posts").document(spinner.getSelectedItem().toString()).collection("posts").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        int x = 0;
                        if (task.isSuccessful()) {
                            for (final DocumentSnapshot document : task.getResult()) {
                                if(x == pos) {
                                    ((MainActivity)getActivity()).setFragToPost(document.getId(), spinner.getSelectedItem().toString(),false);
                                }
                                x++;
                            }
                        }
                    }});
            }
        });

        return v;
    }
}
