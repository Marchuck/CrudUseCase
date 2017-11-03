package pl.marchuck.crud.crud;

import android.support.v4.widget.SwipeRefreshLayout;

import pl.marchuck.crud.base.CrudPresenter;

public abstract class RefreshCrudListener<RequestParam>
        implements SwipeRefreshLayout.OnRefreshListener {

    private final CrudPresenter<?, ?, RequestParam> presenter;

    public RefreshCrudListener(CrudPresenter<?, ?, RequestParam> presenter) {
        this.presenter = presenter;
    }

    protected abstract RequestParam provideRequest();

    @Override
    public void onRefresh() {
        presenter.requestShowItems(provideRequest());
    }
}
