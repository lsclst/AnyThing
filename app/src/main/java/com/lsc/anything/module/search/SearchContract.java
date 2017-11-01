package com.lsc.anything.module.search;

import com.lsc.anything.base.mvp.BaseIPresenter;
import com.lsc.anything.base.mvp.BaseIview;
import com.lsc.anything.entity.gank.GankItem;

import java.util.List;

/**
 * Created by lsc on 2017/10/23 0023.
 *
 * @author lsc
 */

public interface SearchContract {
    interface SearchView extends BaseIview {
        void showHistories();

        void showSearchResult(int requestType, List<GankItem> searchResult);
    }

    interface SearchPresenter extends BaseIPresenter {
        void onLoadMore();

        void search(String keyword);
    }

}
