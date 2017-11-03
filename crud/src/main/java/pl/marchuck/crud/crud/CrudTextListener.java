package pl.marchuck.crud.crud;

import android.support.v7.widget.SearchView;

import pl.marchuck.crud.base.Hideable;


public class CrudTextListener implements SearchView.OnQueryTextListener {

    private final CrudAdapter adapter;
    private final Hideable fabHideable;

    public CrudTextListener(CrudAdapter adapter, Hideable fabHideable) {
        this.adapter = adapter;
        this.fabHideable = fabHideable;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        adapter.filterDataSet(query);
        fabHideable.show();
        return false;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        adapter.filterDataSet(query);
        fabHideable.hide();
        return false;
    }

}