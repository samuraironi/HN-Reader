package com.hackernews.macintoshuser.hackernews.fragments;

import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.hackernews.macintoshuser.hackernews.MainActivity;
import com.hackernews.macintoshuser.hackernews.R;
import com.hackernews.macintoshuser.hackernews.adapter.HackerNewsAdapter;
import com.hackernews.macintoshuser.hackernews.dto.StoryDAO;
import com.hackernews.macintoshuser.hackernews.dto.StoryDTO;
import com.hackernews.macintoshuser.hackernews.service.HackerService;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by macintoshuser on 8/12/16.
 */
public class FragmentList extends android.support.v4.app.Fragment implements AdapterView.OnItemClickListener, AbsListView.OnScrollListener{

    FragmentTransaction fragmentTransaction;
    ListView list;
    private ProgressDialog dialog;
    private List<StoryDTO> stories;
    Handler handler;
    String storiesIds[];
    private boolean lastItem = false;
    private int lastItemCount = 10;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialize(view);
    }

    private void initialize(View view){
        handler = new Handler();
        stories = new ArrayList<StoryDTO>();
        GetStoriesTask task = new GetStoriesTask(0);
        task.execute();
        list = (ListView)view.findViewById(R.id.fragment_list);
        list.setOnItemClickListener(this);
        list.setOnScrollListener(this);
    }



    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        StoryDTO story = (StoryDTO) parent.getAdapter().getItem(position);

        StoryDAO dao = new StoryDAO(getActivity());
        dao.open();
        dao.setStoryViewed(story.getId());
        dao.close();

        ((MainActivity) getActivity()).showFragmentDetail(story.getUrl());
    }

    @Override
    public void onScroll(AbsListView lw, final int firstVisibleItem,
                         final int visibleItemCount, final int totalItemCount) {

                final int lastItem = firstVisibleItem + visibleItemCount;
                if(lastItem == totalItemCount) {
                    this.lastItem = true;
                    lastItemCount = totalItemCount;
                }

    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
            if(lastItem)
            {
                if(storiesIds != null) {
                    GetOtherStoriesTask task = new GetOtherStoriesTask(lastItemCount, storiesIds);
                    task.execute();
                    lastItem = false;
                }
            }
        }

    }

    public class GetStoriesTask extends AsyncTask<Void, Void, Boolean> {


        //List<StoryDTO> stories;
        Handler handler;
        int start_count = 0;

        GetStoriesTask(int count) {
            dialog = new ProgressDialog(getActivity());
            //stories = new ArrayList<StoryDTO>();
            handler = new Handler();
            start_count = count;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage(getActivity().getResources().getString(R.string.loading));
            dialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            storiesIds = HackerService.getAllStoriesIds(getActivity().getApplication().getApplicationContext());
            if(storiesIds == null)
                return false;
            else {
                stories = HackerService.getTopTenStories(getActivity().getApplicationContext(), storiesIds);
                return true;
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {

            if(dialog != null)
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }

            if(success){
                HackerNewsAdapter adapter = new HackerNewsAdapter(getActivity().getApplicationContext(), stories);
                list.setAdapter(adapter);

                int count_of_get = storiesIds.length / 10;
                for(int i = 10;i<count_of_get;i=i+10)
                {
                    //stories.addAll(HackerService.getTenStories(getActivity().getApplicationContext(), storiesIds, i));
                    //((HackerNewsAdapter)list.getAdapter()).notifyDataSetChanged();
                    //GetOtherStoriesTask task = new GetOtherStoriesTask(i, storiesIds);
                    //task.execute();
                }
            }else{

            }
        }

        @Override
        protected void onCancelled() {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }

    public class GetOtherStoriesTask extends AsyncTask<Void, Void, Boolean> {

        String storiesIds[];
        List<StoryDTO> tenStories;
        int start_count = 0;

        GetOtherStoriesTask(int count, String stories_Ids[]) {
            this.tenStories = new ArrayList<StoryDTO>();
            handler = new Handler();
            start_count = count;
            this.storiesIds = stories_Ids;
            dialog = new ProgressDialog(getActivity());

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage(getActivity().getResources().getString(R.string.loading));
            dialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            if(storiesIds == null)
                return false;
            else {
                this.tenStories = HackerService.getTenStories(getActivity().getApplicationContext(), storiesIds, start_count);
                return true;
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if(dialog != null)
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }

            stories.addAll(this.tenStories);
            ((HackerNewsAdapter) list.getAdapter()).notifyDataSetChanged();
        }

        @Override
        protected void onCancelled() {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }

}
