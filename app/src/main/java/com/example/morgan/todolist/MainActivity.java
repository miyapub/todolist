package com.example.morgan.todolist;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    public SimpleAdapter simpleAdapter=null;//适配器
    public List<Map<String, Object>> todo_data = new ArrayList<>(); //todo 列表

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        todo_data=(new Json()).getInfo(getBaseContext(),"todo");//取出内容
        //
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputTitleDialog();

            }
        });



        //render_list();
        /*
        new Thread(new Runnable() {
            @Override
            public void run() {
                //和UI无关的，和数据有关的都在这里
                //绑定
                ListView list=(ListView)findViewById(R.id.list);

                simpleAdapter = new SimpleAdapter(getBaseContext(), todo_data,
                        R.layout.item, new String[] { "todo","status"},
                        new int[] { R.id.todoCheck,R.id.status });

                runOnUiThread(new Runnable(){
                    @Override
                    public void run() {
                        list.setAdapter(simpleAdapter);
                        render_list();
                    }
                });
            }
        }).start();*/

        new Thread(new Runnable() {
            @Override
            public void run() {
                //和UI无关的，和数据有关的都在这里
                final ListView list=(ListView)findViewById(R.id.list);

                simpleAdapter = new SimpleAdapter(getBaseContext(), todo_data,
                        R.layout.item, new String[] { "todo","status"},
                        new int[] { R.id.todoCheck,R.id.status });
                list.setAdapter(simpleAdapter);
                runOnUiThread(new Runnable(){
                    @Override
                    public void run() {
                        //和UI有关的，都在这里


                        //重新做布局

                        render_list();
                    }
                });
            }
        }).start();


        ListView list=(ListView)findViewById(R.id.list);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //positon为点击到的listView的索引
                final Map<String, Object> map=todo_data.get(position);
                //获取title的值
                String todo=(String)map.get("todo");
                final RadioButton rb=(RadioButton)view.findViewById(R.id.todoCheck);
                final TextView status=(TextView)view.findViewById(R.id.status);
                //
                //
                if(rb.isChecked()){
                    //rb.setChecked(false);
                    map.put("status","ready");
                    //status.setText("ready");
                }else{
                    //rb.setChecked(true);
                    map.put("status","finish");
                    //status.setText("finish");
                }
                simpleAdapter.notifyDataSetChanged();

                //todo_data.set(position,map);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //map.put("todo","222");
                        (new Json()).saveInfo(getBaseContext(),"todo",todo_data);
                        //和UI无关的，和数据有关的都在这里
                        runOnUiThread(new Runnable(){
                            @Override
                            public void run() {
                                //和UI有关的，都在这里
                                //重新做布局

                                render_list();
                            }
                        });
                    }
                }).start();
                //
                /*
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        map.put("todo","222");
                        Alert("title","999");
                        runOnUiThread(new Runnable(){
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(5000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                //render_list();
                            }
                        });
                    }
                }).start();*/
                //Intent intent=new Intent(this,NewActivity.class);
                //intent.putExtra("title",title);
                //startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_delAll) {
            todo_data.clear();
            Json json=new Json();
            json.saveInfo(getBaseContext(),"todo",todo_data);
            //render_list();
            simpleAdapter.notifyDataSetChanged();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void inputTitleDialog() {
        final EditText input = new EditText(this);
        input.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View view,boolean hasFocus){
                if(hasFocus){
                    show_soft_input(view);
                }else{
                    hide_soft_input(view);
                }
            }
        });
        input.setHint("请输入");
        //input.setFocusable(true);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("").setView(input).setNegativeButton("取消", null);
        builder.setPositiveButton("确认",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        final String todoText = input.getText().toString();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Map<String, Object> item = new HashMap<String, Object>();
                                item.put("todo",todoText);
                                item.put("startTime","2017");
                                item.put("endTime","2018");
                                //item.put("status","finish");
                                item.put("status","ready");
                                //填充数据
                                todo_data.add(item);
                                (new Json()).saveInfo(getBaseContext(),"todo",todo_data);

                                runOnUiThread(new Runnable(){
                                    @Override
                                    public void run() {
                                        simpleAdapter.notifyDataSetChanged();
                                        render_list();
                                    }
                                });
                            }
                        }).start();
                        //
                    }
                });
        builder.show();
    }
    public void hide_soft_input(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        //boolean isOpen=imm.isActive();//isOpen若返回true，则表示输入法打开
        //imm.showSoftInput(view,InputMethodManager.SHOW_FORCED);//强制显示键盘
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0); //强制隐藏键盘
    }
    public void show_soft_input(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        //boolean isOpen=imm.isActive();//isOpen若返回true，则表示输入法打开
        imm.showSoftInput(view,InputMethodManager.SHOW_FORCED);//强制显示键盘
        //imm.hideSoftInputFromWindow(view.getWindowToken(), 0); //强制隐藏键盘
    }

    public void Alert(String title,String msg){
        new AlertDialog.Builder(this).setTitle(title).setMessage(msg)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }})
                .setNegativeButton("取消",null)
                .show();
    }

    public void render_list(){
        /*
        todo_data = new ArrayList<Map<String, Object>>(); //todo列表
        for(int i=0; i<todo_data.size(); i++) {
            Map<String, Object> it = todo_data.get(i);
            todo_data.add(it);
        }
        Collections.reverse(todo_data);//倒叙 内容反转
        //重新做布局
        */



        final ListView list=(ListView)findViewById(R.id.list);


        for(int i = 0; i < list.getChildCount(); i++){
            View v = list.getChildAt(i);
            final RadioButton r = (RadioButton)v.findViewById(R.id.todoCheck);
            TextView tv=(TextView)v.findViewById(R.id.status);

            //if((todo_data.get(i)).get("status").equals("finish")){
            if(tv.getText().equals("finish")){
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //和UI无关的，和数据有关的都在这里
                        runOnUiThread(new Runnable(){
                            @Override
                            public void run() {
                                r.setChecked(true);
                                r.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                                r.setTextColor(Color.parseColor("#cccccc"));
                            }
                        });
                    }
                }).start();

            }
            //if((todo_data.get(i)).get("status").equals("ready")){
            if(tv.getText().equals("ready")){
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //和UI无关的，和数据有关的都在这里
                        runOnUiThread(new Runnable(){
                            @Override
                            public void run() {
                                r.setChecked(false);
                                r.getPaint().setFlags(Paint.LINEAR_TEXT_FLAG);
                                r.setTextColor(Color.parseColor("#000000"));
                            }
                        });
                    }
                }).start();

            }
        }

    }
}
