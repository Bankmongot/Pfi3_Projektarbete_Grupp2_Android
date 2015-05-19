package se.mah.k3.klarappo;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */

public class LoginFragment extends Fragment implements ValueEventListener {




    public LoginFragment() {
        // Required empty public constructor
    }



    public void sendQuestion(){
        EditText question = (EditText) getActivity().findViewById(R.id.question);
        Constants.question = question.getText().toString();
        Log.d("LoginFragment", Constants.question);
        Constants.checkmyFirebaseRef().child(Constants.userName).child("Question").setValue(Constants.question);

        //Log.d("LoginFragment", String.valueOf(Constants.myFirebaseRef));
    }

    public class Answer {
        private int votes;
        private String alternative;
        public Answer() {}

        public Answer(String alternative, int votes) {
            this.alternative = alternative;
            this.votes = votes;
        }

        public String getAlternative(){
            return alternative;
        }

        public int getVotes(){
            return votes;
        }
    }


    public void sendAlts(){
        //Firebase answerRef = Constants.myFirebaseRef.child(Constants.userName).child("answers");

        Firebase answerRef = Constants.checkmyFirebaseRef().child(Constants.userName);

        Map<String, Answer> ourMap = new HashMap<String, Answer>();

        EditText alt1 = (EditText) getActivity().findViewById(R.id.editTextAlt1);
        EditText alt2 = (EditText) getActivity().findViewById(R.id.editTextAlt2);
        EditText alt3 = (EditText) getActivity().findViewById(R.id.editTextAlt3);
        EditText alt4 = (EditText) getActivity().findViewById(R.id.editTextAlt4);

        Constants.alt1 = alt1.getText().toString();
        Constants.alt2 = alt2.getText().toString();
        Constants.alt3 = alt3.getText().toString();
        Constants.alt4 = alt4.getText().toString();

        Answer one = new Answer(Constants.alt1, 0);
        Answer two = new Answer(Constants.alt2, 0);
        Answer three = new Answer(Constants.alt3, 0);
        Answer four = new Answer(Constants.alt4, 0);

        ourMap.put("one", one);
        ourMap.put("two", two);
        ourMap.put("three", three);
        ourMap.put("four", four);

        answerRef.setValue(ourMap);
    }

    public void shittyFunction(){
        EditText alt1 = (EditText) getActivity().findViewById(R.id.editTextAlt1);
        EditText alt2 = (EditText) getActivity().findViewById(R.id.editTextAlt2);
        EditText alt3 = (EditText) getActivity().findViewById(R.id.editTextAlt3);
        EditText alt4 = (EditText) getActivity().findViewById(R.id.editTextAlt4);

        Constants.alt1 = alt1.getText().toString();
        Constants.alt2 = alt2.getText().toString();
        Constants.alt3 = alt3.getText().toString();
        Constants.alt4 = alt4.getText().toString();

        Constants.checkmyFirebaseRef().child(Constants.userName).child("Alt1").setValue(Constants.alt1);
        Constants.checkmyFirebaseRef().child(Constants.userName).child("Alt2").setValue(Constants.alt2);
        Constants.checkmyFirebaseRef().child(Constants.userName).child("Alt3").setValue(Constants.alt3);
        Constants.checkmyFirebaseRef().child(Constants.userName).child("Alt4").setValue(Constants.alt4);

        Constants.checkmyFirebaseRef().child(Constants.userName).child("Vote1").setValue(0);
        Constants.checkmyFirebaseRef().child(Constants.userName).child("Vote2").setValue(0);
        Constants.checkmyFirebaseRef().child(Constants.userName).child("Vote3").setValue(0);
        Constants.checkmyFirebaseRef().child(Constants.userName).child("Vote4").setValue(0);

        Constants.checkmyFirebaseRef().child(Constants.userName).child("Theme").setValue("asdf");
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View returnView = inflater.inflate(R.layout.fragment_login, container, false);
        View v = returnView.findViewById(R.id.btnLogon);
        v.setOnClickListener(new View.OnClickListener() {
            //Click on loginButton
            @Override
            public void onClick(View v) {

                //In firebase you read a value by adding a listener, then it will trigger once connected and on all changes.
                //There is no readvalue as one could expect only listeners.
                //Get the ScreenNbr child
                Firebase fireBaseEntryForScreenNbr = Constants.checkmyFirebaseRef().child("ScreenNbr");
                //Ok listen the changes will sho up in the method onDataChange
                fireBaseEntryForScreenNbr.addValueEventListener(LoginFragment.this);
            }
        });
        return returnView;
    }




    @Override
    public void onDataChange(DataSnapshot snapshot) {
        if (snapshot.getValue()!=null) {
            long val = (long) snapshot.getValue();
            String screenNbrFromFirebase = String.valueOf(val);
            Log.i("LoginFragment", "Screen nbr entered: " + val + " Value from firebase: "+screenNbrFromFirebase);
            EditText screenNumber = (EditText) getActivity().findViewById(R.id.screenNumber);
            EditText name = (EditText) getActivity().findViewById(R.id.name);

            Constants.userName = name.getText().toString();


            //Are we on the right screen
            if (screenNbrFromFirebase.equals(screenNumber.getText().toString())){
                Log.i("LoginFragment", "Logged in");


                sendQuestion();
                //sendAlts();
                shittyFunction();

                FragmentManager fm;
                fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.container, new MainFragment());
                ft.commit();
            }else{
                Toast.makeText(getActivity(),"Not the correct Screen",Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onCancelled(FirebaseError firebaseError) {

    }
}
