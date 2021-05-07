package com.elegion.recyclertest;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

public class MainActivity extends AppCompatActivity implements ContactsAdapter.OnItemClickListener, LoaderManager.LoaderCallbacks<String> {
    public static final int LOADER_MANAGER_ID = 11;
    LoaderManager loaderManager = getLoaderManager();
    String id;
    // добавить фрагмент с recyclerView ---
    // добавить адаптер, холдер и генератор заглушечных данных ---
    // добавить обновление данных и состояние ошибки ---
    // добавить загрузку данных с телефонной книги ---
    // добавить обработку нажатий ---
    // добавить декораторы ---

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, RecyclerFragment.newInstance())
                    .commit();
        }
    }

    @Override
    public void onItemClick(String id) {
      this.id = id;
      loaderManager.restartLoader(LOADER_MANAGER_ID, null,this).forceLoad();
    }

    @Override
    public Loader<String> onCreateLoader(int i, Bundle bundle) {
        return new BackGroundLoader(this, id);
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        if (data != null) {
            startActivity(new Intent(Intent.ACTION_CALL).setData(Uri.parse("tel:" + data)));
        }
        else {
            Toast.makeText(this,"Данного номера не существует", Toast.LENGTH_LONG);
        }
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {
    }
}
