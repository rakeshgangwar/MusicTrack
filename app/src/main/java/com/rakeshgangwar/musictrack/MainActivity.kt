package com.rakeshgangwar.musictrack

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Intent
import android.os.Bundle
import android.renderscript.Sampler
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_main.*
import android.widget.Toast
import com.google.firebase.database.DatabaseError
import android.R.attr.author
import android.R.attr.data
import android.content.Context
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.SimpleCursorAdapter
import com.google.firebase.database.DataSnapshot
import java.nio.file.Files.size






class MainActivity : AppCompatActivity() {

    class songInfo(var timestamp: Long, var trackName: String, var trackAlbum: String, var  trackArtist: String, var trackID: String)

    val mAuth:FirebaseAuth = FirebaseAuth.getInstance()
    lateinit var dbReference:DatabaseReference
    var songList: ArrayList<String> = ArrayList()
    lateinit var songAdapter:ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        displayName.setText("Hello, "+mAuth.currentUser?.displayName)
        songAdapter = ArrayAdapter(this,android.R.layout.simple_list_item_1, songList)
        songListView.adapter = songAdapter

        signoutButton.setOnClickListener(View.OnClickListener {
            mAuth.signOut()
        })

        mAuth.addAuthStateListener({ firebaseAuth ->
            if (firebaseAuth.currentUser == null) {
                val intent = Intent(this, LoginActivity::class.java)
                finish()
                startActivity(intent)
            }
        })
        dbReference = FirebaseDatabase.getInstance().getReference(mAuth.currentUser?.uid)

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val dataSet:Map<String, Map<String, String>> = dataSnapshot.value as Map<String, Map<String, String>>
                songList.clear()

                val keySet:Set<String> = dataSet.keys
                val keySetIterator:Iterator<String> = keySet.iterator()

                while (keySetIterator.hasNext()){
                    var key:String = keySetIterator.next()
                    songList.add(dataSet.get(key)!!.get("trackName")!!)
                }

                songAdapter.notifyDataSetChanged()
                Log.v("Database", "loadPost:onDataChange")
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.v("Database", "loadPost:onCancelled", databaseError.toException())

            }
        }

        dbReference.addValueEventListener(postListener)

    }

    private fun showProgress(show: Boolean) {

        val shortAnimTime = resources.getInteger(android.R.integer.config_shortAnimTime).toLong()

        mainLayout.visibility = if (show) View.GONE else View.VISIBLE
        mainLayout.animate()
                .setDuration(shortAnimTime)
                .alpha((if (show) 0 else 1).toFloat())
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        mainLayout.visibility = if (show) View.GONE else View.VISIBLE
                    }
                })

        main_progress.visibility = if (show) View.VISIBLE else View.GONE
        main_progress.animate()
                .setDuration(shortAnimTime)
                .alpha((if (show) 1 else 0).toFloat())
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        main_progress.visibility = if (show) View.VISIBLE else View.GONE
                    }
                })

    }
}
