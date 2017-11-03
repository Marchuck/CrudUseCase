package pl.marchuck.crudusecase;

import android.util.Log;

import java.util.List;

import io.reactivex.Observable;
import pl.marchuck.crud.base.CrudPresenter;
import pl.marchuck.crud.crud.CrudView;

public class PetsPresenter extends CrudPresenter<Pet, CrudView<Pet>, String> {
    //our mock for pets repository
    private Repository<Pet, String> petsRepository = new PetsRepository();

    @Override
    protected Observable<Pet> createItem(Pet pet) {

        return petsRepository.createItem(pet);
    }

    @Override
    protected Observable<Pet> updateItem(Pet pet) {
        return petsRepository.updateItem(pet);
    }

    @Override
    protected Observable<List<Pet>> readItems(String requestParams) {
        return petsRepository.readItems(requestParams);
    }

    @Override
    protected Observable<List<Pet>> deleteItem(List<Pet> pojos) {
        return petsRepository.deleteItem(pojos);
    }

    @Override
    protected void handleReadItemsError(Throwable throwable) {
        Log.e(TAG, "handleReadItemsError: " + throwable);
    }
}
