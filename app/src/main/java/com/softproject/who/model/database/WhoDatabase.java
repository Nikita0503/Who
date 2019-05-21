package com.softproject.who.model.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;

import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;

public class WhoDatabase {
    private DBHelper mDBHelper;
    private SQLiteDatabase mDatabase;

    public WhoDatabase(Context context) {
        mDBHelper = new DBHelper(context);
        mDatabase = mDBHelper.getWritableDatabase();
    }

    public Single<ArrayList<Integer>> getApiIds(){
        return Single.create(new SingleOnSubscribe<ArrayList<Integer>>() {
            @Override
            public void subscribe(SingleEmitter<ArrayList<Integer>> e) throws Exception {
                Log.d("DB", "--- Rows in mytable: ---");
                // делаем запрос всех данных из таблицы mytable, получаем Cursor
                Cursor c = mDatabase.query("whotable", null, null, null, null, null, null);
                ArrayList<Integer> listIds = new ArrayList<Integer>();
                // ставим позицию курсора на первую строку выборки
                // если в выборке нет строк, вернется false
                if (c.moveToFirst()) {
                    // определяем номера столбцов по имени в выборке
                    int idColIndex = c.getColumnIndex("id");
                    int apiIdColIndex = c.getColumnIndex("api_id");
                    do {
                        int id = c.getInt(idColIndex);
                        int apiId = c.getInt(apiIdColIndex);
                        // получаем значения по номерам столбцов и пишем все в лог
                        Log.d("whoDB",
                                "ID = " + id +
                                        ", apiID = " + apiId);
                        listIds.add(apiId);
                        // переход на следующую строку
                        // а если следующей нет (текущая - последняя), то false - выходим из цикла
                    } while (c.moveToNext());
                } else
                    Log.d("DB", "0 rows");
                c.close();
            }
        });
    }

    public Completable addNewApiId(final int apiId) {
        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(CompletableEmitter e) throws Exception {
                ContentValues cv = new ContentValues();
                cv.put("apiID", apiId);
                long rowID = mDatabase.insert("whotable", null, cv);
                Log.d("DB", "row inserted, ID = " + rowID);
                e.onComplete();
            }
        });
    }
}
