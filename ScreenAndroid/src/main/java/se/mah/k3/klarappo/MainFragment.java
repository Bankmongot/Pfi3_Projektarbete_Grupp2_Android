package se.mah.k3.klarappo;

import android.app.Fragment;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.MutableData;
import com.firebase.client.Transaction;
import com.firebase.client.ValueEventListener;


public class MainFragment extends Fragment implements View.OnClickListener, View.OnTouchListener, ValueEventListener{
    long lastTimeStamp = System.currentTimeMillis();
    long timeLastRound;
    int width;
    int height;
    private long roundTrip = 0;

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


        //Add listeners for the touch events onTouch will be called when screen is touched.
        rootView.setOnTouchListener(this);

        //Add listeners to initiate a measure of roundtrip time onClick will be called.
        View v = rootView.findViewById(R.id.iv_refresh);
        rootView.findViewById(R.id.buttonAlt1).setOnClickListener(this);
        rootView.findViewById(R.id.buttonAlt2).setOnClickListener(this);
        rootView.findViewById(R.id.buttonAlt3).setOnClickListener(this);
        rootView.findViewById(R.id.buttonAlt4).setOnClickListener(this);

        v.setOnClickListener(this);

        //Create listeners for response time back so know when the token returns



        Firebase fireBaseEntryForMyID = Constants.myFirebaseRef.child(Constants.userName); //My part of the firebase
        Firebase fireBaseEntryForRoundBack =  fireBaseEntryForMyID.child("RoundTripBack"); //My roundtrip (Check firebase)
        //Listen for changes on "RoundTripBack" entry onDataChange will be called when "RoundTripBack" is changed
        fireBaseEntryForRoundBack.addValueEventListener(this);
        return rootView;
    }


    public void updateVote(String vote) {
        Firebase upvotesRef = new Firebase("https://popping-torch-1741.firebaseio.com/"+Constants.userName+"/"+vote);

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


     //Start a new time measure of roundtrip time
     @Override
    public void onClick(View v) {
         if (v.getId()==R.id.iv_refresh) {
             roundTrip = roundTrip + 1; //Assuming that we are the only one using our ID
             lastTimeStamp = System.currentTimeMillis();  //remember when we sent the token
             Constants.myFirebaseRef.child(Constants.userName).child("RoundTripTo").setValue(roundTrip);
         }
         if (v.getId()==R.id.buttonAlt1){
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
         }
    }

    //called if we move on the screen send the coordinates to fireBase
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:  // If it is the motionEvent move.
                float xRel = event.getX()/width;
                float yRel = event.getRawY()/height;//Compensate for menubar can probably be solved more beautiful test with getY to see the difference
                Constants.myFirebaseRef.child(Constants.userName).child("xRel").setValue(xRel);  //Set the x Value
                Constants.myFirebaseRef.child(Constants.userName).child("yRel").setValue(yRel);  //Set the y value

                Constants.myFirebaseRef.child(Constants.userName).child("Question").setValue(Constants.question);

        }
        return true; //Ok we consumed the event and no-one can use it it is ours!
    }

    //This is called when the roundtrip is completed so show the time
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        if (roundTrip > 0 && dataSnapshot != null) {
            roundTrip = (long) dataSnapshot.getValue();
            timeLastRound = System.currentTimeMillis() - lastTimeStamp;
            TextView timeLastTV = (TextView) getActivity().findViewById(R.id.timelast);
            timeLastTV.setText("" + timeLastRound);
        }
    }

    @Override
    public void onCancelled(FirebaseError firebaseError) {

    }







}

