package pl.marchuck.crud.base;

public interface Insertable<Pojo extends Unique> {
    void insert(Pojo pojo);
}
