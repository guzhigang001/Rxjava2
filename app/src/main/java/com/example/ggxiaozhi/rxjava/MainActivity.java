package com.example.ggxiaozhi.rxjava;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.net.HttpURLConnection;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private Button mButton;

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mButton = (Button) findViewById(R.id.btn);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Observable<String> observable = getObservable();
                Observer<String> observer = getObserver();
              /*  Observable.create(new ObservableOnSubscribe<String>() {
                    @Override
                    public void subscribe(ObservableEmitter<String> e) throws Exception {
                        //在这里我们进行网络请求 请求结果返回一个字符串
                        e.onNext("网络请求结果");
                    }
                }).subscribeOn(Schedulers.io()).
                        observeOn(AndroidSchedulers.mainThread()).
                        subscribe(new Consumer<String>() {
                            @Override
                            public void accept(String s) throws Exception {
                                Log.d(TAG, "accept: " + s);
                            }
                        });*/

                Observable<Integer> just = Observable.just(1);

                Observable<String> map = just.map(new Function<Integer, String>() {
                    /**
                     * map返回的也是一个Observable<String>
                     * @param integer 传入的类型
                     * @return 返回结果为字符串
                     */
                    @Override
                    public String apply(Integer integer) throws Exception {

                        return integer + "value";
                    }
                });
                map.subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.d(TAG, "accept: " + s);
                    }
                });
            }
        });

        int userId = 111111;
        Observable.just(userId).flatMap(new Function<Integer, ObservableSource<Result>>() {
            @Override
            public ObservableSource<Result> apply(Integer integer) throws Exception {
                /**
                 *通过userId登录
                 * 登录成功返回结果result
                 */
                Result result = null;//这里是通过传入的userId作为请求参数请求网络返回用户信息
                return Observable.just(result);
            }
        }).flatMap(new Function<Result, ObservableSource<User>>() {
            @Override
            public ObservableSource<User> apply(Result result) throws Exception {
                /**
                 * 根据返回的登录结果result
                 * 请求包含用户的帐号 年龄等个人信息User
                 */
                final User user = null;//这里是通过传入的result作为请求参数请求网络返回用户信息其他信息
                return Observable.create(new ObservableOnSubscribe<User>() {
                    @Override
                    public void subscribe(ObservableEmitter<User> e) throws Exception {
                        e.onNext(user);
                    }
                });
            }
        }).subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Consumer<User>() {
                    @Override
                    public void accept(User user) throws Exception {
                        /**
                         * 通过返回的User等到name更新UI
                         */
                        Log.d(TAG, "accept: " + user.getName());
                    }
                });
    }

    public Observable<String> getObservable() {
//        return Observable.create(new ObservableOnSubscribe<String>() {
//            @Override
//            public void subscribe(ObservableEmitter<String> e) throws Exception {
//                e.onNext("吃饭");
//                e.onNext("睡觉");
//                e.onNext("打豆豆");
//                e.onComplete();
////                e.onError(new Throwable("错误"));
//            }
//        });
//        String[] strings={"2","2","2"};
//        return Observable.fromArray(strings);

        return Observable.just("吃饭", "睡觉", "打豆豆");
    }

    Disposable dd;

    public Observer<String> getObserver() {

        return new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                dd = d;
                Log.d(TAG, "onSubscribe: ");
            }

            @Override
            public void onNext(String value) {
                if ("睡觉".equals(value)) {
                    dd.dispose();
                }
                Log.d(TAG, "onNext: " + value);
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError: " + e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete: ");
            }
        };
    }
}
