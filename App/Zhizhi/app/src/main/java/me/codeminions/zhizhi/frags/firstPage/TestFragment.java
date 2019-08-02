package me.codeminions.zhizhi.frags.firstPage;

import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import me.codeminions.common.view.app.Fragment;
import me.codeminions.common.view.widget.recycler.RecyclerAdapter;
import me.codeminions.zhizhi.R;
import me.codeminions.zhizhi.bean.Answer;
import me.codeminions.zhizhi.helper.RefreshAdapter;
import me.codeminions.zhizhi.tools.MHandler;
import me.codeminions.zhizhi.tools.SpacesItemDecoration;
import me.codeminions.zhizhi.view.DropRefreshRecyclerView.OnRefreshListener;
import me.codeminions.zhizhi.view.DropRefreshRecyclerView.RefreshListView;

public class TestFragment extends Fragment {

    RefreshListView listView;
    RefreshAdapter adapter;
    List<Answer> list = new ArrayList<>();

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_attent;
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);

        listView = root.findViewById(R.id.list_attent);
        listView.setLayoutManager(new LinearLayoutManager(root.getContext()));

        adapter = new RefreshAdapter();
        // 设置列表装饰线
        int space = 25;
        listView.addItemDecoration(new SpacesItemDecoration(space));

        for (int i = 0; i < 15;i++)
            list.add(new Answer("有哪些你第一眼就钟情的佳句？", "我失去了一只臂膀 ，就睁开了一只眼睛。——顾城八岁诗作《杨树》\n" +
                    "你来人间一趟，你要看看太阳。和你的心上人，一起走在街上。——海子《夏天的太阳》"));

        adapter.replace(list);
        adapter.setListener(new RecyclerAdapter.AdapterListenerImpl<Answer>() {
            @Override
            public void onItemClick(RecyclerAdapter.ViewHolder holder, Answer data) {
                Integer a = holder.getAdapterPosition();
                Toast.makeText(getActivity(), a.toString() + "   待实现", Toast.LENGTH_SHORT).show();

            }
        });
        listView.setAdapter(adapter);
        listView.setListener(new OnRefreshListener() {
            @Override
            public void onReFresh() {
                refresh();
            }
        });
    }

    void refresh(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(101);
            }
        }).start();
    }

    MHandler handler = new MHandler(this, new MHandler.HandleCallBack() {
        @Override
        public void handleMessage(Message msg, WeakReference reference) {
            switch (msg.what) {
                case 101:
                    if (reference != null) {
                        listView.refreshCompete();
                    }
            }
        }
    });
}
