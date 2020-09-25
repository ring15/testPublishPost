package com.founq.sdk.testpublishpost;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.founq.sdk.testpublishpost.widget.EmojiCallback;
import com.founq.sdk.testpublishpost.widget.EmojiLayout;

public class EmojiLayoutActivity extends AppCompatActivity {

    private EmojiLayout mEmojiLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emoji_layout);
        mEmojiLayout = findViewById(R.id.emoji_layout);
        mEmojiLayout.setEmojiCallback(new EmojiCallback() {
            @Override
            public void onEmojiClick(String emoji) {
                Log.e("emoji", emoji == null ? "null" : emoji);
            }

            @Override
            public void onDeleteClick() {
                Log.e("emoji", "delete");
            }
        });
    }
}