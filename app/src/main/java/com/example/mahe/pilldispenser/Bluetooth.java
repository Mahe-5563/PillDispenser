package com.example.mahe.pilldispenser;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mahe.pilldispenser.ClassFiles.Tray;
import com.example.mahe.pilldispenser.TrayACtivity.ActivityTray1;
import com.example.mahe.pilldispenser.TrayACtivity.ActivityTray2;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.UUID;

public class Bluetooth extends AppCompatActivity {


    private TextView mBluetoothStatus;
    private TextView mReadBuffer;

    Button btn_OPEN, btn_CLOSE, btn_BTCON, btn_READY;

    private Button mScanBtn;
    private Button mOffBtn;
    private Button mListPairedDevicesBtn;
    private Button mDiscoverBtn;
    private Button Morn;
    private Button Afternoon;
    private BluetoothAdapter mBTAdapter;
    private Set<BluetoothDevice> mPairedDevices;
    private ArrayAdapter<String> mBTArrayAdapter;
    private ListView mDevicesListView;
    private CheckBox mLED1;
    Button tabform, sendMorn, sendAft, sendNight, sendMorn2, sendAft2, sendNight2, open, close;

    private BluetoothSocket mmSocket;
    private InputStream mmInStream;
    private OutputStream mmOutStream;

    private final static int MESSAGE_READ = 2; // used in bluetooth handler to identify message update

    Handler mHandler; // Our main handler that will receive callback notifications



    private final String TAG = Bluetooth.class.getSimpleName();

    public ConnectedThread mConnectedThread; // bluetooth background worker thread to send and receive data
    private BluetoothSocket mBTSocket = null; // bi-directional client-to-client data path

    private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); // "random" unique identifier

    SharedPreferences alarmclocksharedpreference;
    SharedPreferences.Editor alarmclockeditor;

    SharedPreferences tabletsharedpreference;
    SharedPreferences.Editor tableteditor;

    final String SPALARM = "SPALARM";

    final String SPNAF = "SPNAF";

    final String SPTray1 = "SPTray1";
    final String DBNAME = "MYDB";

    int id;

    Tray t1 = new Tray();
    Tray t2 = new Tray();

    String spNaF;

    // #defines for identifying shared types between calling functions
    private final static int REQUEST_ENABLE_BT = 1; // used to identify adding bluetooth names

    private final static int CONNECTING_STATUS = 3; // used in bluetooth handler to identify message status


    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        if (getSupportActionBar() != null) getSupportActionBar().setTitle("Dispenser");


        btn_READY = findViewById(R.id.btn_READY);
        btn_OPEN = findViewById(R.id.btn_OPEN);
        btn_CLOSE = findViewById(R.id.btn_CLOSE);

        mBluetoothStatus = (TextView)findViewById(R.id.bluetoothStatus);
        mReadBuffer = (TextView) findViewById(R.id.readBuffer);
        mScanBtn = (Button)findViewById(R.id.scan);
        mOffBtn = (Button)findViewById(R.id.off);
        mDiscoverBtn = (Button)findViewById(R.id.discover);
        mListPairedDevicesBtn = (Button)findViewById(R.id.PairedBtn);

        alarmclocksharedpreference = getSharedPreferences(getString(R.string.SharedPreferencesDBNAME), MODE_PRIVATE);
        tabletsharedpreference = getSharedPreferences(getString(R.string.SharedPreferencesDBNAME), MODE_PRIVATE);

        //Checking for Alarms, if they are set.
        spNaF = alarmclocksharedpreference.getString(SPNAF, null);


        checkForValues();

        mBTArrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1);
        mBTAdapter = BluetoothAdapter.getDefaultAdapter(); // get a handle on the bluetooth radio

        mDevicesListView = (ListView)findViewById(R.id.devicesListView);
        mDevicesListView.setAdapter(mBTArrayAdapter); // assign model to view
        mDevicesListView.setOnItemClickListener(mDeviceClickListener);

        // Ask for location permission if not already allowed
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);



        mHandler = new Handler(){
            public void handleMessage(android.os.Message msg){
                if(msg.what == MESSAGE_READ){
                    String readMessage = null;
                    try {
                        readMessage = new String((byte[]) msg.obj, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    mReadBuffer.setText(readMessage);
                }

                if(msg.what == CONNECTING_STATUS){
                    if(msg.arg1 == 1)
                    {
                        mBluetoothStatus.setText("Connected to Device: " + (String)(msg.obj));

                        //startActivity(new Intent(Bluetooth.this, HomeActivity.class));

                    }

                    else
                        mBluetoothStatus.setText("Connection Failed");
                }
            }
        };

        if (mBTArrayAdapter == null) {
            // Device does not support Bluetooth
            mBluetoothStatus.setText("Status: Bluetooth not found");
            Toast.makeText(getApplicationContext(),"Bluetooth device not found!",Toast.LENGTH_SHORT).show();
        }
        else {

            /*mLED1.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    if(mConnectedThread != null) //First check to make sure thread created
                        mConnectedThread.write("1");
                }
            });*/

            /*tabform.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    startActivity(new Intent(MainActivity.this, TraysActivity.class));
                    finish();

                }
            });*/





            mScanBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bluetoothOn(v);

                }
            });

            mOffBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    bluetoothOff(v);

                }
            });

            mListPairedDevicesBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    listPairedDevices(v);
                }
            });

            mDiscoverBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    discover(v);
                }
            });

            btn_OPEN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Toast.makeText(Bluetooth.this, "Opening Tray(s)... ", Toast.LENGTH_SHORT).show();
                    if(mConnectedThread != null)
                    {
                        mConnectedThread.write("8");
                    }

                }
            });

            btn_CLOSE.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Toast.makeText(Bluetooth.this, "Closing Tray(s)...", Toast.LENGTH_SHORT).show();
                    if(mConnectedThread != null)
                    {
                        mConnectedThread.write("9");
                    }

                }
            });


            btn_READY.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent i = getIntent();

                    id = i.getIntExtra(getString(R.string.intenty), 0);

                    if(id > 0)
                    {

                        Toast.makeText(Bluetooth.this, "Ready to open the tray", Toast.LENGTH_SHORT).show();

                    }

                    else
                    {

                        Toast.makeText(Bluetooth.this, "Cannot Perform Function. Make Sure the trays " +
                                "and Alarms are chosen Corrrectly!", Toast.LENGTH_LONG).show();

                    }

                    switch(id)
                    {
                        case 1:

                            sendDataMornBF();

                            break;
                        case 2:

                            sendDataMornAF();

                            break;
                        case 3:

                            sendDataAftBF();

                            break;
                        case 4:

                            sendDataAftAF();

                            break;
                        case 5:

                            sendDataNightBF();

                            break;
                        case 6:

                            sendDataNightAF();

                            break;
                    }

                }
            });


        }

    }

    private void checkForValues() {


        if(spNaF == null)
        {
            Toast.makeText(this, "Alarm Time not Set", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Bluetooth.this, AlarmActivity.class));
        }

        //Checking for tablets, if they are set.
        Gson gson = new Gson();
        String json = tabletsharedpreference.getString(getString(R.string.tray1), "");
        String json2 = tabletsharedpreference.getString(getString(R.string.tray2), "");
        //Toast.makeText(this, json, Toast.LENGTH_SHORT).show();
        t1 = gson.fromJson(json, Tray.class);
        t2 = gson.fromJson(json2, Tray.class);

        if(t1 == null)
        {

            startActivity(new Intent(Bluetooth.this, ActivityTray1.class));

        }
        else
        {
            Toast.makeText(this, "Tray 1 values not set", Toast.LENGTH_SHORT).show();
            //Toast.makeText(this, ""+t1.getAftnoon_aft_food(), Toast.LENGTH_SHORT).show();
        }

        if(t2 == null)
        {

            startActivity(new Intent(Bluetooth.this, ActivityTray2.class));

        }
        else
        {
            Toast.makeText(this, "Tray 2 values not set", Toast.LENGTH_SHORT).show();
            //Toast.makeText(this, ""+t2.getAftnoon_aft_food(), Toast.LENGTH_SHORT).show();
        }

    }

    private void sendDataMornBF() {

        if(t1.getMorn_bef_food() == 1 && t2.getMorn_bef_food() == 1) //If both the trays have Value
        {

            //Toast.makeText(this, "Tray1 true Tray2 true", Toast.LENGTH_SHORT).show();
            if(mConnectedThread != null)
            {

                mConnectedThread.write("3");

            }

        }

        if(t1.getMorn_bef_food() == 1 && t2.getMorn_bef_food() == 0) //If tray 1 has value and tray 2 has no Value
        {

            //Toast.makeText(this, "Tray1 true Tray2 false", Toast.LENGTH_SHORT).show();
            if(mConnectedThread != null)
            {

                mConnectedThread.write("1");

            }

        }

        if(t1.getMorn_bef_food() == 0 && t2.getMorn_bef_food() == 1) //If tray 1 has no value and tray 2 has value
        {

            //Toast.makeText(this, "Tray1 false Tray2 true", Toast.LENGTH_SHORT).show();
            if(mConnectedThread != null)
            {

                mConnectedThread.write("2");

            }

        }

        if(t1.getMorn_bef_food() == 0 && t2.getMorn_bef_food() == 0) //If both the trays has no Value
        {

            //Toast.makeText(this, "Tray1 false Tray2 false", Toast.LENGTH_SHORT).show();
            if(mConnectedThread != null)
            {

                mConnectedThread.write("0");

            }

        }

    }

    private void sendDataMornAF() {

        if(t1.getMorn_aft_food() == 1 && t2.getMorn_aft_food() == 1) //If both the trays have Value
        {

            Toast.makeText(this, "Tray1 true Tray2 true", Toast.LENGTH_SHORT).show();
            if(mConnectedThread != null)
            {

                mConnectedThread.write("3");

            }

        }

        if(t1.getMorn_aft_food() == 1 && t2.getMorn_aft_food() == 0) //If tray 1 has value and tray 2 has no Value
        {

            Toast.makeText(this, "Tray1 true Tray2 false", Toast.LENGTH_SHORT).show();
            if(mConnectedThread != null)
            {

                mConnectedThread.write("1");

            }

        }

        if(t1.getMorn_aft_food() == 0 && t2.getMorn_aft_food() == 1) //If tray 1 has no value and tray 2 has value
        {

            Toast.makeText(this, "Tray1 false Tray2 true", Toast.LENGTH_SHORT).show();
            if(mConnectedThread != null)
            {

                mConnectedThread.write("2");

            }

        }

        if(t1.getMorn_aft_food() == 0 && t2.getMorn_aft_food() == 0) //If both the trays has no Value
        {

            Toast.makeText(this, "Tray1 false Tray2 false", Toast.LENGTH_SHORT).show();
            if(mConnectedThread != null)
            {

                mConnectedThread.write("0");

            }

        }

    }

    private void sendDataAftBF() {

        if(t1.getAftnoon_bef_food() == 1 && t2.getAftnoon_bef_food() == 1) //If both the trays have Value
        {

            //Toast.makeText(this, "Tray1 true Tray2 true", Toast.LENGTH_SHORT).show();
            if(mConnectedThread != null)
            {

                mConnectedThread.write("3");

            }

        }

        if(t1.getAftnoon_bef_food() == 1 && t2.getAftnoon_bef_food() == 0) //If tray 1 has value and tray 2 has no Value
        {

            //Toast.makeText(this, "Tray1 true Tray2 false", Toast.LENGTH_SHORT).show();
            if(mConnectedThread != null)
            {

                mConnectedThread.write("1");

            }

        }

        if(t1.getAftnoon_bef_food() == 0 && t2.getAftnoon_bef_food() == 1) //If tray 1 has no value and tray 2 has value
        {

            //Toast.makeText(this, "Tray1 false Tray2 true", Toast.LENGTH_SHORT).show();
            if(mConnectedThread != null)
            {

                mConnectedThread.write("2");

            }

        }

        if(t1.getAftnoon_bef_food() == 0 && t2.getAftnoon_bef_food() == 0) //If both the trays has no Value
        {

            //Toast.makeText(this, "Tray1 false Tray2 false", Toast.LENGTH_SHORT).show();
            if(mConnectedThread != null)
            {

                mConnectedThread.write("0");

            }

        }

    }

    private void sendDataAftAF() {

        if(t1.getAftnoon_aft_food() == 1 && t2.getAftnoon_aft_food() == 1) //If both the trays have Value
        {

            //Toast.makeText(this, "Tray1 true Tray2 true", Toast.LENGTH_SHORT).show();
            if(mConnectedThread != null)
            {

                mConnectedThread.write("3");

            }

        }

        if(t1.getAftnoon_aft_food() == 1 && t2.getAftnoon_aft_food() == 0) //If tray 1 has value and tray 2 has no Value
        {

            //Toast.makeText(this, "Tray1 true Tray2 false", Toast.LENGTH_SHORT).show();
            if(mConnectedThread != null)
            {

                mConnectedThread.write("1");

            }

        }

        if(t1.getAftnoon_aft_food() == 0 && t2.getAftnoon_aft_food() == 1) //If tray 1 has no value and tray 2 has value
        {

            //Toast.makeText(this, "Tray1 false Tray2 true", Toast.LENGTH_SHORT).show();
            if(mConnectedThread != null)
            {

                mConnectedThread.write("2");

            }

        }

        if(t1.getAftnoon_aft_food() == 0 && t2.getAftnoon_aft_food() == 0) //If both the trays has no Value
        {

            //Toast.makeText(this, "Tray1 false Tray2 false", Toast.LENGTH_SHORT).show();
            if(mConnectedThread != null)
            {

                mConnectedThread.write("0");

            }

        }


    }

    private void sendDataNightBF() {

        if(t1.getNight_bef_food() == 1 && t2.getNight_bef_food() == 1) //If both the trays have Value
        {

            //Toast.makeText(this, "Tray1 true Tray2 true", Toast.LENGTH_SHORT).show();
            if(mConnectedThread != null)
            {

                mConnectedThread.write("3");

            }

        }

        if(t1.getNight_bef_food() == 1 && t2.getNight_bef_food() == 0) //If tray 1 has value and tray 2 has no Value
        {

            //Toast.makeText(this, "Tray1 true Tray2 false", Toast.LENGTH_SHORT).show();
            if(mConnectedThread != null)
            {

                mConnectedThread.write("1");

            }

        }

        if(t1.getNight_bef_food() == 0 && t2.getNight_bef_food() == 1) //If tray 1 has no value and tray 2 has value
        {

           // Toast.makeText(this, "Tray1 false Tray2 true", Toast.LENGTH_SHORT).show();
            if(mConnectedThread != null)
            {

                mConnectedThread.write("2");

            }

        }

        if(t1.getNight_bef_food() == 0 && t2.getNight_bef_food() == 0) //If both the trays has no Value
        {

            //Toast.makeText(this, "Tray1 false Tray2 false", Toast.LENGTH_SHORT).show();
            if(mConnectedThread != null)
            {

                mConnectedThread.write("0");

            }

        }


    }

    private void sendDataNightAF() {

        if(t1.getNight_aft_food() == 1 && t2.getNight_aft_food() == 1) //If both the trays have Value
        {

            //Toast.makeText(this, "Tray1 true Tray2 true", Toast.LENGTH_SHORT).show();
            if(mConnectedThread != null)
            {

                mConnectedThread.write("3");

            }

        }

        if(t1.getNight_aft_food() == 1 && t2.getNight_aft_food() == 0) //If tray 1 has value and tray 2 has no Value
        {

            //Toast.makeText(this, "Tray1 true Tray2 false", Toast.LENGTH_SHORT).show();
            if(mConnectedThread != null)
            {

                mConnectedThread.write("1");

            }

        }

        if(t1.getNight_aft_food() == 0 && t2.getNight_aft_food() == 1) //If tray 1 has no value and tray 2 has value
        {

            //Toast.makeText(this, "Tray1 false Tray2 true", Toast.LENGTH_SHORT).show();
            if(mConnectedThread != null)
            {

                mConnectedThread.write("2");

            }

        }

        if(t1.getNight_aft_food() == 0 && t2.getNight_aft_food() == 0) //If both the trays has no Value
        {

            //Toast.makeText(this, "Tray1 false Tray2 false", Toast.LENGTH_SHORT).show();
            if(mConnectedThread != null)
            {

                mConnectedThread.write("0");

            }

        }

    }

    private void bluetoothOn(View view){
        if (!mBTAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            mBluetoothStatus.setText("Bluetooth enabled");
            Toast.makeText(getApplicationContext(),"Bluetooth turned on",Toast.LENGTH_SHORT).show();

        }
        else{
            Toast.makeText(getApplicationContext(),"Bluetooth is already on", Toast.LENGTH_SHORT).show();
        }
    }

    // Enter here after user selects "yes" or "no" to enabling radio
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent Data){
        // Check which request we're responding to
        if (requestCode == REQUEST_ENABLE_BT) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // The user picked a contact.
                // The Intent's data Uri identifies which contact was selected.
                mBluetoothStatus.setText("Enabled");
            }
            else
                mBluetoothStatus.setText("Disabled");
        }
    }

    private void bluetoothOff(View view){
        mBTAdapter.disable(); // turn off
        mBluetoothStatus.setText("Bluetooth disabled");
        Toast.makeText(getApplicationContext(),"Bluetooth turned Off", Toast.LENGTH_SHORT).show();
    }

    private void discover(View view){
        // Check if the device is already discovering
        if(mBTAdapter.isDiscovering()){
            mBTAdapter.cancelDiscovery();
            Toast.makeText(getApplicationContext(),"Discovery stopped",Toast.LENGTH_SHORT).show();
        }
        else{
            if(mBTAdapter.isEnabled()) {
                mBTArrayAdapter.clear(); // clear items
                mBTAdapter.startDiscovery();
                Toast.makeText(getApplicationContext(), "Discovery started", Toast.LENGTH_SHORT).show();
                registerReceiver(blReceiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));
            }
            else{
                Toast.makeText(getApplicationContext(), "Bluetooth not on", Toast.LENGTH_SHORT).show();
            }
        }
    }

    final BroadcastReceiver blReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(BluetoothDevice.ACTION_FOUND.equals(action)){
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // add the name to the list
                mBTArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                mBTArrayAdapter.notifyDataSetChanged();
            }
        }
    };

    private void listPairedDevices(View view){
        mPairedDevices = mBTAdapter.getBondedDevices();
        if(mBTAdapter.isEnabled()) {
            // put it's one to the adapter
            for (BluetoothDevice device : mPairedDevices)
                mBTArrayAdapter.add(device.getName() + "\n" + device.getAddress());

            Toast.makeText(getApplicationContext(), "Show Paired Devices", Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText(getApplicationContext(), "Bluetooth not on", Toast.LENGTH_SHORT).show();
    }

    private AdapterView.OnItemClickListener mDeviceClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {

            if(!mBTAdapter.isEnabled()) {
                Toast.makeText(getBaseContext(), "Bluetooth not on", Toast.LENGTH_SHORT).show();
                return;
            }

            mBluetoothStatus.setText("Connecting...");
            // Get the device MAC address, which is the last 17 chars in the View
            String info = ((TextView) v).getText().toString();
            final String address = info.substring(info.length() - 17);
            final String name = info.substring(0,info.length() - 17);

            // Spawn a new thread to avoid blocking the GUI one
            new Thread()
            {
                public void run() {
                    boolean fail = false;

                    BluetoothDevice device = mBTAdapter.getRemoteDevice(address);

                    try {
                        mBTSocket = createBluetoothSocket(device);
                    } catch (IOException e) {
                        fail = true;
                        Toast.makeText(getBaseContext(), "Socket creation failed", Toast.LENGTH_SHORT).show();
                    }
                    // Establish the Bluetooth socket connection.
                    try {
                        mBTSocket.connect();
                    } catch (IOException e) {
                        try {
                            fail = true;
                            mBTSocket.close();
                            mHandler.obtainMessage(CONNECTING_STATUS, -1, -1)
                                    .sendToTarget();
                        } catch (IOException e2) {
                            //insert code to deal with this
                            Toast.makeText(getBaseContext(), "Socket creation failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                    if(fail == false) {
                        mConnectedThread = new ConnectedThread(mBTSocket);
                        mConnectedThread.start();

                        mHandler.obtainMessage(CONNECTING_STATUS, 1, -1, name)
                                .sendToTarget();
                    }
                }
            }.start();
        }
    };

    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
        try {
            final Method m = device.getClass().getMethod("createInsecureRfcommSocketToServiceRecord", UUID.class);
            return (BluetoothSocket) m.invoke(device, BTMODULEUUID);
        } catch (Exception e) {
            Log.e(TAG, "Could not create Insecure RFComm Connection",e);
        }
        return  device.createRfcommSocketToServiceRecord(BTMODULEUUID);
    }


    class ConnectedThread extends Thread {

        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the input and output streams, using temp objects because
            // member streams are final
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) { }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[1024];  // buffer store for the stream
            int bytes; // bytes returned from read()
            // Keep listening to the InputStream until an exception occurs
            while (true) {
                try {
                    // Read from the InputStream
                    bytes = mmInStream.available();
                    if(bytes != 0) {
                        buffer = new byte[1024];
                        SystemClock.sleep(100); //pause and wait for rest of data. Adjust this depending on your sending speed.
                        bytes = mmInStream.available(); // how many bytes are ready to be read?
                        bytes = mmInStream.read(buffer, 0, bytes); // record how many bytes we actually read
                        mHandler.obtainMessage(MESSAGE_READ, bytes, -1, buffer)
                                .sendToTarget(); // Send the obtained bytes to the UI activity
                    }
                } catch (IOException e) {
                    e.printStackTrace();

                    break;
                }
            }
        }

        /* Call this from the main activity to send data to the remote device */
        public void write(String input) {
            byte[] bytes = input.getBytes();           //converts entered String into bytes
            try {
                mmOutStream.write(bytes);
            } catch (IOException e) { }
        }

        /* Call this from the main activity to shutdown the connection */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) { }
        }
    }

}
