package pl.marchuck.crudusecase;

import java.util.List;

import io.reactivex.Observable;

public interface Repository<Pojo, Request> {

    Observable<Pojo> createItem(Pojo pet);

    Observable<Pojo> updateItem(Pojo modifiedPet);

    Observable<List<Pojo>> readItems(Request requestParam);

    Observable<List<Pojo>> deleteItem(List<Pojo> pojos);
}
