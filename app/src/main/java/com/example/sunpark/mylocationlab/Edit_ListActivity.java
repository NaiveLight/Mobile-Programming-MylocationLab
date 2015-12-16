package com.example.sunpark.mylocationlab;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

public class Edit_ListActivity extends AppCompatActivity {

    ListView memoListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit__list);

        EditActivity edit = new EditActivity();
        memoListView.setAdapter(edit.baseAdapter);
    }
}
