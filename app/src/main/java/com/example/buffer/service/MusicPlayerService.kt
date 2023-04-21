package com.example.buffer.service


import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.AsyncTask
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.bumptech.glide.Glide
import com.example.buffer.Models.ItemsItem
import com.example.buffer.R
import com.example.buffer.Repository.AllSongCategoryRep
import com.example.buffer.ViewModels.MainViewModel
import com.example.buffer.ViewModels.MyViewModelFactory
import com.example.buffer.helper.SharedPrefrenceService
import com.example.buffer.helper.constant
import com.example.buffer.ui.MainActivity


@Suppress("DEPRECATION")
class MusicPlayerService : Service() {
    private  val mBinder :Binder = MyServiceBinder()
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var sharedPrefrenceService: SharedPrefrenceService
    private lateinit var remoteViews: RemoteViews
    private  var prepare: Boolean = false


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
            stopSelf()
            stopForeground(true)

        }

        mediaPlayer.setOnPreparedListener {
            Log.d("Abhishek"," prepared")
            sharedPrefrenceService.write("isPlaying","true")

            startService(Intent(this,MusicPlayerService::class.java))
            prepare=true

            play()
        }


    }

        inner class MyServiceBinder : Binder() {
             fun getService() : MusicPlayerService = this@MusicPlayerService
        }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("Abhishek"," action"+intent?.action+" PLAY "+isPlaying())
        when(intent?.action){
           constant.MUSIC_SERVICE_ACTION_PAUSE ->{
               if (isPlaying()) {
                   sharedPrefrenceService.write("isPlaying","false")
                   puause()
                   val intnt = Intent("custom-event-name")
                   intnt.putExtra("Event","pause")
                   LocalBroadcastManager.getInstance(this).sendBroadcast(intnt)

               } else {
                   sharedPrefrenceService.write("isPlaying","true")
                   play()
                   val intnt = Intent("custom-event-name")
                   intnt.putExtra("Event","play")
                   LocalBroadcastManager.getInstance(this).sendBroadcast(intnt)
               }

           }
            constant.MUSIC_SERVICE_ACTION_PREV->{
                sharedPrefrenceService.write("isPlaying","true")
                restart()
                val intnt = Intent("custom-event-name")
                intnt.putExtra("Event","play")
                LocalBroadcastManager.getInstance(this).sendBroadcast(intnt)

            }
            constant.MUSIC_SERVICE_ACTION_STOP->{

                val intnt = Intent("custom-event-name")
                intnt.putExtra("Event","pause")
                LocalBroadcastManager.getInstance(this).sendBroadcast(intnt)
                sharedPrefrenceService.write("isPlaying","false")
                mediaPlayer.stop()
                stopForeground(true)
            }
            constant.MUSIC_SERVICE_ACTION_NEXT->{
                sharedPrefrenceService.write("isPlaying","false")
                puause()
                val intnt = Intent("custom-event-name")
                intnt.putExtra("Event","pause")
                LocalBroadcastManager.getInstance(this).sendBroadcast(intnt)
            }

        }

        return START_STICKY
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
                .setSmallIcon(R.mipmap.ic_launcher3)
                .setStyle(NotificationCompat.DecoratedCustomViewStyle())
                .setCustomBigContentView(getRemoteViews())
                .setPriority(NotificationManager.IMPORTANCE_LOW)
                .setContentIntent(contentIntent)
                .setAutoCancel(true)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build()

              startForeground(1,notification)
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
         val url = sharedPrefrenceService.read("SongImage","No")
        remoteViews.setTextViewText(R.id.lblWidgetCurrentMusicName,name)

        remoteViews.setTextViewText(R.id.lblWidgetCurrentArtistName,track.publisher?.artist)

        if(sharedPrefrenceService.read("isPlaying","No")=="true"){
            remoteViews.setImageViewResource(R.id.btnWidgetPlayPauseMusic,R.drawable.ic_baseline_pause_24)
        }
        else{
            remoteViews.setImageViewResource(R.id.btnWidgetPlayPauseMusic,R.drawable.ic_baseline_play_arrow_24)
        }
    AsyncTask.execute {
            try {
                val bitmap = Glide.with(applicationContext)
                    .asBitmap()
                    .load(url)
                    .submit(512, 512)
                    .get()
                remoteViews.setImageViewBitmap(R.id.imgWidgetAlbumArt, bitmap)

            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }



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
       
        showNotification()
        mediaPlayer.pause()



    }
    fun play(){
        try {
            showNotification()
            mediaPlayer.start()
        }catch (e:Exception){
            Log.d("Abhishek","NotPlayed")
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
        stopForeground(true)

    }
    fun stop1(){
        mediaPlayer.stop()
        stopForeground(true)
    }

    override fun onDestroy() {
        Log.d("Abhishek","onDestroy called")
        mediaPlayer.stop()
        mediaPlayer.release()
        super.onDestroy()
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

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        sharedPrefrenceService.write("isPlaying","false")
        stop()
    }

    fun restart(){
        mediaPlayer.pause()
        mediaPlayer.seekTo(0)
        play()
    }

}