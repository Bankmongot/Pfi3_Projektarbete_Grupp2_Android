package se.mah.k3.klarappo;


import android.annotation.TargetApi;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class AlternativeInput extends Fragment implements ValueEventListener {

    private View theView;
    private LinearLayout theLayout;
    private Firebase myFirebaseRef;

    public AlternativeInput() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View altView = inflater.inflate(R.layout.fragment_alternative_input, container, false);
        theView = altView;



        return altView;
    }

    @Override
     public void onViewCreated(View view, Bundle savedInstanceState){
        LinearLayout ll = (LinearLayout) view.findViewById(R.id.TheLinearLayout);

        for(int i = 0; i<Constants.numOfAlts; i++) {

            System.out.println(Constants.numOfAlts);

            EditText editText = new EditText(getActivity());
            TextView tv = new TextView(getActivity());
            tv.setText("Alternative: " + (1+i));

            editText.setId(Integer.valueOf(1 + i));
            editText.setHint("Enter your alternative here...");
            editText.setMaxLines(1);
            editText.setMaxWidth(50);
            ll.addView(tv);
            ll.addView(editText);

        }
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
