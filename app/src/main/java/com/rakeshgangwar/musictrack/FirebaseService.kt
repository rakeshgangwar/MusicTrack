package com.rakeshgangwar.musictrack

import android.util.Log
import com.rakeshgangwar.musictrack.Application.Companion.database

/**
 * Created by dell on 7/30/2017.
 */

class FirebaseService {
    fun writeToDb(userId: String, track: String, album: String, artist: String, trackID: String){

        class songInfo(var timestamp: Long, var trackName: String, var trackAlbum: String, var  trackArtist: String, var trackID: String)

        val song = songInfo(System.currentTimeMillis(),track, album, artist, trackID)
        val trackDB = database.getReference(userId)

        trackDB.push().setValue(song).addOnSuccessListener({
            Log.v("FirebaseMusicTrack","Successfully Inserted")
        }).addOnFailureListener({
            Log.v("FirebaseMusicTrack","Not inserted")
        })
    }
}