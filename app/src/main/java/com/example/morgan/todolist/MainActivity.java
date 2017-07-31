package com.example.morgan.todolist;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    public List<Map<String, Object>> todo_data = new ArrayList<Map<String, Object>>(); //todo 原始列表

    public List<Map<String, Object>> todo_list_data = new ArrayList<Map<String, Object>>(); //todo 显示的列表
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputTitleDialog();

            }
        });

        ListView dotolist=(ListView)findViewById(R.id.list);
        dotolist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //positon为点击到的listView的索引
                Map<String,Object> map=todo_list_data.get(position);
                //获取title的值
                String todo=(String)map.get("todo");
                map.put("todo","222");
                //Alert("title",todo);
                render_list();
                //Intent intent=new Intent(this,NewActivity.class);
                //intent.putExtra("title",title);
                //startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
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

        //input.setFocusable(true);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);



        builder.setTitle("输入代办事项").setIcon(
                R.drawable.plus).setView(input).setNegativeButton("取消", null);
        builder.setPositiveButton("确认",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        final String todoText = input.getText().toString();
                        //
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



                                runOnUiThread(new Runnable(){
                                    @Override
                                    public void run() {
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
        todo_list_data = new ArrayList<Map<String, Object>>(); //todo列表
        for(int i=0; i<todo_data.size(); i++) {
            Map<String, Object> it = todo_data.get(i);
            todo_list_data.add(it);
        }
        Collections.reverse(todo_list_data);//倒叙 内容反转
        SimpleAdapter simpleAdapter = new SimpleAdapter(getBaseContext(), todo_list_data,
                R.layout.item, new String[] { "todo"},
                new int[] { R.id.todoCheck});
        //绑定
        ListView list=(ListView)findViewById(R.id.list);
        list.setAdapter(simpleAdapter);
    }
}
