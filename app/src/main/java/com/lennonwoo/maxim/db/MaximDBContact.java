package com.lennonwoo.maxim.db;

import android.provider.BaseColumns;

public class MaximDBContact {

    public static abstract class MaximTable implements BaseColumns {
        public static final String TABLE_NAME = "maxim";
        public static final String COLUMN_NAME_CONTENT = "content";
    }
}
