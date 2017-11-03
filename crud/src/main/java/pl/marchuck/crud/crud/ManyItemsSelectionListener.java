package pl.marchuck.crud.crud;

import android.support.v7.view.ActionMode;
import android.view.Menu;

import java.util.List;

public interface ManyItemsSelectionListener {

    void onSpecialModeStarted(ActionMode actionMode, Menu menu);

    void performAction(List<Integer> selectedPositions);
}
