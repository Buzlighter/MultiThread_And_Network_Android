package com.example.coursera_task;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class MainActivity extends AppCompatActivity implements CompaniesAdapter.OnItemListener {
    static final int icon_pic = R.id.action_add_pic;
    static final int icon_text = R.id.action_add_text;

    private RecyclerView recyclerView;
    public static List<Object> items = new ArrayList<>();
    private CompaniesAdapter companiesAdapter;
    private Toolbar toolbar;
    private static int imageIndex = 1;
    private static int textIndex = 1;
    public static final String[] arrOfStrings =
            {"AMAZON", "APPLE", "FACEBOOK", "FORD",
                    "GOOGLE", "HP", "INTEL", "MASTERCARD",
                    "NVIDIA", "SAMSUNG", "SONY", "TOYOTA", "WALMART"};
    private TypedArray typedArr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.rv_list);
        toolbar = findViewById(R.id.toolbar);

        typedArr = getResources().obtainTypedArray(R.array.array_companies);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Company List");
    }

    @Override
    protected void onResume() {
        super.onResume();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        fillListDraw(typedArr, 0);
        fillListString(0);
        adapt();
    }

    private void adapt() {
        companiesAdapter = new CompaniesAdapter(items, this, this);
        recyclerView.setAdapter(companiesAdapter);
        companiesAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tool_bar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case icon_pic:
                fillListDraw(typedArr, imageIndex);
                imageIndex++;
                companiesAdapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(companiesAdapter.getItemCount() - 1);
                break;
            case icon_text:
                fillListString(textIndex);
                textIndex++;
                companiesAdapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(companiesAdapter.getItemCount() - 1);
                break;
        }
        return true;
    }

    public void fillListDraw(TypedArray arr, int index) {
        if (index < arr.length()) {
            int element = arr.getResourceId(index, -1);
            items.add(element);
        }
        else {
            imageIndex = 0;
        }
    }

    public void fillListString (int index){
        if (index < arrOfStrings.length) {
            items.add(arrOfStrings[index]);
        }
        else {
            textIndex = 0;
        }
    }

    @Override
    public void onItemClick(int position) {
        items.remove(position);
        companiesAdapter.notifyDataSetChanged();
    }
}
