package me.codeminions.zhizhi.frags.firstPage;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.geek.thread.GeekThreadManager;
import com.geek.thread.ThreadPriority;
import com.geek.thread.ThreadType;
import com.geek.thread.task.GeekRunnable;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import me.codeminions.common.view.app.Fragment;
import me.codeminions.common.view.widget.recycler.RecyclerAdapter;
import me.codeminions.zhizhi.R;
import me.codeminions.zhizhi.activity.AnswerActivity;
import me.codeminions.zhizhi.bean.Answer;
import me.codeminions.zhizhi.helper.AnswerViewHolder;
import me.codeminions.zhizhi.helper.RefreshAdapter;
import me.codeminions.zhizhi.net.HttpUtil;
import me.codeminions.zhizhi.tools.Constant;
import me.codeminions.zhizhi.tools.MHandler;
import me.codeminions.zhizhi.tools.SpacesItemDecoration;
import me.codeminions.zhizhi.view.DropRefreshRecyclerView.OnRefreshListener;
import me.codeminions.zhizhi.view.DropRefreshRecyclerView.RefreshListView;
import okhttp3.Call;
import okhttp3.Response;

public class RecommendFragment extends Fragment {

    List<Answer> titles = new ArrayList<>();
    RefreshListView listView;
    RefreshAdapter adapter;

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_recommend;
    }


    // TODO: 19-3-10 设置下拉刷新，刷新后缓存，下次打开不用请求，直到触发下拉 
    @Override
    protected void requestData() {
        Log.i("RecommendFragment", "假装自己正在网络请求");
//        for (int i = 0; i < 15;i++)
//            titles.add(new Answer("有哪些你第一眼就钟情的佳句？", "我失去了一只臂膀 ，就睁开了一只眼睛。——顾城八岁诗作《杨树》\n" +
//                    "你来人间一趟，你要看看太阳。和你的心上人，一起走在街上。——海子《夏天的太阳》"));

        GeekThreadManager.getInstance().execute(new GeekRunnable(ThreadPriority.LOW) {
            @Override
            public void run() {

                HttpUtil.sendHttpRequest(Constant.URL_BASE + Constant.URL_EXPLORE, new HttpUtil.MyCallback() {
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String getData = response.body().string();
                        parseJson(getData);

                        if(response.isSuccessful()){
                            Message msg = new Message();
                            msg.what = 101;
                            handler.sendMessage(msg);
                        }
                    }
                });
            }
        }, ThreadType.NORMAL_THREAD);
    }


    @Override
    protected void initData() {
        adapter = new RefreshAdapter();

        adapter.setListener(new RecyclerAdapter.AdapterListenerImpl<Answer>() {
            @Override
            public void onItemClick(RecyclerAdapter.ViewHolder holder, Answer data) {
                Integer a = holder.getAdapterPosition();
                Toast.makeText(getActivity(), a.toString(), Toast.LENGTH_SHORT).show();

                AnswerActivity.actionStart(getActivity(), data);
            }
        });

        listView.setListener(new OnRefreshListener() {
            @Override
            public void onReFresh() {
                refresh();
            }
        });
    }
    void refresh(){

        handler.sendEmptyMessage(102);
    }

    protected void initList() {

        adapter.replace(titles);
        listView.setAdapter(adapter);
    }


    @Override
    protected void initWidget(View root) {
        super.initWidget(root);

        LinearLayoutManager manager = new LinearLayoutManager(root.getContext());
        listView = root.findViewById(R.id.list_recommend);

        listView.setLayoutManager(manager);
        int space = 25;
        listView.addItemDecoration(new SpacesItemDecoration(space));
    }


    MHandler handler = new MHandler(this, new MHandler.HandleCallBack() {
        @Override
        public void handleMessage(Message msg, WeakReference reference) {
            switch (msg.what){
                case 101:
                    if(reference != null) {
                        RecommendFragment fragment = (RecommendFragment) reference.get();
                        fragment.initList();
                    }
                    break;
                case 102:
                    listView.refreshCompete();
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    });

    void parseJson(String data){
        Gson gson = new Gson();
        titles = gson.fromJson(data, new TypeToken<List<Answer>>(){}.getType());
    }
}
