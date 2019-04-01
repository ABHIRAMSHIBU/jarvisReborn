package com.sal.sal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
DrawerLayout drawerLayout;
    FloatingActionButton fab;
    TableLayout table;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

         drawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        drawerLayout.closeDrawers();

                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here

                        return true;
                    }
                });
    table=(TableLayout)findViewById(R.id.table);
    init_table();


       fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Click action
                TableRow row;
                //Toast.makeText(getApplicationContext(), "hi", Toast.LENGTH_LONG).show();
                //prepare new  button
                final Button btn = new Button(getApplicationContext());
                btn.setVisibility(View.VISIBLE);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
               btn.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        // TODO Auto-generated method stub
                        showPopup(btn);
                        return true;
                    }
                });

                TableRow.LayoutParams btnp = new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.MATCH_PARENT
                );
                btnp.setMargins(30, 20, 0, 0);

                final int count;
                int nrows = table.getChildCount();
                if (nrows != 0) {
                    row = (TableRow) table.getChildAt(nrows - 1);
                    int nbtn = row.getChildCount();

                    btn.setLayoutParams(btnp);
                    //Toast.makeText(getApplicationContext(), "params\n" + row.getChildAt(nbtn - 1).getLayoutParams().toString(), Toast.LENGTH_LONG).show();

//                    if(nrows==1){
//                        count=nbtn+1;
//                    }
//                    else
                        count = 3 * (nrows-1) + nbtn+1;


                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            setActivity(count);
                        }
                    });
                    // Toast.makeText(getApplicationContext(),""+count,Toast.LENGTH_LONG).show();
                    btn.setText("Room "+String.valueOf(count));
                    if (nbtn == 3) {

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

                        row.addView(btn);
                        table.removeViewAt(nrows - 1);
                        table.addView(row);
                    }
                }
                else {
                    TableRow newrow = new TableRow(getApplicationContext());
                    newrow.setVisibility(View.VISIBLE);
                    TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                     newrow.setLayoutParams(lp);
                    TableRow.LayoutParams params = new TableRow.LayoutParams(
                            TableRow.LayoutParams.WRAP_CONTENT,
                            TableRow.LayoutParams.WRAP_CONTENT
                    );

                    params.setMargins(0, 30, 0, 0);
                    newrow.setLayoutParams(params);
                    btn.setLayoutParams(btnp);
                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            setActivity(1);
                        }
                    });
                    btn.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            // TODO Auto-generated method stub
                            showPopup(btn);
                            return true;
                        }
                    });
                    btn.setText("Room 1");
                    count=1;
                    newrow.addView(btn);
                    table.addView(newrow);


                }

                SharedPreferences pref = getApplicationContext().getSharedPreferences("SAL_main", 0); // 0 - for private mode
                SharedPreferences.Editor editor = pref.edit();
                editor.putInt("nbtns", count);
                editor.apply();
                editor.commit();
            }


        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public  void  init_table(){
        SharedPreferences pref = getApplicationContext().getSharedPreferences("SAL_main", 0); // 0 - for private mode

        int count=pref.getInt("nbtns", -1);



        TableRow.LayoutParams btnp = new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.MATCH_PARENT
        );
        btnp.setMargins(30, 20, 0, 0);
        int i;
        for(i=1;i<=count;i++){
            final Button btn=new Button(getApplicationContext());

            final int finalI = i;
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setActivity(finalI);
                }
            });
            btn.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    // TODO Auto-generated method stub
                    showPopup(btn);
                    return true;
                }
            });
            btn.setText("Room "+String.valueOf(i));
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
    void setActivity(int n){
        Intent intent = new Intent(this, InsideRoom.class);


        intent.putExtra("Roomno", String.valueOf(n));
        startActivity(intent);
    }
    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
       // popup.getMenu().add("Delete");
        popup.getMenu().add(1,1,1,"Delete");

        popup.show();
    }
}