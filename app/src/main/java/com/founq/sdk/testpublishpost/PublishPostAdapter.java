package com.founq.sdk.testpublishpost;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ring on 2020/9/22.
 */
public class PublishPostAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_TEXT = -1;
    private static final int TYPE_PHOTO = -2;

    //图片标签：数字之前内容
    private static final String TEMP_BEFORE = "<--IMG";
    //图片标签：数字之后内容
    private static final String TEMP_AFTER = "-->";

    private Context mContext;

    //所有文字及图片标签的字符串
    private String mContentString = "";
    //存放图片路径的数组
    private List<String> mPhotoPathList = new ArrayList<>();
    //用来展示图文混排列表的数组
    private List<TextAndImg> mTextAndImgList = new ArrayList<>();

    //TODO: 这个不太好，想想有没有更好的办法
    //将获取焦点的EditText保存为全局变量
    private EditText mEditText;
    //获取焦点的EditText的位置
    private int point;

    //将position存为全局变量（position如果为final很有可能会出错的）
    private int localPosition;

    public PublishPostAdapter(Context context) {
        mContext = context;
        //清除图文混排的数组之后再重新赋值
        mTextAndImgList.clear();
        dealString("", 0, 0);
    }

    /**
     * 用来进行插入图片的操作
     *
     * @param paths 插入的图片的路径
     */
    public void insertPhoto(List<String> paths) {
        for (String path : paths) {
            //将图片路径保存到全局数组中
            mPhotoPathList.add(path);
            //按照在数组中的位置生成图片标签
            String temp1 = TEMP_BEFORE + (mPhotoPathList.size() - 1) + TEMP_AFTER;
            if (mEditText != null) {
                //获取焦点的EditText存在时，获取光标位置
                int index = mEditText.getSelectionStart();
                if (index < 0 || index >= mEditText.length()) {
                    //在图文数组中对应的文字内容的尾部插入图片标签
                    String s = mTextAndImgList.get(point).content + temp1;
                    mTextAndImgList.set(point, new TextAndImg(s, false, -1));
                    //重新给文本字符串赋值
                    mContentString = splicing();
                } else {
                    //在图文数组中对应的文字内容光标位置处插入图片标签
                    String s = mTextAndImgList.get(point).content.substring(0, index);
                    String s1 = mTextAndImgList.get(point).content.substring(index);
                    mTextAndImgList.set(point, new TextAndImg(s + temp1 + s1, false, -1));
                    mContentString = splicing();
                }
            } else {
                //获取焦点的EditText不存在时，直接更新文本内容即可
                mContentString += temp1;
            }
            mTextAndImgList.clear();
            dealString(mContentString, 0, 0);
        }
    }

    private String splicing() {
        StringBuilder content = new StringBuilder();
        for (TextAndImg textAndImg : mTextAndImgList) {
            if (textAndImg.isPhoto) {
                content.append(TEMP_BEFORE).append(textAndImg.photoIndex).append(TEMP_AFTER);
            } else {
                content.append(textAndImg.content);
            }
        }
        return content.toString();
    }

    /**
     * 将文本内容拆分成图片和文字
     *
     * @param content        拆分的内容
     * @param index          内容在mTextAndImgList中的位置
     * @param numOfPhotoPath 轮循mPhotoPathList
     */
    private void dealString(String content, int index, int numOfPhotoPath) {
        //需要拆分的内容为空，直接添加个空的文字行
        if (content.isEmpty()) {
            TextAndImg textAndImg = new TextAndImg("", false, -1);
            mTextAndImgList.add(textAndImg);
            return;
        }
        //如果没有图片，则直接添加相应文字内容即可
        if (mPhotoPathList.size() <= 0) {
            TextAndImg textAndImg = new TextAndImg(content, false, -1);
            mTextAndImgList.add(textAndImg);
            return;
        }
        //判断图片是否全部拆分完
        if (numOfPhotoPath < mPhotoPathList.size()) {
            //当前图片标签
            String temp1 = TEMP_BEFORE + numOfPhotoPath + TEMP_AFTER;
            //下一个图片的标签
            String temp2 = TEMP_BEFORE + (numOfPhotoPath + 1) + TEMP_AFTER;
            //将需要拆分的内容拆分，正常情况下会拆分成三个
            String[] splits = content.split(temp1);

            //将拆分后的内容，和图片放到TextAndImg中，TextAndImg用来放置文字内容或图片路径，以及是否为图片，若为图片，图片在路径数组中的位置
            TextAndImg textAndImg1 = new TextAndImg(splits.length > 0 ? splits[0] : "", false, -1);
            TextAndImg textAndImg2 = new TextAndImg(mPhotoPathList.get(numOfPhotoPath), true, numOfPhotoPath);
            TextAndImg textAndImg3 = new TextAndImg(splits.length > 1 ? splits[1] : "", false, -1);
            //若，图文混排数组的大小比当前拆分内容对应的位置小或相等（正常情况，只有最开始mTextAndImgList为0时），直接在尾部添加拆分后的内容即可
            //其余情况，把当前拆分的内容移除掉，再添加新拆分的内容（新拆分的内容合起来就是移除的那个内容，所以，不会多删的）
            if (mTextAndImgList.size() > index) {
                mTextAndImgList.remove(index);
                mTextAndImgList.add(index, textAndImg3);
                mTextAndImgList.add(index, textAndImg2);
                mTextAndImgList.add(index, textAndImg1);
            } else {
                mTextAndImgList.add(textAndImg1);
                mTextAndImgList.add(textAndImg2);
                mTextAndImgList.add(textAndImg3);
            }

            //轮循新的图文混排数组，找到其中，包含下一个图片标签的内容
            for (int i = 0; i < mTextAndImgList.size(); i++) {
                String s = mTextAndImgList.get(i).content;
                if (s != null && s.contains(temp2)) {
                    //嵌套调用当前函数，拆分包含下个标签的内容
                    dealString(s, i, numOfPhotoPath + 1);
                }
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        int type = TYPE_TEXT;
        if (mTextAndImgList.get(position).isPhoto) {
            type = TYPE_PHOTO;
        }
        return type;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        View view = null;
        if (viewType == TYPE_TEXT) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_text, parent, false);
            holder = new TextViewHolder(view);
        } else {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_photo, parent, false);
            holder = new PhotoViewHolder(view);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        localPosition = position;

        if (holder instanceof TextViewHolder) {
            final TextAndImg textAndImg = mTextAndImgList.get(position);
            final TextViewHolder viewHolder = (TextViewHolder) holder;
            viewHolder.tvTest.setText(textAndImg.content);
            if (position == mTextAndImgList.size() - 1) {
                viewHolder.tvTest.setFocusable(true);
                viewHolder.tvTest.requestFocus();
            }
            //文本输入框添加焦点监听事件，获取到焦点则保存到全局变量中
            viewHolder.tvTest.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        mEditText = viewHolder.tvTest;
                        point = localPosition;
                    }
                }
            });
            viewHolder.tvTest.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    //文本输入框内容变化时要更新整个图文字符串
                    textAndImg.content = viewHolder.tvTest.getText().toString();
                    mTextAndImgList.set(localPosition, textAndImg);
                    mContentString = splicing();

                }
            });
        } else if (holder instanceof PhotoViewHolder) {
            final TextAndImg textAndImg = mTextAndImgList.get(position);
            PhotoViewHolder viewHolder = (PhotoViewHolder) holder;
            //删除图片事件
            viewHolder.deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int index = textAndImg.photoIndex;
                    //先移除当前图片标签
                    mContentString = mContentString.replace(TEMP_BEFORE + index + TEMP_AFTER, "");
                    //轮循图片路径数组，将当前图片之后的图片标签数字减一
                    for (int i = index; i < mPhotoPathList.size(); i++) {
                        String temp = TEMP_BEFORE + i + TEMP_AFTER;
                        String temp1 = TEMP_BEFORE + (i - 1) + TEMP_AFTER;
                        mContentString = mContentString.replace(temp, temp1);
                    }
                    //在图片路径数组中删除当前图片路径，则，在当前图片之后的图片路径的位置会自动减少一
                    mPhotoPathList.remove(index);
                    //重新给文本字符串赋值
                    mTextAndImgList.clear();
                    dealString(mContentString, 0, 0);
                    //重新加载
                    notifyDataSetChanged();
                }
            });
            //加载图片
            Glide.with(mContext)
                    .load(textAndImg.content)
                    .into(viewHolder.mPhotoImg);
        }
    }

    @Override
    public int getItemCount() {
        return mTextAndImgList.size();
    }

    //文本加载layout
    class TextViewHolder extends
            RecyclerView.ViewHolder {
        private EditText tvTest;

        public TextViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTest = itemView.findViewById(R.id.et_test);
        }
    }

    //图片加载layout
    class PhotoViewHolder extends
            RecyclerView.ViewHolder {
        private ImageView mPhotoImg;
        private Button deleteBtn;

        public PhotoViewHolder(@NonNull View itemView) {
            super(itemView);
            mPhotoImg = itemView.findViewById(R.id.img_photo);
            deleteBtn = itemView.findViewById(R.id.deleteBtn);
        }
    }
}
