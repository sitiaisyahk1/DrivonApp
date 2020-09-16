package com.sitiaisyah.idn.drivondesign.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.sitiaisyah.idn.drivondesign.R
import com.sitiaisyah.idn.drivondesign.model.Users
import com.sitiaisyah.idn.drivondesign.utils.Constan
import kotlinx.android.synthetic.main.activity_sign_up.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.startActivity

class SignUpActivity : AppCompatActivity() {

    private var auth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        supportActionBar?.hide()

        auth = FirebaseAuth.getInstance()

        btn_signup.onClick {
            if (et_signup_email.text.isNotEmpty() &&
                et_signup_name.text.isNotEmpty() &&
                et_signup_hp.text.isNotEmpty() &&
                et_signup_password.text.isNotEmpty() &&
                et_signup_confirm_password.text.isNotEmpty()
            ){
                authUserSignUp(
                    et_signup_email.text.toString(),
                    et_signup_password.text.toString()
                )
            }
        }
    }

    //proses autentication
    private fun authUserSignUp(email: String, pass: String): Boolean?{

        auth = FirebaseAuth.getInstance()
        var status: Boolean? = null
        val TAG = "tag"

        auth?.createUserWithEmailAndPassword(email, pass)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful){
                    if (insertUser(
                            et_signup_name.text.toString(),
                            et_signup_email.text.toString(),
                            et_signup_hp.text.toString(),
                            task.result?.user!!
                        )){
                        startActivity<LoginActivity>()
                    }
                } else {
                    status = false
                }
            }

        return status
    }

    //menambahkan datauser ke database
    private fun insertUser(name: String, email: String, hp: String, users: FirebaseUser): Boolean {
        var user = Users()
        user.uid = users.uid
        user.name = name
        user.email = email
        user.hp = hp

        val database = FirebaseDatabase.getInstance()
        var key = database.reference.push().key
        val myRef = database.getReference(Constan.tb_uaser)

        myRef.child(key!!).setValue(user)

        return true
    }
}