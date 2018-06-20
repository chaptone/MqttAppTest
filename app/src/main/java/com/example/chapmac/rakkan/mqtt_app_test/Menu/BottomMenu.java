package com.example.chapmac.rakkan.mqtt_app_test.Menu;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chapmac.rakkan.mqtt_app_test.MainActivity;
import com.example.chapmac.rakkan.mqtt_app_test.R;
import com.example.chapmac.rakkan.mqtt_app_test.Subscribe.SubscribeAdapter;
import com.example.chapmac.rakkan.mqtt_app_test.Subscribe.SubscribeDialog;
import com.example.chapmac.rakkan.mqtt_app_test.Subscribe.SubscribeItem;

import java.util.ArrayList;

public class BottomMenu extends BottomSheetDialogFragment {

    private BottomMenuListener bottomMenuListener;

    private ArrayList<BottomMenuItem> bottomMenuItems;

    private BottomMenuAdapter bottomMenuAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bottom_menu,container,false);

        bottomMenuItems = new ArrayList<>();
        bottomMenuItems.add(new BottomMenuItem(R.drawable.ic_cloud_done, MainActivity.CLIENT.getServerURI(),R.drawable.ic_info));
        bottomMenuItems.add(new BottomMenuItem(R.drawable.ic_cloud_off, "Disconnect",0));

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        bottomMenuAdapter = new BottomMenuAdapter(bottomMenuItems);
        recyclerView.setAdapter(bottomMenuAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        bottomMenuAdapter.setOnCilckItemListener(new BottomMenuAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if(position == 1) {
                    bottomMenuListener.applyTextsFromBottomMenu("Disconnect");
                }
            }
            @Override
            public void onDeleteClick(int position) {
                dismiss();
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            bottomMenuListener = (BottomMenuListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString()+" must implement DialogListener");
        }
    }

    public interface BottomMenuListener{
        void applyTextsFromBottomMenu(String inform);
    }

}
