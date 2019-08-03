package me.codeminions.zhizhi.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.geek.thread.GeekThreadManager;
import com.geek.thread.ThreadPriority;
import com.geek.thread.ThreadType;
import com.geek.thread.task.GeekRunnable;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;

import me.codeminions.common.net.RequestResult;
import me.codeminions.common.widget.recycler.RecyclerAdapter;
import me.codeminions.zhizhi.R;
import me.codeminions.common.bean.Answer;
import me.codeminions.zhizhi.helper.AnswerViewHolder;
import me.codeminions.zhizhi.net.AnswerLoad;
import me.codeminions.zhizhi.net.HttpUtil;
import me.codeminions.zhizhi.tools.Constant;
import me.codeminions.zhizhi.tools.MHandler;
import me.codeminions.zhizhi.tools.SpacesItemDecoration;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.Response;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener {

    public static void actionStart(Context context) {
        context.startActivity(new Intent(context, SearchActivity.class));
    }

    private final int GET_SEARCH = 2;
    private final int GET_RESPONSE = 101;

    ImageView back;
    EditText editText;
    String searchContent;

    List<Answer> list;

    FrameLayout frameLayout;
    RecyclerView oldRecycler;
    RecyclerView currentRecycler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        editText = findViewById(R.id.edit_search);
        frameLayout = findViewById(R.id.layout_result);
        back = findViewById(R.id.img_search);

        back.setOnClickListener(this);
        // 设置回车键监听
        // getText必须在监听事件中获得，否则获取不到
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || (event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode() && KeyEvent.ACTION_DOWN == event.getAction())) {

                    searchContent = editText.getText().toString();
                    if (!searchContent.isEmpty()) {
                        editText.setText(searchContent.trim());
                        handler.sendEmptyMessage(GET_SEARCH);
                    }
                }
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_search:
                finish();
                break;
        }
    }

    // TODO: 19-3-3 体验优化：这里可以先加载10，然后填充到布局，再加载20，填充到list末 
    void refreshList() {
        if (currentRecycler != null) {
            oldRecycler = currentRecycler;
            frameLayout.removeView(oldRecycler);
        }

        currentRecycler = new RecyclerView(this);
        currentRecycler.setLayoutManager(new LinearLayoutManager(this));
        // Item间的空隙
        currentRecycler.addItemDecoration(new SpacesItemDecoration(25));

        RecyclerAdapter<Answer> adapter = new RecyclerAdapter<Answer>() {
            @Override
            public int getItemViewType(int position, Answer data) {
                return R.layout.item_view_answer;
            }

            @Override
            public ViewHolder<Answer> onCreateViewHolder(View root, int ViewType) {
                return new AnswerViewHolder(root);
            }
        };
        adapter.setListener(new RecyclerAdapter.AdapterListenerImpl<Answer>() {
            @Override
            public void onItemClick(RecyclerAdapter.ViewHolder holder, Answer data) {
                Log.i("zhizhi-qa", data.getQuestionId()+"/"+ data.getAnswerId());
                AnswerActivity.actionStart(SearchActivity.this, data);
            }
        });
        adapter.replace(list);
        currentRecycler.setAdapter(adapter);

        RecyclerView.LayoutParams lp1 = new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT);
        currentRecycler.setLayoutParams(lp1);

        frameLayout.addView(currentRecycler);
    }


    void requestResult() {
        new AnswerLoad().getSearchAnswer(searchContent)
                .subscribe(new RequestResult<>(new RequestResult.OnRequestResult<List<Answer>>() {
                    @Override
                    public void onSuccess(List<Answer> answer) {
                        list = answer;
                        Log.i("List Size", String.valueOf(list.size()));

                        refreshList();
                    }

                    @Override
                    public void onFail(Throwable e) {
                        Log.i("retrofit_error", e.getMessage());
                    }
                }));
    }

    MHandler handler = new MHandler(this, new MHandler.HandleCallBack() {
        @Override
        public void handleMessage(Message msg, WeakReference reference) {
            switch (msg.what) {
                case GET_SEARCH:
                    if (reference != null) {
                        SearchActivity activity = (SearchActivity) reference.get();
                        activity.requestResult();
                    }
                    break;
            }
        }
    });

}
