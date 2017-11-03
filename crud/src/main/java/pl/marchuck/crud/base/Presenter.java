package pl.marchuck.crud.base;

public abstract class Presenter<View> {
    public static final String TAG = Presenter.class.getSimpleName() + "<View>";
    protected View view;

    public void attachView(View view) {
        this.view = view;
    }

}
