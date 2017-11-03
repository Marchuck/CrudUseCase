package pl.marchuck.crud.crud;

import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.bignerdranch.android.multiselector.MultiSelector;

import pl.marchuck.crud.base.ClickListener;
import pl.marchuck.crud.base.FilterStrategy;
import pl.marchuck.crud.base.Unique;
import pl.marchuck.crud.base.VeryBaseCrudFragment;

public abstract class BaseCrudPojoFragment<Pojo extends Unique, Request, Parent extends AppCompatActivity>
        extends VeryBaseCrudFragment<Pojo, Request, Parent> {

    protected abstract FilterStrategy<Pojo> provideFilterStrategy();

    protected abstract int provideViewHolderLayoutRes();

    protected abstract CrudViewHolder<Pojo> provideViewHolder(View v, MultiSelector multiSelector);

    @Override
    protected CrudAdapter<Pojo> provideAdapter(CrudDeleteActionListener crudListener,
                                               SpecialModeCallback specialModeCallback,
                                               ClickListener<Pojo> clickListener) {
        return new CrudAdapter<Pojo>(crudListener, specialModeCallback, provideFilterStrategy(), clickListener) {
            @Override
            public CrudViewHolder<Pojo> provideViewHolder(View v, MultiSelector multiSelector) {
                return BaseCrudPojoFragment.this.provideViewHolder(v, multiSelector);
            }

            @Override
            protected int provideLayoutRes() {
                return BaseCrudPojoFragment.this.provideViewHolderLayoutRes();
            }
        };
    }
}
