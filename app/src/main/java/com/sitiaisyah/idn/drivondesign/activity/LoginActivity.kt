package com.sitiaisyah.idn.drivondesign.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.sitiaisyah.idn.drivondesign.MainActivity
import com.sitiaisyah.idn.drivondesign.R
import com.sitiaisyah.idn.drivondesign.model.Users
import com.sitiaisyah.idn.drivondesign.utils.Constan
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class LoginActivity : AppCompatActivity() {

    var googleSignInClient : GoogleSignInClient? = null
    private var auth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()

        auth = FirebaseAuth.getInstance()

        btn_signup_gmail.onClick {
            signIn()
        }

        tv_signup_link.onClick {
            startActivity<SignUpActivity>()
        }

        btn_login.onClick {
            if (et_login_email.text.isNotEmpty() &&
                et_login_password.text.isNotEmpty()){
                authUserSignIn(
                    et_login_email.text.toString(),
                    et_login_password.text.toString()
                )
            }
        }

    }

    //autentikasi signin email
    private fun authUserSignIn(email: String, pass: String){
        var status: Boolean? = null

        auth?.signInWithEmailAndPassword(email, pass)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful){
                    startActivity<MainActivity>()
                    finish()
                } else {
                    toast("login failed")
                    Log.e("error", "message")
                }
            }
    }

    //request sign in gmail/gugel
    private fun signIn(){
        val gson = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gson)

        val signInIntent = googleSignInClient?.signInIntent
        startActivityForResult(signInIntent, 4)
    }

    //todo 6
    //hasil request
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 4){
            val task = GoogleSignIn
                .getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account)
            } catch (e: ApiException){

            }
        }
    }


    //autentication firebase sign in
    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount?) {

        var uid = String()
        val credential = GoogleAuthProvider.getCredential(account?.idToken, null)

        auth?.signInWithCredential(credential)
            ?.addOnCompleteListener (this) { task ->
                if (task.isSuccessful){
                    val user = auth?.currentUser
                    ckeckDatabase(task.result?.user?.uid, account)
                    uid = user?.uid.toString()
                } else {

                }
            }
    }

    //check database
    private fun ckeckDatabase(uid: String?, account: GoogleSignInAccount?) {

        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference(Constan.tb_uaser)
        val query = myRef.orderByChild("uid").equalTo(auth?.uid)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    startActivity<MainActivity>()
                } else {
                    account?.displayName?.let {
                        account.email?.let { it1 ->
                            insertUser(it, it1,"", uid )
                        }
                    }
                }
            }

        })
    }

    //menambahkan data user ke realtima databse
    private fun insertUser(name: String, email: String, hp: String, idUser: String?): Boolean {
        val user = Users()
        user.email = email
        user.name = name
        user.hp = hp
        user.uid = auth?.uid

        val database = FirebaseDatabase.getInstance()
        val key = database.reference.push().key
        val myRef = database.getReference(Constan.tb_uaser)

        myRef.child(key?: "")
            .setValue(user)

        startActivity<AuthenticationHpActivity>(Constan.key to key)

        return true
    }
}