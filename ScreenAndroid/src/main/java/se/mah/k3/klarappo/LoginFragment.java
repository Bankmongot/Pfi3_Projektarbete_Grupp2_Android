package se.mah.k3.klarappo;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.math.BigInteger;
import java.security.SecureRandom;



/**
 * A simple {@link Fragment} subclass.
 */

public class LoginFragment extends Fragment implements ValueEventListener {

    private Firebase myFirebaseRef;



    public LoginFragment() {
        // Required empty public constructor
    }

    public void sendQuestion(){
        EditText question = (EditText) getActivity().findViewById(R.id.question);
        Constants.question = question.getText().toString();

        Log.d("LoginFragment", Constants.question);
        Constants.checkmyFirebaseRef().child(Constants.ID).child("Question").setValue(Constants.question);
    }

    public void numberOfAlternatives(){
        Firebase answerRef = Constants.checkmyFirebaseRef().child(Constants.ID).child("numOfAlternatives");

        Spinner numOfAlts = (Spinner) getActivity().findViewById(R.id.theNumOfAlts);
        int theAlts = numOfAlts.getSelectedItemPosition();
        theAlts = theAlts+2;
        Constants.numOfAlts = theAlts;


        answerRef.setValue(theAlts);
    }

    public void sendTheme(){
        Spinner spinner = (Spinner) getActivity().findViewById(R.id.spinner);
        String theme = spinner.getSelectedItem().toString();

        Constants.checkmyFirebaseRef().child(Constants.ID).child("Theme").setValue(theme);
    }

    public void sendActiveState(){
        Constants.checkmyFirebaseRef().child(Constants.ID).child("Active").setValue(true);
    }

    public void genRandom(){
        SecureRandom random = new SecureRandom();

        String value = new BigInteger(130, random).toString(32);

        Constants.ID = value;
    }

    public void sendUsername(){
        EditText name = (EditText) getActivity().findViewById(R.id.name);
        Constants.userName = name.getText().toString();

        Constants.checkmyFirebaseRef().child(Constants.ID).child("Creator").setValue(Constants.userName);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View returnView = inflater.inflate(R.layout.fragment_login, container, false);
        Log.d("LoginFragment", "Inside OncreateView");


        Button d = (Button) returnView.findViewById(R.id.btnLogon);
        d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View returnView) {
                Log.d("LoginFragment", "Pressed Login button");

                Firebase fireBaseEntryForScreenNbr = Constants.checkmyFirebaseRef().child("ScreenNbr");

                logMeIn();
                fireBaseEntryForScreenNbr.addValueEventListener(LoginFragment.this);
            }

        });


        return returnView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("LoginFragment", "Activity Created");
    }


    public void logMeIn(){
        Log.d("LoginFragment", "LogMeIn init");

        myFirebaseRef = Constants.checkmyFirebaseRef();
        Firebase refNbr = myFirebaseRef.child("ScreenNbr");

        refNbr.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {

                long val = (long) snapshot.getValue();
                String screenNbrFromFirebase = String.valueOf(val);
                Log.i("LoginFragment", "Screen nbr entered: " + val + " Value from firebase: "+screenNbrFromFirebase);
                EditText screenNumber = (EditText) getActivity().findViewById(R.id.screenNumber);



                //Are we on the right screen
                if (screenNbrFromFirebase.equals(screenNumber.getText().toString())){
                    Log.i("LoginFragment", "Logged in");

                    genRandom();
                    sendUsername();
                    sendQuestion();
                    sendTheme();
                    numberOfAlternatives();
                    sendActiveState();

                    FragmentManager fm;
                    fm = getFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.replace(R.id.container, new AlternativeInput());
                    ft.commit();
                }   else {
                    Toast.makeText(getActivity(),"Not the correct Screen",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }




    @Override
    public void onDataChange(DataSnapshot snapshot) {
        if (snapshot.getValue()!=null) {
            Log.i("LoginFragment", "DATASNAPSHOT IS NOT NULL");
        }
    }

    @Override
    public void onCancelled(FirebaseError firebaseError) {
        Log.d("LoginFragment", "Error: "+firebaseError);
    }
}
