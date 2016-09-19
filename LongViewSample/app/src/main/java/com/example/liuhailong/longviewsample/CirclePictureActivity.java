package com.example.liuhailong.longviewsample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.liuhailong.longviewsample.view.CirclePictureView;

/*

只是一个圆形的图片功能,后期会加上图片可以根据收拾截取的功能

*/
public class CirclePictureActivity extends AppCompatActivity {

    private CirclePictureView circlePictureView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle_picture);

        circlePictureView = (CirclePictureView) findViewById(R.id.draw_circle);

    }
}
