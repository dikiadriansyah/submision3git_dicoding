package com.example.submision3fundamental.views.adapter;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.submision3fundamental.DatabaseApp;
import com.example.submision3fundamental.R;
import com.example.submision3fundamental.db.UserDAO;
import com.example.submision3fundamental.models.User;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder> {
    private ArrayList<User> listUser = new ArrayList<>();
    private OnItemClickCallback onItemClickCallback;

    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    public void setData(ArrayList<User> items) {
        listUser.clear();
        listUser.addAll(items);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favorite_item, parent, false);
        return new FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int position) {
        holder.setItem(listUser.get(position));

        holder.itemView.setOnClickListener(view -> onItemClickCallback.onItemClicked(listUser.get(holder.getAdapterPosition())));
    }

    @Override
    public int getItemCount() {
        return listUser.size();
    }

    class FavoriteViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.fav_txt_htmlUrl) TextView html_url;
        @BindView(R.id.favo_txt_username) TextView username;
        @BindView(R.id.favo_img_avatar) ImageView avatar;
        @BindView(R.id.fav_btn_delete_fav) Button btnDeleteFav;

        FavoriteViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void setItem(User item) {
            html_url.setText(item.getHtmlUrl());
            username.setText(item.getUsername());
            Picasso.get()
                    .load(item.getAvatar())
                    .resize(100, 100)
                    .centerCrop()
                    .into(avatar);

            btnDeleteFav.setOnClickListener(view -> {
                final AlertDialog.Builder alert = new AlertDialog.Builder(itemView.getContext());
                alert.setTitle(R.string.alert_delete);
                alert.setMessage(R.string.confirm_delete);
                alert.setCancelable(false);
                alert.setPositiveButton(R.string.yes, (dialogInterface, i) -> {
                    UserDAO userDAO = Room.databaseBuilder(itemView.getContext(), DatabaseApp.class, "user")
                            .allowMainThreadQueries()
                            .build()
                            .userDAO();

                    userDAO.deleteByUsername(item.getUid());

                    listUser.remove(item);
                    notifyDataSetChanged();

                    Snackbar.make(view, R.string.delete_success, Snackbar.LENGTH_SHORT).show();
                });
                alert.setNegativeButton(R.string.no, ((dialogInterface, i) -> alert.setCancelable(true)));
                alert.show();
            });
        }
    }

    public interface OnItemClickCallback {
        void onItemClicked(User data);
    }
}
