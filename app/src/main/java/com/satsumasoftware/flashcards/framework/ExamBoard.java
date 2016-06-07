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

package com.satsumasoftware.flashcards.framework;

import android.os.Parcel;
import android.os.Parcelable;

public class ExamBoard implements Parcelable {

    private int mId;
    private String mIdentifier, mName;


    public ExamBoard(int id, String identifier, String name) {
        mId = id;
        mIdentifier = identifier;
        mName = name;
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


    protected ExamBoard(Parcel in) {
        mId = in.readInt();
        mIdentifier = in.readString();
        mName = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeString(mIdentifier);
        dest.writeString(mName);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ExamBoard> CREATOR = new Creator<ExamBoard>() {
        @Override
        public ExamBoard createFromParcel(Parcel in) {
            return new ExamBoard(in);
        }

        @Override
        public ExamBoard[] newArray(int size) {
            return new ExamBoard[size];
        }
    };
}
