package com.example.rohithreddy.kmstockflow;
        import android.app.AlertDialog;
        import android.content.Context;
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.database.sqlite.SQLiteDatabase;
        import android.media.MediaPlayer;
        import android.net.Uri;
        import android.os.Environment;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.util.Log;
        import android.widget.MediaController;
        import android.widget.SeekBar;
        import android.widget.VideoView;
        import java.io.File;
        import java.io.FileInputStream;
        import java.io.FileOutputStream;
        import java.io.FileWriter;
        import java.io.InputStreamReader;
        import java.text.SimpleDateFormat;
        import java.util.Date;
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
                        // Re-Set the videoView that acts as the anchor for the MediaController
                        //   mediaController.setAnchorView(videoView);
                    }
                });
               mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                    @Override
                    public void onCompletion(MediaPlayer vmp) {
                        Intent intent = new Intent();
                        intent.setClass(playvideo.this, rmain.class);
                      /*  FileOutputStream outputStream;
                        try {
                            outputStream = openFileOutput(filename, Context.MODE_APPEND);
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-d HH-mm-ss");
                            datetime = sdf.format(new Date());
                            System.out.println(datetime);
                            outputStream.write(datetime.getBytes());
                            outputStream.close();
                            db.execSQL("INSERT INTO videodata VALUES('"+  phone +"','"+  lati +"','"+  longi +"','"+ starttime +"','"+ datetime +"');");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            FileInputStream fileIn=openFileInput("myfile");
                            InputStreamReader InputRead= new InputStreamReader(fileIn);
                            char[] inputBuffer= new char[READ_BLOCK_SIZE];
                            String s="";
                            int charRead;
                            while ((charRead=InputRead.read(inputBuffer))>0) {
                                String readstring=String.copyValueOf(inputBuffer,0,charRead);
                                s +=readstring;
                            }
                            InputRead.close();
                            File filepath = new File(file, "output" + ".csv");  // file path to save
                            FileWriter writer = new FileWriter(filepath);
                            writer.append(s.toString());
                            writer.flush();
                            writer.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }*/
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
/* private class startrecording extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... arg0) {
            repeatTask = new Timer();
            repeatTask.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            System.out.print("its recordeedddddddddd");
                            GPSTracker gps = new GPSTracker(getActivity());
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-d HH-mm-ss");
                            lm = sdf.format(new Date());
                            if (gps.canGetLocation()) {
                                latitude = gps.getLatitude();
                                longitude = gps.getLongitude();
                                if (latitude == 0.0) {
                                    Toast.makeText(getActivity(), "wait for location and try again",
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    longi = String.valueOf(longitude);
                                    lati = String.valueOf(latitude);
                                    try {
                                        longi = String.valueOf(longitude);
                                        lati = String.valueOf(latitude);
                                        String l1 = "  " + longitude + "  ";
                                        String l2 = "  " + latitude + "  ";
                                        db.execSQL("INSERT INTO lasttime1 VALUES('" + phone + "','Off','" + lm + "','" + longi + "','" + lati + "');");
                                        db.execSQL("DELETE FROM lasttime1 WHERE phone="+phone+" ");
                                        db.execSQL("INSERT INTO lasttime1 VALUES('" + phone + "','Off','" + lm + "','" + longi + "','" + lati + "');");
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                }
                            } else {
                                if(pause.equals(0))
                                {

                                }
                                else
                                    gps.showSettingsAlert();
                            }

                        }
                    });
                }
            }, 0, 5000);

            return null;
        }
    }
    @Override
    public void onStart() {
        pause =1;
        super.onStart();
        // Toast.makeText(getContext(), "onStart called", Toast.LENGTH_LONG).show();
    }
    @Override
    public void onResume() {
        pause =1;
        super.onResume();
        // Toast.makeText(getContext(), "onResumed called", Toast.LENGTH_LONG).show();

    }
    @Override
    public void onDestroy() {
        pause =0 ;
        super.onDestroy();
        System.out.print("destroyeddddddddddddd");
        // Toast.makeText(getContext(), "onDestroy called", Toast.LENGTH_LONG).show();

    }
    @Override
    public void onStop() {
        pause =0 ;
        super.onStop();
        System.out.print("stopppppppppp");
        // Toast.makeText(getContext(), "onStop called", Toast.LENGTH_LONG).show();

    }
    @Override
    public void onPause() {
        pause =0 ;
        super.onPause();
        // Toast.makeText(getContext(), "onPause called", Toast.LENGTH_LONG).show();

    }*/
