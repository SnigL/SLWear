package com.filreas.gosthlm;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.wearable.view.WearableListView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.filreas.shared.dto.BusDto;
import com.filreas.shared.dto.FavouriteSiteLiveUpdateDto;
import com.filreas.shared.dto.MetroDto;
import com.filreas.shared.dto.TrainDto;
import com.filreas.shared.dto.TramDto;
import com.filreas.shared.utils.GoSthlmLog;

import java.util.ArrayList;
import java.util.List;

public class StationViewPagerAdapter extends PagerAdapter {
    final Context context;
    final List<FavouriteSiteLiveUpdateDto> sites;
    private ISwipeToRefreshEnabler swipeToRefreshEnabler;

    LayoutInflater inflater;

    public StationViewPagerAdapter(
            Context context,
            List<FavouriteSiteLiveUpdateDto> sites,
            ISwipeToRefreshEnabler swipeToRefreshEnabler) {
        this.context = context;
        this.sites = sites;
        this.swipeToRefreshEnabler = swipeToRefreshEnabler;
    }

    @Override
    public int getCount() {
        return sites.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        GoSthlmLog.d("created an item @ " + position);

        inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.viewpager_item, container,
                false);

        TextView siteName = (TextView) itemView.findViewById(R.id.fromText);

        FavouriteSiteLiveUpdateDto current = sites.get(position);


        String name = current.getName();
        if(current.getErrorMessage() != null){
            name += " " + current.getErrorMessage();
        }

        siteName.setText(name);

        ArrayList<DepartureListItem> departures = DepartureListItemMapper.CreateDepartures(current);

        WearableListView departureListView =
                (WearableListView) itemView.findViewById(R.id.departures_list);
        departureListView.setAdapter(new DepartureListItemAdapter(context, departures));
        departureListView.addOnCentralPositionChangedListener(new WearableListView.OnCentralPositionChangedListener() {
            @Override
            public void onCentralPositionChanged(int i) {
                swipeToRefreshEnabler.onSwipeToRefreshEnabled(i == 0);
            }
        });

        container.addView(itemView);

        itemView.setTag(current.getSiteId());

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}