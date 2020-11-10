package com.droiduino.bluetoothconn;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {

    private String deviceName = null;
    private String deviceAddress;
    public static Handler handler;
    public static BluetoothSocket mmSocket;
    public static ConnectedThread connectedThread;
    public static CreateConnectThread createConnectThread;

    public boolean verdadeiro = false;
    public boolean sedutor = false;
    public boolean curioso = false;

    private final static int CONNECTING_STATUS = 1; // used in bluetooth handler to identify message status
    private final static int MESSAGE_READ = 2; // used in bluetooth handler to identify message update

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // UI Initialization
        final Toolbar toolbar = findViewById(R.id.toolbar);
        final ImageView imageView = findViewById(R.id.imageView);
        imageView.setBackgroundColor(getResources().getColor(R.color.colorOff));

        final EditText editText = findViewById(R.id.et_texto);

        // If a bluetooth device has been selected from SelectDeviceActivity
        deviceName = getIntent().getStringExtra("deviceName");
        if (deviceName != null){
            // Get the device address to make BT Connection
            deviceAddress = getIntent().getStringExtra("deviceAddress");
            // Show progree and connection status
            toolbar.setSubtitle("Conectando ao " + deviceName + "...");

            /*
            This is the most important piece of code. When "deviceName" is found
            the code will call a new thread to create a bluetooth connection to the
            selected device (see the thread code below)
             */
            BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            createConnectThread = new CreateConnectThread(bluetoothAdapter,deviceAddress);
            createConnectThread.start();
        }

        /*
        Second most important piece of Code. GUI Handler
         */
        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg){
                switch (msg.what){
                    case CONNECTING_STATUS:
                        switch(msg.arg1){
                            case 1:
                                toolbar.setSubtitle("Conectado ao " + deviceName);
                                toolbar.setVisibility(View.INVISIBLE);
                                imageView.setVisibility(View.VISIBLE);
                                editText.setSelected(true);
                                break;
                            case -1:
                                toolbar.setSubtitle("Falha na conexão");
                                break;
                        }
                        break;

                }
            }
        };

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                        actionId == EditorInfo.IME_ACTION_DONE ||
                        event != null &&
                                event.getAction() == KeyEvent.ACTION_DOWN &&
                                event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    if (event == null || !event.isShiftPressed()) {

                        if (editText.getText().toString().toUpperCase().equals("CURIOSO")){
                            editText.setText("");

                            String cmdText = null;

                            // Command to turn on LED on Arduino. Must match with the command in Arduino code
                            cmdText = "<curioso>";

                            final AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                            alertDialog.setMessage("O desejo pelo novo, por conhecer, por saber. A curiosidade traz " +
                                    "emoções nos leva mais longe. A curiosidade desperta o conhecimento");
                            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Continuar",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            finalizar();
                                        }
                                    });
                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Sair",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            connectedThread.write("<apagartodos>");
                                            System.exit(0);
                                        }
                                    });
                            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0xFFFFFF66));

                            alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                                @Override
                                public void onShow(DialogInterface dialogInterface) {
                                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#FC1034"));
                                    alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(Color.parseColor("#FC1034"));
                                }
                            });

                            alertDialog.show();

                            // Send command to Arduino board
                            connectedThread.write(cmdText);

                            curioso = true;
                        }
                        else if (editText.getText().toString().toUpperCase().equals("SEDUTOR")){
                            editText.setText("");

                            String cmdText = null;

                            // Command to turn on LED on Arduino. Must match with the command in Arduino code
                            cmdText = "<sedutor>";

                            final AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                            alertDialog.setMessage("O prazer pela conquista. A sedução está relacionada a confiança," +
                                    " determinação. O exercício da argumentação, da troca.");
                            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Continuar",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            finalizar();
                                        }
                                    });
                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Sair",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            connectedThread.write("<apagartodos>");
                                            System.exit(0);
                                        }
                                    });
                            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0xFFFFFF66));

                            alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                                @Override
                                public void onShow(DialogInterface dialogInterface) {
                                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#FC1034"));
                                    alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(Color.parseColor("#FC1034"));
                                }
                            });

                            alertDialog.show();

                            // Send command to Arduino board
                            connectedThread.write(cmdText);

                            sedutor = true;
                        }
                        else if (editText.getText().toString().toUpperCase().equals("VERDADEIRO")){
                            editText.setText("");

                            String cmdText = null;

                            // Command to turn on LED on Arduino. Must match with the command in Arduino code
                            cmdText = "<verdadeiro>";

                            final AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                            alertDialog.setMessage("A verdade e transparente sempre. É viver sem culpa, é a demonstração" +
                                    " do caráter. A sinceridade em primeiro lugar.");
                            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Continuar",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            finalizar();
                                        }
                                    });
                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Sair",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            connectedThread.write("<apagartodos>");
                                            System.exit(0);
                                        }
                                    });
                            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0xFFFFFF66));

                            alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                                @Override
                                public void onShow(DialogInterface dialogInterface) {
                                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#FC1034"));
                                    alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(Color.parseColor("#FC1034"));
                                }
                            });

                            alertDialog.show();

                            // Send command to Arduino board
                            connectedThread.write(cmdText);

                            verdadeiro = true;
                        }

                        return true; // consume.
                    }
                }
                return false; // pass on to other listeners.
            }
        });
    }

    /* ============================ Thread to Create Bluetooth Connection =================================== */
    public static class CreateConnectThread extends Thread {

        public CreateConnectThread(BluetoothAdapter bluetoothAdapter, String address) {
            /*
            Use a temporary object that is later assigned to mmSocket
            because mmSocket is final.
             */
            BluetoothDevice bluetoothDevice = bluetoothAdapter.getRemoteDevice(address);
            BluetoothSocket tmp = null;
            UUID uuid = bluetoothDevice.getUuids()[0].getUuid();

            try {
                /*
                Get a BluetoothSocket to connect with the given BluetoothDevice.
                Due to Android device varieties,the method below may not work fo different devices.
                You should try using other methods i.e. :
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
                 */
                tmp = bluetoothDevice.createInsecureRfcommSocketToServiceRecord(uuid);

            } catch (IOException e) {
                Log.e(TAG, "Socket's create() method failed", e);
            }
            mmSocket = tmp;
        }

        public void run() {
            // Cancel discovery because it otherwise slows down the connection.
            BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            bluetoothAdapter.cancelDiscovery();
            try {
                // Connect to the remote device through the socket. This call blocks
                // until it succeeds or throws an exception.
                mmSocket.connect();
                Log.e("Status", "Device connected");
                handler.obtainMessage(CONNECTING_STATUS, 1, -1).sendToTarget();
            } catch (IOException connectException) {
                // Unable to connect; close the socket and return.
                try {
                    mmSocket.close();
                    Log.e("Status", "Cannot connect to device");
                    handler.obtainMessage(CONNECTING_STATUS, -1, -1).sendToTarget();
                } catch (IOException closeException) {
                    Log.e(TAG, "Could not close the client socket", closeException);
                }
                return;
            }

            // The connection attempt succeeded. Perform work associated with
            // the connection in a separate thread.
            connectedThread = new ConnectedThread(mmSocket);
            connectedThread.run();
        }

        // Closes the client socket and causes the thread to finish.
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "Could not close the client socket", e);
            }
        }
    }

    /* =============================== Thread for Data Transfer =========================================== */
    public static class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

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
            int bytes = 0; // bytes returned from read()
            // Keep listening to the InputStream until an exception occurs
            while (true) {
                try {
                    /*
                    Read from the InputStream from Arduino until termination character is reached.
                    Then send the whole String message to GUI Handler.
                     */
                    buffer[bytes] = (byte) mmInStream.read();
                    String readMessage;
                    if (buffer[bytes] == '\n'){
                        readMessage = new String(buffer,0,bytes);
                        Log.e("Arduino Message",readMessage);
                        handler.obtainMessage(MESSAGE_READ,readMessage).sendToTarget();
                        bytes = 0;
                    } else {
                        bytes++;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }
            }
        }

        /* Call this from the main activity to send data to the remote device */
        public void write(String input) {
            byte[] bytes = input.getBytes(); //converts entered String into bytes
            try {
                mmOutStream.write(bytes);
            } catch (IOException e) {
                Log.e("Send Error","Unable to send message",e);
            }
        }

        /* Call this from the main activity to shutdown the connection */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) { }
        }
    }

    /* ============================ Terminate Connection at BackPress ====================== */
    @Override
    public void onBackPressed() {
        // Terminate Bluetooth Connection and close app
        if (createConnectThread != null){
            createConnectThread.cancel();
        }
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }

    public void finalizar(){
        if (curioso && verdadeiro && sedutor){
            EditText editText = findViewById(R.id.et_texto);

            editText.setText("");

            final AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
            alertDialog.setMessage("Parabéns!\n" +
                                   "Missão cumprida.");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Sair",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            connectedThread.write("<apagartodos>");
                            System.exit(0);
                        }
                    });
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0xFFFFFF66));

            alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialogInterface) {
                    alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(Color.parseColor("#FC1034"));
                }
            });

            alertDialog.show();
        }
    }
}
