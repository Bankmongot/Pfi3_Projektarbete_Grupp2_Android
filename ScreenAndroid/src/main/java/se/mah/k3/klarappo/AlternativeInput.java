package se.mah.k3.klarappo;


import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.app.Fragment;
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

    public void setNumOfInputBoxes(){
        int index = Constants.numOfAlts;

        for(int i = 0; i<Constants.numOfAlts; i++){
            EditText et = new EditText(getActivity());

            theLayout.addView(et);
        }
    }



    public class EnterText extends Activity {

        Button btnMyLine,btnSave;
        LinearLayout LLEnterText;
        int _intMyLineCount;

        String ETTitleEnterText = "hej";
        String StartTabHost = "Hej";

        private List<EditText> editTextList = new ArrayList<EditText>();
        private List<TextView> textviewList=new ArrayList<TextView>();
        private List<LinearLayout> linearlayoutList=new ArrayList<LinearLayout>();
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(theView);

        for(int i = 0; i<Constants.numOfAlts; i++){
        }

            LLEnterText=(LinearLayout) findViewById(R.id.TheLinearLayout);

            //LLEnterText.setOrientation(LinearLayout.VERTICAL);
            //btnMyLine=(Button) findViewById(R.id.btnMyLines);
            //btnSave=(Button) findViewById(R.id.btnSave);


            btnMyLine.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LLEnterText.addView(linearlayout(_intMyLineCount));
                    _intMyLineCount++;
                }
            });

         /*   btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ETTitleEnterText.getText().length() == 0)
                    {
                        Toast.makeText(EnterText.this, "Please Enter Full Details", Toast.LENGTH_LONG).show();
                    }else{

                        for (EditText editText : editTextList) {
                            .VARClass._ArrLinesDetails.add(editText.getText().toString());
                        }
                        for(TextView textview:textviewList){
                            StartTabHost.VARClass._ArrLinesTitle.add(textview.getText().toString());
                        }
                        for(int i=0;i<StartTabHost.VARClass._ArrLinesTitle.size();i++)
                        {
                            Log.d("LinesTitle", StartTabHost.VARClass._ArrLinesTitle.get(i));
                            Log.d("LinesDetails",StartTabHost.VARClass._ArrLinesDetails.get(i));
                        }

                    }

                }
            }); */
        }

        private EditText editText(int _intID) {
            EditText editText = new EditText(this);
            editText.setId(_intID);
            editText.setHint("My lines");
            editText.setWidth(180);
            editText.setBackgroundColor(Color.WHITE);
            editTextList.add(editText);
            return editText;
        }
        private TextView textView(int _intID)
        {
            TextView txtviewAll=new TextView(this);
            txtviewAll.setId(_intID);
            txtviewAll.setText("My lines:");
            txtviewAll.setTextColor(Color.RED);
            txtviewAll.setTypeface(Typeface.DEFAULT_BOLD);
            textviewList.add(txtviewAll);
            return txtviewAll;
        }
        private LinearLayout linearlayout(int _intID)
        {
            LinearLayout LLMain=new LinearLayout(this);
            LLMain.setId(_intID);
            LLMain.addView(textView(_intID));
            LLMain.addView(editText(_intID));
            LLMain.setOrientation(LinearLayout.HORIZONTAL);
            linearlayoutList.add(LLMain);
            return LLMain;

        }

    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View altView = inflater.inflate(R.layout.fragment_alternative_input, container, false);
        theView = altView;
        LinearLayout ll = new LinearLayout(getActivity());
        theLayout = ll;

        //setNumOfInputBoxes();

        for(int i = 0; i<Constants.numOfAlts; i++){
            EditText et = new EditText(getActivity());

            theLayout.addView(et);
        }

        // Inflate the layout for this fragment
        return altView;
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
