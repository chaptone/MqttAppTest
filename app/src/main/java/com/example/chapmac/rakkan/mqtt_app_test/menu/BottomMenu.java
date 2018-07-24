package com.example.chapmac.rakkan.mqtt_app_test.menu;

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

import com.amulyakhare.textdrawable.TextDrawable;
import com.example.chapmac.rakkan.mqtt_app_test.R;

import java.util.ArrayList;

import static com.example.chapmac.rakkan.mqtt_app_test.main.LoadingActivity._PREFER;

public class BottomMenu extends BottomSheetDialogFragment {

    private BottomMenuListener bottomMenuListener;

    private ArrayList<BottomMenuItem> bottomMenuList;

    private BottomMenuAdapter bottomMenuAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bottom_menu,container,false);

        String name = _PREFER.getConnection().getName();
        String host = "tcp://"+ _PREFER.getConnection().getHost()+":"+ _PREFER.getConnection().getPort();
        String letter = _PREFER.getConnection().getName().substring(0,1).toUpperCase();

        int color = _PREFER.getConnection().getColor();
        TextDrawable drawable = TextDrawable.builder().buildRound(letter, color);

        bottomMenuList = new ArrayList<>();
        bottomMenuList.add(new BottomMenuItem(1,0,name,host,R.drawable.ic_info));
        bottomMenuList.add(new BottomMenuItem(0,0, "Disconnect","",0));

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        bottomMenuAdapter = new BottomMenuAdapter(bottomMenuList);
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
