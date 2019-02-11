package hellomeet.vdartvidhya.com.coremodule;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import hellomeet.vdartvidhya.com.Adapter.UserAdapter;
import hellomeet.vdartvidhya.com.Models.MeetUsers;
import hellomeet.vdartvidhya.com.R;

/**
 * Created by Vidhya on 2/9/2019.
 * HelloMeet
 */
public class Admin extends AppCompatActivity {

    //***************************************************************************************
    //Declaration
    private static final String TAG = "Admin Page";
    FirebaseFirestore myDB;
    //a list to store all the MeetUsers from firebase database
    List<MeetUsers> Users;
    @BindView(R.id.tvCount)
    TextView tvCount;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;
    List<MeetUsers> mUserData = new ArrayList<>();
    //***************************************************************************************

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_admin);

        try {

            GetInitialize();

            Controllisteners();

            GetResponse();

            GetAllUserList();

            LoadRecyclerView();

        } catch (Exception e) {

            e.printStackTrace();
        }

    }

    //***************************************************************************************


    private void GetInitialize() {

        ButterKnife.bind(this);

        // Init FireStore
        myDB = FirebaseFirestore.getInstance();

        //list for store objects of user
        Users = new ArrayList<>();

    }
    //***************************************************************************************


    private void Controllisteners() {


    }
    //***************************************************************************************

    @Override
    public void onBackPressed() {

        Constants.ExitDialog(this);

    }


    public void LoadRecyclerView() {
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(Admin.this,LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLinearLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(Admin.this,DividerItemDecoration.VERTICAL));

    }
    //***************************************************************************************


    MeetUsers meetUsers;
    private void GetAllUserList() {

        myDB.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {


                    for (DocumentChange documentChange : task.getResult().getDocumentChanges()) {

                        String Id = documentChange.getDocument().getData().get("Id").toString();
                        String userName  = documentChange.getDocument().getData().get("userName").toString();
                        String userEmail = documentChange.getDocument().getData().get("userEmail").toString();

                        meetUsers=new MeetUsers(Id, userName, userEmail);
                        mUserData.add(meetUsers);
                        Log.e("Values: ", Id+userEmail+userName);
                    }

                    //load recycler
                    UserAdapter mUserAdapter = new UserAdapter(Admin.this, mUserData);
                    recyclerView.setAdapter(mUserAdapter);

                } else {
                    Log.e(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }
    //***************************************************************************************

    private void GetResponse() {

        myDB.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<String> list = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        document.getData();
                        list.add(document.getId());
                    }
                    Log.e(TAG, list.toString());
                   // tvCount.setText(String.valueOf(list.size()));
                    Constants.animateTextView(0, list.size(), tvCount);

                } else {
                    Log.e(TAG, "Error getting documents: ", task.getException());
                }

            }
        });

    }
    //***************************************************************************************


}//END
