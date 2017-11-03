package pl.marchuck.crud.crud;

import android.support.v7.widget.SearchView;

import pl.marchuck.crud.base.Hideable;


public class CrudTextListener implements SearchView.OnQueryTextListener {

    private final CrudAdapter adapter;
    private final Hideable buttonHideable;

    public CrudTextListener(CrudAdapter adapter, Hideable buttonHideable) {
        this.adapter = adapter;
        this.buttonHideable = buttonHideable;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        adapter.filterDataSet(query);
        buttonHideable.show();
        return false;
    }

    @Override
    public boolean onQueryTextChange(String query) {

        if (query.length() == 0) {
            buttonHideable.show();
        } else {
            adapter.filterDataSet(query);
            buttonHideable.hide();
        }
        return false;
    }

}