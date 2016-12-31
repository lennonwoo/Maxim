package com.lennonwoo.maxim.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lennonwoo.maxim.R;
import com.lennonwoo.maxim.db.MaximDB;
import com.lennonwoo.maxim.model.Maxim;

import java.util.List;

public class MaximListAdapter extends BaseItemDraggableAdapter<Maxim> {
    private Context context;
    public MaximListAdapter(List<Maxim> data, Context context) {
        super(R.layout.card_maxim, data);
        this.context = context;
    }


    @Override
    protected void convert(final BaseViewHolder helper, final Maxim maxim) {
        helper.setText(R.id.maxim, maxim.getMaxim());
        helper.setOnClickListener(R.id.maxim, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialDialog.Builder(context)
                        .title("Update Maxim")
                        .input(null, maxim.getMaxim(), false, new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                            }
                        })
                        .positiveText("OK")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                String s = dialog.getInputEditText().getText().toString();
                                int id = maxim.getId();
                                MaximDB.getInstance(context).updateMaxim(id, s);
                                maxim.setMaxim(s);
                                helper.setText(R.id.maxim, maxim.getMaxim());
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
}
