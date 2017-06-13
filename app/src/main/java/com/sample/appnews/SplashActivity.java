package com.sample.appnews;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;

import com.sample.appnews.activity.GuideActivity;
import com.sample.appnews.activity.MainActivity;
import com.sample.appnews.utils.CacheUtils;

public class SplashActivity extends Activity {
    public static final String FIRST_MAIN = "first_main";
    private RelativeLayout rl_splash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
//        rl_splash = (RelativeLayout) findViewById(R.id.rl_splash);
//        //渐变
//        AlphaAnimation aa = new AlphaAnimation(0, 1);
//        //持续时间
//        aa.setDuration(1500);
//        //动画结束后应适用其转换
//        aa.setFillAfter(true);
//        //缩放
//        ScaleAnimation sa = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//        sa.setDuration(1500);
//        sa.setFillAfter(true);
//        //旋转
//        RotateAnimation ra = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//        ra.setDuration(1500);
//        ra.setFillAfter(true);
//        //添加三个动画没有先后顺序
//        AnimationSet set = new AnimationSet(false);
//        set.addAnimation(aa);
//        set.addAnimation(sa);
//        set.addAnimation(ra);
//        rl_splash.setAnimation(set);
//        set.setAnimationListener(new MyAnimationListener());
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }


    class MyAnimationListener implements Animation.AnimationListener {

        //动画结束的时候调用该方法
        @Override
        public void onAnimationEnd(Animation animation) {
            Intent intent;
            //判断是否第一次进入软件
            Boolean isFirst = CacheUtils.getBoolean(SplashActivity.this, FIRST_MAIN);
            if (isFirst) {
                //如果不是第一次进入 跳转到主界面
                intent = new Intent(SplashActivity.this, MainActivity.class);
            } else {
                //如果是第一次进入  跳转到引导界面
                intent = new Intent(SplashActivity.this, GuideActivity.class);
            }
            startActivity(intent);
            //关闭splash页面
            finish();
            // Toast.makeText(SplashActivity.this, "动画播放完成了", Toast.LENGTH_SHORT).show();
        }

        //动画开始的时候调用该方法
        @Override
        public void onAnimationStart(Animation animation) {
        }

        //动画重复播放的时候调用该方法
        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }

}
