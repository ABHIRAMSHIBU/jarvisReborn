package com.sal.sal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Contacts;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class InsideRoom extends AppCompatActivity {
    Socket socket;
    OutputStream output;
    InputStream input;
    String writecmd="";
    TableLayout table;
    String roomno;
    String res="";
    int count;
    Handler mHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inside_room);
        Intent intent = getIntent();
         roomno= intent.getStringExtra("Roomno");
        SharedPreferences pref = getApplicationContext().getSharedPreferences("INSIDE_ROOM"+roomno, 0); // 0 - for private mode

        count=pref.getInt("nsw", 0);




        table=(TableLayout)findViewById(R.id.table);
        init_table();



        new Thread(new ClientThread()).start();


    }

    public void addswitch(View view) {

        final ToggleButton t=new ToggleButton(getApplicationContext());
        TableRow.LayoutParams btnp = new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.MATCH_PARENT
        );
        btnp.setMargins(30, 20, 0, 0);
        t.setLayoutParams(btnp);

        count+=1;
        int nrows=table.getChildCount();
        t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch_action(count,t);//configure--->2  on--->1 off--->0
            }
        });
        if(nrows==0){
            TableRow newrow=new TableRow(getApplicationContext());
            TableRow.LayoutParams params = new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.MATCH_PARENT
            );

            params.setMargins(0, 30, 0, 0);
            newrow.setLayoutParams(params);
            t.setText("SWITCH 1\nOFF");
            t.setTextOn("SWITCH 1\nON");
            t.setTextOff("SWITCH 1\nOFF");
            table.addView(newrow);
            newrow.addView(t);

        }
        else{
            TableRow lastrow=(TableRow)table.getChildAt(nrows-1);
            int nbtn=lastrow.getChildCount();
            if(nbtn==3){
                TableRow newrow=new TableRow(getApplicationContext());
                TableRow.LayoutParams params = new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.MATCH_PARENT
                );

                params.setMargins(0, 30, 0, 0);
                newrow.setLayoutParams(params);
                t.setText("SWITCH "+count+"\nOFF");
                t.setTextOn("SWITCH "+count+"\nON");
                t.setTextOff("SWITCH "+count+"\nOFF");
                table.addView(newrow);
                newrow.addView(t);

            }
            else{
                t.setText("SWITCH "+count+"\nOFF");
                t.setTextOn("SWITCH "+count+"\nON");
                t.setTextOff("SWITCH "+count+"\nOFF");
                lastrow.addView(t);
                table.removeViewAt(nrows-1);
                table.addView(lastrow);
            }

        }
        SharedPreferences pref = getApplicationContext().getSharedPreferences("INSIDE_ROOM"+roomno ,0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("nsw", count);
        editor.apply();
        editor.commit();

    }

class UITOAST implements  Runnable{
        String msg;
    public  UITOAST( String msg){
     this.msg=msg;
    }
    @Override
    public void run() {
        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
    }
}
    class read implements Runnable{

    @Override
    public void run() {
        if(socket.isConnected()){
        while(socket!=null && socket.isConnected()){
        try {
            Log.d("now","sleeping"+socket.getInputStream().available());
            while(socket.getInputStream().available()==0) {Thread.sleep(100); }
            Log.d("read","About to read");

            BufferedReader in = new BufferedReader(new InputStreamReader(input));
            res=in.readLine();

            Log.d("read",res);

//            new Thread(new UITOAST(res)).start();
            ;
           runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(getApplicationContext(), res, Toast.LENGTH_SHORT).show();
                }
            });

        } catch (Exception e) {
            Log.d("errorread","error"+e);
        }

    }}
    else{
            Log.d("error","notconnected");
        }
    }
}

class write implements Runnable{

    @Override
    public void run() {
        while (true) {
            if(writecmd.equals(""))
                continue;
            if (socket != null && socket.isConnected()) {
                PrintWriter out = null;
                try {
                    Thread.sleep(1000);
                    out = new PrintWriter(new BufferedWriter(
                            new OutputStreamWriter(socket.getOutputStream())));

                    out.print(writecmd+"\r\n");
                    out.flush();
                    Log.d("write", "sucess");
                    writecmd="";
                    //out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("errorwrite", e.toString());
                }


            } else {
                Log.d("error", "retry socket");
            }
        }
    }
}

    class ClientThread implements Runnable {

        @Override
        public void run() {

            try {
                InetAddress serverAddr = InetAddress.getByName("abhiramshibu.tk");

                socket = new Socket(serverAddr, 9998);
                input=socket.getInputStream();
                Log.d("sucess","connected");
                if(socket.isConnected()) {
                   new Thread(new read()).start();

                  new Thread(new write()).start();


                }

            } catch (UnknownHostException e1) {
                e1.printStackTrace();
                Log.d("error","H"+e1);
            } catch (IOException e1) {
                e1.printStackTrace();
                Log.d("error",""+e1);
            }

        }

    }
    public  void  init_table(){
       // SharedPreferences pref = getApplicationContext().getSharedPreferences("INSIDE_ROOM"+roomno, 0); // 0 - for private mode





        TableRow.LayoutParams btnp = new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.MATCH_PARENT
        );
        btnp.setMargins(30, 20, 0, 0);
        int i;
        for(i=1;i<=count;i++){
            final ToggleButton btn=new ToggleButton(getApplicationContext());
            final int finalI1 = i;
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch_action(finalI1,btn);
                }
            });

            final int finalI = i;
//            btn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    setActivity(finalI);
//                }
//            });
            btn.setText("SWITCH "+String.valueOf(i)+"\nOFF");
            btn.setTextOn("SWITCH "+String.valueOf(i)+"\nON");
            btn.setTextOff("SWITCH "+String.valueOf(i)+"\nOFF");
            btn.setVisibility(View.VISIBLE);
            btn.setLayoutParams(btnp);



            if ((i-1)%3==0 || i==1) {

                //Toast.makeText(getApplicationContext(),"3 button",Toast.LENGTH_LONG).show();
                TableRow newrow = new TableRow(getApplicationContext());
                newrow.setVisibility(View.VISIBLE);
                //TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                // newrow.setLayoutParams(lp);
                TableRow.LayoutParams params = new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.MATCH_PARENT
                );

                params.setMargins(0, 30, 0, 0);
                newrow.setLayoutParams(params);

                newrow.addView(btn);
                table.addView(newrow);
            } else {
                TableRow row=(TableRow)table.getChildAt((int)((i-1)/3));
                row.addView(btn);
                table.removeViewAt((int)((i-1)/3));
                table.addView(row);
            }






        }


    }
    public void switch_action(int sno, ToggleButton action){


        if(action.getText().toString().equals("SWITCH " + sno + "\nON")){
            //turn on;
            //$set <pinNo> <State> <DeviceId/RoomNo>
            writecmd="$set "+sno+" "+1+" "+roomno;

        }
        else{
            //turn off
            writecmd="$set "+sno+" "+0+" "+roomno;
        }



        Toast.makeText(getApplicationContext(),"SWITCH"+sno+" "+action.getText().toString(),Toast.LENGTH_LONG).show();



    }

}
