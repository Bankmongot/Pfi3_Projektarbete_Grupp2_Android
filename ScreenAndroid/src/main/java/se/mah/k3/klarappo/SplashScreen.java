package se.mah.k3.klarappo;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class SplashScreen extends Fragment {

    private boolean timeToRun;
    private Firebase myFirebaseRef;



    public SplashScreen() {
        // Required empty public constructor
    }




    public void checkRun() {
        myFirebaseRef = Constants.checkmyFirebaseRef(); //With this there is only one ref to firebase. Use Always

        Firebase refToBoolean = myFirebaseRef.child("timeToRun"); //Or what your boolean is called CHANGE HERE

        refToBoolean.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {

                //timeToRun = snapshot.getValue();
                System.out.println(snapshot.getValue());

                //You should only listen for changes on a specific part
                timeToRun = (boolean) snapshot.getValue();


                if(timeToRun == false){
                    Log.d("SplashScreen", "false, expanding LoginFragment!!");
                    FragmentManager fm;
                    fm = getFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.replace(R.id.container, new LoginFragment());
                    ft.commit();
                } else if ( timeToRun == true && Constants.alreadyRunning == false){
                    Log.d("SplashScreen", "true, expanding Mainfragent!!");
                    FragmentManager fm;
                    fm = getFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.replace(R.id.container, new MainFragment());
                    ft.commit();
                }

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }

        });

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        checkRun();
        return inflater.inflate(R.layout.fragment_splash_screen, container, false);

    }


}
