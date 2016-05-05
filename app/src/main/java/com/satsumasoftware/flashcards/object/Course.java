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

package com.satsumasoftware.flashcards.object;

import android.content.Context;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.satsumasoftware.flashcards.db.ExamBoardDbHelper;
import com.satsumasoftware.flashcards.db.SubjectDbHelper;
import com.satsumasoftware.flashcards.util.CsvUtils;
import com.univocity.parsers.csv.CsvParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Course implements Parcelable {

    private String mSubjectIdentifier, mBoardIdentifier;

    public Course(String subject, String examBoard) {
        mSubjectIdentifier = subject;
        mBoardIdentifier = examBoard;
    }


    public String getSubjectIdentifier() {
        return mSubjectIdentifier;
    }

    public String getExamBoardIdentifier() {
        return mBoardIdentifier;
    }

    public Subject getSubject(Context context) {
        SubjectDbHelper dbHelper = new SubjectDbHelper(context);
        Cursor cursor = dbHelper.getReadableDatabase().query(
                SubjectDbHelper.TABLE_NAME,
                null,
                SubjectDbHelper.COL_IDENTIFIER + "=?",
                new String[] {mSubjectIdentifier},
                null, null, null);
        cursor.moveToFirst();
        int id = cursor.getInt(cursor.getColumnIndex(SubjectDbHelper.COL_ID));
        String name = cursor.getString(cursor.getColumnIndex(SubjectDbHelper.COL_NAME));
        String colorIdentifier = cursor.getString(cursor.getColumnIndex(SubjectDbHelper.COL_MDU_COLOR_IDENTIFIER));
        cursor.close();

        return new Subject(id, mSubjectIdentifier, name, colorIdentifier);
    }

    public ExamBoard getExamBoard(Context context) {
        ExamBoardDbHelper dbHelper = new ExamBoardDbHelper(context);
        Cursor cursor = dbHelper.getReadableDatabase().query(
                ExamBoardDbHelper.TABLE_NAME,
                null,
                ExamBoardDbHelper.COL_IDENTIFIER + "=?",
                new String[] {mBoardIdentifier},
                null, null, null);
        cursor.moveToFirst();
        int id = cursor.getInt(cursor.getColumnIndex(ExamBoardDbHelper.COL_ID));
        String name = cursor.getString(cursor.getColumnIndex(ExamBoardDbHelper.COL_NAME));
        cursor.close();

        return new ExamBoard(id, mBoardIdentifier, name);
    }

    public ArrayList<Topic> getTopics(Context context) {
        ArrayList<Topic> topics = new ArrayList<>();
        String filename = mSubjectIdentifier + "_" + mBoardIdentifier + "_topics.csv";
        CsvParser parser = CsvUtils.getMyParser();
        try {
            List<String[]> allRows = parser.parseAll(context.getAssets().open(filename));
            for (String[] line : allRows) {
                // topics csv files in the following format:
                //      | id | identifier | name
                int id = Integer.parseInt(line[0]);
                topics.add(new Topic(id, line[1], line[2], this));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return topics;
    }


    protected Course(Parcel in) {
        mSubjectIdentifier = in.readString();
        mBoardIdentifier = in.readString();
    }

    public static final Creator<Course> CREATOR = new Creator<Course>() {
        @Override
        public Course createFromParcel(Parcel in) {
            return new Course(in);
        }

        @Override
        public Course[] newArray(int size) {
            return new Course[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mSubjectIdentifier);
        dest.writeString(mBoardIdentifier);
    }
}
