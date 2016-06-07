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

package com.satsumasoftware.flashcards.framework.flashcard;

import android.os.Parcelable;
import android.support.annotation.StringDef;
import android.text.Spanned;

import com.satsumasoftware.flashcards.framework.topic.Topic;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public interface FlashCard extends Parcelable {

    int getId();

    Spanned getQuestion();

    Spanned getAnswer();

    Topic getTopic();

    @StringDef({STANDARD, LANGUAGE})
    @Retention(RetentionPolicy.SOURCE)
    @interface CourseType {}
    String STANDARD = "standard", LANGUAGE = "language";

    @CourseType String getFlashCardType();

}
