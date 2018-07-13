package me.amyhgu.parstagram;

import android.widget.ImageView;

import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import me.amyhgu.parstagram.model.Post;

public class PostHelper {

    String username = ParseUser.getCurrentUser().getUsername();

    public void handleFaves(Post post, ImageView ivHeart) {
        List userFaves = post.getUserFaves();
        if (userFaves == null) {
            userFaves = new ArrayList();
            userFaves.add(username);
            post.setUserFaves(userFaves);

            ivHeart.setImageResource(R.drawable.ic_heart_active);
        } else if (userFaves.contains(username)) {
            userFaves.remove(username);
            post.setUserFaves(userFaves);

            ivHeart.setImageResource(R.drawable.ic_heart);
        } else {
            userFaves.add(username);
            post.setUserFaves(userFaves);

            ivHeart.setImageResource(R.drawable.ic_heart_active);
        }
    }

    public void setHeartImage(Post post, ImageView ivHeart) {
        List userFaves = post.getUserFaves();
        if (userFaves == null) {
            ivHeart.setImageResource(R.drawable.ic_heart);
        } else if (userFaves.contains(username)) {
            ivHeart.setImageResource(R.drawable.ic_heart_active);
        } else {
            ivHeart.setImageResource(R.drawable.ic_heart);
        }
    }

    public String getLikesString(Post post) {
        List faves = post.getUserFaves();
        int numLikes = (faves == null) ? 0 : faves.size();
        return String.format("%s likes", Integer.toString(numLikes));
    }
}
