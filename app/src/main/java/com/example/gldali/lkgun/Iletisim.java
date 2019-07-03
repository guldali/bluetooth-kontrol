package com.example.gldali.lkgun;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;

public class Iletisim extends AppCompatActivity {
    String address=null;
    private ProgressDialog progress;
    BluetoothAdapter myBluetooth = null;

    BluetoothSocket btSocket = null;//Bağlantonı olusmSI İÇİN KULLANILMASI GREKEN DEĞİŞKENLERDEN BİR TANESİ
    BluetoothDevice bluetoothDevice;//bizim bağlanmak istediğimiz cihaz
    BluetoothServerSocket bluetoothServerSocket;

    private boolean isBtConnected=false;
    static  final UUID myUUDI =UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");//Kimlik  numarası gibi düşünülenilir.Bluetooth haberleşmesinin gerceklesmesi için bu id nin kullanılması gerekiyor
    Button ileri,geri,sag,sol, basla,dur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iletisim);

        Intent newint=getIntent();
        address=newint.getStringExtra(MainActivity.EXTRA_ADDRESS);

        ileri =findViewById(R.id.btn_ileri);
        geri =findViewById(R.id.btn_geri);
        sag=findViewById(R.id.btn_sağ);
        sol =findViewById(R.id.btn_sol);
        basla=findViewById(R.id.btn_baslat);
        dur=findViewById(R.id.btn_dur);

        basla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btSocket!=null){//bağlantı var ise
                    try {
                         btSocket.getOutputStream().write("X".toString().getBytes());
                    }catch (IOException e){

                    }

                }
            }
        });
        dur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btSocket!=null){//bağlantı var ise
                    try {
                        btSocket.getOutputStream().write("S".toString().getBytes());
                    }catch (IOException e){

                    }

                }
            }
        });

        ileri.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (btSocket!=null){
                    switch (event.getAction())
                    {
                        case MotionEvent.ACTION_DOWN:
                        try {
                            btSocket.getOutputStream().write("F".toString().getBytes());
                        }catch (Exception e){

                        }
                        break;
                        case MotionEvent.ACTION_UP:
                        try {
                            btSocket.getOutputStream().write("S".toString().getBytes());
                        }
                        catch (Exception e){

                        }
                        break;
                    }
                }
                return false;
            }
        });
        sag.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (btSocket!=null){
                    switch (event.getAction())
                    {
                        case MotionEvent.ACTION_DOWN:
                            try {
                                btSocket.getOutputStream().write("R".toString().getBytes());
                            }catch (Exception e){

                            }
                            break;
                        case MotionEvent.ACTION_UP:
                            try {
                                btSocket.getOutputStream().write("S".toString().getBytes());
                            }
                            catch (Exception e){

                            }
                            break;
                    }
                }
                return false;
            }
        });
        sol.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (btSocket!=null){
                    switch (event.getAction())
                    {
                        case MotionEvent.ACTION_DOWN:
                            try {
                                btSocket.getOutputStream().write("L".toString().getBytes());
                            }catch (Exception e){

                            }
                            break;
                        case MotionEvent.ACTION_UP:
                            try {
                                btSocket.getOutputStream().write("S".toString().getBytes());
                            }
                            catch (Exception e){

                            }
                            break;
                    }
                }
                return false;
            }
        });
        geri.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (btSocket!=null){
                    switch (event.getAction())
                    {
                        case MotionEvent.ACTION_DOWN:
                            try {
                                btSocket.getOutputStream().write("B".toString().getBytes());
                            }catch (Exception e){

                            }
                            break;
                        case MotionEvent.ACTION_UP:
                            try {
                                btSocket.getOutputStream().write("S".toString().getBytes());
                            }
                            catch (Exception e){

                            }
                            break;
                    }
                }
                return false;
            }
        });
        new BTbaglan().execute();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Disconnect();
    }

    private void Disconnect() {
        if(btSocket!=null){
            try {
                btSocket.close();
            }catch (IOException e){
                //msg error
            }
        }
        finish();
    }


    private class BTbaglan extends AsyncTask<Void, Void, Void> {
        private boolean ConnectSuccess = true;

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(Iletisim.this, "Baglanıyor...", "Lütfen Bekleyin");
        }

        // https://gelecegiyazanlar.turkcell.com.tr/konu/android/egitim/android-301/asynctask
        @Override
        protected Void doInBackground(Void... devices) {
            try {
                if (btSocket == null || !isBtConnected) {
                    myBluetooth = BluetoothAdapter.getDefaultAdapter();
                    BluetoothDevice cihaz = myBluetooth.getRemoteDevice(address);
                    btSocket = cihaz.createInsecureRfcommSocketToServiceRecord(myUUDI);
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    btSocket.connect();


                }
            } catch (IOException e) {
                ConnectSuccess = false;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (!ConnectSuccess) {
                // msg("Baglantı Hatası, Lütfen Tekrar Deneyin");
                Toast.makeText(getApplicationContext(), "Bağlantı Hatası Tekrar Deneyin", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                //   msg("Baglantı Basarılı");
                Toast.makeText(getApplicationContext(), "Bağlantı Başarılı", Toast.LENGTH_SHORT).show();

                isBtConnected = true;
            }
            progress.dismiss();
        }

    }

}
