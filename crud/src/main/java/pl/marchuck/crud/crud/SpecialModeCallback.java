package pl.marchuck.crud.crud;

import android.support.annotation.IdRes;
import android.support.v7.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;

import com.bignerdranch.android.multiselector.ModalMultiSelectorCallback;
import com.bignerdranch.android.multiselector.MultiSelector;

public class SpecialModeCallback extends ModalMultiSelectorCallback {

    private final MultiSelector multiSelector;
    private final @IdRes int specialModeId;
    private final ManyItemsSelectionListener callback;

    public SpecialModeCallback(MultiSelector multiSelector,
                               ManyItemsSelectionListener selectionListener,
                               @IdRes int specialModeId) {
        super(multiSelector);
        this.specialModeId = specialModeId;
        this.multiSelector = multiSelector;
        this.callback = selectionListener;
    }

    @Override
    public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
        super.onCreateActionMode(actionMode, menu);
        callback.onSpecialModeStarted(actionMode, menu);
        return true;
    }

    @Override
    public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {

        if (menuItem.getItemId() == specialModeId) {
            actionMode.finish();
            callback.performAction(multiSelector.getSelectedPositions());
            multiSelector.clearSelections();
            return true;

        }
        return false;
    }
};