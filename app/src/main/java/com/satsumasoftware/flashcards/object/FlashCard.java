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

import android.os.Parcel;
import android.os.Parcelable;
import android.text.Html;
import android.text.Spanned;

public class FlashCard implements Parcelable {

    private int mId, mPageRef;
    private String mQuestion, mAnswer;
    private Topic mTopic;


    public FlashCard(int id, String question, String answer, int pageRef, Topic topic) {
        mId = id;
        mQuestion = question;
        mAnswer = answer;
        mPageRef = pageRef;
        mTopic = topic;
    }


    public int getId() {
        return mId;
    }

    public Spanned getQuestion() {
        return Html.fromHtml(mQuestion);
    }

    public Spanned getAnswer() {
        if (mAnswer.equals("#see-guide")) {
            return Html.fromHtml("This answer is too long to fit on a flashcard, but you can " +
                    "check <b>page " + mPageRef + "</b> for details.");
        } else {
            return Html.fromHtml(mAnswer);
        }
    }

    public Topic getTopic() {
        return mTopic;
    }

    public int getPageReference() {
        return mPageRef;
    }


    protected FlashCard(Parcel in) {
        mId = in.readInt();
        mPageRef = in.readInt();
        mQuestion = in.readString();
        mAnswer = in.readString();
        mTopic = in.readParcelable(Topic.class.getClassLoader());
    }

    public static final Creator<FlashCard> CREATOR = new Creator<FlashCard>() {
        @Override
        public FlashCard createFromParcel(Parcel in) {
            return new FlashCard(in);
        }

        @Override
        public FlashCard[] newArray(int size) {
            return new FlashCard[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeInt(mPageRef);
        dest.writeString(mQuestion);
        dest.writeString(mAnswer);
        dest.writeParcelable(mTopic, flags);
    }
}
