package co.potes.icesi.startagrocol.fragments_inversor;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import co.potes.icesi.startagrocol.R;

public class Fragment_Mis_Inversiones extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_mis_inversiones, container, false);


        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
