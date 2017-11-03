package pl.marchuck.crud.base;

import android.os.Bundle;
import android.os.Looper;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bignerdranch.android.multiselector.MultiSelector;

import java.util.Arrays;
import java.util.List;

import pl.marchuck.crud.R;
import pl.marchuck.crud.crud.CrudAdapter;
import pl.marchuck.crud.crud.CrudDeleteActionListener;
import pl.marchuck.crud.crud.CrudTextListener;
import pl.marchuck.crud.crud.CrudView;
import pl.marchuck.crud.crud.ManyItemsSelectionListener;
import pl.marchuck.crud.crud.RefreshCrudListener;
import pl.marchuck.crud.crud.SpecialModeCallback;

public abstract class VeryBaseCrudFragment<Pojo extends Unique, RequestParam,
        Parent extends AppCompatActivity>
        extends BaseFragment<Parent> implements CrudView<Pojo> {

    public static final String TAG = VeryBaseCrudFragment.class.getSimpleName();

    MultiSelector multiSelector = new MultiSelector();
    CrudAdapter<Pojo> crudAdapter;
    RefreshCrudListener<RequestParam> refreshCrudListener;
    CrudTextListener searchCrudListener;

    SearchView searchview;
    RecyclerView recyclerView;

    SwipeRefreshLayout swipeRefreshLayout;
    ProgressBar progressbar;
    TextView pojosEmpty;
    FloatingActionButton fab;
    RelativeLayout container;

    private boolean isInDeleteMode;

    public VeryBaseCrudFragment() {
    }

    private void iWishIHadButterknife(View v) {
        container = v.findViewById(R.id.fragment_pojos_container);
        searchview = v.findViewById(R.id.fragment_pojos_searchview);
        recyclerView = v.findViewById(R.id.fragment_pojos_recyclerview);
        swipeRefreshLayout = v.findViewById(R.id.fragment_pojos_swipe_refresh_layout);
        progressbar = v.findViewById(R.id.fragment_pojos_progressbar);
        pojosEmpty = v.findViewById(R.id.fragment_pojos_empty);
        fab = v.findViewById(R.id.create_pojo);
        fab.setOnClickListener((x) -> {
            if (isInDeleteMode) return;
            onFabClicked();
        });
    }

    protected abstract void onFabClicked();

    protected abstract CrudAdapter<Pojo> provideAdapter(CrudDeleteActionListener crudListener,
                                                        SpecialModeCallback specialModeCallback,
                                                        ClickListener<Pojo> clickListener);

    protected abstract CrudPresenter<Pojo, CrudView<Pojo>, RequestParam> providePresenter();

    protected abstract String provideTitle();

    protected abstract RequestParam provideDefaultItemsRequest();

    protected abstract RequestParam provideRefreshItemsRequest();

    protected abstract String printItemsToDelete(List<Pojo> pojos);

    protected abstract void openDetail(Pojo selectedPojo);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_pojos, container, false);

        iWishIHadButterknife(v);

        crudAdapter = provideAdapter(getCrudListener(), getSpecialModeCallback(), this::openDetail)
                .withMultiSelector(multiSelector);

        setupTitle();
        setupAdapter();
        setupSwipe();
        setupSearchView();
        providePresenter().attachView(this);
        providePresenter().requestShowItems(provideDefaultItemsRequest());
        return v;
    }


    private void setupSearchView() {
        searchCrudListener = new CrudTextListener(crudAdapter, new Hideable() {
            @Override
            public void hide() {
                if (fab.getVisibility() == View.GONE) return;
                fab.setVisibility(View.GONE);
            }

            @Override
            public void show() {
                if (fab.getVisibility() == View.VISIBLE) return;
                fab.setVisibility(View.VISIBLE);
            }
        });
        searchview.setOnQueryTextListener(searchCrudListener);
    }

    private void setupSwipe() {
        refreshCrudListener = new RefreshCrudListener<RequestParam>(providePresenter()) {
            @Override
            protected RequestParam provideRequest() {
                return provideRefreshItemsRequest();
            }
        };
        swipeRefreshLayout.setOnRefreshListener(refreshCrudListener);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorOfError, R.color.colorAccent);

    }

    private SpecialModeCallback getSpecialModeCallback() {
        return new SpecialModeCallback(multiSelector, getManyItemsListener(), R.id.menu_item_delete);
    }

    private CrudDeleteActionListener getCrudListener() {
        return callback -> getDirectParent().startSupportActionMode(callback);
    }

    private ManyItemsSelectionListener getManyItemsListener() {
        return new ManyItemsSelectionListener() {
            @Override
            public void onSpecialModeStarted(ActionMode actionMode, Menu menu) {
                getDirectParent().getMenuInflater().inflate(R.menu.menu_crud_delete, menu);
                isInDeleteMode = true;
            }

            @Override
            public void performAction(List<Integer> selectedPositions) {
                List<Pojo> companies = crudAdapter.getPojosToDelete(selectedPositions);
                providePresenter().requestDeleteItems(companies);
                isInDeleteMode = false;
            }
        };
    }

    private void setupTitle() {
        if (getDirectParent().getSupportActionBar() != null) {
            ActionBar bar = getDirectParent().getSupportActionBar();
            bar.setTitle(provideTitle());
        }
    }

    private void setupAdapter() {

        crudAdapter.dataSetEmpty
                .distinctUntilChanged()
                .subscribe(empty -> pojosEmpty.setVisibility(empty ? View.VISIBLE : View.GONE),
                        throwable -> {
                            Log.e(TAG, "setupAdapter: ", throwable);
                        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(crudAdapter);

    }

    @Override
    public void onItemCreated(Pojo pojo) {
        Log.d(TAG, "onItemCreated: ");
        crudAdapter.insert(pojo);
    }

    @Override
    public void onItemUpdated(Pojo company) {
        Log.d(TAG, "onItemUpdated: ");
        crudAdapter.updateItem(company);
    }

    @Override
    public void showItems(List<Pojo> companies) {
        crudAdapter.refreshDataset(companies);
    }

    @Override
    public void showErrorFetchItems(String string) {
        Log.e(TAG, "showErrorFetchItems: " + string);
    }

    @Override
    public void onItemsDeleted(List<Pojo> pojos) {
        Log.w(TAG, "onItemsDeleted: " + Arrays.asList(pojos.toArray()));
        crudAdapter.deleteItems(pojos);
    }

    @Override
    public void requestAreYouSureToDeleteMessage(List<Pojo> pojos) {
        Log.d(TAG, "requestAreYouSureToDeleteMessage: ");
        new AlertDialog.Builder(getDirectParent())
                .setTitle("Warning")
                .setCancelable(true)
                .setMessage(printItemsToDelete(pojos))
                .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                    providePresenter().performDeleteItems(pojos);

                })
                .setNeutralButton(android.R.string.cancel, (a, b) -> {
                    a.dismiss();
                }).show();
    }

    @Override
    public void showFailedToDelete() {
        Log.e(TAG, "showFailedToDelete: ");
    }

    @Override
    public void showFailedUpdateItem() {
        Log.e(TAG, "showFailedUpdateItem: ");
    }

    @Override
    public void showFailedCreateItem() {
        Log.e(TAG, "showFailedCreateItem: ");
    }

    @Override
    public void hideLoading() {
        if (isOnMainThread()) {
            swipeRefreshLayout.setRefreshing(false);
        } else swipeRefreshLayout.post(() -> swipeRefreshLayout.setRefreshing(false));
    }

    @Override
    public void showLoading() {
        if (isOnMainThread()) {
            swipeRefreshLayout.setRefreshing(true);
        } else swipeRefreshLayout.post(() -> swipeRefreshLayout.setRefreshing(true));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getDirectParent().getMenuInflater().inflate(R.menu.menu_crud_delete, menu);
    }

    protected Insertable<Pojo> getInsertable() {
        return crudAdapter;
    }

    private boolean isOnMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }
}
