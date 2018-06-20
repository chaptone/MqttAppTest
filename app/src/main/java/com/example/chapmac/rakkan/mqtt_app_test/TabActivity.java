package com.example.chapmac.rakkan.mqtt_app_test;

import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.example.chapmac.rakkan.mqtt_app_test.Menu.BottomMenu;
import com.example.chapmac.rakkan.mqtt_app_test.Publish.PublishDialog;
import com.example.chapmac.rakkan.mqtt_app_test.Publish.PublishFragment;
import com.example.chapmac.rakkan.mqtt_app_test.Subscribe.SubscribeDialog;
import com.example.chapmac.rakkan.mqtt_app_test.Subscribe.SubscribeFragment;
import com.example.chapmac.rakkan.mqtt_app_test.Home.HomeFragment;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttException;

public class TabActivity extends AppCompatActivity implements
        SubscribeDialog.DialogListener,
        PublishDialog.DialogListener,
        BottomMenu.BottomMenuListener{

    FloatingActionButton fab,fab1,fab2;
    Animation fabOpen,fabClose,rotateForward,rotateBackward;
    boolean isOpen = false;

    private SubscribeFragment subscribeFragment;
    private PublishFragment publishFragment;
    private HomeFragment homeFragment;

    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
                Log.i("Check",tab.getPosition()+"");
                switch (tab.getPosition()){
                    case 0:
                        getSupportActionBar().setTitle("Home");
                        break;
                    case 1:
                        getSupportActionBar().setTitle("Publish");
                        break;
                    case 2:
                        getSupportActionBar().setTitle("Subscribe");
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        mViewPager.setOffscreenPageLimit(2);

        fab = findViewById(R.id.fab);
        fab1 = findViewById(R.id.fab1);
        fab2 = findViewById(R.id.fab2);

        fabOpen = AnimationUtils.loadAnimation(this,R.anim.fab_open);
        fabClose = AnimationUtils.loadAnimation(this,R.anim.fab_close);

        rotateForward = AnimationUtils.loadAnimation(this,R.anim.rotate_forward);
        rotateBackward = AnimationUtils.loadAnimation(this,R.anim.rotate_backward);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animationFab();
            }
        });
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animationFab();
                PublishDialog publishDialog = new PublishDialog();
                publishDialog.show(getSupportFragmentManager(),"Publish Dialog");
            }
        });
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animationFab();
                SubscribeDialog subscribeDialog = new SubscribeDialog();
                subscribeDialog.show(getSupportFragmentManager(),"Subscribe Dialog");
            }
        });
    }

    public void animationFab(){
        if (isOpen) {
            fab.startAnimation(rotateBackward);
            fab1.startAnimation(fabClose);
            fab2.startAnimation(fabClose);
            fab1.setClickable(false);
            fab2.setClickable(false);
            isOpen = false;
        } else {
            fab.startAnimation(rotateForward);
            fab1.startAnimation(fabOpen);
            fab2.startAnimation(fabOpen);
            fab1.setClickable(true);
            fab2.setClickable(true);
            isOpen = true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tab, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu) {
            BottomMenu bottomMenu = new BottomMenu();
            bottomMenu.show(getSupportFragmentManager(),"bottomMenu");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void applyTextsFromSubscribeDialog(String subscribe) {
        Toast.makeText(this, "Subscribe to " + subscribe, Toast.LENGTH_LONG).show();

        subscribeFragment.addSub(subscribe);
    }

    @Override
    public void applyTextsFromPublishDialog(String topic, String message) {
        Toast.makeText(this, "Publish topic " + topic + "/"+message, Toast.LENGTH_LONG).show();

        publishFragment.addPublisher(topic,message);
    }

    @Override
    public void applyTextsFromBottomMenu(String inform) {
        Toast.makeText(this, "Disconnected", Toast.LENGTH_LONG).show();

        disconnect();
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    homeFragment = new HomeFragment();
                    return homeFragment;
                case 1:
                    publishFragment = new PublishFragment();
                    return publishFragment;
                default:
                    subscribeFragment = new SubscribeFragment();
                    return subscribeFragment;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position){
                case 0 :
                    return "Home";
                case 1 :
                    return "Publish";
                case 2:
                    return "Subscribe";
            }
            return null;
        }
    }

    public void disconnect(){
        try {
            IMqttToken token = MainActivity.CLIENT.disconnect();
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Toast.makeText(TabActivity.this,"Disconnected!",Toast.LENGTH_LONG).show();
                    finish();
                }
                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Toast.makeText(TabActivity.this,"Couldn't disconnect",Toast.LENGTH_LONG).show();
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }
}
