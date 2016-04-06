package com.udacity.stage2.popularmoviess2.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.ExecOnCreate;
import net.simonvt.schematic.annotation.IfNotExists;
import net.simonvt.schematic.annotation.OnConfigure;
import net.simonvt.schematic.annotation.OnCreate;
import net.simonvt.schematic.annotation.OnUpgrade;
import net.simonvt.schematic.annotation.Table;

/**
 * Created by Rashida on 3/8/2016.
 */


@Database(version = MovieDatabase.VERSION,
        packageName = "com.udacity.stage2.popularmoviess2.provider")
public class MovieDatabase {

    private MovieDatabase() {
    }

    public static final int VERSION = 1;

    @Table(MovieColumns.class) public static final String MOVIES = "movies";

    @OnCreate
    public static void onCreate(Context context, SQLiteDatabase db) {
    }

    @OnUpgrade
    public static void onUpgrade(Context context, SQLiteDatabase db, int oldVersion,
                                            int newVersion) {
    }

    @OnConfigure
    public static void onConfigure(SQLiteDatabase db) {
    }

    @ExecOnCreate
    public static final String EXEC_ON_CREATE = "SELECT * FROM " + MOVIES;
}

