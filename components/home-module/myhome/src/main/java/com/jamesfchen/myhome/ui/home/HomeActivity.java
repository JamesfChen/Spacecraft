package com.jamesfchen.myhome.ui.home;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.gson.Gson;
import com.jamesfchen.uicomponent.mvp.RxActivity;
import com.jamesfchen.common.util.BarUtil;
import com.jamesfchen.mockserver.model.WeatherData;
import com.jamesfchen.myhome.R;
import com.jamesfchen.myhome.api.RetrofitHelper;
import com.jamesfchen.myhome.api.WeatherApi;
import com.jamesfchen.myhome.modle.ListRes;
import com.orhanobut.logger.Logger;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

/**
 * Copyright ® $ 2017
 * All right reserved.
 * Code Link : https://github.com/HawksJamesf/Spacecraft
 *
 * @author: hawks jamesf
 * @since: 2017/7/4
 */
public class HomeActivity extends RxActivity<HomePresenter> implements HomeContract.View {

    private static final String TAG = "HomeActivity---";
    //    private RecyclerView mrvHome;
    RecyclerView rv;
    ListView lv;

    @Override
    public HomePresenter createPresenter() {
        return new HomePresenter();
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            BarUtil.setStatusBarTransparent(this);
            BarUtil.setNavBarImmersive(this);
//            BarUtil.setBarsTransparent(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        }
    }


    @SuppressLint("CheckResult")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        presenter.load();
        WeatherApi weatherApi = RetrofitHelper.createWeatherApi();
        weatherApi.getCurrentWeatherDate("London")
                .subscribe(weatherData -> {
                    //onSuccess
                    Logger.t(TAG).json(new Gson().toJson(weatherData));
                    int a= 1;

                }, throwable -> {
                    int a= 1;
                    Logger.t(TAG).json(new Gson().toJson(throwable.getStackTrace()));
                });

        weatherApi.getFiveData("London")
                .subscribe(weatherDataListRes -> {
                    int a= 1;
                    Logger.t(TAG).json(new Gson().toJson(weatherDataListRes));
                }, throwable -> {
                    int a= 1;
                    Logger.t(TAG).json(new Gson().toJson(throwable.getStackTrace()));

                });

//        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentByTag("tag_navigation_host");
//        if (navHostFragment == null) {
//            navHostFragment = NavHostFragment.create(R.navigation.nav_graph);
//        }
//
//        getSupportFragmentManager().beginTransaction()
//                .add(R.id.fl_nav_host, navHostFragment, "tag_navigation_host")
//                .setPrimaryNavigationFragment(navHostFragment)// this is the equivalent to app:defaultNavHost="true"
//                .commitNowAllowingStateLoss();
        AppBarLayout abl = findViewById(R.id.abl);
        abl.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
            }
        });
        CollapsingToolbarLayout ctbl = findViewById(R.id.ctbl);
        rv = (RecyclerView) findViewById(R.id.rv);
        rv.setAdapter(new MyAdapter());
        rv.setLayoutManager(new LinearLayoutManager(rv.getContext(), RecyclerView.VERTICAL, false));
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) rv.getLayoutParams();
        AppBarLayout.ScrollingViewBehavior behavior = (AppBarLayout.ScrollingViewBehavior) layoutParams.getBehavior();
        behavior.setOverlayTop(120);


        lv = findViewById(R.id.lv);
        lv.setNestedScrollingEnabled(true);
        lv.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return 23;
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                Log.d("ParallaxActivity", "ListView:getView:position" + position);
                MyViewHolder myViewHolder = null;
                if (convertView == null) {
//                    View itemView = LayoutInflater.from(HomeActivity.this).inflate(R.layout.item_my, parent, false);
//                    myViewHolder = new MyViewHolder(itemView);
//                    itemView.setTag(myViewHolder);
//                    convertView = itemView;

                } else {
                    myViewHolder = (MyViewHolder) convertView.getTag();
                }
                try {
                    Thread.sleep(50);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                myViewHolder.tvText.setText("ListView:position:" + position);
                return convertView;

            }
        });
    }

    static class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new MyViewHolder(
                    LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image_and_text, parent, false)

            );
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            holder.tvText.setText("asfasdfas");
        }

        @Override
        public int getItemCount() {
            return 13;
        }
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvText;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvText = itemView.findViewById(R.id.tv_text);
        }
    }


    @Override
    public boolean onSupportNavigateUp() {
//        return Navigation.findNavController(R.id.).navigateUp();
        return super.onSupportNavigateUp();
    }


}
