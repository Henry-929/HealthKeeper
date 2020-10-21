package comp5216.sydney.edu.au.assignment2.news;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import comp5216.sydney.edu.au.assignment2.R;

public class NewsAdapter extends BaseAdapter{
    private ArrayList<NewsBean> list;
    private Context context;

    public NewsAdapter(Context context, ArrayList<NewsBean> list) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;

        if(convertView != null){
            view = convertView;
        }else {

            view = View.inflate(context, R.layout.view_main_article, null);//将一个布局文件转换成一个view对象

        }
        ImageView item_img_icon = (ImageView) view.findViewById(R.id.item_img_icon);
        TextView item_tv_des = (TextView) view.findViewById(R.id.item_tv_des);
        TextView item_tv_title = (TextView) view.findViewById(R.id.item_tv_title);

        NewsBean newsBean = list.get(position);

        item_img_icon.setImageDrawable(newsBean.icon);
        item_tv_title.setText(newsBean.title);
        item_tv_des.setText(newsBean.des);

        return view;
    }
}
