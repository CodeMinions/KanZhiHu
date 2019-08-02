package me.codeminions.zhizhi.frags.firstPage;

import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
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
import me.codeminions.zhizhi.net.HttpUtil;
import me.codeminions.zhizhi.tools.Constant;
import me.codeminions.zhizhi.view.DropRefreshRecyclerView.OnRefreshListener;
import me.codeminions.zhizhi.view.DropRefreshRecyclerView.RefreshListView;
import me.codeminions.common.view.widget.recycler.RecyclerAdapter;
import me.codeminions.zhizhi.R;
import me.codeminions.zhizhi.bean.Answer;
import me.codeminions.zhizhi.helper.RefreshAdapter;
import me.codeminions.zhizhi.tools.MHandler;
import me.codeminions.zhizhi.tools.SpacesItemDecoration;
import okhttp3.Call;
import okhttp3.Response;

public class AttentFragment extends Fragment {

    List<Answer> answerList = new ArrayList<>();
    RefreshListView listView;
    RefreshAdapter adapter;
    LinearLayoutManager manager;

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_attent;
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);

        manager = new LinearLayoutManager(root.getContext());
        listView = root.findViewById(R.id.list_attent);

        listView.setLayoutManager(manager);

        // 设置列表装饰线
        int space = 25;
        listView.addItemDecoration(new SpacesItemDecoration(space));

    }

    /**
     * 请求服务器
     * 删去注释和initList中的add就好了，减少请求
     */
    @Override
    protected void requestData() {
        Log.i("AttentFragment", "假装自己正在网络请求");

//        for (int i = 0; i < 15;i++)
//            answerList.add(new Answer("有哪些你第一眼就钟情的佳句？", "我失去了一只臂膀 ，就睁开了一只眼睛。——顾城八岁诗作《杨树》\n" +
//                    "你来人间一趟，你要看看太阳。和你的心上人，一起走在街上。——海子《夏天的太阳》"));

        //        titles = Arrays.asList(new String[]{" 目前求 π 的算法中哪种收敛最快？ ", " 为什么我感觉张鹤帆很奇怪？ ", " 怎么样客观看待张云雷现象？ ", " 为什么中国摔跤和柔道如此相似？ ", " 网络小说里有什么沙雕情节？ ", " 电影《流浪地球》有哪些细节和彩蛋？ ", " 王鸥是怎么度过她的人生低谷的（就是众所周知的夜光剧本事件之后那段时间）？ ", " 你见过哪些蠢到家的罪犯？ ", " 八月长安笔下的梗哪个最戳你？ ", " 怎么评价欧阳娜娜的穿搭？ "});
        GeekThreadManager.getInstance().execute(new GeekRunnable(ThreadPriority.LOW) {
            @Override
            public void run() {
                HttpUtil.sendHttpRequest(Constant.URL_BASE + Constant.URL_EXPLORE, new HttpUtil.MyCallback() {
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {
                            String getData = response.body().string();
                            parseJson(getData);

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

    }


    protected void initList() {

        adapter = new RefreshAdapter();
        answerList.add(0, null);
        adapter.replace(answerList);
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
                requestData();
            }
        });
    }

    MHandler handler = new MHandler(this, new MHandler.HandleCallBack() {
        @Override
        public void handleMessage(Message msg, WeakReference reference) {
            switch (msg.what) {
                case 101:
                    if (reference != null) {
                        AttentFragment fragment = (AttentFragment) reference.get();
                        fragment.initList();
                        handler.sendEmptyMessage(102);
                    }
                    break;
                case 102:
                    listView.refreshCompete();
                    isLoad = 1;
                    break;
            }
        }
    });

    void parseJson(String data) {
        Gson gson = new Gson();
        answerList = gson.fromJson(data, new TypeToken<List<Answer>>() {
        }.getType());
        Log.i("List Size", String.valueOf(answerList.size()));
    }
}
