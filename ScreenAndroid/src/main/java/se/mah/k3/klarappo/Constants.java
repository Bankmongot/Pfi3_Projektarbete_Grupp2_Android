package se.mah.k3.klarappo;

import com.firebase.client.Firebase;


public class Constants {
    public static Firebase myFirebaseRef;
    public static String userName = "Username";
    public static String question = "";



    public static String ID;

    public static boolean theBoolean;

    public static boolean alreadyRunning = false;
    public static boolean runItOnce = true;

    public static Long numOfAlts;


    public static String URL = "https://popping-torch-1741.firebaseio.com/";


    //Firebase cannot handle multiple references
    public static Firebase checkmyFirebaseRef(){
        if(myFirebaseRef == null){
           myFirebaseRef = new Firebase("https://popping-torch-1741.firebaseio.com/");
        }
        return myFirebaseRef;
    }
}
