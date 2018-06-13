package com.example.chapmac.rakkan.mqtt_app_test;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class SubscribeFragment extends Fragment {
    private ArrayList<SubscribeItem> subscribeItems;

    private RecyclerView recyclerView;
    private Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    public SubscribeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_subscribe, container, false);

        loadData();

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        adapter = new Adapter(subscribeItems);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        adapter.setOnCilckItemListener(new Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                subscribeItems.get(position).changeText1("Click");
                adapter.notifyItemChanged(position);
            }
            @Override
            public void onDeleteClick(int position) {
                subscribeItems.remove(position);
                adapter.notifyItemRemoved(position);
            }
        });

        return view;
    }

    public void addSub(String topic) {
        subscribeItems.add(new SubscribeItem(R.drawable.ic_local_offer, topic, "Line2"));
        adapter.notifyItemInserted(subscribeItems.size());
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Shared preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(subscribeItems);
        editor.putString("task list",json);
        editor.apply();
    }

    public void loadData(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Shared preferences", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("task list",null);
        Type type = new TypeToken<ArrayList<SubscribeItem>>() {}.getType();
        subscribeItems = gson.fromJson(json,type);

        if(subscribeItems == null){
            subscribeItems = new ArrayList<>();
        }
    }
}
