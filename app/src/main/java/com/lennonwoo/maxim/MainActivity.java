package com.lennonwoo.maxim;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback;
import com.chad.library.adapter.base.listener.OnItemSwipeListener;
import com.lennonwoo.maxim.db.MaximDB;
import com.lennonwoo.maxim.model.Maxim;
import com.lennonwoo.maxim.service.MaximService;
import com.lennonwoo.maxim.ui.MaximListAdapter;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static String TAG = MainActivity.class.getSimpleName();

    RecyclerView recyclerView;
    FloatingActionButton fab;
    MaximDB maximDB;
    List<Maxim> list;
    MaximListAdapter adapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_setting:
                Intent intent = new Intent(this, SettingActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void init() {
        fab = (FloatingActionButton) findViewById(R.id.fab_add);
        recyclerView = (RecyclerView) findViewById(R.id.list_maxim);
        maximDB = MaximDB.getInstance(this);
        list = maximDB.getAllMaxim();
        initAdapter();
        initOnClickListener();
        initService();
    }

    private void initAdapter() {
        OnItemSwipeListener swipeListener = new OnItemSwipeListener() {
            @Override
            public void onItemSwipeStart(RecyclerView.ViewHolder viewHolder, int i) {
            }

            @Override
            public void clearView(RecyclerView.ViewHolder viewHolder, int i) {
            }

            @Override
            public void onItemSwiped(RecyclerView.ViewHolder viewHolder, int i) {
                Maxim removeMaxim = adapter.getItem(i);
                maximDB.deleteMaxim(removeMaxim.getId());
            }

            @Override
            public void onItemSwipeMoving(Canvas canvas, RecyclerView.ViewHolder viewHolder, float v, float v1, boolean b) {
                Paint paint = new Paint();
                paint.setTextSize(40);
                paint.setColor(Color.RED);
                canvas.drawText("Remove this maxim", 0, 80, paint);
            }
        };
        adapter = new MaximListAdapter(list, this);

        ItemDragAndSwipeCallback itemDragAndSwipeCallback = new ItemDragAndSwipeCallback(adapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemDragAndSwipeCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        adapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        adapter.enableSwipeItem();
        adapter.setOnItemSwipeListener(swipeListener);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void initOnClickListener() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialDialog.Builder(MainActivity.this)
                        .title("New Maxim")
                        .input(null, null, false, new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                            }
                        })
                        .positiveText("OK")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                String s = dialog.getInputEditText().getText().toString();
                                int id = 1;
                                if (list.size() != 0)
                                    id = list.get(list.size() - 1).getId() + 1;
                                Maxim m = new Maxim(s, id);
                                list.add(m);
                                adapter.notifyDataSetChanged();
                                maximDB.saveMaxim(s);
                            }
                        })
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                dialog.dismiss();
                            }
                        })
                        .negativeText("Cancel")
                        .show();
            }
        });
    }

    private void initService() {
        Log.d(TAG, "Init Service");
        Intent intent = new Intent(this, MaximService.class);
        startService(intent);
    }
}
