package comp5216.sydney.edu.au.assignment2.news;

import android.content.Context;

import androidx.core.content.ContextCompat;

import java.util.ArrayList;

import comp5216.sydney.edu.au.assignment2.R;

public class NewsUtils {
    public static ArrayList<NewsBean> getAllNews(Context context) {
        ArrayList<NewsBean> arrayList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            NewsBean newsBean = new NewsBean();
            newsBean.title = "鸟瞰暴雨后的武汉 全市已转移16万人次";
            newsBean.des = "7月5-6日，武汉普降暴雨-大暴雨，中心城区、蔡甸部分地区出现特大暴雨。江夏大道汤逊湖大桥段，被湖水冲破的路障。记者贾代腾飞 陈卓摄";
            newsBean.icon = ContextCompat.getDrawable(context, R.drawable.icon_male);
            newsBean.news_url = "http://slide.news.sina.com.cn/s/slide_1_2841_101020.html#p=1";
            arrayList.add(newsBean);

            NewsBean newsBean1 = new NewsBean();
            newsBean1.title = "安徽暴雨 三四十条鳄鱼逃至附近农田";
            newsBean1.des = "因强降雨造成内涝，安徽省芜湖市芜湖县花桥镇鳄鱼湖农庄所养鳄鱼逃跑至附近农田。。据悉，溜出来的鳄鱼为散养的扬子鳄，比较温驯。初步预计有三四十条，具体数量未统计，其中最大的约1.8米长。图为网友拍摄到的农田中的鳄鱼。";
            newsBean1.icon = ContextCompat.getDrawable(context, R.drawable.icon_setting_info);
            newsBean1.news_url = "http://slide.news.sina.com.cn/s/slide_1_2841_101024.html#p=1";
            arrayList.add(newsBean1);

            NewsBean newsBean2 = new NewsBean();
            newsBean2.title = "暴雨过后 南京理工大学变“奇幻森林”";
            newsBean2.des = "近日，持续强降雨，导致地势低洼的南京理工大学出现严重积水。这一组几张照片，南理工恍若童话世界中。网友：泡在水中的南理工，也可以倔强地刷出颜值新高度。";
            newsBean2.icon = ContextCompat.getDrawable(context, R.drawable.icon_navigation_home);
            newsBean2.news_url = "http://slide.news.sina.com.cn/s/slide_1_2841_101010.html#p=1";
            arrayList.add(newsBean2);
        }
        return arrayList;
    }
}
