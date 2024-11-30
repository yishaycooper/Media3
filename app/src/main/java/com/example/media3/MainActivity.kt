package com.example.media3

import android.content.ComponentName
import androidx.media3.session.MediaController
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.SessionToken
import com.example.media3.databinding.ActivityMainBinding
import com.google.common.util.concurrent.MoreExecutors


@UnstableApi
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var mediaController: MediaController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Create a session token
        val sessionToken = SessionToken(
            applicationContext,
            ComponentName(this, PlayerService::class.java)
        )

        // Link MediaController with service
        val mediaControllerFuture = MediaController.Builder(this, sessionToken).buildAsync()

        mediaControllerFuture.addListener(
            {
                mediaController = mediaControllerFuture.get()
                binding.playerView.player = mediaController

                val mediaItem = MediaItem.Builder()
                    .setUri("https://live-par-2-abr.livepush.io/vod/bigbuckbunnyclip.mp4")
                    .setMediaMetadata(
                        MediaMetadata.Builder()
                            .setTitle("Big Buck Bunny")
                            .build()
                    )
                    .build()

                // Send media item to the service and play
                mediaController?.setMediaItem(mediaItem)
                mediaController?.prepare() // Prepare the media

                mediaController?.play()
            },
            MoreExecutors.directExecutor()
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaController?.release()
    }
}




//class MainActivity : AppCompatActivity() {
//
//    private lateinit var binding: ActivityMainBinding
//
//    @OptIn(UnstableApi::class)
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        binding = ActivityMainBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//
////    Creating session token (links UI with service and starts it)
//        val sessionToken = SessionToken(applicationContext, ComponentName(this, PlayerService::class.java))
//
////    Instantiating our MediaController and linking it to the service using the session token
//        val mediacontrollerFuture = MediaController.Builder(this, sessionToken).buildAsync()
//
//        binding.playerView.player
//
//
//    }
//
//
//}












//class MainActivity : AppCompatActivity() {
//    /* This is the global variable of the player
//          (which is basically a media controller)
//          you're going to use to control playback,
//          you're not gonna need anything else other than this,
//          which is created from the media controller */
//    lateinit var player: Player
//
//    @OptIn(UnstableApi::class)
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//
////    Creating session token (links UI with service and starts it)
//        val sessionToken = SessionToken(applicationContext, ComponentName(this, PlayerService::class.java))
//
////    Instantiating our MediaController and linking it to the service using the session token
//        val mediacontrollerFuture = MediaController.Builder(this, sessionToken).buildAsync()
//
//        mediacontrollerFuture.addListener({
//            player = mediacontrollerFuture.get()
//
//            loadMediaItem("https://live-par-2-abr.livepush.io/vod/bigbuckbunnyclip.mp4".toUri())
//
//        }, MoreExecutors.directExecutor())
//
//    }
//
//    fun loadMediaItem(uri: Uri) {
//        /* We use setMediaId as a unique identifier for the media (which is needed for mediasession and we do NOT use setUri because we're gonna do
//           something like setUri(mediaItem.mediaId) when we need to load the media like we did above in the MusicPlayerService and more precisely when we were building the session */
//        val newItem = MediaItem.Builder()
//            .setMediaId("$uri") // setMediaId NOT setUri
//            .build()
//
////        Load into our activity's MediaController
//        player.setMediaItem(newItem)
//        player.prepare()
//        player.play()
//    }
//
//
//}








//class MainActivity : AppCompatActivity() {
//
//
//    private lateinit var binding: ActivityMainBinding
//    private lateinit var playerView: PlayerView
//    private lateinit var player: ExoPlayer
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        binding = ActivityMainBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//
//        playerView = findViewById(R.id.playerView)
//
//        // Initialize ExoPlayer
//        player = ExoPlayer.Builder(this).build()
//
//        // Attach the player to the PlayerView
//        playerView.player = player
//
//        // Add a media item to play
//        val mediaItem =
//            MediaItem.fromUri("https://live-par-2-abr.livepush.io/vod/bigbuckbunnyclip.mp4")
//        player.setMediaItem(mediaItem)
//
//        // Prepare and start playback
//        player.prepare()
//        player.play()
//
//        val serviceIntent = Intent(this, PlayerService::class.java)
//        ContextCompat.startForegroundService(this, serviceIntent)
//
//    }
//
//}