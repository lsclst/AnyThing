package com.lsc.anything.entity.gank;

import java.util.List;

/**
 * Created by lsc on 2017/9/15 0015.
 *
 * @author lsc
 */

public class GankResult {

    private boolean error;

    private List<GankItem> results;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public List<GankItem> getResults() {
        return results;
    }

    public void setResults(List<GankItem> results) {
        this.results = results;
    }

}
