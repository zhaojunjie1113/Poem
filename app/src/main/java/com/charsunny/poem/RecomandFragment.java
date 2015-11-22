package com.charsunny.poem;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.charsunny.poem.dummy.DummyContent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

/**
 * Created by junjiezhao on 15/11/22.
 */
public class RecomandFragment extends Fragment {

    private ListView listView;
    private View headerView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_main, container, false);
        // Set the adapter
        listView = (ListView)view.findViewById(R.id.listview);
        headerView = inflater.inflate(R.layout.recommand_header,listView,false);
        listView.addHeaderView(headerView);
        List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        for (int i =0; i < 20; i ++) {
            HashMap<String, String> item = new HashMap<String, String>();
            item.put("content", "test");
            item.put("detail", "detail" + i);
            list.add(item);
        }
        listView.setAdapter(new SimpleAdapter(getActivity(),list, R.layout.content_column, new String[]{"content", "detail"}, new int[]{R.id.id, R.id.content}));
        return view;
    }
}
