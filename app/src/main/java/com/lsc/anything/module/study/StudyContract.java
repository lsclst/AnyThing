package com.lsc.anything.module.study;

import com.lsc.anything.base.mvp.BaseIPresenter;
import com.lsc.anything.base.mvp.BaseIview;
import com.lsc.anything.entity.gank.GankItem;

import java.util.List;

/**
 * Created by lsc on 2017/9/14 0014.
 *
 * @author lsc
 */

public interface StudyContract {
    interface StudyView extends BaseIview {
        void showDetailStudy(GankItem study);
        void showStudysResult(int requestType, List<GankItem> studys);

    }

    interface StudyPresenter extends BaseIPresenter {
        void getStudyGank(int requestType);
    }
}
