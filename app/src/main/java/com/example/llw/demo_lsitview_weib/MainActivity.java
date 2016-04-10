package com.example.llw.demo_lsitview_weib;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements Mylistview.IRefreshlistener {
    private Mylistview mylistview;
    private List<Myget_item> list;
    private Mybaseadapter mybaseadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        intiview();
    }

    public void intiview() {
        mylistview = (Mylistview) findViewById(R.id.listview_id);
        mylistview.setInterface(this);
        list = new ArrayList<>();
        Initview_adapter();
    }

    public void Initview_adapter() {
        for (int i = 0; i < 40; i++) {
            list.add(new Myget_item("当今世界"));
        }
        Myadapter(list);
    }

    public void Myadapter(List<Myget_item> list) {
        if (mybaseadapter == null) {
            mybaseadapter = new Mybaseadapter(list, this);
            mylistview.setAdapter(mybaseadapter);
        } else {
            mybaseadapter.OnDateChanget(list);
        }
    }

    String[] newstr = {"最新数据", "刷新书籍"};

    //获取最新数据
    public void Get_New_Date() {
        for (int s = 0; s < 2; s++) {
            list.add(0, new Myget_item(newstr[s]));
        }
    }

    @Override
    public void onRefresh() {
        Get_New_Date();
        Myadapter(list);
        mylistview.RefreshComplete();
    }


}
