package com.founq.sdk.testpublishpost;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.goto_post:
                startActivity(new Intent(TestActivity.this, MainActivity.class));
                break;
            case R.id.goto_emoji:
                startActivity(new Intent(TestActivity.this, EmojiActivity.class));
                break;
            case R.id.goto_emoji_layout:
                startActivity(new Intent(TestActivity.this, EmojiLayoutActivity.class));
                break;
        }
    }
}