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
import android.support.annotation.IntDef;
import android.text.Html;
import android.text.Spanned;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class LanguagesFlashCard implements FlashCard {

    @IntDef({GENERAL_VOCAB, FOUNDATION, HIGHER})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Tier {}
    public static final int GENERAL_VOCAB = 0, FOUNDATION = 1, HIGHER = 2;

    private int mId, mTier;
    private String mEnglish, mAnswerPrefix, mAnswer;
    private Topic mTopic;


    public LanguagesFlashCard(int id, String answerPrefix, String answer, String english,
                              int tier, Topic topic) {
        mId = id;
        mEnglish = english;
        mAnswerPrefix = answerPrefix;
        mAnswer = answer;
        mTier = tier;
        mTopic = topic;
    }


    @Override
    public int getId() {
        return mId;
    }

    @Override
    public Spanned getQuestion() {
        return Html.fromHtml(mEnglish);
    }

    @Override
    public Spanned getAnswer() {
        if (mAnswerPrefix != null) {
            return Html.fromHtml("<i>" + mAnswerPrefix + "</i>  " + mAnswer);
        } else {
            return Html.fromHtml(mAnswer);
        }
    }

    public @Tier int getTier() {
        return mTier;
    }

    @Override
    public Topic getTopic() {
        return mTopic;
    }

    @Override
    public String getFlashCardType() {
        return FlashCard.LANGUAGE;
    }


    protected LanguagesFlashCard(Parcel in) {
        mId = in.readInt();
        mEnglish = in.readString();
        mAnswerPrefix = in.readString();
        mAnswer = in.readString();
        mTier = in.readInt();
        mTopic = in.readParcelable(Topic.class.getClassLoader());
    }

    public static final Creator<LanguagesFlashCard> CREATOR = new Creator<LanguagesFlashCard>() {
        @Override
        public LanguagesFlashCard createFromParcel(Parcel in) {
            return new LanguagesFlashCard(in);
        }

        @Override
        public LanguagesFlashCard[] newArray(int size) {
            return new LanguagesFlashCard[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeInt(mTier);
        dest.writeString(mEnglish);
        dest.writeString(mAnswerPrefix);
        dest.writeString(mAnswer);
        dest.writeParcelable(mTopic, flags);
    }
}
