package edu.fsu.cs.mobile.teammatefinder;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;


public class FormFragment extends android.app.Fragment {

    private FirebaseFirestore database;
    private String[] slist;
    private Spinner spinner;
    private EditText date;
    private EditText time;
    private EditText comment;
    private EditText rank;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_form, container, false);
        Button back = v.findViewById(R.id.inputBack);
        database = FirebaseFirestore.getInstance();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).setFragToMainMenu();
            }
        });

        date = v.findViewById(R.id.dateInput);
        time = v.findViewById(R.id.timeInput);
        comment = v.findViewById(R.id.commentInput);
        rank = v.findViewById(R.id.rankInput);

        spinner = v.findViewById(R.id.spinner);
        database.collection("posts").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    LinkedList<String> list = new LinkedList<>();
                    for (DocumentSnapshot document : task.getResult()) {
                        list.add(document.getId());
                    }
                    slist = new String[list.size()];
                    int i = 0;
                    for(Object o : list) {
                        slist[i] = o.toString();
                        i++;
                    }
                    System.out.println(slist[0]);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),R.layout.support_simple_spinner_dropdown_item,slist);
                    spinner.setAdapter(adapter);
                }
            }
        });
        Button sBtn = v.findViewById(R.id.inputSubmit);
        sBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DocumentReference ref = database.collection("posts").document(spinner.getSelectedItem().toString());
                Map<String, Object> post = new HashMap<>();
                post.put("date", date.getText().toString());
                post.put("time", time.getText().toString());
                post.put("comment", comment.getText().toString());
                post.put("rank", rank.getText().toString());
                post.put("posterEmail", FirebaseAuth.getInstance().getCurrentUser().getEmail());
                ref.collection("posts").document().set(post);
                ((MainActivity)getActivity()).setFragToMainMenu();
            }
        });

        return v;
    }
}
