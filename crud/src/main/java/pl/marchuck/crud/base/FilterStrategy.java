package pl.marchuck.crud.base;

/**
 * Created by lukasz.marczak on 21/10/2017.
 */

public interface FilterStrategy<Pojo> {
    boolean filter(String query, Pojo pojo);
}
