package me.amyhgu.parstagram;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseException;

import java.util.ArrayList;
import java.util.List;

import me.amyhgu.parstagram.model.Post;

public class FeedActivity extends AppCompatActivity {

    RecyclerView rvPosts;
    static ArrayList<Post> posts;
    private PostAdapter postAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        rvPosts = (RecyclerView) findViewById(R.id.rvPosts);
        posts = new ArrayList<>();

        postAdapter = new PostAdapter(posts);
        // set up RecyclerView (layout manager, use adapter)
        rvPosts.setLayoutManager(new LinearLayoutManager(this));
        // set adapter
        rvPosts.setAdapter(postAdapter);

        loadTopPosts();
    }

    private void loadTopPosts() {
        final Post.Query postsQuery = new Post.Query();
        postsQuery.getTop().withUser();

        postsQuery.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                if (e == null) {
                    for (int i = 0; i < objects.size(); i++) {
                        Log.d("HomeActivity", "Post[" + i + "] = "
                                + objects.get(i).getDescription()
                                + "\nusername = " + objects.get(i).getUser().getUsername());
                        Post post = new Post();
                        post.setUser(objects.get(i).getUser());
                        post.setImage(objects.get(i).getImage());
                        post.setDescription(objects.get(i).getDescription());
                        posts.add(post);
                        postAdapter.notifyItemInserted(posts.size() - 1);
                    }
                } else {
                    e.printStackTrace();
                }
            }
        });
    }
}
