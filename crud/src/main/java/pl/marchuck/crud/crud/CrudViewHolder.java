package pl.marchuck.crud.crud;

import android.view.View;

import com.bignerdranch.android.multiselector.MultiSelector;
import com.bignerdranch.android.multiselector.SwappingHolder;

import pl.marchuck.crud.base.ClickListener;

public abstract class CrudViewHolder<Pojo> extends SwappingHolder implements View.OnClickListener, View.OnLongClickListener {

    protected Pojo currentPojo;
    protected final MultiSelector multiSelector;
    protected CrudDeleteActionListener deleteListener;
    protected SpecialModeCallback specialCallback;
    protected ClickListener<Pojo> clickListener;

    public void bindBase(Pojo pojo) {
        this.currentPojo = pojo;
        bind(pojo);
    }

    public abstract void bind(Pojo pojo);

    public CrudViewHolder(View itemView,
                          MultiSelector multiSelector) {
        super(itemView, multiSelector);
        this.multiSelector = multiSelector;
        onCreateViewHolder(itemView);
    }

    public CrudViewHolder<Pojo> additionalSetup(CrudDeleteActionListener deleteListener,
                                                SpecialModeCallback specialCallback,
                                                ClickListener<Pojo> clickListener) {

        this.deleteListener = deleteListener;
        this.specialCallback = specialCallback;
        this.clickListener = clickListener;
        itemView.setOnClickListener(this);
        itemView.setLongClickable(true);
        itemView.setOnLongClickListener(this);
        return this;
    }

    protected abstract void onCreateViewHolder(View itemView);

    @Override
    public void onClick(View v) {

        if (currentPojo == null) {
            return;
        }
        if (!multiSelector.tapSelection(this)) {
            clickListener.onClick(currentPojo);
        }
    }

    @Override
    public boolean onLongClick(View v) {
        deleteListener.startSupportActionMode(specialCallback);
        multiSelector.setSelected(this, true);
        return true;
    }
}
