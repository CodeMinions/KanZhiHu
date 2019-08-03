package me.codeminions.zhizhi.frags.firstPage;

import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import me.codeminions.common.app.Fragment;
import me.codeminions.common.bean.Answer;
import me.codeminions.common.net.RequestResult;
import me.codeminions.common.widget.recycler.RecyclerAdapter;
import me.codeminions.zhizhi.R;
import me.codeminions.zhizhi.activity.AnswerActivity;
import me.codeminions.zhizhi.helper.RefreshAdapter;
import me.codeminions.zhizhi.net.AnswerLoad;
import me.codeminions.zhizhi.tools.MHandler;
import me.codeminions.zhizhi.tools.SpacesItemDecoration;
import me.codeminions.zhizhi.view.DropRefreshRecyclerView.OnRefreshListener;
import me.codeminions.zhizhi.view.DropRefreshRecyclerView.RefreshListView;

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

        new AnswerLoad().getAnswerList()
                .subscribe(new RequestResult<>(new RequestResult.OnRequestResult<List<Answer>>() {
                    @Override
                    public void onSuccess(List<Answer> answers) {
                        titles = answers;
                        initList();
                    }

                    @Override
                    public void onFail(Throwable e) {
                        Log.i("retrofit_error", e.getMessage());
                    }
                }));
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
}
