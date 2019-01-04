package com.todotrain.nekketsu.todotrain;

import android.content.Intent;
import android.content.res.AssetManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    ScheduleListAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);

        AssetManager assetManager = getResources().getAssets();

        //路線をリストに追加する
        try{
            String files[] = assetManager.list("");
            for(int i=0; i < files.length; i++){
                if(files[i].endsWith(".json")){
                    InputStream is = this.getAssets().open(files[i]);
                    String json = InputStreamToString(is);
                    JSONArray parentJsonObj = new JSONArray(json);
                    for(int j=0; j<parentJsonObj.length(); j++){
                        JSONObject obj = parentJsonObj.getJSONObject(j);
                        if(obj.getString("dc:title").equals("日比谷線")){
                            Log.d("json", obj.toString());
                            JSONArray array = obj.getJSONArray("odpt:stationOrder");
                            for(int k=0; k<array.length(); k++){
                                JSONObject station = array.getJSONObject(k);
                                RailWay railWay = new RailWay();
                                railWay.jp_name = station.getJSONObject("odpt:stationTitle").getString("ja");
                                railWay.en_name = station.getJSONObject("odpt:stationTitle").getString("en");
                                ShareData.railWays.add(railWay);
                            }
                            for(int k=0; k<ShareData.railWays.size(); k++){
                                Log.d("result", ShareData.railWays.get(k).jp_name);
                            }
                        }

                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try{
            for(int i=0; i < ShareData.railWays.size(); i++){
                RailWay railWay = ShareData.railWays.get(i);
                //ファイル名が順に取得できる
                //Jsonファイルを使いたい
                String files[] = assetManager.list("");
                for(int j=0; j<files.length; j++){
                    if(files[j].startsWith(railWay.jp_name)){
                        InputStream is = this.getAssets().open(files[j]);
                        BufferedReader br = new BufferedReader(new InputStreamReader(is));
                        String str;
                        while((str=br.readLine()) != null){
                            String csv[] = str.split(",");
                            railWay.bssids.add(csv[1]);
                        }
                        ShareData.railWays.set(i, railWay);
                        break;
                    }
                }
            }
            for(int i=0; i<ShareData.railWays.size(); i++){
                Log.d("name", ShareData.railWays.get(i).jp_name);
                for(int j=0; j<ShareData.railWays.get(i).bssids.size(); j++){
                    Log.d("bssid", ShareData.railWays.get(i).bssids.get(j));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void plusButtonTapped(View view){
        Intent intent = new Intent(getApplication(), EditActivity.class);
        startActivityForResult(intent, 0);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        Log.d("更新", "リストを更新するよ");
        adapter = new ScheduleListAdapter(this, R.layout.schedule_item, ShareData.schedules);
        listView.setAdapter(adapter);
//        listView.setOnItemClickListener(onItemClickListener);
    }
}
