package com.example.ggxiaozhi.rxjava;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.jakewharton.rxbinding2.view.RxView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class Main3Activity extends AppCompatActivity {
    private static final String TAG = "Main3Activity";
    private Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        mButton = (Button) findViewById(R.id.btn);
//        RxView.clicks(mButton).throttleFirst(2, TimeUnit.SECONDS).subscribe(new Consumer<Object>() {
//            @Override
//            public void accept(Object o) throws Exception {
//                SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                String dateStr = dateformat.format(System.currentTimeMillis());
//                Log.d(TAG, "accept: "+dateStr);
//            }
//        });

        //        Observable.zip(Observable.just(1), Observable.just("a"), new BiFunction<Integer, String, String>() {
//            @Override
//            public String apply(Integer integer, String s) throws Exception {
//                return null;
//            }
//        });
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                /*两者合并*/
                Observable.merge(getObervableLocal(), getObervableWeb()).subscribeOn(Schedulers.io()).
                        observeOn(AndroidSchedulers.mainThread()).
                        subscribe(new Consumer<List<User>>() {
                            @Override
                            public void accept(List<User> users) throws Exception {
//                                for (int i = 0; i <users.size() ; i++) {
//                                    Log.d(TAG, "accept: "+users.get(i).getName());
//                                }
                                Log.d(TAG, "accept: "+users);
                            }
                        });
            }
        });
    }
    /**
     * 本地数据(从数据库中取)
     *
     * @return 返回本地数据(手机端)的购物车信息
     */
    public Observable<List<User>> getObervableLocal() {
        //用User对象代表商品实例
        //我们用手机购买一般数据会缓存到手机的数据库当中
        User user = new User("裤子", "16元");
        User user1 = new User("衬衫", "18元");

        List<User> mList = new ArrayList<>();
        mList.add(user);
        mList.add(user1);
        return Observable.just(mList);
    }

    /**
     * web数据(从网络中请求)
     *
     * @return 返回网络数据
     */
    public Observable<List<User>> getObervableWeb() {
        //网络请求 查看服务器购物车是否有数据
        User user = new User("衣服", "20元");
        User user1 = new User("裤子", "22元");

        List<User> mList = new ArrayList<>();//模拟请求返回的数据
        mList.add(user);
        mList.add(user1);
        return Observable.fromArray(mList).subscribeOn(Schedulers.io());//网络请求在子线程
    }


}
