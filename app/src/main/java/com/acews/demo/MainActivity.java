package com.acews.demo;

import android.os.Bundle;
import android.view.View;

import com.acews.sdk.jni.CustomHorizontalPicker;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList<String> arrayList = new ArrayList<>();
        for (int i = 0; i <= 30; i++) {
            arrayList.add(String.valueOf(i));
        }

        CustomHorizontalPicker horizontalPicker = findViewById(R.id.horizontal_picker);
        horizontalPicker.setData(arrayList);
        horizontalPicker.setCurrentIndex(5);
        horizontalPicker.setOnValueChangeListener(new CustomHorizontalPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(View view, int position) {

            }
        });

    }

}
