package me.codeminions.zhizhi.frags.firstPage;

import android.util.Log;

import me.codeminions.common.view.app.Fragment;
import me.codeminions.zhizhi.R;

public class HostListFragment extends Fragment {

    @Override
    protected void requestData() {
        Log.i("HostListFragment", "假装自己正在网络请求");
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_hotlist;
    }
}
