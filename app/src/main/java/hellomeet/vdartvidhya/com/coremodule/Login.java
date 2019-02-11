package hellomeet.vdartvidhya.com.coremodule;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import hellomeet.vdartvidhya.com.Models.MeetUsers;
import hellomeet.vdartvidhya.com.R;
import hellomeet.vdartvidhya.com.Utils.CustomDialog;

/**
 * Created by Vidhya on 2/9/2019.
 * HelloMeet
 */
public class Login extends AppCompatActivity {


    //***************************************************************************************

    private static final String TAG = "HelloMeet";
    private static final int RC_SIGN_IN = 0;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    @BindView(R.id.activity_main)
    LinearLayout parentLayout;
    @BindView(R.id.sign_in_button)
    Button signInButton;
    @BindView(R.id.name_text_view)
    TextView nameTextView;
    @BindView(R.id.email_text_view)
    TextView emailTextView;
    @BindView(R.id.bt_no)
    Button NoButton;
    @BindView(R.id.tvquestion)
    TextView tvquestion;
    @BindView(R.id.tvparticipants)
    TextView tvparticipants;

    //a list to store all the MeetUsers from firebase database
    List<MeetUsers> Users;

    FirebaseFirestore myDB;

    //***************************************************************************************
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_login);

        try {

            GetInitialize();

            Controllisteners();

        } catch (Exception e) {

            e.printStackTrace();
        }

    }

    ProgressDialog progressBar;

    private void GetResponse() {

        progressBar = new ProgressDialog(this);
        progressBar.setMessage("Please wait");
        progressBar.setTitle("Information");

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
                    progressBar.dismiss();
                    tvquestion.setText("");
                    //tvquestion.setText("Thank you for your response");
                    //tvparticipants.setText("Total number of participants (" + list.size() + ") in this meeting..");

                    ShowSuccessDialog(list);

                    NoButton.setText("Close");

                } else {
                    Log.e(TAG, "Error getting documents: ", task.getException());
                }

            }
        });

    }

    private void ShowSuccessDialog(List<String> list) {
        CustomDialog customDialog=new CustomDialog(Login.this);
        customDialog.setDescription("Total number of participants - " + list.size() + "");
        customDialog.setTitle("Thanks");
        customDialog.setPossitiveButtonTitle("Ok");
        customDialog.setNegativeButtonVisible(View.GONE);
        customDialog.setLayoutColor(R.color.green_400);
        customDialog.setImage(R.drawable.ic_done_all_black_24dp);
        customDialog.setOnPossitiveListener(new CustomDialog.possitiveOnClick() {
            @Override
            public void onPossitivePerformed() {
                customDialog.dismiss();
            }
        });
    }


    //***************************************************************************************

    private void GetInitialize() {

        try {

            ButterKnife.bind(this);

            Constants.changeStatusBarColour(this);

            // Init FireStore
            myDB = FirebaseFirestore.getInstance();

            //list for store objects of user
            Users = new ArrayList<>();

            FirebaseAuth();


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //***************************************************************************************

    private void FirebaseAuth() {
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                try {
                    FirebaseUser user = firebaseAuth.getCurrentUser();

                    if (user != null) {
                        signInButton.setVisibility(View.GONE);
                        NoButton.setVisibility(View.VISIBLE);



                        //signOutButton.setVisibility(View.VISIBLE);
                        // MeetUsers is signed in
                        Log.e(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                        if (user.getDisplayName() != null) {
                            String Username, UserEmail;
                            Username = user.getDisplayName();
                            UserEmail = user.getEmail();

                            nameTextView.setText("Hi " + Username);
                            emailTextView.setText(UserEmail);

                            AddUser(Constants.GetTimeStamp(), Username, UserEmail);

                           // emailTextView.setText("vidhyabtech91@gmail.com");
                            //Move to admin login
                            if (emailTextView.getText().toString().equalsIgnoreCase("hellomeet3@gmail.com")) {
                                Intent admin = new Intent(Login.this, Admin.class);
                                startActivity(admin);
                                Login.this.finish();
                            }else{
                                GetResponse();

                            }

                        }
                    } else {
                        // MeetUsers is signed out
                        Log.e(TAG, "onAuthStateChanged:signed_out");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        };
    }


    private void AddUser(String Id, String userName, String userEmail) {

        Map data = new HashMap<>();
        data.put("Id", Id);
        data.put("userName", userName);
        data.put("userEmail", userEmail);

        myDB.collection("users")
                .document(userEmail)
                .set(data)
                .addOnSuccessListener(new OnSuccessListener() {

                    @Override
                    public void onSuccess(Object o) {
                       // Constants.SnackBar(Login.this, "Thank you for your response..", parentLayout, 1);

                    }

                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Constants.SnackBar(Login.this, "Data error while adding the data", parentLayout, 2);
                    }
                });

    }


    //***************************************************************************************
    GoogleApiClient mGoogleApiClient;

    private void Controllisteners() {

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInButton.setVisibility(View.GONE);
                NoButton.setVisibility(View.GONE);
                //signOutButton.setVisibility(View.VISIBLE);
                signIn();
            }
        });

        NoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login.this.finishAffinity();//finish all the activity
            }
        });
       /* signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                        new ResultCallback<Status>() {
                            @Override
                            public void onResult(Status status) {
                                signInButton.setVisibility(View.VISIBLE);
                                signOutButton.setVisibility(View.GONE);
                                //RemoveUser();--commented
                                emailTextView.setText(" ".toString());
                                nameTextView.setText(" ".toString());


                            }
                        });
            }

        });*/

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                    }
                } /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


    }

    private void RemoveUser() {
        String StrEmailId = emailTextView.getText().toString();
        myDB.collection("users").document(StrEmailId).delete()
                .addOnSuccessListener(new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {
                        // Constants.SnackBar(Login.this, "Signed out successfully..", parentLayout, 1);
                    }

                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        // Constants.SnackBar(Login.this, "Data error while deleting the data", parentLayout, 2);
                    }
                });
    }

    //***************************************************************************************

    private void signIn() {
        try {
            Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
            startActivityForResult(signInIntent, RC_SIGN_IN);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //***************************************************************************************

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);

            } else {
                // Google Sign In failed, update UI appropriately
                Constants.SnackBar(Login.this, "Google Sign In failed..", parentLayout, 2);
            }
        }


    }
    //***************************************************************************************

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    //***************************************************************************************

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
    //***************************************************************************************

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Constants.SnackBar(Login.this, "Authentication failed", parentLayout, 2);
                        }
                    }
                });
    }

    //***************************************************************************************


    @Override
    public void onBackPressed() {

        Constants.ExitDialog(this);

    }


    //***************************************************************************************

}//END
