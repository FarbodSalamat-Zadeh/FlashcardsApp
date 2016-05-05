/*
 * Copyright 2016 Farbod Salamat-Zadeh
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.satsumasoftware.flashcards.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.satsumasoftware.flashcards.object.Course;
import com.satsumasoftware.flashcards.util.CsvUtils;
import com.univocity.parsers.csv.CsvParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CourseDbHelper extends SQLiteOpenHelper {

    /* General Database and Table information */
    private static final String DATABASE_NAME = "course.db";
    public static final String TABLE_NAME = "courses";
    public static final int DATABASE_VERSION = 2;

    /* All Column Names */
    public static final String COL_ID = "id";
    public static final String COL_SUBJECT_IDENTIFIER = "subject_identifier";
    public static final String COL_EXAM_BOARD_IDENTIFIER = "exam_board_identifier";

    /* SQL CREATE command creates all columns as defined above */
    private static final String SQL_CREATE = "CREATE TABLE " +
            TABLE_NAME + " (" +
            COL_ID + " INTEGER, " +
            COL_SUBJECT_IDENTIFIER + " TEXT, " +
            COL_EXAM_BOARD_IDENTIFIER + " TEXT" +
            ")";

    /* SQL DROP command deletes the SQL table */
    private static final String SQL_DELETE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    private Context mContext;


    public CourseDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE);
        populateDatabase(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE);
        onCreate(db);
    }

    private void populateDatabase(SQLiteDatabase db) {
        CsvParser parser = CsvUtils.getMyParser();
        try {
            List<String[]> allRows = parser.parseAll(mContext.getAssets().open(CsvUtils.COURSES));
            for (String[] line : allRows) {

                ContentValues values = new ContentValues();

                values.put(COL_ID, Integer.parseInt(line[0]));
                values.put(COL_SUBJECT_IDENTIFIER, line[1]);
                values.put(COL_EXAM_BOARD_IDENTIFIER, line[2]);

                db.insert(TABLE_NAME, null, values);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Course> getCourses() {
        Cursor cursor = getReadableDatabase().query(TABLE_NAME,
                null, null, null, null, null, null);

        ArrayList<Course> list = new ArrayList<>();

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String subject = cursor.getString(cursor.getColumnIndex(COL_SUBJECT_IDENTIFIER));
            String examBoard = cursor.getString(cursor.getColumnIndex(COL_EXAM_BOARD_IDENTIFIER));
            list.add(new Course(subject, examBoard));
            cursor.moveToNext();
        }
        cursor.close();

        return list;
    }

}
