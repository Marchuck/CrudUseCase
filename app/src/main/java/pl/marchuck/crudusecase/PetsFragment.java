package pl.marchuck.crudusecase;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bignerdranch.android.multiselector.MultiSelector;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import io.reactivex.Observable;
import pl.marchuck.crud.base.CrudPresenter;
import pl.marchuck.crud.base.FilterStrategy;
import pl.marchuck.crud.crud.BaseCrudPojoFragment;
import pl.marchuck.crud.crud.CrudView;
import pl.marchuck.crud.crud.CrudViewHolder;

public class PetsFragment extends BaseCrudPojoFragment<Pet, String, MainActivity> {

    PetsPresenter presenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new PetsPresenter();
    }

    @Override
    protected FilterStrategy<Pet> provideFilterStrategy() {
        return (query, pet) -> pet.name.toLowerCase().startsWith(query.toLowerCase());
    }

    @Override
    protected int provideViewHolderLayoutRes() {
        return R.layout.cell_pet;
    }

    @Override
    protected CrudViewHolder<Pet> provideViewHolder(View v, MultiSelector multiSelector) {
        return new CrudViewHolder<Pet>(v, multiSelector) {

            TextView nameLabel;

            @Override
            protected void onCreateViewHolder(View itemView) {
                nameLabel = itemView.findViewById(R.id.cell_pet_name);
            }

            @Override
            public void bind(Pet pet) {
                nameLabel.setText(pet.name);
            }
        };
    }


    @Override
    protected void onFabClicked() {
        String uuid = UUID.randomUUID().toString();
        Pet pet = new Pet(uuid, "Cat " + uuid.substring(0, 7));
        presenter.requestCreateItem(pet);
    }

    @Override
    protected CrudPresenter<Pet, CrudView<Pet>, String> providePresenter() {
        return presenter;
    }

    @Override
    protected String provideTitle() {
        return "My favourite pets";
    }

    @Override
    protected String provideDefaultItemsRequest() {
        return "";
    }

    @Override
    protected String provideRefreshItemsRequest() {
        return "";
    }

    @Override
    protected String printItemsToDelete(List<Pet> pets) {
        return "Are you sure to delete " +

                Observable.fromIterable(pets)
                        .map(pet -> pet.name)
                        .toList()
                        .map(list -> Arrays.toString(list.toArray()))
                        .blockingGet() + " ?";
    }

    @Override
    protected void openDetail(Pet selectedPet) {
        Toast.makeText(getActivity(), "Clicked pojo " + selectedPet, Toast.LENGTH_SHORT).show();
    }
}
