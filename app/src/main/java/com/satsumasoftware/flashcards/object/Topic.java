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
import android.os.Parcel;
import android.os.Parcelable;

import com.satsumasoftware.flashcards.util.CsvUtils;
import com.univocity.parsers.csv.CsvParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Topic implements Parcelable {

    private int mId;
    private String mIdentifier, mName;
    private Course mCourse;

    public Topic(int id, String identifier, String name, Course course) {
        mId = id;
        mIdentifier = identifier;
        mName = name;
        mCourse = course;
    }

    public int getId() {
        return mId;
    }

    public String getIdentifier() {
        return mIdentifier;
    }

    public String getName() {
        return mName;
    }

    public Course getCourse() {
        return mCourse;
    }


    public ArrayList<FlashCard> getFlashCards(Context context) {
        ArrayList<FlashCard> flashCards = new ArrayList<>();
        String filename = mCourse.getSubjectIdentifier() + "_" + mCourse.getExamBoardIdentifier() +
                "_" + mIdentifier + ".csv";
        CsvParser parser = CsvUtils.getMyParser();
        try {
            List<String[]> allRows = parser.parseAll(context.getAssets().open(filename));
            for (String[] line : allRows) {
                // flashcard csv files in the following format:
                //      | id | question | answer | page_ref
                int id = Integer.parseInt(line[0]);
                flashCards.add(new FlashCard(id, line[1], line[2], Integer.parseInt(line[3]), this));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return flashCards;
    }

    
    protected Topic(Parcel in) {
        mId = in.readInt();
        mIdentifier = in.readString();
        mName = in.readString();
        mCourse = in.readParcelable(Course.class.getClassLoader());
    }

    public static final Creator<Topic> CREATOR = new Creator<Topic>() {
        @Override
        public Topic createFromParcel(Parcel in) {
            return new Topic(in);
        }

        @Override
        public Topic[] newArray(int size) {
            return new Topic[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeString(mIdentifier);
        dest.writeString(mName);
        dest.writeParcelable(mCourse, flags);
    }
}