package com.victorxu.muses.creation.contract;

import android.net.Uri;

public interface FilterCreateContract {
    interface Model {
        void uploadFilter(String filterName, int brushSize, int brushIntensity, int smooth, Uri uri, okhttp3.Callback callback);
    }

    interface View {
        void initRootView(android.view.View view);

        void initListener();

        void quit();

        void showToast(int resId);

        void showToast(CharSequence text);
    }

    interface Presenter {
        void loadRootView(android.view.View view);

        void loadListener();

        void uploadFilter(String filterName, int brushSize, int brushIntensity, int smooth, Uri uri);
    }
}