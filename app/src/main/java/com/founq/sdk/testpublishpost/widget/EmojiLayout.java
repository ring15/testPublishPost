package com.founq.sdk.testpublishpost.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.founq.sdk.testpublishpost.GridSpacingItemDecoration;
import com.founq.sdk.testpublishpost.MyLayoutManager;
import com.founq.sdk.testpublishpost.R;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by ring on 2020/9/24.
 */
public class EmojiLayout extends LinearLayout {

    private Context mContext;

    //一个viewpager中行数
    private int row;
    //一个viewpager中列数
    private int column;
    //两个emoji之间的间隙
    private int spacing;
    //圆点未点击时的drawable
    private Drawable dotNormalDrawable;
    //圆点选中状态的drawable
    private Drawable dotSelectDrawable;
    //圆点直接的间距
    private int dotSpacing;
    //emoji大小
    private float emojiSize;

    //显示所有emoji的viewpager界面
    private CustomViewpager mViewpager;
    //显示圆点的容器
    private LinearLayout mDotLayout;

    //viewpager的个数
    private int viewPagerNum;
    //每个viewpager包含emoji的个数
    private int emojiNum;
    //将emoji分成一个二维数组，按每一个viewpager中的emoji数组分类
    private List<List<String>> mEmojiList = new ArrayList<>();

    public EmojiLayout(Context context) {
        this(context, null);
    }

    public EmojiLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EmojiLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initEmojiLayout(context, attrs, defStyleAttr);
    }

    private void initEmojiLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        mContext = context;
        //通过xml获取相应配置信息
        TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.EmojiLayout, defStyleAttr, 0);
        row = array.getInteger(R.styleable.EmojiLayout_row, 3);
        column = array.getInteger(R.styleable.EmojiLayout_column, 8);
        spacing = array.getDimensionPixelOffset(R.styleable.EmojiLayout_emoji_spacing, 20);
        emojiSize = array.getDimension(R.styleable.EmojiLayout_emoji_size, 25);
        dotSpacing = array.getDimensionPixelOffset(R.styleable.EmojiLayout_dot_spacing, 10);
        dotNormalDrawable = array.getDrawable(R.styleable.EmojiLayout_dot_normal);
        dotSelectDrawable = array.getDrawable(R.styleable.EmojiLayout_dot_select);
        if (dotNormalDrawable == null) {
            dotNormalDrawable = mContext.getDrawable(R.drawable.icon_dot_normal);
        }if (dotSelectDrawable == null) {
            dotSelectDrawable = mContext.getDrawable(R.drawable.icon_dot_select);
        }

        //设置LinearLayout为竖直排布
        this.setOrientation(VERTICAL);

        //新建viewpager，并将viewpager添加到LinearLayout中
        mViewpager = new CustomViewpager(context);
        ViewGroup.LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
        this.addView(mViewpager, params);

        //新建放置圆点的容器，并将其添加到LinearLayout中
        mDotLayout = new LinearLayout(context);
        mDotLayout.setGravity(Gravity.CENTER);
        mDotLayout.setOrientation(HORIZONTAL);
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, 80, 1);
        this.addView(mDotLayout, layoutParams);

        init();
    }


    private void init() {
        //每个viewpager中emoji的数量等于 行乘列 减去多余的删除按钮数量
        emojiNum = row * column - 1;
        int size = EmojiProvider.emojis.size();
        //由emoji的总数量和emoji在每个viewpager的数量，算出有多少个viewpager
        viewPagerNum = size % emojiNum == 0 ? size / emojiNum : size / emojiNum + 1;
        mEmojiList.clear();
        for (int i = 0; i < viewPagerNum; i++) {
            //将总emoji的list按第几个viewpager划分
            LinkedList<String> emoji = new LinkedList<>();
            for (int j = i * emojiNum; j < (i + 1) * emojiNum && j < size; j++) {
                emoji.add(EmojiProvider.emojis.get(j));
            }
            mEmojiList.add(emoji);

            //安装viewpager的数量添加小圆点
            ImageView imageView = new ImageView(mContext);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            imageView.setImageDrawable(dotNormalDrawable);
            imageView.setPadding(dotSpacing, dotSpacing, dotSpacing, dotSpacing);
            //给每个小圆点添加点击事件
            final int finalI = i;
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewpager.setCurrentItem(finalI);
                }
            });
            mDotLayout.addView(imageView, params);
        }

        //设置当前选中viewpager
        setSelected(0);

        mViewpager.setOffscreenPageLimit(2);
        mViewpager.setAdapter(new ViewPagerAdapter());
        //设置当前选中viewpager
        mViewpager.setCurrentItem(0);
        mViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
//                viewpager滑动时，更新小圆点的选中状态
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
                ((ImageView)(mDotLayout.getChildAt(i))).setImageDrawable(dotSelectDrawable);
            } else {
                ((ImageView)(mDotLayout.getChildAt(i))).setImageDrawable(dotNormalDrawable);
            }
        }
    }

    class ViewPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return viewPagerNum;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            //每个viewpager的item都是一个recyclerview
            EmojiAdapter adapter = new EmojiAdapter(getContext());
            adapter.setEmojiList(mEmojiList.get(position), column);
            adapter.setTextSize(emojiSize);
            RecyclerView recyclerView = new RecyclerView(mContext);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new MyLayoutManager(getContext(), column));
            //设置recyclerview每个item之间的间距，如果不设置，尾部会留白
            recyclerView.addItemDecoration(new GridSpacingItemDecoration(column, spacing, true));
            ViewGroup.LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            container.addView(recyclerView, layoutParams);
            return recyclerView;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((RecyclerView) object);
        }
    }
}
