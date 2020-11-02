package comp5216.sydney.edu.au.assignment2.addMeal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

import comp5216.sydney.edu.au.assignment2.R;

public class FoodAdapter extends BaseAdapter {

    private ArrayList<UsersFood> list;
    private Context context;

    //通过构造方法接受要显示的食物数据集合
    public FoodAdapter(Context context, ArrayList<UsersFood> list) {
        this.list = list;
        this.context = context;
    }

    public void addfood(UsersFood usersFood) {
        if (list == null) {
            list = new ArrayList<UsersFood>();
        }
        list.add(usersFood);
        notifyDataSetChanged();
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
        //1.复用converView优化listview,创建一个view作为getview的返回值用来显示一个条目
        if(convertView == null){
            //方法一:
            //context:上下文, resource:要转换成view对象的layout的id, root:将layout用root(ViewGroup)包一层作为codify的返回值,一般传null
            convertView = LayoutInflater.from(context).inflate(R.layout.view_meal_display,parent,false);
            //将一个布局文件转换成一个view对象

            //方法二:
            //通过LayoutInflater将布局转换成view对象
            //view =  LayoutInflater.from(context).inflate(R.layout.item_news_layout, null);

            //方法三：系统级开发
            //通过context获取系统服务得到一个LayoutInflater，通过LayoutInflater将一个布局转换为view对象
            //LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //view = layoutInflater.inflate(R.layout.item_news_layout, null);

        }

        //3.获取postion位置条目对应的list集合中的新闻数据，Bean对象
        UsersFood usersFood = list.get(position);

        //2.获取view上的子控件对象
        TextView food_name = (TextView) convertView.findViewById(R.id.food_name);
        TextView food_intake_calorie = (TextView) convertView.findViewById(R.id.food_intake_calorie);
        ImageView food_image = (ImageView) convertView.findViewById(R.id.food_image);

        //4.将数据设置给这些子控件做显示
        food_name.setText(usersFood.getFoodname());
        food_intake_calorie.setText(usersFood.getCalorie());
        food_image.setImageBitmap(usersFood.getIcon());
        //设置imageView的图片

        return convertView;
    }
}
