package com.victorxu.muses.creation.contract;

import com.victorxu.muses.gson.PageFilter;

import java.util.List;

public interface SearchFilterContract {
    interface Model {

        void getFilterData(okhttp3.Callback callback);

        void getFilterData(int page, okhttp3.Callback callback);

        void getMoreFilterData(okhttp3.Callback callback);

        boolean checkDataStatus();

        boolean checkPageStatus();

        void setKeyword(String keyword);

        void setAllPages(int allPages);

        void setLocalFilterData(List<PageFilter.FilterBean> data);

        void addLocalFilterData(List<PageFilter.FilterBean> data);

        void cancelTask();
    }

    interface View {

        void initRootView(android.view.View view);

        void showFilterData(List<PageFilter.FilterBean> data);

        void showMoreFilterData(List<PageFilter.FilterBean> data);

        void showLoadMore();

        void hideLoadMore(boolean isCompleted, boolean isEnd);

        void showEmptyPage();

        void hideEmptyPage();

        void showToast(int resId);

        void showToast(CharSequence text);
    }

    interface Presenter {

        void loadRootView(android.view.View view);

        void loadDataToView(String keyword);

        void loadMoreDataToView();

        void destroy();
    }
}
