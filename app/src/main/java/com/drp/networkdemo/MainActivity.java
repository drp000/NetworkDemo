package com.drp.networkdemo;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.drp.network.errorhandler.ExceptionHandle;
import com.drp.network.observer.BaseObserver;
import com.drp.networkdemo.api.GankApi;
import com.drp.networkdemo.api.GankApiInterface;
import com.drp.networkdemo.beans.GankData;
import com.drp.networkdemo.beans.GankItem;
import com.drp.networkdemo.databinding.ActivityMainBinding;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setPresenter(this);
    }

    @SuppressLint("CheckResult")
    public void loadGankData(View view) {
        GankApi.getService(GankApiInterface.class)
                .getGankListByCategory("all", 1, 10)
                .compose(GankApi.getInstance().applySchedulers(new BaseObserver<GankData<List<GankItem>>>() {
                    @Override
                    public void onSuccess(GankData<List<GankItem>> listGankData) {
                        Toast.makeText(MainActivity.this, listGankData.results.toString(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(ExceptionHandle.ResponeThrowable e) {
                        Toast.makeText(MainActivity.this, "onFailure:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }));
    }

}