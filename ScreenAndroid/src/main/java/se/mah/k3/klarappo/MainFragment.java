package se.mah.k3.klarappo;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.graphics.Point;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.MutableData;
import com.firebase.client.Transaction;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Handler;


public class MainFragment extends Fragment implements View.OnClickListener, View.OnTouchListener, ValueEventListener{
    int width;
    int height;
    private Firebase myFirebaseRef;
    private ArrayList <Button> myButtonArray = new ArrayList<>();
    private View publicView;
    private LinearLayout ll;




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


    public Button createButton(int _id){
        _id++;
        System.out.println("Inside to create button " + _id);
        Button b = new Button(getActivity());
        b.setId(_id);
        b.setOnClickListener(this);
        b.setText("Alt: " + Integer.toString(_id));
        myButtonArray.add(b);


        return b;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        ll = (LinearLayout) view.findViewById(R.id.linearLayoutVote);
        publicView = this.getView();

        Log.d("MainFragment", "View created");
        delayFunction();
    }

    public void drawButtons(){
        int counter = 0;

            if (Constants.numOfAlts != null) {
                Log.d("MainFragment", "Alright, drawing "+Constants.numOfAlts+" buttons.");
                for (int i = 0; i < Constants.numOfAlts; i++) {
                    counter++;
                    System.out.println("Button: " + i + " added.");
                    ll.addView(createButton(i));
                }
            } else {
                System.out.println("numOfAlts is null, gotta fix it. " + Constants.numOfAlts);
            }
        Log.d("MainFragment", "There is " + counter + " buttons");
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

    public void getNumOfAlts(String temp){
        Firebase tempRef = new Firebase("https://popping-torch-1741.firebaseio.com/"+temp+"/numOfAlternatives");

        //System.out.println("The url is" + tempRef);

        tempRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                if (snapshot != null) {
                    Constants.numOfAlts = (Long) snapshot.getValue();
                    //Log.d("MainFragment", "numOfAlts is " + Constants.numOfAlts);
                } else {
                    System.out.println("!!!!!   the snapshot data is null");
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

                Constants.theBoolean = (boolean) snapshot.getValue();
                if (Constants.theBoolean == true) {
                    Constants.ID = dasTemp;
                    getNumOfAlts(dasTemp);
                    //Log.d("AlternativeInput", "the ID is " + Constants.ID);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });
    }


    public void updateVote(String vote) {
        Firebase upvotesRef = new Firebase("https://popping-torch-1741.firebaseio.com/"+Constants.ID+"/Votes/"+vote);

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
                Log.d("MainFragment", "Commited: " + committed + ", " + "the current data is: " + currentData);
            }
        });
    }

    public void delayFunction(){
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("Loading. Please wait...");
        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(false);

        new CountDownTimer(3000, 1000) {

            public void onTick(long millisUntilFinished) {
                dialog.show();
            }

            public void onFinish() {
                drawButtons();
                dialog.hide();
            }
        }.start();
    }

    public Toast toastMaker(String text){
        Toast t = new Toast(getActivity());
        t.makeText(getActivity(), text, Toast.LENGTH_SHORT);
        t.show();
        return t;
    }


     @Override
    public void onClick(View v) {
         for(Button b: myButtonArray){
             if(v.getId()==b.getId()){
                 updateVote(b.getText().toString());
             }
         }
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

