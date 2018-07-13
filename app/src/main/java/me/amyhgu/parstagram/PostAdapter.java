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
import com.bumptech.glide.request.RequestOptions;
import com.parse.ParseFile;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.lang.ref.WeakReference;
import java.util.List;

import me.amyhgu.parstagram.model.ClickListener;
import me.amyhgu.parstagram.model.Post;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    private List<Post> mPosts;
    private final ClickListener listener;
    Context context;
    PostHelper helper;

    // pass in Tweets array in constructor
    public PostAdapter(List<Post> posts, ClickListener listener) {

        mPosts = posts;
        this.listener = listener;
    }

    // for each row, inflate the layout and cache references into ViewHolder
    // only called when creating new rows
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        helper = new PostHelper();

        View postView = inflater.inflate(R.layout.item_post, parent, false);
        ViewHolder viewHolder = new ViewHolder(postView, listener);
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
        holder.tvCommentName.setText(post.getUser().getUsername());

        Glide.with(context)
                .load(post.getImage().getUrl())
                .into(holder.ivPicture);

        ParseFile propic = post.getUser().getParseFile("propic");

        if (propic != null) {
            Glide.with(context)
                    .load(propic.getUrl())
                    .apply(RequestOptions.circleCropTransform())
                    .into(holder.ivPropic);
        } else {
            holder.ivPropic.setImageResource(R.drawable.ic_user_filled);
        }


    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    // create ViewHolder class

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView ivPicture;
        ImageView ivPropic;
        ImageView ivFavorite;
        TextView tvUsername;
        TextView tvDescription;
        TextView tvCommentName;
        private WeakReference<ClickListener> listenerRef;

        public ViewHolder(View itemView, ClickListener listener) {
            super(itemView);
            // perform findViewById lookups
            tvUsername = (TextView) itemView.findViewById(R.id.tvUsername);
            tvDescription = (TextView) itemView.findViewById(R.id.tvDescription);
            tvCommentName = (TextView) itemView.findViewById(R.id.tvCommentName);
            ivPicture = (ImageView) itemView.findViewById(R.id.ivPicture);
            ivPropic = (ImageView) itemView.findViewById(R.id.ivProfilePic);
            ivFavorite = (ImageView) itemView.findViewById(R.id.ivFavorite);
            listenerRef = new WeakReference<>(listener);

            itemView.setOnClickListener(this);
            ivPropic.setOnClickListener(this);
            ivFavorite.setOnClickListener(this);
        }

        public void onClick(View view) {
            // gets item position
            int position = getAdapterPosition();
            // make sure the position is valid, i.e. actually exists in the view
            if (position != RecyclerView.NO_POSITION) {
                // get the tweet at the position, this won't work if the class is static
                Post post = mPosts.get(position);
                if (view.getId() == ivPropic.getId()) {
                    listenerRef.get().onPropicClicked(post.getUser());
                } else if (view.getId() == ivFavorite.getId()) {
                    post.setIsFave();
                    post.setNumFaves();
//                } else if (view.getId() == ivRetweet.getId()) {
//                    helper.retweetItem(tweet, client, ivRetweet);
                } else {
                    // create intent for the new activity
                    Intent intent = new Intent(context, DetailsActivity.class);
                    // pass extras
                    intent.putExtra("username", post.getUser().getUsername());
                    intent.putExtra("description", post.getDescription());
                    intent.putExtra("image", post.getImage().getUrl());
                    intent.putExtra("timestamp", post.getRelativeTimestamp());

                    ParseFile propic = post.getUser().getParseFile("propic");
                    String propicUrl;
                    if (propic != null) {
                        propicUrl = post.getUser().getParseFile("propic").getUrl();
                    } else {
                        propicUrl = null;
                    }
                    intent.putExtra("propic", propicUrl);
                // show the activity
                    context.startActivity(intent);
                }
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

