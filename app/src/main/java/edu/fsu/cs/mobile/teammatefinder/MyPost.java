package edu.fsu.cs.mobile.teammatefinder;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MyPost extends android.app.Fragment {

    private ListView lv;
    private FirebaseFirestore database;
    private String username;
    private String game;
    private String post;

    private boolean isdelete = false;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_my_post, container, false);

        database = FirebaseFirestore.getInstance();
        lv = v.findViewById(R.id.myCommentsList);

        Bundle bundle = getArguments();
        game = bundle.getString("gameID");
        post = bundle.getString("postID");

        DocumentReference ref = database.collection("posts").document(game).collection("posts").document(post);
        ref.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot snap, FirebaseFirestoreException e) {
                if (snap != null && snap.exists()) {
                    TextView t1 = v.findViewById(R.id.myPostDate);
                    t1.setText(snap.get("date").toString());
                    TextView t2 = v.findViewById(R.id.myPostTime);
                    t2.setText(snap.get("time").toString());
                    TextView t3 = v.findViewById(R.id.myPostRank);
                    t3.setText(snap.get("rank").toString());
                }
            }
        });

        Button back = v.findViewById(R.id.myPostbackbtn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).setFragToMyPosts();
            }
        });

        final Button delete = v.findViewById(R.id.myPostDelete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isdelete = true;
                database.collection("posts").document(game).collection("posts").document(post).collection("comments").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                document.getReference().delete();
                            }
                        }
                    }
                });
                database.collection("posts").document(game).collection("posts").document(post).delete();
                ((MainActivity)getActivity()).setFragToMyPosts();
            }
        });

        setList();

        Button add = v.findViewById(R.id.myPostAdd);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText input = new EditText(getActivity());
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), android.R.style.Theme_Material_Dialog_Alert);
                builder.setTitle("Add Comment");
                builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        final DocumentReference ref = database.collection("posts").document(game).collection("posts").document(post);
                        database.collection("users").whereEqualTo("email",FirebaseAuth.getInstance().getCurrentUser().getEmail()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (DocumentSnapshot document : task.getResult()) {
                                        Map<String, Object> comment = new HashMap<>();
                                        comment.put("comment", input.getText().toString());
                                        comment.put("poster", document.get("username").toString());
                                        ref.collection("comments").document().set(comment);
                                    }
                                }
                            }
                        });
                    }
                }).setNegativeButton("Back", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        return;
                    }
                }).setView(input).show();
            }
        });

        database.collection("posts").document(game).collection("posts").document(post).collection("comments").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {
                if(!isdelete) setList();
            }
        });

        return v;
    }

    private void setList() {
        database.collection("posts").document(game).collection("posts").document(post).collection("comments").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<String> list = new ArrayList<String>();
                    for (final DocumentSnapshot document : task.getResult()) {
                        list.add(document.get("poster").toString() + ":  " + document.get("comment").toString());
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, list);
                    lv.setAdapter(adapter);
                }
            }});
    }
}
