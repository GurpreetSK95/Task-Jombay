package me.gurpreetsk.task_jombay.ui.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import me.gurpreetsk.task_jombay.ui.fragment.CompletedLessonFragment;
import me.gurpreetsk.task_jombay.ui.fragment.PendingLessonFragment;

/**
 * Created by Gurpreet on 15/05/17.
 */

public class ViewPagerScreenSlideAdapter extends FragmentStatePagerAdapter {

    private static final String TAG = ViewPagerScreenSlideAdapter.class.getSimpleName();


    public ViewPagerScreenSlideAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new CompletedLessonFragment();
            case 1:
                return new PendingLessonFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
