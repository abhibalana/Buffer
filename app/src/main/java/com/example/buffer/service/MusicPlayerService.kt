package com.example.buffer.service


import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager

import com.bumptech.glide.request.transition.Transition
import com.example.buffer.R
import com.example.buffer.helper.SharedPrefrenceService
import com.example.buffer.helper.constant
import com.example.buffer.ui.MainActivity



@Suppress("DEPRECATION")
class MusicPlayerService : Service() {
    private  val mBinder :Binder = MyServiceBinder()
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var sharedPrefrenceService: SharedPrefrenceService
    private lateinit var bitmap:Bitmap
    private lateinit var remoteViews: RemoteViews

    override fun onCreate() {
        Log.d("Abhishek"," oncreate Called")
        super.onCreate()
        sharedPrefrenceService = SharedPrefrenceService()
        sharedPrefrenceService.init(applicationContext)


        mediaPlayer = MediaPlayer()
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
        mediaPlayer.setOnCompletionListener {
            Log.d("Abhishek", " song completed")
            sharedPrefrenceService.write("isPlaying","false")
            cancelNotification()
            stopSelf()
        }


        mediaPlayer.setOnPreparedListener {
            Log.d("Abhishek"," prepared")
            sharedPrefrenceService.write("isPlaying","true")
            cancelNotification()
            showNotification()
                play()

        }

    }

        inner class MyServiceBinder : Binder() {
             fun getService() : MusicPlayerService = this@MusicPlayerService
        }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val action = intent?.action
        when(action){
           constant.MUSIC_SERVICE_ACTION_PAUSE ->{
               if (isPlaying()) {
                   puause()
                   val intnt = Intent("custom-event-name")
                   intnt.putExtra("Event","pause")
                   LocalBroadcastManager.getInstance(this).sendBroadcast(intnt)
                   sharedPrefrenceService.write("isPlaying","false")
                   remoteViews.setImageViewResource(R.id.btnWidgetPlayPauseMusic,R.drawable.ic_baseline_play_arrow_24)

               } else {
                  play()
                   val intnt = Intent("custom-event-name")
                   intnt.putExtra("Event","play")
                   LocalBroadcastManager.getInstance(this).sendBroadcast(intnt)
                   remoteViews.setImageViewResource(R.id.btnWidgetPlayPauseMusic,R.drawable.ic_baseline_pause_24)
                   sharedPrefrenceService.write("isPlaying","true")

               }

           }
            constant.MUSIC_SERVICE_ACTION_PREV->{
                restart()
                val intnt = Intent("custom-event-name")
                intnt.putExtra("Event","play")
                LocalBroadcastManager.getInstance(this).sendBroadcast(intnt)
                remoteViews.setImageViewResource(R.id.btnWidgetPlayPauseMusic,R.drawable.ic_baseline_pause_24)
                sharedPrefrenceService.write("isPlaying","true")
            }
            constant.MUSIC_SERVICE_ACTION_STOP->{
                stop()
                val intnt = Intent("custom-event-name")
                intnt.putExtra("Event","pause")
                LocalBroadcastManager.getInstance(this).sendBroadcast(intnt)
                sharedPrefrenceService.write("isPlaying","false")
                cancelNotification()
            }

        }
         showNotification()
        return START_NOT_STICKY
    }
    fun showNotification(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.d("Abhishek","Notification  Follow")

            val chan = NotificationChannel(
                "MyChannelId",
                "My Foreground Service",
                NotificationManager.IMPORTANCE_LOW
            )
            chan.lockscreenVisibility = Notification.VISIBILITY_SECRET
            val manager =
                (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
            manager.createNotificationChannel(chan)
            val notificationBuilder = NotificationCompat.Builder(
                this, "MyChannelId"
            )
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            val contentIntent = PendingIntent.getActivity(
                this, 0,
               intent, PendingIntent.FLAG_IMMUTABLE
            )






            val notification: Notification = notificationBuilder.setOngoing(true)
                .setSmallIcon(com.example.buffer.R.mipmap.ic_launcher3)
                .setStyle(NotificationCompat.DecoratedCustomViewStyle())
                .setCustomBigContentView(getRemoteViews())
                .setPriority(NotificationManager.IMPORTANCE_LOW)
                .setContentIntent(contentIntent)
                .setAutoCancel(true)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build()

            manager.notify(0,notification)
        }
        else{
            Log.d("Abhishek","Notification Doesnt Follow")
        }
    }

    fun getRemoteViews():RemoteViews{
         remoteViews = RemoteViews(packageName,R.layout.notification)
        setUpRemoteViews(remoteViews)
        updateRemoteViews(remoteViews)
        return remoteViews
    }
    fun setUpRemoteViews(remoteViews: RemoteViews){
        val closeIntent = Intent(this, MusicPlayerService::class.java)
        closeIntent.action = constant.MUSIC_SERVICE_ACTION_STOP
        val pcloseIntent = PendingIntent.getService(this, 0, closeIntent, PendingIntent.FLAG_IMMUTABLE)

        val playNextIntent = Intent(this, MusicPlayerService::class.java)
        playNextIntent.action = constant.MUSIC_SERVICE_ACTION_NEXT
        val pNextIntent = PendingIntent.getService(this, 0, playNextIntent, PendingIntent.FLAG_IMMUTABLE)

        val playPrevIntent = Intent(this, MusicPlayerService::class.java)
        playPrevIntent.action = constant.MUSIC_SERVICE_ACTION_PREV
        val pPrevIntent = PendingIntent.getService(this, 0, playPrevIntent, PendingIntent.FLAG_IMMUTABLE)

        val playPauseIntent = Intent(this, MusicPlayerService::class.java)
        playPauseIntent.action = constant.MUSIC_SERVICE_ACTION_PAUSE
        val pplayPauseIntent = PendingIntent.getService(this, 0, playPauseIntent, PendingIntent.FLAG_IMMUTABLE)

        remoteViews.setImageViewResource(R.id.btnWidgetCloseService, R.drawable.ic_baseline_close_24)
        remoteViews.setImageViewResource(R.id.btnWidgetPlayPrevious, R.drawable.ic_baseline_skip_previous_24)
        remoteViews.setImageViewResource(R.id.btnWidgetPlayNext, R.drawable.ic_baseline_skip_next_24)

        remoteViews.setOnClickPendingIntent(R.id.btnWidgetCloseService, pcloseIntent)
        remoteViews.setOnClickPendingIntent(R.id.btnWidgetPlayPrevious, pPrevIntent)
        remoteViews.setOnClickPendingIntent(R.id.btnWidgetPlayNext, pNextIntent)
        remoteViews.setOnClickPendingIntent(R.id.btnWidgetPlayPauseMusic, pplayPauseIntent)
    }

    fun updateRemoteViews(remoteViews: RemoteViews){
        val name = sharedPrefrenceService.read("SongName","No")
        val track = sharedPrefrenceService.getTrack("Track","No")

        remoteViews.setTextViewText(R.id.lblWidgetCurrentMusicName,name)

        remoteViews.setTextViewText(R.id.lblWidgetCurrentArtistName,track.publisher?.artist)
        if(sharedPrefrenceService.read("isPlaying","No")=="true"){
           remoteViews.setImageViewResource(R.id.btnWidgetPlayPauseMusic,R.drawable.ic_baseline_pause_24)
        }
        else{
            remoteViews.setImageViewResource(R.id.btnWidgetPlayPauseMusic,R.drawable.ic_baseline_play_arrow_24)
        }




    }
    fun cancelNotification(){
        val mNotificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        mNotificationManager.cancelAll()

    }


    override fun onBind(p0: Intent?): IBinder {
        Log.d("Abhishek","onBind Called")
        return mBinder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        return true

    }


    fun isPlaying():Boolean{
        return mediaPlayer.isPlaying
    }
    fun puause(){
        cancelNotification()
        showNotification()
        mediaPlayer.pause()

    }
    fun play(){
        try {
            mediaPlayer.start()
        }catch (e:Exception){
            e.stackTrace
        }
    }
    fun prepare(url:String){
        try{
            mediaPlayer.setDataSource(url)
            mediaPlayer.prepareAsync()
        }catch (e:Exception){
            Log.d("Abhishek"," stack"+e.stackTrace)
        }
    }

    fun stop(){
        mediaPlayer.stop()
        mediaPlayer.reset()

    }

    override fun onRebind(intent: Intent?) {
        super.onRebind(intent)
    }
    fun getDuration():Int{
        return mediaPlayer.duration
    }

    fun getCurrentPosition():Int{
            return   mediaPlayer.currentPosition


    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }
    fun restart(){
        mediaPlayer.pause()
        mediaPlayer.seekTo(0)
        play()
    }

}