package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView=findViewById(R.id.listview);

        Dexter.withContext(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        Toast.makeText(MainActivity.this, "Runtime Permission Granted", Toast.LENGTH_SHORT).show();
                        ArrayList<File> mysongs=fetchSongs(Environment.getExternalStorageDirectory());
                        String [] items=new String[mysongs.size()];
                        int i=0;
                        for( i=0;i<mysongs.size();i++)
                        {
                            items[i]=mysongs.get(i).getName().replace(".opus","");
                        }

                        ArrayAdapter<String> adapter=new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1,items);
                        listView.setAdapter(adapter);
                        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                            @Override
                            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                                Intent intent=new Intent(MainActivity.this,PlaySong.class);
                                String currentSong=listView.getItemAtPosition(i).toString();
                                intent.putExtra("songlist",mysongs);
                                intent.putExtra("currentsong",currentSong);
                                intent.putExtra("position",i);
                                startActivity(intent);
                                return false;
                            }


                        });

                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                          permissionToken.continuePermissionRequest();
                    }
                })
                .check();
    }

    public ArrayList<File> fetchSongs(File file) {
         ArrayList<File> arrayList = new ArrayList<>();
         File[] songs=file.listFiles();
         if(songs !=null)
         {
             for(File myfile: songs)
             {
                 if(myfile.isDirectory() && myfile.isHidden() )
                 {
                     arrayList.addAll(fetchSongs(myfile));
                 }
                 else{
                     if(myfile.getName().endsWith(".opus") && !myfile.getName().startsWith("."))
                     {
                         arrayList.add(myfile);
                     }
                 }
             }
         }
         return arrayList;

    }
}