package com.example.chenyong.demoanimation;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.chenyong.demoanimation.adapter.RecyclerViewAdapter;
import com.example.chenyong.demoanimation.databinding.ActivityTransitionFromBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by focus on 16/11/16.
 */

public class TransitionFromActivity extends AppCompatActivity implements RecyclerViewAdapter.OnItemClickListener{
    private static List<ViewModel> items = new ArrayList<>();

    static {
        for (int i = 1; i <= 10; i++) {
            items.add(new ViewModel("Item " + i, "http://lorempixel.com/500/500/animals/" + i));
        }
    }
    private ActivityTransitionFromBinding mBinding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_transition_from);
        mBinding.recycler.setLayoutManager(new GridLayoutManager(this, 2));
    }

    @Override
    public void onEnterAnimationComplete() {
        super.onEnterAnimationComplete();
        setRecyclerAdapter(mBinding.recycler);
    }
    private void setRecyclerAdapter(RecyclerView recyclerView) {
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(items);
        adapter.setOnItemClickListener(this);
        recyclerView.setAdapter(adapter);
    }


    @Override
    public void onItemClick(View view, ViewModel viewModel) {
        TransitionToActivity.startActivity(TransitionFromActivity.this, view, viewModel);
    }
}
