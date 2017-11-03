package pl.marchuck.crud.crud;

import java.util.List;

import pl.marchuck.crud.base.Loadable;

public interface CrudView<Pojo> extends Loadable {

    void onItemCreated(Pojo pojo);

    void onItemUpdated(Pojo pojo);

    void showItems(List<Pojo> pojos);

    void showErrorFetchItems(String string);

    void onItemsDeleted(List<Pojo> pojo);

    void requestAreYouSureToDeleteMessage(List<Pojo> pojos);

    void showFailedToDelete();

    void showFailedUpdateItem();

    void showFailedCreateItem();
}
