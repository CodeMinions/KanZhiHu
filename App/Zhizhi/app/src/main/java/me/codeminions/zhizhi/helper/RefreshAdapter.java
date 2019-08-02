package me.codeminions.zhizhi.helper;

import android.support.annotation.NonNull;
import android.view.View;

import me.codeminions.common.view.widget.recycler.RecyclerAdapter;
import me.codeminions.zhizhi.R;
import me.codeminions.zhizhi.bean.Answer;
import me.codeminions.zhizhi.view.DropRefreshRecyclerView.IRefreshHeader;

public class RefreshAdapter extends RecyclerAdapter<Answer> {

    private IRefreshHeader header;

    public void setHeader(IRefreshHeader header) {
        this.header = header;
    }

    @Override
    public int getItemViewType(int position, Answer data) {
        if (position == 0)
            return R.layout.item_refreshheader;
        else
            return R.layout.item_view_answer;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder<Answer> holder, int position) {
        if (position != 0)
            super.onBindViewHolder(holder, position);
    }

    @Override
    public ViewHolder<Answer> onCreateViewHolder(View root, int viewType) {
        if(viewType == R.layout.item_refreshheader)
            return new AnswerViewHolder(header.getHeader());
        else
            return new AnswerViewHolder(root);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

