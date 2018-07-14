package com.example.arce.instagram;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;

import com.example.arce.instagram.model.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity {

    static final int MAX_CHAT_MESSAGES_TO_SHOW = 25;

    @BindView(R.id.rvPosts) RecyclerView rvPosts;
    ArrayList<Post> myPosts;
    PostAdapter adapter;
    boolean mFirstLoad=true;
    static final int POLL_INTERVAL = 1000; // milliseconds

    SwipeRefreshLayout swipeContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.nux_dayone_landing_logo);
        getSupportActionBar().setDisplayUseLogoEnabled(true);


        swipeContainer=(SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        //swipe container listener
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                fetchTimelineAsync(0);
                // Toast.makeText(getApplicationContext(),"Got it",Toast.LENGTH_LONG).show();
            }
        });

        //Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

       // final String User = ParseUser.getCurrentUser().getObjectId();
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);

        myHandler.postDelayed(mRefreshMessagesRunnable, POLL_INTERVAL);

        //initialize ArrayList and Adapter
        myPosts = new ArrayList<>();
        adapter = new PostAdapter(myPosts);

        //Recycler view setup (layout manager and adapter)
        rvPosts.setLayoutManager(new LinearLayoutManager(this));
        //set the adapter
        rvPosts.setAdapter(adapter);

        refreshMessages();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent;
                switch (item.getItemId()) {
                    case R.id.action_home:
                        return true;
                    case R.id.action_logOut:
                        intent = new Intent(HomeActivity.this,LogoutActivity.class);
                        startActivity(intent);
                        return true;
                    case R.id.action_photo:
                        intent = new Intent(HomeActivity.this,PhotoActivity.class);
                        startActivity(intent);
                        return true;
                    default:
                            return true;
                }
            }
        });

    }


    /*
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        myPosts = new ArrayList<>();
        adapter = new PostAdapter(myPosts);
    }

    public View onCreateView LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_home, parent, false);

        @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Recycler view setup (layout manager and adapter)
        rvPosts.setLayoutManager(new LinearLayoutManager(this));
        //set the adapter
        rvPosts.setAdapter(adapter);
         swipeContainer=(SwipeRefreshLayout) findViewById(R.id.swipeContainer);
         BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
    }
     */

    public void fetchTimelineAsync(int page) {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        // Configure limit and sort order
        query.setLimit(MAX_CHAT_MESSAGES_TO_SHOW);

        // get the latest 50 messages, order will show up newest to oldest of this group
        query.orderByDescending("createdAt");
        // Execute query to fetch all messages from Parse asynchronously
        // This is equivalent to a SELECT query with SQL
        query.findInBackground(new FindCallback<Post>() {
            public void done(List<Post> messages, ParseException e) {
                if (e == null) {
                    myPosts.clear();
                    myPosts.addAll(messages);
                    adapter.notifyDataSetChanged(); // update adapter
                } else {
                    Log.e("message", "Error Loading Messages" + e);
                }
            }
        });
        swipeContainer.setRefreshing(false);
    }

    // Query messages from Parse so we can load them into the chat adapter
    void refreshMessages() {
        // Construct query to execute
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        // Configure limit and sort order
        query.setLimit(MAX_CHAT_MESSAGES_TO_SHOW);

        // get the latest 50 messages, order will show up newest to oldest of this group
        query.orderByDescending("createdAt");
        // Execute query to fetch all messages from Parse asynchronously
        // This is equivalent to a SELECT query with SQL
        query.findInBackground(new FindCallback<Post>() {
            public void done(List<Post> messages, ParseException e) {
                if (e == null) {
                    myPosts.clear();
                    myPosts.addAll(messages);
                    adapter.notifyDataSetChanged(); // update adapter
                    // Scroll to the bottom of the list on initial load
                    if (mFirstLoad) {
                        rvPosts.scrollToPosition(0);
                        mFirstLoad = false;
                    }
                } else {
                    Log.e("message", "Error Loading Messages" + e);
                }
            }
        });
    }

    Handler myHandler = new Handler();  // android.os.Handler
    Runnable mRefreshMessagesRunnable = new Runnable() {
        @Override
        public void run() {
            refreshMessages();
            myHandler.postDelayed(this, POLL_INTERVAL);
        }
    };
}
