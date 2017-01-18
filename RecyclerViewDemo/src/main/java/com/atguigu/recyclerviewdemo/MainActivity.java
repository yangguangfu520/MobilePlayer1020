package com.atguigu.recyclerviewdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnAdd;
    private Button btnDelete;
    private Button btnList;
    private Button btnGrid;
    private Button btnFlow;
    private RecyclerView recyclerview;
    private ArrayList<String> datas;
    private RecyclerDemoAdapter adapter;

    /**
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2017-01-18 14:46:31 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private void findViews() {
        setContentView(R.layout.activity_main);
        btnAdd = (Button) findViewById(R.id.btn_add);
        btnDelete = (Button) findViewById(R.id.btn_delete);
        btnList = (Button) findViewById(R.id.btn_List);
        btnGrid = (Button) findViewById(R.id.btn_Grid);
        btnFlow = (Button) findViewById(R.id.btn_flow);
        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);

        btnAdd.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        btnList.setOnClickListener(this);
        btnGrid.setOnClickListener(this);
        btnFlow.setOnClickListener(this);
    }

    /**
     * Handle button click events<br />
     * <br />
     * Auto-created on 2017-01-18 14:46:31 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    @Override
    public void onClick(View v) {
        if (v == btnAdd) {
            // Handle clicks for btnAdd
        } else if (v == btnDelete) {
            // Handle clicks for btnDelete
        } else if (v == btnList) {
            // Handle clicks for btnList
            recyclerview.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        } else if (v == btnGrid) {
            // Handle clicks for btnGrid
            recyclerview.setLayoutManager(new GridLayoutManager(this,3,GridLayoutManager.VERTICAL,false));
        } else if (v == btnFlow) {
            // Handle clicks for btnFlow
            recyclerview.setLayoutManager(new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL));
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        findViews();
        setAdapter();
    }

    private void setAdapter() {
        //准备数据
        datas = new ArrayList<>();
        for (int i = 0; i < 200; i++) {
            datas.add("Data_"+i) ;

        }

        adapter = new RecyclerDemoAdapter(this,datas);
        //设置RecyclerView的适配器
        recyclerview.setAdapter(adapter);

        //布局管理器
        //List 第一个参数：上下文；第二个参数：方向；第三个参数：是否倒序
        recyclerview.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        //Grid

//        recyclerview.setLayoutManager(new GridLayoutManager(this,3,GridLayoutManager.VERTICAL,false));
        //瀑布流
//        recyclerview.setLayoutManager(new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL));


        //设置分割线
        recyclerview.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));
    }
}
