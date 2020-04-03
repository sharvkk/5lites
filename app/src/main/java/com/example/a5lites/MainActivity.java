package com.example.a5lites;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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

import org.w3c.dom.Text;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Set;

public class MainActivity extends Activity {

    //Changes:
    LinearLayout outputLayout;
    ArrayList<BluetoothDevice> outputList;

    BluetoothAdapter bluetoothAdapter = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Mi kelele changes:

        outputLayout = findViewById(R.id.myOutputLayout);
        outputList = new ArrayList<>();
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
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
    }

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
}