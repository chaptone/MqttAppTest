package com.example.chapmac.rakkan.mqtt_app_test;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class SubscribeFragment extends Fragment {
    private ArrayList<SubscribeItem> subscribeItems;

    private RecyclerView recyclerView;
    private Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private DatabaseReference mDatabase;
    private ListView listView;

    public SubscribeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i("Check","OnDestroyView");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_subscribe, container, false);

        Log.i("Check","OnCreateView");

        mDatabase = FirebaseDatabase.getInstance().getReference().child("connect").child("sub");

//        loadData();
//        subscribeItems = new ArrayList<>();
//
//        recyclerView = view.findViewById(R.id.recyclerView);
//        recyclerView.setHasFixedSize(true);
//        layoutManager = new LinearLayoutManager(getActivity());
//        adapter = new Adapter(subscribeItems);
//
//        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.setAdapter(adapter);
        listView = view.findViewById(R.id.listView);
        FirebaseListAdapter<String> firebaseListAdapter = new FirebaseListAdapter<String>(
                getActivity(),String.class,android.R.layout.simple_list_item_2,mDatabase
        ){

            @Override
            protected void populateView(View v, String model, int position) {
                TextView textView = v.findViewById(android.R.id.text1);
                textView.setText(model);
                TextView textView2 = v.findViewById(android.R.id.text2);
                textView2.setText("555");
            }
        };
        listView.setAdapter(firebaseListAdapter);

//        adapter.setOnCilckItemListener(new Adapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(int position) {
//                subscribeItems.get(position).changeText1("Click");
//                adapter.notifyItemChanged(position);
//            }
//            @Override
//            public void onDeleteClick(int position) {
//                subscribeItems.remove(position);
//                adapter.notifyItemRemoved(position);
//            }
//        });

//        mDatabase.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                String topic = dataSnapshot.getValue(String.class);
//                subscribeItems.add(new SubscribeItem(R.drawable.ic_local_offer, topic, "Line2"));
//                adapter.notifyItemInserted(subscribeItems.size());
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });

        return view;
    }

    public void addSub(String topic) {
//        subscribeItems.add(new SubscribeItem(R.drawable.ic_local_offer, topic, "Line2"));
//        adapter.notifyItemInserted(subscribeItems.size());
//        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Shared preferences", Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        Gson gson = new Gson();
//        String json = gson.toJson(subscribeItems);
//        editor.putString("task list",json);
//        editor.apply();
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
