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
import android.content.res.Resources;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.ColorRes;
import android.support.annotation.IntDef;
import android.util.Log;

import com.satsumasoftware.flashcards.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class Subject implements Parcelable {

    @IntDef({VARIANT_100, VARIANT_200, VARIANT_300, VARIANT_400, VARIANT_500, VARIANT_600,
            VARIANT_700, VARIANT_800, VARIANT_900})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ColorVariant {}
    public static final int VARIANT_100 = 100, VARIANT_200 = 200, VARIANT_300 = 300,
            VARIANT_400 = 400, VARIANT_500 = 500, VARIANT_600 = 600, VARIANT_700 = 700,
            VARIANT_800 = 800, VARIANT_900 = 900;

    private int mId;
    private String mAbbr, mName, mMduColorIdentifier;


    public Subject(int id, String abbreviation, String name, String mduColorIdentifier) {
        mId = id;
        mAbbr = abbreviation;
        mName = name;
        mMduColorIdentifier = mduColorIdentifier;
    }


    public int getId() {
        return mId;
    }

    public String getAbbreviation() {
        return mAbbr;
    }

    public String getName() {
        return mName;
    }

    public String getMduColorIdentifier() {
        return mMduColorIdentifier;
    }

    public @ColorRes int getColorRes(Context context, @ColorVariant int colorVariant) {
        int resId;
        try {
            // The MDU library must be used in the app for this to work
            resId = context.getResources().getIdentifier(
                    "mdu_" + mMduColorIdentifier + "_" + String.valueOf(colorVariant),
                    "color", context.getPackageName());
        } catch (Resources.NotFoundException e) {
            resId = R.color.mdu_white;
            Log.e("getColorRes", "Failed to resolve color resource");
            e.printStackTrace();
        }
        return resId;
    }


    protected Subject(Parcel in) {
        mId = in.readInt();
        mAbbr = in.readString();
        mName = in.readString();
    }

    public static final Creator<Subject> CREATOR = new Creator<Subject>() {
        @Override
        public Subject createFromParcel(Parcel in) {
            return new Subject(in);
        }

        @Override
        public Subject[] newArray(int size) {
            return new Subject[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeString(mAbbr);
        dest.writeString(mName);
    }
}
