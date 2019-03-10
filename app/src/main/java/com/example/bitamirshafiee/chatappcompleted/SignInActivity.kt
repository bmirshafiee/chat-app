package com.example.bitamirshafiee.chatappcompleted

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_sign_in.*

class SignInActivity : AppCompatActivity(){

    companion object {
        private const val TAG = "SignInActivity"
        private const val RC_SIGN_IN = 9001
    }

    //A client for interacting with the Google Sign In API.
    private var googleSignInClient: GoogleSignInClient? = null

    private  var fireBaseAuth : FirebaseAuth? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        fireBaseAuth = FirebaseAuth.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        sign_in_button.setOnClickListener {
            signIn()
        }
    }

    override fun onStart() {
        super.onStart()

//        val currentUser = fireBaseAuth.currentUser
//
//        startActivity(Intent(this@SignInActivity, MainActivity::class.java))
//        finish()

//        Log.d(TAG, "IS SHE LOGGED IN BEFORE: $currentUser")
    }

    private fun signIn(){
        val signInIntent = googleSignInClient!!.signInIntent
        //startActivityForResult : Start an activity and expect something in return
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);

        if(requestCode == RC_SIGN_IN){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)
                fireBaseAuthWithGoogle(account!!)

            }catch (e: ApiException){
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e)
                Toast.makeText(this, "Google sign in failed", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun fireBaseAuthWithGoogle( account: GoogleSignInAccount){
        Log.d(TAG, "firebaseAuthWithGoogle:" + account.id!!)
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        Log.d(TAG, "firebaseAuthWithGoogle:" + credential)
        //Task is an API that represents asynchronous method calls,
        fireBaseAuth!!.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->

                // If sign in fails, display a message to the user. If sign in succeeds
                // the auth state listener will be notified and logic to handle the
                // signed in user can be handled in the listener.


                if (task.isSuccessful){
                    Log.d(TAG, "signInWithCredential:success")
                    val user = fireBaseAuth!!.currentUser
                    startActivity(Intent(this@SignInActivity, MainActivity::class.java))
                    finish()

                }else{
                    Log.w(TAG, "firebaseAuthWithGoogle:", task.exception)
                    Toast.makeText(this@SignInActivity, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()

                }


            }


    }
}

