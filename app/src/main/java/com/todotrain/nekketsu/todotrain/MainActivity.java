package com.todotrain.nekketsu.todotrain;

import android.content.res.AssetManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AssetManager assetManager = getResources().getAssets();

        //路線をリストに追加する
        try{
            String files[] = assetManager.list("");
            for(int i=0; i < files.length; i++){
                if(files[i].endsWith(".json")){
                    InputStream is = this.getAssets().open(files[i]);
                    String json = InputStreamToString(is);
                    JSONObject parentJsonObj = new JSONObject(json);
                    for(int j=0; j<parentJsonObj.length(); l++){

                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        try{
            String files[] = assetManager.list("");
            for(int i=0; i < files.length; i++){
                //ファイル名が順に取得できる
                //Jsonファイルを使いたい
                if(files[i].endsWith(".txt")){
                    InputStream is = this.getAssets().open(files[i]);
                    BufferedReader br = new BufferedReader(new InputStreamReader(is));
                    String str;
                    RailWay railWay = new RailWay();
                    while((str=br.readLine()) != null){
                        String csv[] = str.split(",");
                        railWay.bssids.add(csv[1]);
                    }
                    ShareData.railWays.add(railWay);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // InputStream -> String
    static String InputStreamToString(InputStream is) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            Log.d("一行",line);
            sb.append(line);
        }
        br.close();
        return sb.toString();
    }

}
