package com.hackernews.macintoshuser.hackernews.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.hackernews.macintoshuser.hackernews.R;

/**
 * Created by macintoshuser on 8/12/16.
 */
public class FragmentDetail extends Fragment{

    WebView viewer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        Bundle bundle = getArguments();
        String url = bundle.getString("url");
        viewer = (WebView)view.findViewById(R.id.webView1);

        this.goToLink(url);
        return view;
    }

    public void goToLink(String url){
        viewer.loadUrl(url);
    }
}
