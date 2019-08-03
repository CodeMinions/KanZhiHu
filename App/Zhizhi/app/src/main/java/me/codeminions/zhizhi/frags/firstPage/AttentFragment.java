package me.codeminions.zhizhi.frags.firstPage;

import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import me.codeminions.common.app.Fragment;
import me.codeminions.common.bean.Answer;
import me.codeminions.common.net.RequestResult;
import me.codeminions.common.widget.recycler.RecyclerAdapter;
import me.codeminions.zhizhi.R;
import me.codeminions.zhizhi.helper.RefreshAdapter;
import me.codeminions.zhizhi.net.AnswerLoad;
import me.codeminions.zhizhi.tools.SpacesItemDecoration;
import me.codeminions.zhizhi.view.DropRefreshRecyclerView.OnRefreshListener;
import me.codeminions.zhizhi.view.DropRefreshRecyclerView.RefreshListView;

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

        new AnswerLoad().getAnswerList()
                .subscribe(new RequestResult<>(new RequestResult.OnRequestResult<List<Answer>>() {
                    @Override
                    public void onSuccess(List<Answer> answers) {
                        answerList = answers;
                        initList();
                        listView.refreshCompete();
                        isLoad = 1;
                    }

                    @Override
                    public void onFail(Throwable e) {
                        Log.i("retrofit_error", e.getMessage());
                    }
                }));
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
}
