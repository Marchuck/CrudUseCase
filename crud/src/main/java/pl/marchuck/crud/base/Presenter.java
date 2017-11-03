package pl.marchuck.crud.base;

import android.support.annotation.CallSuper;

public abstract class Presenter<View> {
    public static final String TAG = Presenter.class.getSimpleName() + "<View>";
    protected View view;

    public void attachView(View view) {
        this.view = view;
    }

    @CallSuper
    public void destroy() {
    }
}
