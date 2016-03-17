package com.atom.magnus.autologin_utf;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by magnus on 3/9/16.
 */
public class DatabaseHandler extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME= "LoginUtf";

    private static final String TABLE_LOGIN = "Login";

    public static final String KEY_ID = "ID";

    public static final String KEY_RA = "RA";

    public static final String KEY_SENHA = "Senha";


    public DatabaseHandler(Context context) {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USER_TABLE = String.format(
                "CREATE TABLE %s (" +
                        "`%s` INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "`%s` VARCHAR(10), " +
                        "`%s` VARCHAR(200)" +
                        ");",
                TABLE_LOGIN,
                KEY_ID, KEY_RA, KEY_SENHA);
        db.execSQL(CREATE_USER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_LOGIN);

        onCreate(db);
    }


    public void AddUser(User user){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_RA, user.getRA());
        values.put(KEY_SENHA, user.getSenha());

        db.insert(TABLE_LOGIN, null, values);
        db.close();
    }

    public User getUser(int ID){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_LOGIN, new String[] {KEY_ID
                        ,KEY_RA
                        ,KEY_SENHA}
                ,KEY_ID + "=?"
                ,new String[] {String.valueOf(ID)}
                ,null
                ,null
                ,null
                ,null);
        if(cursor != null)
            cursor.moveToFirst();

        User user = new User(Integer.parseInt(cursor.getString(0))
                ,cursor.getString(1)
                ,cursor.getString(2));
        return user;
    }

    public List<User> getLastUser() {

        List<User> userList = new ArrayList<User>();

        String selectQuery = "SELECT  * FROM " + TABLE_LOGIN + " ORDER BY id DESC LIMIT 1";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                user.setID(Integer.parseInt(cursor.getString(0)));
                user.setRA(cursor.getString(1));
                user.setSenha(cursor.getString(2));
                userList.add(user);
            }while (cursor.moveToNext());
        }

        return userList;
    }


    public int getUserCount(){
        String countQuery = "SELECT * FROM " + TABLE_LOGIN;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);


        return  cursor.getCount();
    }

    public int updateUser(User user){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_RA, user.getRA());
        values.put(KEY_SENHA, user.getSenha());

        return db.update(TABLE_LOGIN, values, KEY_ID + "=?"
        ,new String[] {String.valueOf(user.getID())});
    }

    public void deleteUser(User user){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_LOGIN, KEY_ID + "=?"
        ,new String[] {String.valueOf(user.getID())});
        db.close();

    }

}
