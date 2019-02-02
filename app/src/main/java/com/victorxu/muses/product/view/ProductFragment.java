package com.victorxu.muses.product.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.gyf.barlibrary.ImmersionBar;
import com.victorxu.muses.R;
import com.victorxu.muses.base.BaseSwipeBackFragment;
import com.victorxu.muses.custom.GradationScrollView;
import com.victorxu.muses.glide.GlideApp;
import com.victorxu.muses.product.contract.ProductContract;
import com.victorxu.muses.product.presenter.ProductPresenter;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

public class ProductFragment extends BaseSwipeBackFragment implements ProductContract.View {

    private static final String TAG = "ProductFragment";

    private View mToolBar;
    private AppCompatImageView mProductBack;
    private AppCompatImageView mProductShare;
    private TabLayout mTabLayout;
    private GradationScrollView mScrollView;
    private Banner mBanner;
    private AppCompatTextView mDiscountPrice;
    private AppCompatTextView mOriginPrice;
    private View mSideDetail;
    private View mSideEvaluation;
    private WebView mWebView;
    private ProductPresenter mPresenter;

    private int id;
    private boolean isUp = true;

    public static ProductFragment newInstance(int id) {
        Bundle bundle = new Bundle();
        bundle.putInt("ID", id);
        ProductFragment fragment = new ProductFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Bundle bundle = getArguments();
        if (bundle != null) {
            id = bundle.getInt("ID");
        } else {
            id = 0;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product, container, false);
        mPresenter = new ProductPresenter(this);
        mPresenter.loadRootView(view);
        mPresenter.loadDataToView();
        return attachToSwipeBack(view);
    }

    @Override
    public void initRootView(View view) {
        mToolBar = view.findViewById(R.id.product_page_bar);
        mProductBack = view.findViewById(R.id.product_back);
        mProductShare = view.findViewById(R.id.product_share);
        mTabLayout = view.findViewById(R.id.product_tab_layout);
        mBanner = view.findViewById(R.id.product_banner);
        mDiscountPrice = view.findViewById(R.id.product_price);
        mOriginPrice = view.findViewById(R.id.product_price_origin);
        mSideDetail = view.findViewById(R.id.product_side_detail);
        mSideEvaluation = view.findViewById(R.id.product_side_evaluation);
        mWebView = view.findViewById(R.id.product_web_detail);
        mScrollView = view.findViewById(R.id.product_scrollview);

        mToolBar.setBackgroundColor(Color.argb(0, 255,255,255));

        mTabLayout.setAlpha(0);
        setTabClickListener();

        mOriginPrice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);

        WebSettings webSettings = mWebView.getSettings();
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);

        setScrollViewListener();

    }

    @Override
    public void showBanner() {
        post(() ->
                mBanner.setImages(initDefaultBannerData())
                        .setImageLoader(new ImageLoader() {
                            @Override
                            public void displayImage(Context context, Object path, ImageView imageView) {
                                GlideApp.with(context)
                                        .load(path)
                                        .into(imageView);
                            }
                        })
                        .isAutoPlay(false)
                        .setIndicatorGravity(BannerConfig.RIGHT)
                        .start()
        );
    }

    @Override
    public void showProductDetail(String htmlData) {
        post(() -> mWebView.loadData(htmlData,"text/html", "UTF-8"));
    }

    @Override
    public void showComment() {

    }

    @Override
    public void initImmersionBar() {
        ImmersionBar.with(mActivity).statusBarDarkFont(true).init();
    }

    @Override
    protected int setTitleBar() {
        return R.id.product_page_bar;
    }

    private void setScrollViewListener() {
        mScrollView.setScrollViewListener((GradationScrollView scrollView, int x, int y, int oldX, int oldY) -> {
            if (y <= 0) {   //设置标题的背景颜色
                mToolBar.setBackgroundColor(Color.argb( 0, 255,255,255));
                mTabLayout.setAlpha(0);
                mProductBack.setAlpha(1.0f);
                mProductShare.setAlpha(1.0f);
                mProductBack.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(mActivity,R.color.white)));
                mProductShare.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(mActivity,R.color.white)));
                mProductBack.setBackgroundResource(R.drawable.icon_bg);
                mProductShare.setBackgroundResource(R.drawable.icon_bg);
            } else if (y <= mBanner.getHeight() - mToolBar.getHeight()) {
                if (y <= (mBanner.getHeight() - mToolBar.getHeight()) / 2.0) {
                    float scaleIcon = 2.0f * y / (mBanner.getHeight() - mToolBar.getHeight());
                    mProductBack.setAlpha(1.0f - scaleIcon);
                    mProductShare.setAlpha(1.0f - scaleIcon);
                    if (scaleIcon > 0.9f) {
                        if (!isUp) {
                            mProductBack.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(mActivity,R.color.white)));
                            mProductShare.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(mActivity,R.color.white)));
                            mProductBack.setBackgroundResource(R.drawable.icon_bg);
                            mProductShare.setBackgroundResource(R.drawable.icon_bg);
                            isUp = true;
                        }
                    }
                } else {
                    float scaleIcon = 2.0f * ((float) y / (mBanner.getHeight() - mToolBar.getHeight()) - 0.5f);
                    mProductBack.setAlpha(scaleIcon);
                    mProductShare.setAlpha(scaleIcon);
                    if (scaleIcon < 0.1f) {
                        if (isUp) {
                            mProductBack.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(mActivity,R.color.black)));
                            mProductShare.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(mActivity,R.color.black)));
                            mProductBack.setBackgroundResource(R.color.background_transparent);
                            mProductShare.setBackgroundResource(R.color.background_transparent);
                            isUp = false;
                        }
                    }
                }
                float scale = (float) y / (mBanner.getHeight() - mToolBar.getHeight());
                mTabLayout.setAlpha(scale);
                float alpha = (255 * scale);
                mToolBar.setBackgroundColor(Color.argb((int) alpha, 255,255,255));
            } else {
                mProductBack.setAlpha(1.0f);
                mProductShare.setAlpha(1.0f);
                mProductBack.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(mActivity,R.color.black)));
                mProductShare.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(mActivity,R.color.black)));
                mProductBack.setBackgroundResource(R.color.background_transparent);
                mProductShare.setBackgroundResource(R.color.background_transparent);
                mToolBar.setBackgroundColor(Color.argb( 255, 255,255,255));
            }

            if (y < mSideDetail.getTop() - mToolBar.getHeight()) {
                if (mTabLayout.getSelectedTabPosition() != 0) {
                    mTabLayout.getTabAt(0).select();
                }
            } else if (y > mSideDetail.getTop() - mToolBar.getHeight() && y < mSideEvaluation.getTop() - mToolBar.getHeight()) {
                if (mTabLayout.getSelectedTabPosition() != 1) {
                    mTabLayout.getTabAt(1).select();
                }
            } else if (y > mSideEvaluation.getTop() - mToolBar.getHeight()) {
                if (mTabLayout.getSelectedTabPosition() != 2) {
                    mTabLayout.getTabAt(2).select();
                }
            }
        });
    }

    private void setTabClickListener() {
        List<Integer> tabTitles = new ArrayList<>();
        tabTitles.add(R.string.product);
        tabTitles.add(R.string.detail);
        tabTitles.add(R.string.comment);

        for (int i = 0; i < 3; i++) {
            mTabLayout.addTab(mTabLayout.newTab());
        }

        for (int i = 0; i < mTabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = mTabLayout.getTabAt(i);
            if (tab != null) {
                TextView textView = new TextView(mActivity);
                textView.setText(tabTitles.get(i));
                textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                textView.setTextColor(getResources().getColor(R.color.black));
                tab.setCustomView(textView);
                if (tab.getCustomView() != null) {
                    View tabView = (View) tab.getCustomView().getParent();
                    tabView.setTag(i);
                    tabView.setOnClickListener((v) -> {
                        int position = (int) tabView.getTag();
                        switch (position) {
                            case 0:
                                mScrollView.fullScroll(ScrollView.FOCUS_UP);
                                break;
                            case 1:
                                mScrollView.smoothScrollTo(0, mSideDetail.getTop() - mToolBar.getHeight());
                                break;
                            case 2:
                                mScrollView.smoothScrollTo(0, mSideEvaluation.getTop() - mToolBar.getHeight());
                                break;
                        }
                    });
                }
            }
        }
    }

    private List<Integer> initDefaultBannerData() {
        List<Integer> mDefaultBannerData = new ArrayList<>();
        mDefaultBannerData.add(R.drawable.test_index_1);
        mDefaultBannerData.add(R.drawable.banner_guide);
        mDefaultBannerData.add(R.drawable.banner_dew);
        mDefaultBannerData.add(R.drawable.banner_cubism);
        mDefaultBannerData.add(R.drawable.banner_institute);
        return mDefaultBannerData;
    }
}
