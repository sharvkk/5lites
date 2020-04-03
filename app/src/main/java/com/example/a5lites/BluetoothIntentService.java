package com.example.a5lites;

import android.app.IntentService;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class BluetoothIntentService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_OPEN_DISCOVERY = "com.example.a5lites.action.OPEN";
    private static final String ACTION_CLOSE_DISCOVERY = "com.example.a5lites.action.CLOSE";

    private static SharedPreferences.Editor editor = null;

    static ArrayList<BluetoothDevice> outputList;

    static BluetoothAdapter bluetoothAdapter = null;
    private static Context context1 = null;

    public static void setEditor(SharedPreferences.Editor editor1){
        editor = editor1;
        outputList = new ArrayList<>();
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    public BluetoothIntentService() {
        super("BluetoothIntentService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startDiscovery(Context context) {
        context1 = context;
        Intent intent = new Intent(context, BluetoothIntentService.class);
        intent.setAction(ACTION_OPEN_DISCOVERY);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void stopDiscovery(Context context) {
//        context1 = context;
        Intent intent = new Intent(context, BluetoothIntentService.class);
        intent.setAction(ACTION_CLOSE_DISCOVERY);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_OPEN_DISCOVERY.equals(action)) {
                handleActionFoo();
            } else if (ACTION_CLOSE_DISCOVERY.equals(action)) {
                handleActionBaz();
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFoo() {
        IntentFilter intentFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        context1.registerReceiver(myReceiver, intentFilter);
        intentFilter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        context1.registerReceiver(myReceiver, intentFilter);
        intentFilter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        context1.registerReceiver(myReceiver, intentFilter);
        bluetoothAdapter.startDiscovery();
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionBaz() {
        context1.unregisterReceiver(myReceiver);
    }

    private static BroadcastReceiver myReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Message msg = Message.obtain();
            String action = intent.getAction();
            if(BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)){
                System.out.println("\n\nDiscovery for nearby devicces started!\n\n");
            }

            if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)){
                bluetoothAdapter.startDiscovery();
            }

            if(BluetoothDevice.ACTION_FOUND.equals(action)){
                Toast.makeText(context, "ACTION_FOUND", Toast.LENGTH_SHORT).show();
                System.out.println("Device found!");

                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                if(outputList.size()<1)
                {
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
                        outputList.add(device);
                        editor.putInt("totalDevices", outputList.size());
                        int i=0;
                        for(BluetoothDevice recognizedDevice:outputList){
                            editor.putString(("name"+i), recognizedDevice.getName());
                            editor.commit();
                            editor.putString(("address"+i), recognizedDevice.getAddress());
                            editor.commit();
                            i++;
                        }
//                        stopDiscovery(context);
                    }
                }
            }
        }
    };


}
