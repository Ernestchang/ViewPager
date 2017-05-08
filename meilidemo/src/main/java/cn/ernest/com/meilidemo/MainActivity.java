package cn.ernest.com.meilidemo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ernest.com.meilidemo.bean.MeiShowBean;
import cn.ernest.com.meilidemo.dao.ComPresenter;
import cn.youngkaaa.yviewpager.YFragmentPagerAdapter;
import cn.youngkaaa.yviewpager.YViewPager;
import rx.functions.Action1;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.viewpager)
    YViewPager viewpager;
    @BindView(R.id.back_btn)
    ImageView backBtn;

    public RequestManager mImageLoader;
    private List<FragmentInner> mFragments;

    private ArrayList<MeiShowBean.DataBean> mDatas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_daily_list);
        ButterKnife.bind(this);

        Logger.e("MainActivity getDatas");
        Log.e("ernest", "MainActivity");

        initFragments();
        getDatas();
        viewpager.setAdapter(new FragmentAdapter(getSupportFragmentManager()));
        viewpager.setCurrentItem(0);

    }

    private void initFragments() {
        mFragments = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            FragmentInner fragment = new FragmentInner();
            mFragments.add(fragment);
        }
    }

    private void getDatas() {
        Log.e("ernest", "MainActivity");

        Logger.e("getDatas");
        ComPresenter.getMeiShow().subscribe(new Action1<MeiShowBean>() {
            @Override
            public void call(MeiShowBean meiShowBean) {
                Log.e("ernest", "meiShowBean.success:" + meiShowBean.success);

                Logger.e("meiShowBean.success:" + meiShowBean.success);

                if (meiShowBean.success) {
                    processData(meiShowBean.data);
                }
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                Log.e("ernest", "throwable:" + throwable.toString());

            }
        });
    }

    private void processData(List<MeiShowBean.DataBean> data) {
        mDatas = new ArrayList<>();
        mDatas.addAll(data);
//        for (int i = 0; i < mFragments.size(); i++) {
//            mFragments.get(i).mData = mDatas.get(i);
//
//        }
        int current = viewpager.getCurrentItem();
        mFragments.get(current).refreshData(mDatas.get(current));

    }


    class FragmentAdapter extends YFragmentPagerAdapter {

        public FragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (mDatas != null && mDatas.size() > (position + 1)) {
//
//                Logger.object(mDatas.get(position));
//                mFragments.get(position).refreshData(mDatas.get(position));
                mFragments.get(position).mData = mDatas.get(position);
            }

            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }


    }

    public synchronized RequestManager getImageLoader() {
        if (mImageLoader == null)
            mImageLoader = Glide.with(this);
        return mImageLoader;
    }
}
