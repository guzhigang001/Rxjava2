package com.example.ggxiaozhi.rxjava;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.jakewharton.rxbinding2.widget.RxTextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

public class Main2Activity extends AppCompatActivity {
    private static final String TAG = "Main2Activity";
    private AutoCompleteTextView mView;

    private List<String> strs = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        mView = (AutoCompleteTextView) findViewById(R.id.auto);

        RxTextView.textChanges(mView)//通过Rxbinding的textChanges()监听AutoCompleteTextView文字的变化,他是一个Obervable对象
                .debounce(200, TimeUnit.MILLISECONDS)//利用debounce操作符延迟发送 TimeUnit.MILLISECONDS(毫秒)指定一个参数的单位
                .subscribeOn(AndroidSchedulers.mainThread())//通过Rxbinding监听控件必须在主线程
                .filter(new Predicate<CharSequence>() {
                    @Override
                    public boolean test(CharSequence charSequence) throws Exception {
                        //过滤为空的数据 避免多余请求
                        return charSequence.toString().trim().length() > 0;
                    }
                })
                .switchMap(new Function<CharSequence, ObservableSource<List<String>>>() {
                    @Override
                    public ObservableSource<List<String>> apply(CharSequence charSequence) throws Exception {
                        Log.d(TAG, "apply: " + charSequence.toString());
                        String request = charSequence.toString().trim();//输入的内容
                        /**
                         * 通过输入的内容request发起网络请求
                         * 返回一个模糊匹配的字符串集合用于显示
                         * 这里我们构建一个假的数据
                         */
                        List<String> mList = new ArrayList<String>();
                        mList.add("abc");
                        mList.add("abd");
                        mList.add("abop");
                        mList.add("ac");
                        return Observable.just(mList);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<String>>() {
                    @Override
                    public void accept(List<String> strings) throws Exception {
                        Log.d(TAG, "accept: " + strings.toString());
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                                (getApplicationContext(), android.R.layout.simple_list_item_1, android.R.id.text1, strings);
                        mView.setAdapter(adapter);
                    }
                });
    }
}
