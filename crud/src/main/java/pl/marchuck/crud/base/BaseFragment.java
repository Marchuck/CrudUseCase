package pl.marchuck.crud.base;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

public abstract class BaseFragment<ParentActivity extends FragmentActivity>
        extends Fragment {

    @SuppressWarnings("unchecked")
    protected ParentActivity getDirectParent() {
        return (ParentActivity) getActivity();
    }

}