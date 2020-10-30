package comp5216.sydney.edu.au.assignment2.addMeal;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import comp5216.sydney.edu.au.assignment2.R;

public class FoodAdapter extends BaseAdapter {

    private ArrayList<FoodBean> list;
    private Context context;

    //通过构造方法接受要显示的食物数据集合
    public FoodAdapter(Context context, ArrayList<FoodBean> list) {
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
        //1.复用converView优化listview,创建一个view作为getview的返回值用来显示一个条目
        if(convertView != null){
            view = convertView;
        }else {
            //方法一:
            //context:上下文, resource:要转换成view对象的layout的id, root:将layout用root(ViewGroup)包一层作为codify的返回值,一般传null
            view = View.inflate(context, R.layout.view_meal_display, null);
            //将一个布局文件转换成一个view对象

            //方法二:
            //通过LayoutInflater将布局转换成view对象
            //view =  LayoutInflater.from(context).inflate(R.layout.item_news_layout, null);

            //方法三：系统级开发
            //通过context获取系统服务得到一个LayoutInflater，通过LayoutInflater将一个布局转换为view对象
            //LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //view = layoutInflater.inflate(R.layout.item_news_layout, null);

        }

        //2.获取view上的子控件对象
        TextView food_name = (TextView) view.findViewById(R.id.food_name);
        TextView food_intake_calorie = (TextView) view.findViewById(R.id.food_intake_calorie);
        ImageView food_image = (ImageView) view.findViewById(R.id.food_image);

        //3.获取postion位置条目对应的list集合中的新闻数据，Bean对象
        FoodBean usersFood = list.get(position);

        //4.将数据设置给这些子控件做显示
        food_name.setText(usersFood.foodname);
        food_intake_calorie.setText(usersFood.calorie);
        food_image.setImageDrawable(usersFood.icon);
        //设置imageView的图片

        return view;
    }
}
