package me.amyhgu.parstagram;

import android.widget.ImageView;

import me.amyhgu.parstagram.model.Post;

public class PostHelper {

    public void favoriteItem(Post post) {
        post.setIsFave();
        post.setNumFaves();
    }
}
