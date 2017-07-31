package com.rakeshgangwar.musictrack

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.firebase.auth.FirebaseAuth

/**
 * Created by dell on 7/30/2017.
 */

class SpotifyReceiver : BroadcastReceiver() {

    var mAuth:FirebaseAuth = FirebaseAuth.getInstance()

    internal object BroadcastTypes {
        val SPOTIFY_PACKAGE = "com.spotify.music"
        val PLAYBACK_STATE_CHANGED = SPOTIFY_PACKAGE + ".playbackstatechanged"
        val QUEUE_CHANGED = SPOTIFY_PACKAGE + ".queuechanged"
        val METADATA_CHANGED = SPOTIFY_PACKAGE + ".metadatachanged"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        val action = intent?.getAction()

        val firebaseService: FirebaseService = FirebaseService()

        if (action == BroadcastTypes.METADATA_CHANGED) {
            val trackId = intent.getStringExtra("id")
            val artistName = intent.getStringExtra("artist")
            val albumName = intent.getStringExtra("album")
            val trackName = intent.getStringExtra("track")

            if(mAuth.currentUser!=null){
                firebaseService.writeToDb(mAuth.currentUser!!.uid,trackName,albumName,artistName,trackId)
            }
            else {
                Log.v("FirebaseMusicTrack","Not signed in!")
            }

        }
//        else if (action == BroadcastTypes.PLAYBACK_STATE_CHANGED) {
//            val playing = intent.getBooleanExtra("playing", false)
//            val positionInMs = intent.getIntExtra("playbackPosition", 0)
//            // Do something with extracted information
//        } else if (action == BroadcastTypes.QUEUE_CHANGED) {
//            // Sent only as a notification, your app may want to respond accordingly.
//        }
    }

}