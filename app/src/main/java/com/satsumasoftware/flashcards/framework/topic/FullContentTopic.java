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

package com.satsumasoftware.flashcards.framework.topic;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.satsumasoftware.flashcards.framework.Course;
import com.satsumasoftware.flashcards.framework.flashcard.FlashCard;

import java.util.ArrayList;

public class FullContentTopic implements Topic {

    private Course mCourse;

    public FullContentTopic(Course course) {
        mCourse = course;
    }


    @Override
    public int getId() {
        return 0;
    }

    @Override
    public String getIdentifier() {
        return "0-all";
    }

    @Override
    public String getName() {
        return "All subject content";
    }

    @Override
    public Course getCourse() {
        return mCourse;
    }

    @Override
    public ArrayList<FlashCard> getFlashCards(Context context) {
        ArrayList<FlashCard> allFlashCards = new ArrayList<>();
        for (Topic courseTopic : mCourse.getTopics(context)) {
            ArrayList<FlashCard> topicFlashCards = courseTopic.getFlashCards(context);
            allFlashCards.addAll(topicFlashCards);
        }
        return allFlashCards;
    }


    protected FullContentTopic(Parcel in) {
        mCourse = in.readParcelable(Course.class.getClassLoader());
    }

    public static final Parcelable.Creator<FullContentTopic> CREATOR = new Parcelable.Creator<FullContentTopic>() {
        @Override
        public FullContentTopic createFromParcel(Parcel in) {
            return new FullContentTopic(in);
        }

        @Override
        public FullContentTopic[] newArray(int size) {
            return new FullContentTopic[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(mCourse, flags);
    }
}
