package com.example.media3

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import androidx.media3.ui.PlayerNotificationManager


@UnstableApi
class PlayerService : MediaSessionService() {
    private var mediaSession: MediaSession? = null

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? {
        return mediaSession
    }

    override fun onCreate() {
        super.onCreate()

        // Initialize ExoPlayer
        val player = ExoPlayer.Builder(this).build()

        // Create MediaSession
        mediaSession = MediaSession.Builder(this, player)
            .setSessionActivity(
                PendingIntent.getActivity(
                    this,
                    0,
                    Intent(this, MainActivity::class.java),
                    PendingIntent.FLAG_IMMUTABLE
                )
            )
            .build()

        // Set up a notification for the MediaSession
        val notificationManager = PlayerNotificationManager.Builder(
            this,
            1, // Notification ID
            "playback_channel" // Notification channel ID
        )
            .setMediaDescriptionAdapter(DescriptionAdapter(mediaSession!!))
            .setNotificationListener(object : PlayerNotificationManager.NotificationListener {
                override fun onNotificationCancelled(notificationId: Int, dismissedByUser: Boolean) {
                    if (dismissedByUser) stopSelf()
                }

                override fun onNotificationPosted(notificationId: Int, notification: Notification, ongoing: Boolean) {
                    startForeground(notificationId, notification)
                }
            })
            .build()

        notificationManager.setMediaSessionToken(mediaSession!!.sessionCompatToken)
        notificationManager.setPlayer(player)

        // Create the notification channel for API >= 26
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "playback_channel",
                "Playback Notifications",
                NotificationManager.IMPORTANCE_LOW
            )
            val notificationService = getSystemService(NotificationManager::class.java)
            notificationService?.createNotificationChannel(channel)
        }
    }

    override fun onDestroy() {
        mediaSession?.let {
            it.player.release()
            it.release()
            mediaSession = null
        }
        super.onDestroy()
    }

    private class DescriptionAdapter(private val mediaSession: MediaSession) :
        PlayerNotificationManager.MediaDescriptionAdapter {
        override fun getCurrentContentTitle(player: Player): CharSequence {
            return player.currentMediaItem?.mediaMetadata?.title ?: "Unknown Title"
        }

        override fun createCurrentContentIntent(player: Player): PendingIntent? {
            return mediaSession.sessionActivity
        }

        override fun getCurrentContentText(player: Player): CharSequence? {
            return player.currentMediaItem?.mediaMetadata?.artist
        }

        override fun getCurrentLargeIcon(
            player: Player,
            callback: PlayerNotificationManager.BitmapCallback
        ): Bitmap? {
            // Provide album art or load asynchronously
            return null
        }
    }
}




//@UnstableApi
//class PlayerService : MediaSessionService() {
//    private var mediaSession: MediaSession? = null
//
//    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? {
//        return mediaSession
//    }
//
//    override fun onCreate() {
//        super.onCreate()
//
//        // Initialize ExoPlayer
//        val player = ExoPlayer.Builder(this).build()
//
//        // Create MediaSession
//        mediaSession = MediaSession.Builder(this, player)
//            .setSessionActivity(
//                PendingIntent.getActivity(
//                    this,
//                    0,
//                    Intent(this, MainActivity::class.java),
//                    PendingIntent.FLAG_IMMUTABLE
//                )
//            )
//            .build()
//    }
//
//    override fun onDestroy() {
//        mediaSession?.let {
//            it.player.release()
//            it.release()
//            mediaSession = null
//        }
//        super.onDestroy()
//    }
//}
// Optionally, set default media item or handle metadata updates
//        val mediaItem = MediaItem.Builder()
//            .setUri("https://live-par-2-abr.livepush.io/vod/bigbuckbunnyclip.mp4")
//            .setMediaMetadata(
//                MediaMetadata.Builder()
//                    .setTitle("Big Buck Bunny")
//                    .build()
//            )
//            .build()
//
//        player.setMediaItem(mediaItem)
//        player.prepare()
//        player.play()





//@UnstableApi
//class PlayerService : MediaSessionService() {
//    private var mediaSession: MediaSession? = null
//
//    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? {
//        return mediaSession
//    }
//
//    override fun onCreate() {
//        super.onCreate()
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val channel = NotificationChannel(
//                "playback_channel",
//                "Playback Notifications",
//                NotificationManager.IMPORTANCE_LOW
//            )
//            val notificationManager = getSystemService(NotificationManager::class.java)
//            notificationManager?.createNotificationChannel(channel)
//        }
//
//
//
//        // Create an ExoPlayer instance
//        val player = ExoPlayer.Builder(this).build()
//
//        // Attach a listener for playback state changes
//        player.addListener(object : Player.Listener {
//            override fun onPlaybackStateChanged(state: Int) {
//                if (state == Player.STATE_ENDED) {
//                    stopForeground(STOP_FOREGROUND_DETACH)
//                }
//            }
//        })
//
//        // Build a MediaSession
//        mediaSession = MediaSession.Builder(this, player)
//            .setSessionActivity(
//                PendingIntent.getActivity(
//                    this,
//                    0,
//                    Intent(this, MainActivity::class.java),
//                    PendingIntent.FLAG_IMMUTABLE
//                )
//            )
//            .build()
//
//        // Configure notification
//        val playerNotificationManager = PlayerNotificationManager.Builder(
//            this,
//            1, // Notification ID
//            "playback_channel" // Notification channel ID
//        )
//            .setMediaDescriptionAdapter(DescriptionAdapter(mediaSession!!))
//            .setNotificationListener(object : PlayerNotificationManager.NotificationListener {
//                override fun onNotificationCancelled(notificationId: Int, dismissedByUser: Boolean) {
//                    if (dismissedByUser) {
//                        stopSelf()
//                    }
//                }
//
//                override fun onNotificationPosted(
//                    notificationId: Int,
//                    notification: Notification,
//                    ongoing: Boolean
//                ) {
//                    startForeground(notificationId, notification)
//                }
//            })
//            .build()
//
//        // Attach player and media session to the notification
//        playerNotificationManager.setMediaSessionToken(mediaSession!!.sessionCompatToken)
//        playerNotificationManager.setPlayer(player)
//    }
//
//    override fun onTaskRemoved(rootIntent: Intent?) {
//        mediaSession?.player?.let { player ->
//            if (!player.playWhenReady || player.mediaItemCount == 0 || player.playbackState == Player.STATE_ENDED) {
//                stopSelf()
//            }
//        }
//    }
//
//    override fun onDestroy() {
//        mediaSession?.let {
//            it.player.release()
//            it.release()
//            mediaSession = null
//        }
//        super.onDestroy()
//    }
//
//    private class DescriptionAdapter(private val mediaSession: MediaSession) :
//        PlayerNotificationManager.MediaDescriptionAdapter {
//        override fun getCurrentContentTitle(player: Player): CharSequence {
//            return player.currentMediaItem?.mediaMetadata?.title ?: "Unknown Title"
//        }
//
//        override fun createCurrentContentIntent(player: Player): PendingIntent? {
//            return mediaSession.sessionActivity
//        }
//
//        override fun getCurrentContentText(player: Player): CharSequence? {
//            return player.currentMediaItem?.mediaMetadata?.artist
//        }
//
//        override fun getCurrentLargeIcon(
//            player: Player,
//            callback: PlayerNotificationManager.BitmapCallback
//        ): Bitmap? {
//            // Provide an album art bitmap or load asynchronously
//            return null
//        }
//    }
//}





//@UnstableApi
//class PlayerService : MediaSessionService() {
//
//    //        This service player can be controlled from the controller in MainActivity.
//    lateinit var player: Player
//
//    //        This session manages everything you need about audio playback ane notifications.
//    lateinit var session: MediaSession
//
//    override fun onCreate() {
//        super.onCreate()
//
////            Instantiate player
//        player = ExoPlayer.Builder(applicationContext)
//            .setRenderersFactory(
//                DefaultRenderersFactory(this).setExtensionRendererMode(
//                    DefaultRenderersFactory.EXTENSION_RENDERER_MODE_PREFER /* Prefer extensions, such as FFmpeg */
//                )
//            ).build()
//
////            Instantiate the session (most important part)
//        session = MediaSession.Builder(this, player)
//            .setCallback(object : MediaSession.Callback {
//                override fun onAddMediaItems(
//                    session: MediaSession,
//                    controller: MediaSession.ControllerInfo,
//                    mediaItems: MutableList<MediaItem>
//                ): ListenableFuture<MutableList<MediaItem>> {
//                    /* Update the media items with their URIs */
//                    val updatedMediaItems = mediaItems.map { it.buildUpon().setUri(it.mediaId).build() }.toMutableList()
//                    return Futures.immediateFuture(updatedMediaItems)
//                }
//            }).build()
//    }

//    }
//}