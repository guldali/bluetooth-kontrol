package com.example.gldali.lkgun;
//Laucher ekaranı ilk açtığımızda karsımıza gelen ekran
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    BluetoothAdapter myBluetooth;
    Button button;
    Button buttonCihaz;
    ListView paired_list;
    private Set<BluetoothDevice> pairedDevices;//etrafdaki cihazları temsil eder
    public  static String EXTRA_ADDRESS="device_address";
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myBluetooth=BluetoothAdapter.getDefaultAdapter();//cihazın buetooth özelliği olup olmadığına bakar ,telefonumuzda bluetooth portunu olup olmadığını kontrol ediyor.
        button=findViewById(R.id.btn_bluetooth);
        buttonCihaz=findViewById(R.id.btn_cihazlar);
        paired_list=findViewById(R.id.lst_cihazlar);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              bluetooth();
            }
        });
        buttonCihaz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listdevice();
            }
        });

    }



    private void bluetooth() {
        if(myBluetooth==null){
            Toast.makeText(getApplicationContext(),"bluetooth cihazı yok",Toast.LENGTH_LONG).show();
        }
        if(!myBluetooth.isEnabled())
        {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(intent);
        }
        if(myBluetooth.isEnabled()){
            myBluetooth.disable();
        }
    }
    private void listdevice() {
     pairedDevices=myBluetooth.getBondedDevices();//eşleşmiş cihazları attık
        ArrayList list=new ArrayList();
        if(pairedDevices.size()>0){
            for(BluetoothDevice bt:pairedDevices)
            {
                list.add(bt.getName()+"\n"+bt.getAddress());
            }
        }
        else {
            Toast.makeText(getApplicationContext(),"cihaz yok",Toast.LENGTH_SHORT).show();
        }
          final ArrayAdapter adapter;
         adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, list);
         paired_list.setAdapter(adapter);
         paired_list.setOnItemClickListener(selectDevice);
    }
    public AdapterView.OnItemClickListener selectDevice = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String info=((TextView)view).getText().toString();
            String address=info.substring(info.length()-17);
            Intent comintent = new Intent(MainActivity.this,Iletisim.class);
            comintent.putExtra(EXTRA_ADDRESS,address);
            startActivity(comintent);
        }
    };
}
