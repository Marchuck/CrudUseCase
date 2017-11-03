package pl.marchuck.crud.base;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import pl.marchuck.crud.crud.CrudView;

public abstract class CrudPresenter<Pojo, View extends CrudView<Pojo>, RequestParam>
        extends Presenter<View> {

    public CrudPresenter() {
    }

    protected CompositeDisposable disposables = new CompositeDisposable();

    protected abstract Observable<Pojo> createItem(Pojo pojo);

    protected abstract Observable<Pojo> updateItem(Pojo pojo);

    protected abstract Observable<List<Pojo>> readItems(RequestParam requestParams);

    protected abstract Observable<List<Pojo>> deleteItem(List<Pojo> pojo);

    protected abstract void handleReadItemsError(Throwable throwable);

    public void requestCreateItem(Pojo pojo) {

        view.showLoading();
        disposables.add(
                createItem(pojo)
                        .observeOn(AndroidSchedulers.mainThread())
                        .doFinally(() -> view.hideLoading())
                        .subscribe(this::onItemCreated, this::failedToCreateItem)
        );
    }

    public void requestUpdateItem(Pojo pojo) {

        view.showLoading();
        disposables.add(
                updateItem(pojo)
                        .observeOn(AndroidSchedulers.mainThread())
                        .doFinally(() -> view.hideLoading())
                        .subscribe(this::onItemUpdated, this::failedToUpdateItem));
    }

    private void onItemUpdated(Pojo pojo) {
        view.onItemUpdated(pojo);
    }

    private void failedToUpdateItem(Throwable throwable) {
        view.showFailedUpdateItem();
    }

    private void failedToCreateItem(Throwable throwable) {
        view.showFailedCreateItem();
    }

    private void onItemCreated(Pojo pojo) {
        view.onItemCreated(pojo);
    }

    public void requestDeleteItems(List<Pojo> pojos) {

        view.requestAreYouSureToDeleteMessage(pojos);
    }

    public void performDeleteItems(List<Pojo> pojo) {
        view.showLoading();

        disposables.add(
                deleteItem(pojo)
                        .observeOn(AndroidSchedulers.mainThread())
                        .doFinally(() -> view.hideLoading())
                        .subscribe(this::onItemsDeleted, this::failedToDelete)
        );
    }

    private void failedToDelete(Throwable throwable) {
        view.showFailedToDelete();
    }

    private void onItemsDeleted(List<Pojo> pojo) {
        view.onItemsDeleted(pojo);
    }

    public void requestShowItems(RequestParam param) {
        view.showLoading();

        disposables.add(
                readItems(param)
                        .observeOn(AndroidSchedulers.mainThread())
                        .doFinally(() -> view.hideLoading())
                        .subscribe(this::displayItems, this::handleReadItemsError)
        );
    }

    private void displayItems(List<Pojo> pojos) {
        view.showItems(pojos);
    }
}
