package com.charsunny.poem;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * Created by zjj on 16/2/19.
 */
public class MyDiscorveryStaggeredAdapter extends RecyclerView.Adapter<MyDiscorveryStaggeredAdapter.ViewHolder> {

    private final List<PoemEntity> mValues;
    private final DiscorveryFragment.OnFragmentInteractionListener mListener;
    //public static ArrayList<Integer> colors = new ArrayList<Integer>();
    public MyDiscorveryStaggeredAdapter(List<PoemEntity> items, DiscorveryFragment.OnFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.content_discovery_cell, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyDiscorveryStaggeredAdapter.ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mTitleView.setText(mValues.get(position).name);
        //holder.mPoetView.setText("");//AuthorEntity.getPoetById(mValues.get(position).aid)
        holder.mContentView.setText(mValues.get(position).content);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mTitleView;
        public final TextView mPoetView;
        public final TextView mContentView;
        public PoemEntity mItem;


        public ViewHolder(View view) {
            super(view);
            mView = view;
            ArrayList<Integer> colors = new ArrayList<Integer>();
//            colors.add(0xffFFC1C1);
//            colors.add(0xffFFDAB9);
//            colors.add(0xffC1FFC1);
//            colors.add(0xffCCCCCC);
//            colors.add(0xffEEAEEE);
            colors.add(0xffAEEEEE);
//            mView.setBackgroundColor(colors.get((int)(Math.random()*100) % colors.size()));
            mView.setBackgroundColor(colors.get(0));
            mTitleView = (TextView) view.findViewById(R.id.title);
            mPoetView = (TextView) view.findViewById(R.id.poet);
            mContentView = (TextView) view.findViewById(R.id.poem_content);
            FontManager.sharedInstance(null).applyFont(mTitleView, mPoetView, mContentView);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }

}




