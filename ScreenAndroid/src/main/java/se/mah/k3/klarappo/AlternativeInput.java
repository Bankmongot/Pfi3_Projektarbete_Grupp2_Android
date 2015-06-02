package se.mah.k3.klarappo;


import android.annotation.TargetApi;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
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
import com.firebase.client.MutableData;
import com.firebase.client.Transaction;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class AlternativeInput extends Fragment implements ValueEventListener, View.OnClickListener {

    private View theView;
    private LinearLayout theLayout;
    private Firebase myFirebaseRef;
    private Button b;
    private ArrayList <EditText> myEditTexts = new ArrayList<>();
    private int x = 0;


    public AlternativeInput() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View altView = inflater.inflate(R.layout.fragment_alternative_input, container, false);
        theView = altView;
        b = (Button)theView.findViewById(R.id.button);
        b.setOnClickListener(this);

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
            myEditTexts.add(editText);

        }
     }

    public void prepareToSend() {
         int counter = 0;
         int counter2 = 0;

        for (EditText et : myEditTexts) {
            counter = counter+1;
            String temp = et.getText().toString();
            System.out.println(temp);
            if (("".equals(temp) || (" ".equals(temp))) || temp == null) {
                Log.d("AlternativeInput", "The EditText did not contain text");
                Toast.makeText(getActivity(),"Please fill all fields.",Toast.LENGTH_SHORT).show();
            } else {
                counter2 = counter2+1;
                String x = et.getText().toString();
                sendMyEditTexts(x);
                System.out.println("The edittext " + x);
            }
        }
        if(counter == counter2){
            sendMeToVote();
        } else{
            System.out.println(counter+" "+counter2);
            counter = 0;
            counter2 = 0;
        }
    }

    public void sendMyEditTexts(final String theText){
        String aText = "Alt";
        x++;
        String theNum = aText+Integer.toString(x);

        Firebase ref = new Firebase("https://popping-torch-1741.firebaseio.com/"+Constants.ID+"/Alternatives/"+theNum);

        ref.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData currentData) {
                currentData.setValue(theText);

                return Transaction.success(currentData); //we can also abort by calling Transaction.abort()
            }

            @Override
            public void onComplete(FirebaseError firebaseError, boolean committed, DataSnapshot currentData) {
                //This method will be called once with the results of the transaction.
            }
        });

    }

    public void changeActiveState(){
        Firebase ref = Constants.checkmyFirebaseRef().child("Active");

        ref.setValue(true);
    }

    public void sendMeToVote(){
        changeActiveState();


        FragmentManager fm;
        fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.container, new MainFragment());
        ft.commit();
    }




    @Override
    public void onDataChange(DataSnapshot snapshot){
        if (snapshot.getValue()!=null) {



        }

    }

    @Override
    public void onCancelled(FirebaseError firebaseError) {

    }


    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.button){
            prepareToSend();
        }
    }
}
