package com.founq.sdk.testpublishpost;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.founq.sdk.testpublishpost.widget.EmojiAdapter;
import com.founq.sdk.testpublishpost.widget.MyLayoutManager;

import java.util.LinkedList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EmojiFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EmojiFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    private LinkedList<String> emoji;

    public EmojiFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static EmojiFragment newInstance(LinkedList<String> emoji) {
        EmojiFragment fragment = new EmojiFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, emoji);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            emoji = (LinkedList<String>) getArguments().getSerializable(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_emoji, container, false);
        init(view);
        return view;
    }

    private EmojiAdapter mAdapter;
    private RecyclerView mRecyclerView;

    private void init(View view) {
        mAdapter = new EmojiAdapter(getContext());
        mAdapter.setEmojiList(emoji, 8);
        mRecyclerView = view.findViewById(R.id.recyclerview);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new MyLayoutManager(getContext(), 8));
        int spanCount = 8;
        int spacing = 30;//30px
        boolean includeEdge = true;
        mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
    }
}