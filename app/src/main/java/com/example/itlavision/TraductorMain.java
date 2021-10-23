package com.example.itlavision;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;


public class TraductorMain extends AppCompatActivity {
    FragmentTranslated fragmentTranslated;
    FragmentList fragmentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentTranslated = new FragmentTranslated();
        fragmentList = new FragmentList();
        getSupportFragmentManager().beginTransaction().add(R.id.Fragment, fragmentTranslated).commit();

    }
}
