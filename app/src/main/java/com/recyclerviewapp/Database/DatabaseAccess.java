package com.recyclerviewapp.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.recyclerviewapp.Model.Model;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Hasib on 05-02-2018.
 */

public class DatabaseAccess extends DatabaseHelper {

    private static String TAG = "DatabaseAccess";

    private static DatabaseAccess databaseAccess = null;


    private DatabaseAccess(Context context) {
        super(context);
    }

    public static DatabaseAccess getInstance(Context context) {
        try {
            if (databaseAccess == null)
                databaseAccess = new DatabaseAccess(context);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return databaseAccess;
    }



    /*below drug insertion*/


    public void InsertSongsInBatchInMasterDB(List<Model> models) {
        SQLiteDatabase db = super.getWritableDatabase();
        db.beginTransaction();
        ContentValues values = new ContentValues();
        try {

            for (Model model : models) {
                try {

                    values.put(COLUMN_NAME, model.getSongName());
                    values.put(COLUMN_ARTIST, model.getArtist());
                    values.put(COLUMN_ALBUM, model.getAlbum());


                    long _id = db.insertWithOnConflict(TABLE_SONGS, null,
                            values, SQLiteDatabase.CONFLICT_IGNORE);

                    if (_id == -1) {

                        db.update(TABLE_SONGS, values, COLUMN_NAME + "= '"
                                + model.getSongName() + "'", null);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            db.setTransactionSuccessful();
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.i(TAG, " InsertSongsInBatchInMasterDB : " + ex.getMessage());
        } finally {
            db.endTransaction();
            values.clear();
            db.close();
        }
    }

    public ArrayList<Model> GetSongList(Cursor cursor) {

        ArrayList<Model> orderItemsArrayList = new ArrayList<>();

        try {

            if (cursor.moveToFirst()) {

                while (!cursor.isAfterLast()) {
                    try {
                        Model drugModel = new Model();

                        drugModel = GetSongModel(cursor);

                        if (drugModel != null)
                            orderItemsArrayList.add(drugModel);

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    cursor.moveToNext();
                }

            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.i(TAG, " GetSongList() : " + ex.getMessage());
        }

        return orderItemsArrayList;
    }

    public Model GetSongModel(Cursor cursor) {

        Model drugModel = new Model();
        try {
            drugModel.setSongName(cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
            drugModel.setArtist(cursor.getString(cursor.getColumnIndex(COLUMN_ARTIST)));
            drugModel.setAlbum(cursor.getString(cursor.getColumnIndex(COLUMN_ALBUM)));
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.i(TAG, " GetSongModel() : " + ex.getMessage());
            drugModel = null;
        }

        return drugModel;
    }

    /*Fetch customer details from order db*/

    public ArrayList<Model> GetSongListFromSongsDB(String searchText, boolean isLimit) {
        SQLiteDatabase db = super.getWritableDatabase();
        Cursor cursor = null;
        ArrayList<Model> models = new ArrayList<>();

        try {

            cursor = db.rawQuery(DatabaseQuery.GetQueryForSearchSongInSongsDB(searchText, isLimit), null);

            if (cursor.getCount() > 0)
                models = GetSongList(cursor);

        } catch (Exception ex) {
            ex.printStackTrace();
            Log.i(TAG, " GetSongListFromSongsDB() : " + ex.getMessage());
        }

        if (cursor != null)
            cursor.close();
        db.close();
        return models;
    }

}
