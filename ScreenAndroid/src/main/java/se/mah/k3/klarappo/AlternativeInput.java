package se.mah.k3.klarappo;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class AlternativeInput extends Fragment implements ValueEventListener {


    public AlternativeInput() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {




        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_alternative_input, container, false);
    }


    @Override
    public void onDataChange(DataSnapshot snapshot){
        if (snapshot.getValue()!=null) {



        }

    }

    @Override
    public void onCancelled(FirebaseError firebaseError) {

    }


}
