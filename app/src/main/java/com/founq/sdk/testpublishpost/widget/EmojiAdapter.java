package com.founq.sdk.testpublishpost.widget;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.founq.sdk.testpublishpost.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ring on 2020/9/23.
 */
public class EmojiAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final static int TYPE_EMOJI = 0;
    private final static int TYPE_DELETE = 1;

    private Context mContext;
    //emoji文字显示大小
    private float textSize = 25;

    //存放emoji表情
    private List<String> mEmojiList = new ArrayList<>();
    //总共有多少个item
    private int count = 0;

    public EmojiAdapter(Context context) {
        mContext = context;
    }

    public void setEmojiList(List<String> emojiList, int column) {
        if (emojiList != null) {
            mEmojiList.clear();
            mEmojiList.addAll(emojiList);
        }
        //根据，emoji的数量，加上删除按钮的数量，凑满一行，这些条件，算出总共有多少个item
        if (mEmojiList.size() % column == 0) {
            count = mEmojiList.size() + column;
        } else {
            count = (mEmojiList.size() / column + 1) * column;
        }
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
    }

    @Override
    public int getItemViewType(int position) {
        int type = TYPE_EMOJI;
        //最后一个为删除按钮
        if (position == count - 1) {
            type = TYPE_DELETE;
        }
        return type;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        if (viewType == TYPE_EMOJI) {
            //添加emoji显示文本框
            TextView view = new TextView(mContext);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            view.setLayoutParams(layoutParams);
            view.setGravity(Gravity.CENTER);
            view.setTextSize(textSize);
            holder = new EmojiViewHolder(view);
        } else if (viewType == TYPE_DELETE) {
            //添加删除的图片
            ImageView view = new ImageView(mContext);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            view.setLayoutParams(layoutParams);
            view.setImageResource(R.drawable.delete);
            holder = new DeleteViewHolder(view);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof EmojiViewHolder) {
            EmojiViewHolder viewHolder = (EmojiViewHolder) holder;
            if (position < mEmojiList.size()) {
                viewHolder.mEmojiText.setText(mEmojiList.get(position));
            } else {
                viewHolder.mEmojiText.setText("");
            }
            viewHolder.mEmojiText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO:添加点击事件的回调
                }
            });
        } else if (holder instanceof DeleteViewHolder) {
            DeleteViewHolder viewHolder = (DeleteViewHolder) holder;
            viewHolder.mDeleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO:添加点击事件的回调
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return count;
    }

    class EmojiViewHolder extends RecyclerView.ViewHolder {

        private TextView mEmojiText;

        public EmojiViewHolder(@NonNull View itemView) {
            super(itemView);
            mEmojiText = (TextView) itemView;
        }
    }

    class DeleteViewHolder extends RecyclerView.ViewHolder {

        private ImageView mDeleteBtn;

        public DeleteViewHolder(@NonNull View itemView) {
            super(itemView);
            mDeleteBtn = (ImageView) itemView;
        }
    }
}
