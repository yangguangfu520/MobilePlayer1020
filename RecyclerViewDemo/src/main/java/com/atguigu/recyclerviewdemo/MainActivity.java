package com.atguigu.recyclerviewdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
        } else if (v == btnGrid) {
            // Handle clicks for btnGrid
        } else if (v == btnFlow) {
            // Handle clicks for btnFlow
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
        recyclerview.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));


    }
}
