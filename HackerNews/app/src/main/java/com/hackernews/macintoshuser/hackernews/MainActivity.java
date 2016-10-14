package com.hackernews.macintoshuser.hackernews;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.firebase.client.Firebase;
import com.hackernews.macintoshuser.hackernews.fragments.FragmentDetail;
import com.hackernews.macintoshuser.hackernews.fragments.FragmentList;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialize();
        showListFragment();
    }

    private void initialize(){
        Firebase.setAndroidContext(this);
    }

    private void showListFragment(){
        FragmentList fragment = new FragmentList();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                .beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment).addToBackStack("List");

        fragmentTransaction.commit();
    }

    public void showFragmentDetail(String url){
        FragmentDetail fragment = new FragmentDetail();
        Bundle args = new Bundle();
        args.putString("url", url);
        fragment.setArguments(args);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                .beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment).addToBackStack("Detail");
        fragmentTransaction.commit();
    }
}
