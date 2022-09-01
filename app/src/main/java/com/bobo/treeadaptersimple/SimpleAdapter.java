package com.bobo.treeadaptersimple;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bobo.treeadapter.Node;
import com.bobo.treeadapter.TreeRecyclerAdapter;

import java.util.List;

/**
 * @author bo
 * @date 2022/5/24
 */
public class SimpleAdapter extends TreeRecyclerAdapter {

    public SimpleAdapter(RecyclerView mTree, Context context, List<Node> datas, int defaultExpandLevel, int iconExpand, int iconNoExpand) {
        super(mTree, context, datas, defaultExpandLevel, iconExpand, iconNoExpand);
    }

    public SimpleAdapter(RecyclerView mTree, Context context, List<Node> datas, int defaultExpandLevel) {
        super(mTree, context, datas, defaultExpandLevel);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyHoder(View.inflate(mContext, R.layout.item_tree, null));
    }

    @Override
    public void onBindViewHolder(final Node node, RecyclerView.ViewHolder holder, int position) {

        final MyHoder viewHolder = (MyHoder) holder;

        viewHolder.cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setChecked(node, viewHolder.cb.isChecked());
                if (onSelectListener != null)
                    onSelectListener.onSelectListener(node);
            }
        });

        if (node.isChecked()) {
            viewHolder.cb.setChecked(true);
        } else {
            viewHolder.cb.setChecked(false);
        }

        if (node.getIcon() == -1) {
            viewHolder.icon.setVisibility(View.INVISIBLE);
        } else {
            viewHolder.icon.setVisibility(View.VISIBLE);
            viewHolder.icon.setImageResource(node.getIcon());
        }

        viewHolder.label.setText(node.getName());

    }

    class MyHoder extends RecyclerView.ViewHolder {

        public CheckBox cb;

        public TextView label;

        public ImageView icon;

        public MyHoder(View itemView) {
            super(itemView);

            cb = (CheckBox) itemView
                    .findViewById(R.id.cb_select_tree);
            label = (TextView) itemView
                    .findViewById(R.id.id_tree_node_label);
            icon = (ImageView) itemView.findViewById(R.id.icon);

        }

    }

    public OnSelectListener onSelectListener;

    public void setOnSelectListener(OnSelectListener onSelectListener) {
        this.onSelectListener = onSelectListener;
    }

    public interface OnSelectListener {
        void onSelectListener(Node node);
    }

    public <T, B> void setChildrenChecked(Node<T, B> node, boolean checked) {
        node.setChecked(checked);
        for (Node childrenNode : node.getChildren()) {
            setChildChecked(childrenNode, checked);
        }

        if (node.getParent() != null) {
            node.getParent().setChecked(checked);
            if (!checked) {
                for (Object child : node.getParent().getChildren()) {
                    Node childNode = (Node) child;
                    if (childNode.isChecked()){
                        node.getParent().setChecked(childNode.isChecked());
                        break;
                    }
                }
            }
        }
    }
}
