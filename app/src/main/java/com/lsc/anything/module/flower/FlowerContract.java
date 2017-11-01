package com.lsc.anything.module.flower;

import com.lsc.anything.api.ApiHolder;
import com.lsc.anything.base.mvp.BaseIPresenter;
import com.lsc.anything.base.mvp.BaseIview;
import com.lsc.anything.entity.gank.GankItem;

import java.util.List;

/**
 * Created by lsc on 2017/9/21 0021.
 *
 * @author lsc
 */

public interface FlowerContract {
    interface FlowerView extends BaseIview {
        void showFlowers(int requestType, List<GankItem> flowers);
        void showDetail(List<GankItem> gankItems,int position);
    }

    interface FlowerPresenter extends BaseIPresenter {
        void getFlowers(@ApiHolder.RequestType int requestType);
    }
}
