package com.example.notemanagersystem.ui.logout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.notemanagersystem.DashBoard;
import com.example.notemanagersystem.DatabaseHelper;
import com.example.notemanagersystem.MainActivity;
import com.example.notemanagersystem.R;
import com.example.notemanagersystem.ui.status.Status;
import com.example.notemanagersystem.ui.status.StatusAdapter;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Date;
import java.util.List;

import static com.example.notemanagersystem.DashBoard.currentEmail;

public class LogoutFragment extends Fragment {

    FirebaseAuth mAuth;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_status, container, false);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getContext());
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        mAuth = FirebaseAuth.getInstance();
        if (acct!=null)
        {
            GoogleSignInClient mGoogleSignInClient;
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build();
            mGoogleSignInClient = GoogleSignIn.getClient(getContext(), gso);
            mGoogleSignInClient.signOut();
        }else if (accessToken!=null)
        {
            FirebaseAuth.getInstance().signOut();
            LoginManager.getInstance().logOut();
        }
        Intent intent = new Intent(getContext(), MainActivity.class);
        startActivity(intent);
        return root;
    }
}