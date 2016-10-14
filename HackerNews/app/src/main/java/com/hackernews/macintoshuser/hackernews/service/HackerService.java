package com.hackernews.macintoshuser.hackernews.service;

import android.content.Context;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hackernews.macintoshuser.hackernews.R;
import com.hackernews.macintoshuser.hackernews.dto.StoryDAO;
import com.hackernews.macintoshuser.hackernews.dto.StoryDTO;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * Created by macintoshuser on 8/12/16.
 */
public class HackerService extends SecureServiceClient{

    public static String[] getAllStoriesIds(Context ctx) {
        // Create a new RestTemplate instance
        try {
            if(sClient==null) {
                sClient=new DefaultHttpClient();

                ClientConnectionManager mgr = sClient.getConnectionManager();
                HttpParams params = new BasicHttpParams();
                params.setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
                sClient = new DefaultHttpClient(new ThreadSafeClientConnManager(params,
                        mgr.getSchemeRegistry()), params);

            }

            String url=ctx.getResources().getString(R.string.domain)+"/v0/topstories.json?pretty";
            HttpGet post=new HttpGet(url);

            // Execute HTTP Post Request
            HttpResponse response = sClient.execute(post);
            if(response.getStatusLine().getStatusCode()==200) {
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                String line;
                StringBuilder builder = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
                content.close();
                String data = builder.toString();
                data = data.replace("[", "");
                data = data.replace("]", "");
                String dataId[] = data.split(",");
                //return Arrays.asList(mapper.readValue(builder.toString(), StoryDTO.class));


                return dataId;
            }else
                return null;
        } catch (Exception e) {
            String exception = e.getLocalizedMessage();
            return null;
        }

        /*String url=ctx.getResources().getString(R.string.domain)+"/v0/topstories";
        Firebase ref = new Firebase(url);
        // Attach an listener to read the data at our posts reference
        final StoryDTO[] story = new StoryDTO[1];
        ref.addValueEventListener(new ValueEventListener() {
            //StoryDTO story;
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                //for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                story[0] = snapshot.getValue(StoryDTO.class);
                //}
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });
        return null;*/
    }

    public static StoryDTO getStory(Context ctx, String id) {
        String url = ctx.getResources().getString(R.string.domain)+"/v0/item/"+id;
        Firebase ref = new Firebase(url);
        // Attach an listener to read the data at our posts reference
        final StoryDTO[] story = new StoryDTO[1];
        ref.addValueEventListener(new ValueEventListener() {
            //StoryDTO story;
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                //for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    story[0] = snapshot.getValue(StoryDTO.class);
                //}
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });

        while(story[0] == null){}
        return story[0];
        //return null;
    }

    public static List<StoryDTO> getTopTenStories(Context ctx, String dataId[]) {
        // Create a new RestTemplate instance
        List<StoryDTO> stories = new ArrayList<StoryDTO>();
        for(int i=0;i<10;i++) {
            StoryDTO story = HackerService.getStory(ctx, dataId[i]);
            stories.add(story);

            StoryDAO dao = new StoryDAO(ctx);
            dao.open();
            dao.insertStory(story);
            dao.close();
        }

        return stories;
    }

    public static List<StoryDTO> getTenStories(Context ctx, String dataId[], int start_from) {
        // Create a new RestTemplate instance
        List<StoryDTO> stories = new ArrayList<StoryDTO>();
        int end = dataId.length - start_from;
        if(end > 10) {
            end = start_from + 10;
        }

        for(int i=start_from;i<start_from+10;i++) {
            StoryDTO story = HackerService.getStory(ctx, dataId[i]);
            stories.add(story);

            StoryDAO dao = new StoryDAO(ctx);
            dao.open();
            dao.insertStory(story);
            dao.close();
        }

        return stories;
    }



}
