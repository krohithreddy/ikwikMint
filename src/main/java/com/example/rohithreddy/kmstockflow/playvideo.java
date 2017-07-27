package com.example.rohithreddy.kmstockflow;
        import android.content.Intent;
        import android.database.sqlite.SQLiteDatabase;
        import android.media.MediaPlayer;
        import android.net.Uri;
        import android.os.Environment;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.util.Log;
        import android.widget.MediaController;
        import android.widget.VideoView;
        import java.io.File;
public class playvideo extends AppCompatActivity {
    private VideoView videoView;
    private int position = 0;
    private MediaController mediaController;
    static final int READ_BLOCK_SIZE = 100;
    File file;
    private SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playvideo);
        setTitle("         KWIKMINT   VIDEO ");
        videoView = (VideoView) findViewById(R.id.videoView);
        file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS), "d_vdr");
        if (mediaController == null) {
            mediaController = new MediaController(playvideo.this,false);
            videoView.setMediaController(mediaController);
        }
        try { // ID of video file.
            int id = this.getRawResIdByName("kwikmint");
            videoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + id));
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        videoView.requestFocus();
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            public void onPrepared(MediaPlayer mediaPlayer) {
                videoView.seekTo(position);
                if (position == 0) {
                    videoView.start();
                }
                mediaPlayer.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                    @Override
                    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                    }
                });
               mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                    @Override
                    public void onCompletion(MediaPlayer vmp) {
                        Intent intent = new Intent();
                        intent.setClass(playvideo.this, rmain.class);
                        startActivity(intent);
                    }
                });
            }
        });
    }
    public int getRawResIdByName(String resName) {
        String pkgName = this.getPackageName();
        int resID = this.getResources().getIdentifier(resName, "raw", pkgName);
        return resID;
    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt("CurrentPosition", videoView.getCurrentPosition());
        videoView.pause();
    }
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        position = savedInstanceState.getInt("CurrentPosition");
        videoView.seekTo(position);
    }

}
