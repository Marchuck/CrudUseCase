package pl.marchuck.crudusecase;

import pl.marchuck.crud.base.Unique;

public class Pet implements Unique {

    public final String _id, name;

    public Pet(String _id, String name) {
        this._id = _id;
        this.name = name;
    }

    @Override
    public String toString() {
        return "Pet{" +
                "_id='" + _id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public String get_id() {
        return _id;
    }
}
