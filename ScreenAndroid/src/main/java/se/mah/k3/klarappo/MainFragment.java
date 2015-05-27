package se.mah.k3.klarappo;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.MutableData;
import com.firebase.client.Transaction;
import com.firebase.client.ValueEventListener;

import java.util.HashMap;
import java.util.Map;


public class MainFragment extends Fragment implements View.OnClickListener, View.OnTouchListener, ValueEventListener{
    int width;
    int height;
    private Firebase myFirebaseRef;


    public MainFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y;
        Log.d("MainFragment", "MainFragment view created");


        rootView.setOnTouchListener(this);




        getUsername();

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        LinearLayout ll = (LinearLayout) view.findViewById(R.id.linearLayoutVote);


        for(int i = 0; i<Constants.numOfAlts; i++) {

            Button b = new Button(getActivity());
            b.setText("Alternative " + i + 1);
            b.setOnClickListener(this);
            String theID = Integer.toString(i+1);
            ll.addView(b);

        }


    }


    public void getUsername() {
        Constants.alreadyRunning = true;

        myFirebaseRef = Constants.checkmyFirebaseRef();

        myFirebaseRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {

                Iterable<DataSnapshot> fourChildren = snapshot.getChildren();
                for (DataSnapshot dataSnapshot2 : fourChildren) {
                    String temp;
                    if (dataSnapshot2.getKey().length() == 26) {
                        temp = dataSnapshot2.getKey();

                        getTempActive(temp);
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });
    }

    public void getTempActive(String temp){
        Firebase tempRef = new Firebase("https://popping-torch-1741.firebaseio.com/"+temp+"/Active");
        final String dasTemp = temp;

        tempRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                Constants.theBoolean = (boolean)snapshot.getValue();
                if(Constants.theBoolean == true){
                    Constants.ID = dasTemp;
                    Log.d("AlternativeInput", "the ID is "+Constants.ID);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });
    }


    public void updateVote(String vote) {
        Firebase upvotesRef = new Firebase("https://popping-torch-1741.firebaseio.com/"+Constants.ID+"/"+vote);

        upvotesRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData currentData) {
                if (currentData.getValue() == null) {
                    currentData.setValue(1);
                } else {
                    currentData.setValue((Long) currentData.getValue() + 1);
                }

                return Transaction.success(currentData); //we can also abort by calling Transaction.abort()
            }

            @Override
            public void onComplete(FirebaseError firebaseError, boolean committed, DataSnapshot currentData) {
                //This method will be called once with the results of the transaction.
            }
        });
    }


     @Override
    public void onClick(View v) {

        /* if (v.getId()==R.id.buttonAlt1){
             updateVote("Vote1");
         }
         if (v.getId()==R.id.buttonAlt2){
             updateVote("Vote2");
         }
         if (v.getId()==R.id.buttonAlt3){
             updateVote("Vote3");
         }
         if (v.getId()==R.id.buttonAlt4){
             updateVote("Vote4");
         } */
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
        }
        return true;
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
    }

    @Override
    public void onCancelled(FirebaseError firebaseError) {
    }
}

