package me.amyhgu.parstagram;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import me.amyhgu.parstagram.model.Post;

public class FeedActivity extends AppCompatActivity {

    RecyclerView rvPosts;
    static ArrayList<Post> posts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        rvPosts = (RecyclerView) findViewById(R.id.rvPosts);
        posts = new ArrayList<>();
    }
}
