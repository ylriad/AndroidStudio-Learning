package com.example.tan_lab07;

import android.view.*;
import android.widget.*;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide; // if using Glide
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private List<GitHubUser> users;

    public UserAdapter(List<GitHubUser> users) {
        this.users = users;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        GitHubUser user = users.get(position);
        holder.tvLogin.setText(user.getLogin());

        // Load avatar with Glide (handles caching, threading)
        Glide.with(holder.ivAvatar.getContext())
                .load(user.getAvatarUrl())
                .circleCrop()
                .into(holder.ivAvatar);

        // Optional: open profile on click
        holder.itemView.setOnClickListener(v -> {
            // Could open browser with user.getHtmlUrl()
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        ImageView ivAvatar;
        TextView tvLogin;

        UserViewHolder(View itemView) {
            super(itemView);
            ivAvatar = itemView.findViewById(R.id.ivAvatar);
            tvLogin = itemView.findViewById(R.id.tvLogin);
        }
    }
}