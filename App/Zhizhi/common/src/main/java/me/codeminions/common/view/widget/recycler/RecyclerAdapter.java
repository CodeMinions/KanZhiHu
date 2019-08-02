package me.codeminions.common.view.widget.recycler;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.codeminions.common.R;

@SuppressWarnings({"unchecked", "unused"})
public abstract class RecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder<T>> implements View.OnClickListener, View.OnLongClickListener, AdapterCallback<T> {
    protected List<T> list;
    private AdapterListener<T> listener;

    // TODO: 19-4-3 第一个Item没有加载出来 可能是getItemCount()的问题

    public RecyclerAdapter() {
        this(null);
    }

    public RecyclerAdapter(AdapterListener<T> listener) {
        this(new ArrayList<T>(), listener);
    }

    public RecyclerAdapter(List<T> list, AdapterListener<T> listener) {
        this.list = list;
        this.listener = listener;
    }


    @Override
    public int getItemViewType(int position) {
        return getItemViewType(position, list.get(position));
    }

    @LayoutRes
    public abstract int getItemViewType(int position, T data);

    @NonNull
    @Override
    public ViewHolder<T> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View root = inflater.inflate(viewType, parent, false);
        ViewHolder<T> holder = onCreateViewHolder(root, viewType);

        root.setOnClickListener(this);
        root.setOnLongClickListener(this);

        root.setTag(R.id.tag_recycler_holder, holder);

        holder.unbinder = ButterKnife.bind(holder, root);
        holder.callback = this;

        return holder;
    }

    public abstract ViewHolder<T> onCreateViewHolder(View root, int ViewType);

    @Override
    public void onBindViewHolder(@NonNull ViewHolder<T> holder, int position) {
        T data = list.get(position);
        holder.bind(data);
    }

    public int getItemCount() {
        return list.size();
    }

    public List<T> getItems() {
        return list;
    }

    public void add(T data) {
        list.add(data);
//        notifyDataSetChanged();
        notifyItemInserted(list.size() - 1);
    }

    public void add(T... dataL) {
        if (dataL != null && dataL.length > 0) {
            int startPos = list.size();
            Collections.addAll(list, dataL);
            notifyItemRangeChanged(startPos, list.size());
        }
    }

    public void add(Collection<T> list) {
        if (list != null && list.size() > 0) {
            int startPos = list.size();
            this.list.addAll(list);
            notifyItemRangeChanged(startPos, list.size());
        }
    }

    public void replace(Collection<T> list) {
        this.list.clear();
        if (list != null && list.size() > 0) {
            this.list.addAll(list);
            notifyDataSetChanged();
        }
    }

    public void clear() {
        list.clear();
        notifyDataSetChanged();
    }


    @Override
    public void onClick(View v) {
        ViewHolder holder = (ViewHolder) v.getTag(R.id.tag_recycler_holder);
        if (listener != null) {
            int pos = holder.getAdapterPosition();
            listener.onItemClick(holder, list.get(pos));
        }
    }

    @Override
    public boolean onLongClick(View v) {
        ViewHolder holder = (ViewHolder) v.getTag(R.id.tag_recycler_holder);
        if (listener != null) {
            int pos = holder.getAdapterPosition();
            listener.onItemLongClick(holder, list.get(pos));
            return true;
        }
        return false;
    }

    public void setListener(AdapterListener<T> listener) {
        this.listener = listener;
    }

    public interface AdapterListener<T> {
        void onItemClick(ViewHolder holder, T data);

        void onItemLongClick(ViewHolder holder, T data);
    }

    @Override
    public void update(T data, ViewHolder<T> holder) {
        int pos = holder.getAdapterPosition();
        if(pos >= 0){
            list.remove(pos);
            list.add(data);
            notifyItemChanged(pos);
        }
    }

    public static abstract class ViewHolder<T> extends RecyclerView.ViewHolder {
        private T data;
        private Unbinder unbinder;
        private AdapterCallback<T> callback;

        public ViewHolder(View view) {
            super(view);
        }

        void bind(T data) {
            this.data = data;
            onBind(data);
        }

        protected abstract void onBind(T data);

        public void update(T data) {
            if (callback != null)
                callback.update(data, this);
        }
    }

    public static abstract class AdapterListenerImpl<T> implements AdapterListener<T> {

        @Override
        public void onItemLongClick(ViewHolder holder, T data) {

        }
    }

}