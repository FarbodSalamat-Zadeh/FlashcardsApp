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
import com.satsumasoftware.flashcards.framework.flashcard.LanguagesFlashCard;
import com.satsumasoftware.flashcards.framework.flashcard.StandardFlashCard;
import com.satsumasoftware.flashcards.util.CsvUtils;
import com.univocity.parsers.csv.CsvParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CourseTopic implements Topic {

    private int mId;
    private String mIdentifier, mName;
    private Course mCourse;


    public CourseTopic(int id, String identifier, String name, Course course) {
        mId = id;
        mIdentifier = identifier;
        mName = name;
        mCourse = course;
    }


    @Override
    public int getId() {
        return mId;
    }

    @Override
    public String getIdentifier() {
        return mIdentifier;
    }

    @Override
    public String getName() {
        return mName;
    }

    @Override
    public Course getCourse() {
        return mCourse;
    }


    @Override
    public ArrayList<FlashCard> getFlashCards(Context context) {
        ArrayList<FlashCard> flashCards = new ArrayList<>();
        String filename = mCourse.getSubjectIdentifier() + "_" + mCourse.getExamBoardIdentifier() +
                "_" + mIdentifier + ".csv";
        CsvParser parser = CsvUtils.getMyParser();
        try {
            List<String[]> allRows = parser.parseAll(context.getAssets().open(filename));
            for (String[] line : allRows) {
                flashCards.add(cardDetailsFromLine(line));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return flashCards;
    }

    private FlashCard cardDetailsFromLine(String[] line) {
        int id = Integer.parseInt(line[0]);
        switch (mCourse.getType()) {
            case FlashCard.STANDARD:
                // format:  id | question | answer | page_ref | is_paper_2
                boolean isPaper2 = Integer.parseInt(line[4]) == 1;
                return new StandardFlashCard(id, line[1], line[2], Integer.parseInt(line[3]),
                        isPaper2, this);

            case FlashCard.LANGUAGE:
                // format:  id | french_prefix | french | english | tier
                return new LanguagesFlashCard(id, line[1], line[2], line[3],
                        Integer.parseInt(line[4]), this);

            default:
                throw new IllegalArgumentException("the course type identifier '" +
                        mCourse.getType() + "' is invalid");
        }
    }


    protected CourseTopic(Parcel in) {
        mId = in.readInt();
        mIdentifier = in.readString();
        mName = in.readString();
        mCourse = in.readParcelable(Course.class.getClassLoader());
    }

    public static final Parcelable.Creator<CourseTopic> CREATOR = new Parcelable.Creator<CourseTopic>() {
        @Override
        public CourseTopic createFromParcel(Parcel in) {
            return new CourseTopic(in);
        }

        @Override
        public CourseTopic[] newArray(int size) {
            return new CourseTopic[size];
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
