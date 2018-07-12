package me.amyhgu.parstagram.model;

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

import java.lang.ref.WeakReference;
import java.util.List;

import me.amyhgu.parstagram.DetailsActivity;
import me.amyhgu.parstagram.PostAdapter;
import me.amyhgu.parstagram.R;

public class GridAdapter extends RecyclerView.Adapter<GridAdapter.ViewHolder> {

    private List<Post> mPosts;
    Context context;

    public GridAdapter(List<Post> posts) {
        mPosts = posts;
    }

    @NonNull
    @Override
    public GridAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View postView = inflater.inflate(R.layout.item_grid, parent, false);
        GridAdapter.ViewHolder viewHolder = new GridAdapter.ViewHolder(postView);
        return viewHolder;
    }

    // bind value of Tweet object based on position of element
    @Override
    public void onBindViewHolder(@NonNull GridAdapter.ViewHolder holder, int position) {
        // get data according to position
        Post post = mPosts.get(position);

        Glide.with(context)
                .load(post.getImage().getUrl())
                .into(holder.ivPicture);
    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView ivPicture;
        private WeakReference<ClickListener> listenerRef;

        public ViewHolder(View itemView) {
            super(itemView);
            // perform findViewById lookups
            ivPicture = (ImageView) itemView.findViewById(R.id.ivPicture);
            itemView.setOnClickListener(this);
        }

        public void onClick(View view) {
//            // gets item position
//            int position = getAdapterPosition();
//            // make sure the position is valid, i.e. actually exists in the view
//            if (position != RecyclerView.NO_POSITION) {
//                // get the tweet at the position, this won't work if the class is static
//                Post post = mPosts.get(position);
//                // create intent for the new activity
//                Intent intent = new Intent(context, DetailsActivity.class);
//                // pass extras
//                intent.putExtra("username", post.getUser().getUsername());
//                intent.putExtra("description", post.getDescription());
//                intent.putExtra("image", post.getImage().getUrl());
//                intent.putExtra("timestamp", post.getRelativeTimestamp());
//
//                ParseFile propic = post.getUser().getParseFile("propic");
//                String propicUrl;
//                if (propic != null) {
//                    propicUrl = post.getUser().getParseFile("propic").getUrl();
//                } else {
//                    propicUrl = null;
//                }
//                intent.putExtra("propic", propicUrl);
//                // show the activity
//                context.startActivity(intent);
//            }
        }
    }
}
