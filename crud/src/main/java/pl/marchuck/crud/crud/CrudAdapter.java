package pl.marchuck.crud.crud;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.android.multiselector.MultiSelector;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import pl.marchuck.crud.base.ClickListener;
import pl.marchuck.crud.base.FilterStrategy;
import pl.marchuck.crud.base.Insertable;
import pl.marchuck.crud.base.Unique;

public abstract class CrudAdapter<Pojo extends Unique>
        extends RecyclerView.Adapter<CrudViewHolder<Pojo>> implements Insertable<Pojo> {

    public abstract CrudViewHolder<Pojo> provideViewHolder(View v, MultiSelector multiSelector);

    private MultiSelector multiSelector;
    private final CrudDeleteActionListener deleteListener;
    private final SpecialModeCallback specialCallback;
    private final FilterStrategy<Pojo> filterStrategy;
    private final ClickListener<Pojo> clickListener;

    private List<Pojo> originalDataset = new ArrayList<>();
    private List<Pojo> dataSet = new ArrayList<>();
    public BehaviorSubject<Boolean> dataSetEmpty = BehaviorSubject.create();
    private String recentQuery = "";

    public CrudAdapter(CrudDeleteActionListener deleteListener,
                       SpecialModeCallback specialCallback,
                       FilterStrategy<Pojo> filterStrategy,
                       ClickListener<Pojo> clickListener) {
        this.deleteListener = deleteListener;
        this.specialCallback = specialCallback;
        this.filterStrategy = filterStrategy;
        this.clickListener = clickListener;
    }

    public CrudAdapter<Pojo> withMultiSelector(MultiSelector _multiSelector) {
        multiSelector = _multiSelector;
        return this;
    }

    public void filterDataSet(String query) {
        recentQuery = query;
        if (query.isEmpty()) {
            dataSet.clear();
            dataSet.addAll(originalDataset);
        } else {

            dataSet = Observable.fromIterable(originalDataset)
                    .filter(pojo -> filterStrategy.filter(query, pojo))
                    .toList()
                    .blockingGet();
        }
        notifyDataSetChanged();
        notifyItemRangeChanged(0, getItemCount());
        dataSetEmpty.onNext(dataSet.isEmpty());
    }

    public void refreshDataset(List<Pojo> dataSet) {
        List<Pojo> copy = new ArrayList<>();
        copy.addAll(dataSet);
        originalDataset = copy;
        if (!recentQuery.isEmpty()) {
            filterDataSet(recentQuery);
        } else {
            this.dataSet = copy;
        }
        dataSetEmpty.onNext(dataSet.isEmpty());
        notifyItemRangeChanged(0, getItemCount());
        notifyDataSetChanged();
    }

    @Override
    public CrudViewHolder<Pojo> onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(provideLayoutRes(), null, false);
        return provideViewHolder(v, multiSelector).additionalSetup(deleteListener, specialCallback, clickListener);
    }

    @LayoutRes
    protected abstract int provideLayoutRes();

    @Override
    public void onBindViewHolder(final CrudViewHolder<Pojo> holder, int position) {
        final Pojo place = dataSet.get(position);
        holder.bindBase(place);
    }

    @Override
    public int getItemCount() {
        return dataSet == null ? 0 : dataSet.size();
    }

    public List<Pojo> getPojosToDelete(List<Integer> selectedPositions) {
        return Observable.range(0, getItemCount())
                .filter(selectedPositions::contains)
                .map(integer -> dataSet.get(integer))
                .toList()
                .blockingGet();
    }

    public void updateItem(Pojo pojocompany) {
        for (int i = 0; i < dataSet.size(); i++) {
            if (dataSet.get(i).get_id().equalsIgnoreCase(pojocompany.get_id())) {
                dataSet.set(i, pojocompany);
                notifyItemChanged(i);
                return;
            }
        }
    }

    public void deleteItems(List<Pojo> pojos) {
        this.originalDataset.removeAll(pojos);
        refreshDataset(originalDataset);
        notifyDataSetChanged();
    }

    @Override
    public void insert(Pojo newPojo) {
        if (newPojo.get_id() == null)
            throw new RuntimeException("Illegal state: every pojo must have nonnull _ID!");

        for (int i = 0; i < originalDataset.size(); i++) {
            Pojo current = originalDataset.get(i);
            if (newPojo.get_id().equals(current.get_id())) {
                //edit did happened
                originalDataset.set(i, newPojo);
                notifyItemChanged(i);
                return;
            }
        }
        //new element
        originalDataset.add(newPojo);
        refreshDataset(originalDataset);
    }
}