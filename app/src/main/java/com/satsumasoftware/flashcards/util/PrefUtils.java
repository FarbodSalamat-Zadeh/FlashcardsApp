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

package com.satsumasoftware.flashcards.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.satsumasoftware.flashcards.db.CourseDbHelper;
import com.satsumasoftware.flashcards.db.ExamBoardDbHelper;
import com.satsumasoftware.flashcards.db.SubjectDbHelper;

public final class PrefUtils {

    // Boolean indicating whether the one-time welcome has been done
    // (there will be a different preferences string value for different versions of the
    // database as preferences are not reset on app update)
    public static final String PREF_DATABASE_INITIALISED = "pref_database_initialised_" +
            CourseDbHelper.DATABASE_VERSION +
            ExamBoardDbHelper.DATABASE_VERSION +
            SubjectDbHelper.DATABASE_VERSION;

    public static boolean hasDatabaseInitialised(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getBoolean(PREF_DATABASE_INITIALISED, false);
    }

    public static void markDatabaseInitialised(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putBoolean(PREF_DATABASE_INITIALISED, true).apply();
    }


    public static final String PREF_FLASHCARD_DARK_BKGD = "pref_cardActivity_darkBkgd";

    public static boolean useDarkFlashcardBackground(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getBoolean(PREF_FLASHCARD_DARK_BKGD, true);
    }



}
