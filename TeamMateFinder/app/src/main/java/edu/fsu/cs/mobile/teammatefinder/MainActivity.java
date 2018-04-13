package edu.fsu.cs.mobile.teammatefinder;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setFragToLogin();
    }

    public void setFragToLogin() {
        android.app.FragmentManager manager = getFragmentManager();
        android.app.FragmentTransaction fragmentTransaction = manager.beginTransaction();
        Login fragment = new Login();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();
    }

    public void setFragToSignUp() {
        android.app.FragmentManager manager = getFragmentManager();
        android.app.FragmentTransaction transaction = manager.beginTransaction();

        signUp fragment = new signUp();
        transaction.replace(R.id.container, fragment);
        transaction.commit();

    }

    public void setFragToMainMenu() {
        android.app.FragmentManager manager = getFragmentManager();
        android.app.FragmentTransaction transaction = manager.beginTransaction();

        mainMenu fragment = new mainMenu();
        transaction.replace(R.id.container, fragment);
        transaction.commit();
    }

    public void setFragToForm() {
        android.app.FragmentManager manager = getFragmentManager();
        android.app.FragmentTransaction transaction = manager.beginTransaction();

        FormFragment fragment = new FormFragment();
        transaction.replace(R.id.container, fragment);
        transaction.commit();
    }

    public void setFragToPosts() {
        android.app.FragmentManager manager = getFragmentManager();
        android.app.FragmentTransaction transaction = manager.beginTransaction();

        Posts fragment = new Posts();
        transaction.replace(R.id.container, fragment);
        transaction.commit();
    }

    public void setFragToMyPosts() {
        android.app.FragmentManager manager = getFragmentManager();
        android.app.FragmentTransaction transaction = manager.beginTransaction();

        MyPostsFragment fragment = new MyPostsFragment();
        transaction.replace(R.id.container, fragment);
        transaction.commit();
    }

    public void setFragToPost(String postID, String gameID, boolean convo) {
        android.app.FragmentManager manager = getFragmentManager();
        android.app.FragmentTransaction transaction = manager.beginTransaction();

        Bundle bundle = new Bundle();
        bundle.putString("postID", postID);
        bundle.putString("gameID", gameID);
        bundle.putBoolean("convo", convo);

        Post fragment = new Post();
        fragment.setArguments(bundle);
        transaction.replace(R.id.container, fragment);
        transaction.commit();
    }

    public void setFragToMyPost(String postID, String gameID) {
        android.app.FragmentManager manager = getFragmentManager();
        android.app.FragmentTransaction transaction = manager.beginTransaction();

        Bundle bundle = new Bundle();
        bundle.putString("postID", postID);
        bundle.putString("gameID", gameID);

        MyPost fragment = new MyPost();
        fragment.setArguments(bundle);
        transaction.replace(R.id.container, fragment);
        transaction.commit();
    }

    public void setFragToConversations() {
        android.app.FragmentManager manager = getFragmentManager();
        android.app.FragmentTransaction transaction = manager.beginTransaction();

        Conversations fragment = new Conversations();
        transaction.replace(R.id.container, fragment);
        transaction.commit();
    }
}
