package me.codeminions.zhizhi.helper;

import android.view.View;
import android.widget.TextView;

import me.codeminions.common.widget.recycler.RecyclerAdapter;
import me.codeminions.zhizhi.R;
import me.codeminions.common.bean.Answer;

public class AnswerViewHolder extends RecyclerAdapter.ViewHolder<Answer>{
    private TextView item1, item2, item3, item4;
    public AnswerViewHolder(View view){
        super(view);
        item1 = (TextView) view.findViewById(R.id.q);
        item2 = (TextView) view.findViewById(R.id.a);
        item3 = (TextView) view.findViewById(R.id.zan);
        item4 = (TextView) view.findViewById(R.id.ping);
    }

    @Override
    protected void onBind(Answer data) {
        item1.setText(data.getQuestion());
        item2.setText(String.format("%s: %s", data.getAuthor() ,data.getAnswer()));
        item3.setText(String.format("%s 赞同", data.getPraise()));
        item4.setText(String.format("-    %s", data.getComment()));
    }
}
