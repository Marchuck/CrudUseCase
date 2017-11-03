package pl.marchuck.crudusecase;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import pl.marchuck.crud.base.Unique;

public class PetsRepository implements Repository<Pet, String> {
    private List<Pet> pets = new ArrayList<Pet>() {{
        add(new Pet("1", "Dog"));
        add(new Pet("2", "Duck"));
        add(new Pet("3", "Eagle"));
        add(new Pet("4", "Pig"));
    }};

    static final long FAKE_DELAY = 2000;

    @Override
    public Observable<Pet> createItem(Pet pet) {
        pets.add(pet);
        return Observable.timer(FAKE_DELAY, TimeUnit.MILLISECONDS).map(j -> pet);
    }


    @Override
    public Observable<Pet> updateItem(Pet modifiedPet) {
        int i = 0;
        for (Unique unique : pets) {
            if (unique.get_id().equals(modifiedPet.get_id())) {
                pets.set(i, modifiedPet);
                break;
            }
            ++i;
        }
        return Observable.timer(FAKE_DELAY, TimeUnit.MILLISECONDS).map(j -> modifiedPet);
    }

    @Override
    public Observable<List<Pet>> readItems(String requestParam) {
        return Observable.timer(FAKE_DELAY, TimeUnit.MILLISECONDS).map(j -> pets);
    }

    @Override
    public Observable<List<Pet>> deleteItem(List<Pet> pojos) {
        pets.removeAll(pojos);
        return Observable.timer(FAKE_DELAY, TimeUnit.MILLISECONDS).map(j -> pojos);
    }
}
