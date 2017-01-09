package ly.school.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import ly.school.R;
import ly.school.util.LogUtil;

/**
 * RecyclerView 适配器
 */
public class GongshiAdapter1 extends RecyclerView.Adapter<GongshiAdapter1.ViewHolder> {

    private Map<Integer, List<String>> mlistmore;

    public GongshiAdapter1(Map<Integer, List<String>> mlistmore) {
        this.mlistmore = mlistmore;
    }

    //创建新View，被LayoutManager所调用
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_gongshi, viewGroup, false);
        ViewHolder vh = new ViewHolder(view);

        return vh;
    }


    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
//        if (position < list.size() - 4) {
//            viewHolder.mTextView.setGravity(Gravity.LEFT);
//        } else {
//            viewHolder.mTextView.setGravity(Gravity.RIGHT);
//        }

        viewHolder.text_xn.setText(mlistmore.get(position).get(0).toString());
        viewHolder.text_xq.setText(mlistmore.get(position).get(1).toString());
        viewHolder.text_kcmc.setText(mlistmore.get(position).get(3).toString());
        viewHolder.text_xf.setText(mlistmore.get(position).get(6).toString());
        viewHolder.text_cj.setText(mlistmore.get(position).get(8).toString());
        viewHolder.text_xymc.setText(mlistmore.get(position).get(12).toString());


    }

    //获取数据的数量
    @Override
    public int getItemCount() {
        LogUtil.m("getItemCount"+mlistmore.size());
        return mlistmore.size();
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView text_xn,text_xq,text_kcmc,text_xf,text_cj,text_xymc;

        public ViewHolder(View view) {
            super(view);
            text_xn = (TextView) view.findViewById(R.id.text_xn);
            text_xq = (TextView) view.findViewById(R.id.text_xq);
            text_kcmc = (TextView) view.findViewById(R.id.text_kcmc);
            text_xf = (TextView) view.findViewById(R.id.text_xf);
            text_cj = (TextView) view.findViewById(R.id.text_cj);
            text_xymc = (TextView) view.findViewById(R.id.text_xymc);


        }
    }
}