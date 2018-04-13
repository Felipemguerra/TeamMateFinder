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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;


public class Conversations extends android.app.Fragment {

    private ListView lv;
    private FirebaseFirestore database;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_conversations, container, false);

        database = FirebaseFirestore.getInstance();
        lv = v.findViewById(R.id.convoList);

        Button back = v.findViewById(R.id.convoBack);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).setFragToMainMenu();
            }
        });

        database.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("posts").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<String> list = new ArrayList<String>();
                    for (final DocumentSnapshot document : task.getResult()) {
                        list.add(document.get("gameID").toString() + ": " + document.get("date").toString() + "  " + document.get("time").toString());
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, list);
                    lv.setAdapter(adapter);
                }
            }});


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final int pos = i;
                database.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("posts").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        int x = 0;
                        if (task.isSuccessful()) {
                            for (final DocumentSnapshot document : task.getResult()) {
                                if(x == pos) {
                                    ((MainActivity)getActivity()).setFragToPost(document.get("postID").toString(), document.get("gameID").toString(), true);
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