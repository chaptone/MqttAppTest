package com.example.chapmac.rakkan.mqtt_app_test;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.example.chapmac.rakkan.mqtt_app_test.home.HomeFragment;
import com.example.chapmac.rakkan.mqtt_app_test.menu.BottomMenu;
import com.example.chapmac.rakkan.mqtt_app_test.publish.PublishDialog;
import com.example.chapmac.rakkan.mqtt_app_test.publish.PublishFragment;
import com.example.chapmac.rakkan.mqtt_app_test.subscribe.SubscribeDialog;
import com.example.chapmac.rakkan.mqtt_app_test.subscribe.SubscribeFragment;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttException;

import static com.example.chapmac.rakkan.mqtt_app_test.main.LoadingActivity._PREFER;

public class TabActivity extends AppCompatActivity implements
        SubscribeDialog.DialogListener,
        PublishDialog.DialogListener,
        BottomMenu.BottomMenuListener{

    FloatingActionButton fab,fab1,fab2,fab3;
    Animation fabOpen,fabClose,rotateForward,rotateBackward;
    boolean isOpen = false;

    private SubscribeFragment subscribeFragment;
    private PublishFragment publishFragment;
    private HomeFragment homeFragment;

    private ViewPager mViewPager;

    private Handler handler = new Handler();

    private int color;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        color = _PREFER.getConnection().getColor();
        String hexColor = String.format("#%06X", (0xFFFFFF & color));
        int resId = getResourceByFilename(this,"style", "AppTheme.0xff"+hexColor.substring(1).toLowerCase());
        getTheme().applyStyle(resId,true);
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
        tabLayout.setSelectedTabIndicatorColor(color);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
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
        fab3 = findViewById(R.id.fab3);

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
        fab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animationFab();
                Intent intent = new Intent(TabActivity.this, SensorActivity.class);
                startActivity(intent);
            }
        });

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                showSnackBarConnection("connect");
            }
        },1500);
    }

    public void animationFab(){
        if (isOpen) {
            fab.startAnimation(rotateBackward);
            fab1.startAnimation(fabClose);
            fab2.startAnimation(fabClose);
            fab3.startAnimation(fabClose);
            fab1.setVisibility(View.GONE);
            fab2.setVisibility(View.GONE);
            fab3.setVisibility(View.GONE);
            fab1.setClickable(false);
            fab2.setClickable(false);
            fab3.setClickable(false);
            isOpen = false;
        } else {
            fab.startAnimation(rotateForward);
            fab1.startAnimation(fabOpen);
            fab2.startAnimation(fabOpen);
            fab3.startAnimation(fabOpen);
            fab1.setVisibility(View.VISIBLE);
            fab2.setVisibility(View.VISIBLE);
            fab3.setVisibility(View.VISIBLE);
            fab1.setClickable(true);
            fab2.setClickable(true);
            fab3.setClickable(true);
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
    public void applyTextsFromSubscribeDialog(String status ,String topic) {
        showSnackBar(status,"Subscribe",topic,"0");
        subscribeFragment.addSubscription(topic);
    }

    @Override
    public void applyTextsFromPublishDialog(String status ,String topic, String message) {
        showSnackBar(status,"Publish",topic,message);
        publishFragment.addPublisher(topic,message);
    }

    @Override
    public void applyTextsFromBottomMenu(String inform) {
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

    }

    public void disconnect(){
        try {
            IMqttToken token = MqttHelper.CLIENT.disconnect();
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    _PREFER.edit().removeConnection().apply();
                    finish();
                }
                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    private void showSnackBar(String status,String operation,String topic,String message) {
        Snackbar snackbar = Snackbar.make(findViewById(R.id.main_content),"I don't know content.", Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(color);
        TextView textView = snackBarView.findViewById(android.support.design.R.id.snackbar_text);
        SpannableString content;
        switch (status){
            case "successful":
                switch (operation) {
                    case "Subscribe":
                        //Subscribe to topic successful.
                        content = new SpannableString("Subscribe to " + topic + " successful.");
                        content.setSpan(new UnderlineSpan(), 13, 13 + topic.length(), 0);
                        break;
                    case "Publish":
                        //Publish topic with message successful.
                        content = new SpannableString("Publish " + topic + " with " + message + " successful.");
                        content.setSpan(new UnderlineSpan(), 8, 8 + topic.length(), 0);
                        content.setSpan(new UnderlineSpan(), 19, 19 + message.length(), 0);
                        break;
                    default:
                        content = new SpannableString("I don't know publish or subscribe when success.");
                        break;
                }
                break;
            case "Failed":
                switch (operation) {
                    case "Subscribe":
                        //Failed to subscribe topic.
                        content = new SpannableString("Failed to subscribe " + topic + ".");
                        content.setSpan(new UnderlineSpan(), 10, 10 + topic.length(), 0);
                        snackBarView.setBackgroundColor(Color.parseColor("#d50000"));
                        break;
                    case "Publish":
                        //Failed to publish topic with message.
                        content = new SpannableString("Failed to publish " + topic + " with " + message + ".");
                        content.setSpan(new UnderlineSpan(), 8, 8 + topic.length(), 0);
                        content.setSpan(new UnderlineSpan(), 18, 18 + message.length(), 0);
                        snackBarView.setBackgroundColor(Color.parseColor("#d50000"));
                        break;
                    default:
                        content = new SpannableString("I don't know publish or subscribe when failed.");
                        break;
                }
                break;
            default:
                content = new SpannableString("I don't know success or failed");
        }
        textView.setText(content);
        textView.setTextSize(16);
        textView.setTextColor(getResources().getColor(R.color.white));

        snackbar.show();
    }

    public void showSnackBarConnection(String text){
        Snackbar snackbar = Snackbar.make(findViewById(R.id.main_content), "I don't know anything.", Snackbar.LENGTH_LONG);
        if(text.equals("connect")){
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundColor(color);
            TextView textView = snackBarView.findViewById(android.support.design.R.id.snackbar_text);
            String content2 =  "tcp://" + _PREFER.getConnection().getHost() + ":" + _PREFER.getConnection().getPort();
            textView.setText(content2);
            textView.setTextColor(getResources().getColor(R.color.white));
        }else{
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundColor(getResources().getColor(R.color.colorWrong));
            TextView textView = snackBarView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setText("Couldn't disconnect");
            textView.setTextColor(getResources().getColor(R.color.white));
        }

        snackbar.show();
    }

    public static int getResourceByFilename(Context context, String resourceType, String filename) {
        return context.getResources().getIdentifier(filename, resourceType, context.getPackageName());
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
