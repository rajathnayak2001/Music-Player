package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class PlaySong extends AppCompatActivity {

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.release();
        updateSeek.interrupt();
    }

    TextView textView;
    ImageView play, prev, next;
    ArrayList songs;
    String TextContent;
    int position;
    MediaPlayer mediaPlayer;
    Thread updateSeek;
    SeekBar seek;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_song);
        textView=findViewById(R.id.textView);
        play =findViewById(R.id.play);
        prev=findViewById(R.id.prev);
        next=findViewById(R.id.next);
        seek=findViewById(R.id.seekBar);
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        songs= (ArrayList) bundle.getParcelableArrayList("songlist");

        TextContent=intent.getStringExtra("currentsong");
        textView.setText(TextContent);
        textView.setSelected(true);
        position=intent.getIntExtra("position",0);
        Uri uri=Uri.parse(songs.get(position).toString());
        mediaPlayer= MediaPlayer.create(this,uri);
        mediaPlayer.start();
        seek.setMax(mediaPlayer.getDuration());

        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });

        updateSeek=new Thread(){
            @Override
            public void run() {
                int currentposition=0;
                try{
                    while(currentposition<mediaPlayer.getDuration())
                    {
                        currentposition=mediaPlayer.getDuration();
                        seek.setProgress(currentposition);
                        sleep(800);
                    }
                }catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
        };
        updateSeek.start();

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mediaPlayer.isPlaying())
                {
                    play.setImageResource(R.drawable.ic_baseline_play_circle_outline_24);
                    mediaPlayer.pause();

                }
                else
                {
                    play.setImageResource(R.drawable.ic_baseline_pause_circle_outline_24);
                    mediaPlayer.start();
                }
            }
        });
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
                mediaPlayer.release();
                if(position!=0)
                {
                    position=position-1;
                }
                else
                {
                    position=songs.size()-1;
                }
                Uri uri=Uri.parse(songs.get(position).toString());
                mediaPlayer= MediaPlayer.create(getApplicationContext(),uri);
                mediaPlayer.start();
                seek.setMax(mediaPlayer.getDuration());
                TextContent=songs.get(position).toString();
                textView.setText(TextContent);
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
                mediaPlayer.release();
                if(position!=songs.size()-1)
                {
                    position=position+1;
                }
                else
                {
                    position=0;
                }
                Uri uri=Uri.parse(songs.get(position).toString());
                mediaPlayer= MediaPlayer.create(getApplicationContext(),uri);
                mediaPlayer.start();
                seek.setMax(mediaPlayer.getDuration());
                TextContent=songs.get(position).toString();
                textView.setText(TextContent);
            }
        });
    }
}