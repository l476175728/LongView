package com.example.liuhailong.longviewsample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.liuhailong.longviewsample.view.GuaGuaKaView;

public class GuaGuaKaActivity extends AppCompatActivity {

    private GuaGuaKaView guaGuaKaView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gua_gua_ka);
        guaGuaKaView = (GuaGuaKaView) findViewById(R.id.guaguaka_view);
    }
}
