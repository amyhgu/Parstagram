package me.amyhgu.parstagram;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.lang.ref.WeakReference;
import java.util.List;

import me.amyhgu.parstagram.model.Post;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    private List<Post> mPosts;
    Context context;

    // pass in Tweets array in constructor
    public PostAdapter(List<Post> posts) {
        mPosts = posts;
    }

    // for each row, inflate the layout and cache references into ViewHolder
    // only called when creating new rows
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View tweetView = inflater.inflate(R.layout.item_post, parent, false);
        ViewHolder viewHolder = new ViewHolder(tweetView);
        return viewHolder;
    }


    // bind value of Tweet object based on position of element
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // get data according to position
        Post post = mPosts.get(position);

        // populate views according to the data
        holder.tvUsername.setText(post.getUser().getUsername());
        holder.tvDescription.setText(post.getDescription());

//        // Load favorite icon
//        if (tweet.isFavorited()) {
//            helper.favoriteImage(holder.ivFavorite);
//        } else {
//            helper.unfavoriteImage(holder.ivFavorite);
//        }
//
//        // Load retweet icon
//        if (tweet.isRetweeted()) {
//            helper.retweetOn(holder.ivRetweet);
//        } else {
//            helper.retweetOff(holder.ivRetweet);
//        }

        Glide.with(context)
                .load(post.getImage().getUrl())
                .into(holder.ivPicture);
    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    // create ViewHolder class

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView ivPicture;
        TextView tvUsername;
        TextView tvDescription;

        public ViewHolder(View itemView) {
            super(itemView);
            // perform findViewById lookups
            tvUsername = (TextView) itemView.findViewById(R.id.tvUsername);
            tvDescription = (TextView) itemView.findViewById(R.id.tvDescription);
            ivPicture = (ImageView) itemView.findViewById(R.id.ivPicture);

            itemView.setOnClickListener(this);
        }

        public void onClick(View view) {
            // gets item position
            int position = getAdapterPosition();
            // make sure the position is valid, i.e. actually exists in the view
            if (position != RecyclerView.NO_POSITION) {
                // get the tweet at the position, this won't work if the class is static
                Post post = mPosts.get(position);
//                if (view.getId() == ivCompose.getId()) {
//                    listenerRef.get().onComposeClicked(position, tweet.getUser().getScreenName());
//                } else if (view.getId() == ivFavorite.getId()) {
//                    helper.favoriteItem(tweet, client, ivFavorite);
//                } else if (view.getId() == ivRetweet.getId()) {
//                    helper.retweetItem(tweet, client, ivRetweet);
//                } else {
//                    // create intent for the new activity
//                    Intent intent = new Intent(context, DetailsActivity.class);
//                    // serialize the tweet using parceler, use its short name as a key
//                    intent.putExtra(Tweet.class.getSimpleName(), Parcels.wrap(tweet));
//                    // show the activity
//                    context.startActivity(intent);
//                }
            }
        }
    }


    // Clean all elements of the recycler
    public void clear() {
        mPosts.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Post> list) {
        mPosts.addAll(list);
        notifyDataSetChanged();
    }
}

