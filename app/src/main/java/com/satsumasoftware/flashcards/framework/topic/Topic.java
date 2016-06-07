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
import android.os.Parcelable;

import com.satsumasoftware.flashcards.framework.Course;
import com.satsumasoftware.flashcards.framework.flashcard.FlashCard;
import com.satsumasoftware.flashcards.framework.flashcard.LanguagesFlashCard;
import com.satsumasoftware.flashcards.framework.flashcard.StandardFlashCard;

import java.util.ArrayList;

public interface Topic extends Parcelable {

    int getId();

    String getIdentifier();

    String getName();

    Course getCourse();


    ArrayList<FlashCard> getFlashCards(Context context);


    class FlashCardsRetriever {

        public static ArrayList<FlashCard> filterStandardCards(ArrayList<FlashCard> flashCards,
                                                               @StandardFlashCard.ContentType int contentType) {
            ArrayList<FlashCard> filteredCards = new ArrayList<>();
            for (FlashCard flashCard : flashCards) {
                boolean isPaper2 = ((StandardFlashCard) flashCard).isPaper2();
                boolean condition;
                switch (contentType) {
                    case StandardFlashCard.PAPER_1:
                        condition = !isPaper2;
                        break;
                    case StandardFlashCard.PAPER_2:
                        condition = isPaper2;
                        break;
                    case StandardFlashCard.ALL:
                        condition = true;
                        break;
                    default:
                        throw new IllegalArgumentException("content type '" + contentType +
                                "' is invalid");
                }
                if (condition) filteredCards.add(flashCard);
            }
            return filteredCards;
        }

        public static ArrayList<FlashCard> filterLanguagesCards(ArrayList<FlashCard> flashCards,
                                                                @LanguagesFlashCard.Tier int filterTier) {
            ArrayList<FlashCard> filteredCards = new ArrayList<>();
            for (FlashCard flashCard : flashCards) {
                int flashCardTier = ((LanguagesFlashCard) flashCard).getTier();
                if (flashCardTier == filterTier) {
                    filteredCards.add(flashCard);
                }
            }
            return filteredCards;
        }
    }

}
