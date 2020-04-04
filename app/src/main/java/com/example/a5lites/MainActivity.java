package com.example.a5lites;

import androidx.appcompat.app.AppCompatActivity;


import com.amazonaws.mobileconnectors.dynamodbv2.document.datatype.DynamoDBEntry;
import com.amazonaws.regions.Region;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;

import com.amazonaws.mobileconnectors.dynamodbv2.document.datatype.Document;

import org.w3c.dom.Text;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MainActivity extends Activity {
    private SharedPreferences prefs;
    private String PHONE_NO = "Phone";
    private String MAC = "Mac_add";
    static final String LOG_CHECK_KEY = "logged";
    static final String CREDENTIALS = "Credentials";
    //Changes:
    LinearLayout outputLayout;
//    Button bt1,bt2;
    ArrayList<BluetoothDevice> outputList;
    Button mButton;
    Boolean flag;
    private final String COGNITO_POOL_ID =  "ap-south-1:402f5cc9-0567-4261-bc92-768d44d79b08";
    private final Region COGNITO_REGION =  Region.getRegion("ap-south-1");
    private Context context;

//    SharedPreferences pref = null;

    BluetoothAdapter bluetoothAdapter = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prefs = getSharedPreferences(CREDENTIALS,Context.MODE_PRIVATE);
        String found =  prefs.getString(PHONE_NO,"not saved");
        String mymac = prefs.getString(MAC,"no mac");
        System.out.println(found + "\nmac " + mymac);
        //Mi kelele changes:

//        pref = getSharedPreferences(CHAT_PREFS,MODE_PRIVATE);
        outputLayout = findViewById(R.id.myOutputLayout);
//        bt1 = findViewById(R.id.bt1);
//        bt2 = findViewById(R.id.bt2);
        outputList = new ArrayList<>();
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mButton = findViewById(R.id.sbutton);
        flag = false;
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(flag == false){

                    mButton.setText("Stop services");
                    flag = true;
                }
                if(flag == true){

                    mButton.setText("Start services");
                    flag = false;
                }
            }
        });
    }
    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();

        //Changes:

        IntentFilter intentFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(myReceiver, intentFilter);
        intentFilter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        this.registerReceiver(myReceiver, intentFilter);
        intentFilter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        this.registerReceiver(myReceiver, intentFilter);
        bluetoothAdapter.startDiscovery();
//        getDatabase();
//        GetAllItemsAsyncTask getAllItemsAsyncTask = new GetAllItemsAsyncTask();
//        try{
////            Document i = getAllItemsAsyncTask.execute("d4:63:c6:77:92:52").get();
//            Document document = new Document();
//            document.put("MACid", "d4:63:c6:77:92:52");
//            document.put("status", "negative");
//            document.put("phone", "12345678");
//            Document ping = new Document();
//            ping.put("counter", 1);
//            ping.put("timestamp", "04-04-2020-20:05");
//            document.put("newmac", ping);
////            System.out.println("Size: " + i.size());
//            Integer temp = new PutDataAsyncTask().execute(document).get();
//            System.out.println("O/P: " + temp);
//        }catch (Exception e){
//            e.printStackTrace();
//        }

    }





    private class GetAllItemsAsyncTask extends AsyncTask<String, Void, com.amazonaws.mobileconnectors.dynamodbv2.document.datatype.Document> {
        @Override
        protected com.amazonaws.mobileconnectors.dynamodbv2.document.datatype.Document doInBackground(String... params) {
            DatabaseAccess databaseAccess = DatabaseAccess.getInstance(MainActivity.this);
            return databaseAccess.getItem(params[0]);
        }

        @Override
        protected void onPostExecute(com.amazonaws.mobileconnectors.dynamodbv2.document.datatype.Document documents) {
        }

    }

    private class PutDataAsyncTask extends AsyncTask<com.amazonaws.mobileconnectors.dynamodbv2.document.datatype.Document, Void, Integer> {
        @Override
        protected Integer doInBackground(Document... params) {
            DatabaseAccess databaseAccess = DatabaseAccess.getInstance(MainActivity.this);
            databaseAccess.putItem(params[0]);
            System.out.println("In background!!!");
            return 1;
        }

        @Override
        protected void onPostExecute(Integer integer) {
        }

    }


    private void getDatabase(){
    }

//    public void startBackground(View view){
//        SharedPreferences.Editor edit = pref.edit();
//        edit.commit();
//        BluetoothIntentService.setEditor(edit);
//        BluetoothIntentService.startDiscovery(getBaseContext());
//        Toast.makeText(this, "Background process started!", Toast.LENGTH_SHORT).show();
//    }
//
//    public void stopBackground(View view){
//        Toast.makeText(this, "Background process stopped!", Toast.LENGTH_SHORT).show();
//        bt1.setVisibility(View.GONE);
//        bt2.setVisibility(View.GONE);
//        int totalDevices = pref.getInt("totalDevices", 0);
//        if(totalDevices == 0){
//            System.out.println("Total devices are 0!! :(");
//        }
//        for(int i=0;i<totalDevices;i++){
//            View newView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.list_card, null);
//            TextView name = newView.findViewById(R.id.deviceName);
//            TextView add = newView.findViewById(R.id.devicceAddress);
//            name.setText(pref.getString(("name"+i), "name"));
//            add.setText(pref.getString(("address"+i), "address"));
//            outputLayout.addView(newView);
//        }
//        outputLayout.setVisibility(View.VISIBLE);
//        BluetoothIntentService.stopDiscovery(getBaseContext());
//
//    }


    private BroadcastReceiver myReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Message msg = Message.obtain();
            String action = intent.getAction();
            Log.d("here in list", "onrecieve");
            System.out.println("here in onreceieve");
            if(BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)){
                System.out.println("\n\nDiscovery for nearby devicces started!\n\n");
            }

            if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)){
                bluetoothAdapter.startDiscovery();
            }

            if(BluetoothDevice.ACTION_FOUND.equals(action)){
                Toast.makeText(context, "ACTION_FOUND", Toast.LENGTH_SHORT).show();

                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                if(outputList.size()<1)
                {
                    View newView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.list_card, null);
                    TextView name = newView.findViewById(R.id.deviceName);
                    TextView add = newView.findViewById(R.id.devicceAddress);
                    name.setText(device.getName());
                    add.setText(device.getAddress());
                    outputLayout.addView(newView);
                    outputList.add(device);
                }
                else
                {
                    boolean flag = true;    // flag to indicate that particular device is already in the arlist or not
                    for(int i = 0; i<outputList.size();i++)
                    {
                        if(device.getAddress().equals(outputList.get(i).getAddress()))
                        {
                            flag = false;
                        }
                    }
                    if(flag)
                    {
                        View newView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.list_card, null);
                        TextView name = newView.findViewById(R.id.deviceName);
                        TextView add = newView.findViewById(R.id.devicceAddress);
                        name.setText(device.getName());
                        add.setText(device.getAddress());
                        outputList.add(device);
                        outputLayout.addView(newView);
                    }
                }
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(myReceiver);
    }
}