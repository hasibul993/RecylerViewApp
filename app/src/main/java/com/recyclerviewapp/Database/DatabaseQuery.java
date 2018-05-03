package com.recyclerviewapp.Database;

import android.content.Context;

import org.apache.commons.lang3.StringUtils;


public class DatabaseQuery extends DatabaseHelper {

    public DatabaseQuery(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }


    public static String GetQueryForSearchSongInSongsDB(String searchText, boolean isLimit) {
        String query = "";
        /*Like %text% will search string in whole name but like text% will start search from first letter*/
        try {
            if (!StringUtils.isBlank(searchText)) {
                if (isLimit)
                    query = SELECT_ALL + TABLE_SONGS + WHERE + COLUMN_NAME + " like '" + searchText + "%' " + LIMIT_8;
                else
                    query = SELECT_ALL + TABLE_SONGS + WHERE + COLUMN_NAME + " like '" + searchText + "%' ";
            } else
                query = SELECT_ALL + TABLE_SONGS + ORDER_BY + COLUMN_NAME + ALPHABETICAL_OREDER;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return query;
    }

}
