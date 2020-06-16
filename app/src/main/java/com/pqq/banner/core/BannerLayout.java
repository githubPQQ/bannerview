package com.pqq.banner.core;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.pqq.banner.R;
import com.pqq.banner.util.PixAndDpUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;

public class BannerLayout extends FrameLayout {
    private RecyclerView mRecycleview;
    private LinearLayout mDotLl;
    private List<String> data;
    private Context context;
    private BannerAdapter bannerAdapter;
    private SmoothLinearLayoutManager layoutManager;
    private Disposable disposable;

    private int defaultDotColor;
    private int selectedDotColor;

    private int dotRadius;


    public BannerLayout(@NonNull Context context) {
        super(context);
        init(context, null);
    }

    public BannerLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public BannerLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public BannerLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(@NonNull Context context, @Nullable AttributeSet attrs) {
        this.context = context;
        LayoutInflater layoutInflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.layout_banner, this, true);
        mRecycleview = view.findViewById(R.id.banner_rv);
        mDotLl = view.findViewById(R.id.dot_ll);

        TypedArray arrs = getContext().obtainStyledAttributes(attrs,
                R.styleable.DotView);
        defaultDotColor = arrs.getColor(R.styleable.DotView_dot_default_color,
                ContextCompat.getColor(context, R.color.white));
        selectedDotColor = arrs.getColor(R.styleable.DotView_dot_selected_color,
                ContextCompat.getColor(context, R.color.colorAccent));
        dotRadius = (int) arrs.getDimension(R.styleable.DotView_dot_radius, 10);
        initRecycleView();
    }

    public void initRecycleView() {
        bannerAdapter = new BannerAdapter(context, null);
        layoutManager = new SmoothLinearLayoutManager(context,
                LinearLayoutManager.HORIZONTAL, false);
        mRecycleview.setLayoutManager(layoutManager);
        mRecycleview.setHasFixedSize(true);
        mRecycleview.setAdapter(bannerAdapter);
        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(mRecycleview);

        mRecycleview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    int position = layoutManager.findFirstVisibleItemPosition() % data.size();
                    changeIndicatorPos(position);
                }
            }
        });
    }

    private void changeIndicatorPos(int pos) {
        if (mDotLl != null && mDotLl.getChildCount() > 0) {
            int selectedPos = pos % mDotLl.getChildCount();
            for (int i = 0; i < mDotLl.getChildCount(); i++) {
                View view = mDotLl.getChildAt(i);
                if (view instanceof DotView) {
                    if (i == selectedPos) {
                        ((DotView) view).setColor(selectedDotColor);
                    } else {
                        ((DotView) view).setColor(defaultDotColor);
                    }
                }
            }
        }
    }

    public void setData(List<String> data) {
        if (data == null)
            return;
        this.data = data;
        if (mDotLl.getChildCount() > 0) {
            mDotLl.removeAllViewsInLayout();
        }
        List<View> dots = creatDots(data.size());
        for (View view : dots) {
            mDotLl.addView(view);
        }
        bannerAdapter.setData(data);
        mRecycleview.smoothScrollToPosition(2000);
    }


    public List<View> creatDots(int size) {
        int margin = PixAndDpUtil.dp2px(3, context);

        List<View> views = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            DotView view = new DotView(context);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(dotRadius, dotRadius);
            layoutParams.setMargins(margin, margin, margin, margin);
            view.setLayoutParams(layoutParams);
            view.setColor(defaultDotColor);
            views.add(view);
        }
        return views;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        startAnimitor();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopAnimotor();
    }

    private void startAnimitor() {
        disposable = Observable
                .interval(1000, 2000, TimeUnit.MILLISECONDS)
                .subscribe((time) -> {
                    mRecycleview.smoothScrollToPosition(layoutManager.findFirstVisibleItemPosition() + 1);
                });
    }

    private void stopAnimotor() {
        disposable.dispose();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                stopAnimotor();
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                startAnimitor();
                break;
        }
        return super.dispatchTouchEvent(ev);
    }
}
