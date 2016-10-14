package com.hackernews.macintoshuser.hackernews.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hackernews.macintoshuser.hackernews.R;
import com.hackernews.macintoshuser.hackernews.dto.StoryDAO;
import com.hackernews.macintoshuser.hackernews.dto.StoryDTO;

import java.util.Date;
import java.util.List;

/**
 * Created by macintoshuser on 8/15/16.
 */
public class HackerNewsAdapter extends BaseAdapter{

    List<StoryDTO> data;
    private LayoutInflater mInflater;
    Context context;

    public HackerNewsAdapter(Context context, List<StoryDTO> data)
    {
        this.data = data;
        mInflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
    }


    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v;

        if(convertView == null)
            convertView = mInflater.inflate(R.layout.hacker_news_list_item, parent, false);

            TextView tvTitle = ((TextView)convertView.findViewById(R.id.tvName));
            String title = data.get(position).getTitle();
            tvTitle.setText("Title: " + title);
            ((TextView)convertView.findViewById(R.id.tvScore)).setText("Score: " + data.get(position).getScore());
            ((TextView)convertView.findViewById(R.id.tvCreator)).setText("Author: " + data.get(position).getBy());
            ((TextView)convertView.findViewById(R.id.tvCommentsNumber)).setText("Comments: " + data.get(position).getDescendants());

            Date date  = new Date(data.get(position).getTime());

            ((TextView)convertView.findViewById(R.id.tvPostedDate)).setText("Posted: " + date.toString());

        StoryDAO dao = new StoryDAO(context);
        dao.open();
        int viewed = dao.getViewStory(data.get(position).getId());
        dao.close();

        LinearLayout item = (LinearLayout)convertView.findViewById(R.id.story_item);
        if(viewed != 0)
        {
            item.setBackgroundColor(Color.GREEN);
        }
        else
        {
            item.setBackgroundColor(Color.WHITE);
        }

        return convertView;
    }
}
