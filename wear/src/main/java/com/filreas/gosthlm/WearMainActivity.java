package com.filreas.gosthlm;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import com.filreas.shared.dto.FavouriteSiteLiveUpdateDto;
import com.filreas.shared.utils.GoSthlmLog;

import java.util.ArrayList;
import java.util.List;

public class WearMainActivity extends WearBaseActivity {

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeEventListener mShakeDetector;
    private ViewPager viewPager;
    private PagerAdapter adapter;

    List<FavouriteSiteLiveUpdateDto> favouriteSites;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAmbientEnabled();
        initRefreshOnShake();
        initStationsViewPageAdapter();
    }

    private void initStationsViewPageAdapter() {
        favouriteSites = new ArrayList<>();
        viewPager = (ViewPager) findViewById(R.id.pager);
        adapter = new StationViewPagerAdapter(
                WearMainActivity.this,
                favouriteSites,
                new ISwipeToRefreshEnabler() {
                    @Override
                    public void onSwipeToRefreshEnabled(boolean enable) {
                        getSwipeLayout().setEnabled(enable);
                    }
                });
        viewPager.setAdapter(adapter);
    }

    private void initRefreshOnShake() {
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeEventListener();
        mShakeDetector.setOnShakeListener(new ShakeEventListener.OnShakeListener() {

            @Override
            public void onShake(int count) {
                handleShakeEvent(count);
            }
        });
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.viewpager_main;
    }

    @Override
    protected void updateScreenInfo(final FavouriteSiteLiveUpdateDto updatedSite) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int currentIndex = favouriteSites.indexOf(updatedSite);
                if (currentIndex < 0) {
                    GoSthlmLog.d("updateScreenInfo adding ", updatedSite.getName() + " nrOfMetros " + updatedSite.getMetros().size());
                    favouriteSites.add(updatedSite);
                } else {
                    GoSthlmLog.d("updateScreenInfo updating ", updatedSite.getName() + " nrOfMetros " + updatedSite.getMetros().size());
                    favouriteSites.set(currentIndex, updatedSite);
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void handleShakeEvent(int count) {
        /* When a shake is detected, do some crazy stuff here.
        In our case the refresh view should be called.
         */
    }

    @Override
    public void onEnterAmbient(Bundle ambientDetails) {
        GoSthlmLog.d("--Enter Ambient--");
        super.onEnterAmbient(ambientDetails);
        updateDisplay();
    }

    @Override
    public void onUpdateAmbient() {
        GoSthlmLog.d("--Update Ambient--");
        super.onUpdateAmbient();
        updateDisplay();
    }

    @Override
    public void onResume() {
        GoSthlmLog.d("--On Resume--");
        super.onResume();
        mSensorManager.registerListener(mShakeDetector, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onPause() {
        GoSthlmLog.d("--On Paus--");
        mSensorManager.unregisterListener(mShakeDetector);
        super.onPause();
    }

    @Override
    public void onExitAmbient() {
        GoSthlmLog.d("--Exit Ambient--");
        updateDisplay();
        super.onExitAmbient();
    }

    private void updateDisplay() {
        GoSthlmLog.d("--Update Display--");
        if (isAmbient()) {
        }
    }
}