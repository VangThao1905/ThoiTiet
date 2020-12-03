package vangthao.app.thoitiet.model.users;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class SQLiteDatabasKeepLogin extends SQLiteOpenHelper {
    public SQLiteDatabasKeepLogin(@Nullable Context context, @Nullable String name, @Nullable android.database.sqlite.SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    //truy van khong can tra ve ket qua
    public void QueryData(String sql){
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL(sql);
    }
    //truy vấn có trả kết quả:SELECT
    public Cursor GetData(String sql){
        SQLiteDatabase database = getReadableDatabase();
        return database.rawQuery(sql,null);
    }

    @Override
    public void onCreate(android.database.sqlite.SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(android.database.sqlite.SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
