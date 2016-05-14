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

package com.satsumasoftware.flashcards.ui;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ProgressBar;

import com.satsumasoftware.flashcards.R;
import com.satsumasoftware.flashcards.db.CourseDbHelper;
import com.satsumasoftware.flashcards.db.ExamBoardDbHelper;
import com.satsumasoftware.flashcards.db.SubjectDbHelper;
import com.satsumasoftware.flashcards.util.PrefUtils;

public class InitializeDbActivity extends AppCompatActivity {

    private static final String LOG_TAG = "InitializeDbActivity";

    private AsyncTask<Void, Boolean, Void> mCurrAsyncTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initialize);

        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setIndeterminate(true);

        final SQLiteOpenHelper[] helpers = new SQLiteOpenHelper[] {
                new CourseDbHelper(this),
                new ExamBoardDbHelper(this),
                new SubjectDbHelper(this)};

        final String[] tables = new String[] {
                CourseDbHelper.TABLE_NAME,
                ExamBoardDbHelper.TABLE_NAME,
                SubjectDbHelper.TABLE_NAME};


        mCurrAsyncTask = new AsyncTask<Void, Boolean, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                for (int i = 0; i < helpers.length-1; i++) {
                    Log.d(LOG_TAG, "background processes - starting loop #" + i);
                    SQLiteOpenHelper helper = helpers[i];
                    SQLiteDatabase db = helper.getReadableDatabase();
                    Cursor cursor = db.query(true, tables[i],
                            null, null, null, null, null, null, null);
                    cursor.moveToFirst();
                    cursor.close();
                    System.gc();

                    Log.d(LOG_TAG, "background processes - running total has been published - " +
                            "loop #" + i + " complete");

                    if (isCancelled()) cancel(true);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                PrefUtils.markDatabaseInitialised(getBaseContext());
                Log.d(LOG_TAG, "Completed initialising DBs");
                startActivity(new Intent(InitializeDbActivity.this, MainActivity.class));
                finish();
            }
        }.execute();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mCurrAsyncTask != null) {
            mCurrAsyncTask.cancel(true);
        }
    }
}
