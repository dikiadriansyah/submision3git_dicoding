package com.example.submision3fundamental.views.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.example.submision3fundamental.R;
import com.example.submision3fundamental.viewmodels.DetailViewModels;
import com.example.submision3fundamental.views.adapter.FollowAdapter;

import java.util.Objects;

public class FollowFragment extends Fragment {
    private RecyclerView rootView;
    private String username, queryType;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_follow, container, false);
        rootView = view.findViewById(R.id.list_follow_user);
        rootView.addItemDecoration(new DividerItemDecoration(Objects.requireNonNull(getActivity()), DividerItemDecoration.VERTICAL));

        assert getArguments() != null;
        username = getArguments().getString("username");
        queryType = getArguments().getString("query");

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        FollowAdapter adapter = new FollowAdapter();
        rootView.setAdapter(adapter);

        DetailViewModels detailViewModels = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(DetailViewModels.class);
        detailViewModels.fetchFollow(username, queryType);
        detailViewModels.getUsers().observe(getViewLifecycleOwner(), adapter::setData);
    }
}
