package com.founq.sdk.testpublishpost;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.founq.sdk.testpublishpost.widget.CustomViewpager;
import com.founq.sdk.testpublishpost.widget.EmojiProvider;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static androidx.fragment.app.FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;

public class EmojiActivity extends AppCompatActivity {

    private CustomViewpager mViewPager;
    private LinearLayout mDotLayout;
    private int viewPagerNum;
    private List<Fragment> mFragmentList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emoji);
        mViewPager = findViewById(R.id.view_pager);
        mDotLayout = findViewById(R.id.layout_dot);
        init();
    }

    private void init() {
        viewPagerNum = EmojiProvider.emojis.size() % 23 == 0 ? EmojiProvider.emojis.size() / 23 : EmojiProvider.emojis.size() / 23 + 1;
        for (int i = 0; i < viewPagerNum; i++) {
            LinkedList<String> emoji = new LinkedList<>();
            for (int j = i * 23; j < (i + 1) * 23 && j < EmojiProvider.emojis.size(); j++) {
                emoji.add(EmojiProvider.emojis.get(j));
            }

            mFragmentList.add(EmojiFragment.newInstance(emoji));

            ImageView imageView = new ImageView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            imageView.setImageResource(R.drawable.icon_dot);
            imageView.setPadding(5, 10, 5, 10);
            mDotLayout.addView(imageView, params);
        }

        for (int i = 0; i < mDotLayout.getChildCount(); i++) {
            final int finalI = i;
            mDotLayout.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewPager.setCurrentItem(finalI);
                }
            });
        }

        setSelected(0);

        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, mFragmentList));
        mViewPager.setCurrentItem(0);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
//                mViewPager.requestLayout();
                setSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void setSelected(int index) {
        for (int i = 0; i < mDotLayout.getChildCount(); i++) {
            if (i == index) {
                mDotLayout.getChildAt(i).setSelected(true);
            } else {
                mDotLayout.getChildAt(i).setSelected(false);
            }
        }
    }

    /**
     * Fragment选项卡适配器
     *
     * @author
     */
    class MyFragmentPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> mlistViews;

        public MyFragmentPagerAdapter(@NonNull FragmentManager fm, int behavior, List<Fragment> listViews) {
            super(fm, behavior);
            this.mlistViews = listViews;
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return mlistViews.get(position);
        }

        @Override
        public int getCount() {
            return mlistViews.size();
        }
    }
}