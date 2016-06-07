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
import android.content.res.Resources;

import com.satsumasoftware.flashcards.framework.Course;

public class ThemeUtils {

    public static int getMduTheme(Context context, Course course) {
        String mduColorIdentifier = course.getSubject(context).getMduColorIdentifier();
        String[] words = mduColorIdentifier.split("_");
        String colorName = "";
        for (String word : words) {
            String firstChar = word.substring(0, 1).toUpperCase();
            String newWord = firstChar + word.substring(1);
            colorName = colorName + newWord;
        }

        try {
            String name = "MduTheme." + colorName;
            return context.getResources().getIdentifier(name, "style", context.getPackageName());
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
            return 0;
        }
    }

}
