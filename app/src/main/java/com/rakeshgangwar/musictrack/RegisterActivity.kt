package com.rakeshgangwar.musictrack

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    var mAuth:FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        registerButton.setOnClickListener(View.OnClickListener {

            showProgress(true)
            val username: String = registerEmail.text.toString()
            val password: String = registerPassword.text.toString()
            val name: String = registerDisplayName.text.toString()

            mAuth.createUserWithEmailAndPassword(username,password).addOnCompleteListener(OnCompleteListener { task ->

                showProgress(false)

                if(task.isSuccessful) {
                    val user:FirebaseUser?=FirebaseAuth.getInstance().currentUser
                    if(user!=null){
                        var userProfile: UserProfileChangeRequest = UserProfileChangeRequest.Builder().setDisplayName(name).build()
                        user.updateProfile(userProfile).addOnCompleteListener(OnCompleteListener {
                            Toast.makeText(this, "Registration successful. Please sign in with username and password.", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        })
                    }
                    else {
                        Toast.makeText(this, "User not found.", Toast.LENGTH_SHORT).show()
                    }
                }
                else {
                    Toast.makeText(this, "Registration unsuccessful. "+ task.exception?.localizedMessage, Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            })
        })
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    private fun showProgress(show: Boolean) {

        val shortAnimTime = resources.getInteger(android.R.integer.config_shortAnimTime).toLong()

        register_form.visibility = if (show) View.GONE else View.VISIBLE
        register_form.animate()
                .setDuration(shortAnimTime)
                .alpha((if (show) 0 else 1).toFloat())
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        register_form.visibility = if (show) View.GONE else View.VISIBLE
                    }
                })

        register_progress.visibility = if (show) View.VISIBLE else View.GONE
        register_progress.animate()
                .setDuration(shortAnimTime)
                .alpha((if (show) 1 else 0).toFloat())
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        register_progress.visibility = if (show) View.VISIBLE else View.GONE
                    }
                })

    }
}
