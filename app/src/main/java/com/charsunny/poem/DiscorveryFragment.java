package com.charsunny.poem;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.victor.loading.book.BookLoading;

import java.util.ArrayList;
import java.util.zip.Inflater;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DiscorveryFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DiscorveryFragment#*newInstance} factory method to
 * create an instance of this fragment.
 */
public class DiscorveryFragment extends Fragment{

    // TODO: Customize parameter argument names
    private static final String ARG_DISCORVERY_COUNT = "discorvery-count";
    // TODO: Customize parameters
    private int mDiscorveryCount = 2;
    private OnFragmentInteractionListener mListener;
    private BookLoading bookLoading;
    private MyDiscorveryStaggeredAdapter myDiscorveryStaggeredAdapter;
    private SwipeRefreshLayout mSwipeRefreshWidget;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private int[] colors = new int[]{R.color.discovery1, R.color.discovery2, R.color.discovery3,
            R.color.discovery3, R.color.discovery4, R.color.discovery5};

    private int loadedPage = 0;

    private boolean isLoading = false;

    private ArrayList<PoemEntity> mPoems = new ArrayList<PoemEntity>();


    public DiscorveryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mDiscorveryCount = getArguments().getInt(ARG_DISCORVERY_COUNT);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.content_discovery_list, container, false);

        final LinearLayoutManager mLayoutManager;

        getServerData();
        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            mListener = new OnFragmentInteractionListener() {
                @Override
                public void onFragmentInteraction(PoemEntity item) {
                    Intent it = new Intent(DiscorveryFragment.this.getActivity(), PoemActivity.class);
                    it.putExtra("pid", item.pid);
                    startActivity(it);
                }
            };
            staggeredGridLayoutManager = new StaggeredGridLayoutManager(mDiscorveryCount, StaggeredGridLayoutManager.VERTICAL);
            staggeredGridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);//防止位置发生变化
            recyclerView.setLayoutManager(staggeredGridLayoutManager);
            myDiscorveryStaggeredAdapter = new MyDiscorveryStaggeredAdapter(mPoems, mListener);
            recyclerView.setAdapter(myDiscorveryStaggeredAdapter);
            mLayoutManager = new LinearLayoutManager(this.getActivity());
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

                @Override
                public void onScrolled(RecyclerView rView, int dx, int dy) {
                    super.onScrolled(rView, dx, dy);

                    int lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
                    int totalItemCount = mLayoutManager.getItemCount();

                    if (lastVisibleItem >= totalItemCount - 4 && dy > 0) {
                        getServerData();
                    }
                }
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    //防止第一行到顶部有空白区域
                    staggeredGridLayoutManager.invalidateSpanAssignments();
                }
            });
        }
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void getServerData() {
        if(isLoading) {
            return;
        }
        isLoading = true;
        // 将 loadpage 组合到这个url中
        String url = "http://charsunny.ansinlee.com/feeds";
        if (this.loadedPage > 0) {
            url += "?page=" + this.loadedPage;
        }

        Ion.with(this).load(url).asJsonArray().setCallback(new FutureCallback<JsonArray>() {
            @Override
            public void onCompleted(Exception e, JsonArray result) {
                DiscorveryFragment.this.isLoading = false;
                if (e != null) {
//                    bookLoading.stop();
//                    bookLoading.setVisibility(View.GONE);
                    return;
                }
                DiscorveryFragment.this.loadedPage++;
                for (int i = 0; i < result.size(); i ++) {
                    JsonObject p = result.get(i).getAsJsonObject();
                    int j =  p.get("Id").getAsInt();
                    PoemEntity poem = PoemEntity.getPoemById(j);
                    mPoems.add(poem);
                }
                myDiscorveryStaggeredAdapter.notifyDataSetChanged();

//                bookLoading.stop();
//                bookLoading.setVisibility(View.GONE);
            }
        });
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(PoemEntity item);
    }

}
