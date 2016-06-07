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

package com.satsumasoftware.flashcards.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.ArrayRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import com.satsumasoftware.flashcards.R;
import com.satsumasoftware.flashcards.framework.FlashCard;
import com.satsumasoftware.flashcards.framework.Topic;
import com.satsumasoftware.flashcards.util.ThemeUtils;
import com.satsuware.usefulviews.LabelledSpinner;

import java.util.ArrayList;
import java.util.Collections;

public class TopicDetailActivity extends AppCompatActivity {

    protected static final String EXTRA_TOPIC = "extra_topic";
    private Topic mTopic;

    private ArrayList<FlashCard> mFilteredCards;
    private int mSelectedNumCards;

    private LabelledSpinner mCardNumSpinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mTopic = getIntent().getParcelableExtra(EXTRA_TOPIC);
        if (mTopic == null) {
            throw new NullPointerException("parcelable object received from intent is null");
        }
        setTheme(ThemeUtils.getMduTheme(this, mTopic.getCourse()));
        setContentView(R.layout.activity_topic_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        assert toolbar != null;
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        TextView subjectText = (TextView) findViewById(R.id.subject_text);
        assert subjectText != null;
        TextView topicText = (TextView) findViewById(R.id.topic_text);
        assert topicText != null;

        subjectText.setText(mTopic.getCourse().getSubject(this).getName());
        topicText.setText(mTopic.getName());


        mCardNumSpinner = (LabelledSpinner) findViewById(R.id.spinner_cards);
        assert mCardNumSpinner != null;

        mFilteredCards = mTopic.getFlashCards(this);
        updateCardNumSpinner(mFilteredCards);

        LabelledSpinner spinnerContent = (LabelledSpinner) findViewById(R.id.spinner_content);
        assert spinnerContent != null;

        spinnerContent.setItemsArray(getCardContentOptions());
        spinnerContent.setOnItemChosenListener(new LabelledSpinner.OnItemChosenListener() {
            @Override
            public void onItemChosen(View labelledSpinner, AdapterView<?> adapterView, View itemView, int position, long id) {
                String courseType = mTopic.getCourse().getType();
                ArrayList<FlashCard> allFlashCards = mTopic.getFlashCards(TopicDetailActivity.this);
                switch (courseType) {
                    case FlashCard.STANDARD:
                        // 'position' corresponds to the ContentType values from FlashCardsRetriever
                        mFilteredCards = Topic.FlashCardsRetriever.filterStandardCards(
                                allFlashCards, position);
                        break;
                    case FlashCard.LANGUAGE:
                        // again, 'position' corresponds to the Tier values in LanguagesFlashCard
                        mFilteredCards = Topic.FlashCardsRetriever.filterLanguagesCards(
                                allFlashCards, position);
                        break;
                    default:
                        throw new IllegalArgumentException("the course type identifier '" +
                                courseType + "' is invalid");
                }
                Collections.shuffle(mFilteredCards);
                updateCardNumSpinner(mFilteredCards);
            }

            @Override
            public void onNothingChosen(View labelledSpinner, AdapterView<?> adapterView) {}
        });


        Button button = (Button) findViewById(R.id.button);
        assert button != null;

        if (mTopic.getFlashCards(this).size() == 0) {
            ViewGroup optionsGroup = (ViewGroup) findViewById(R.id.viewGroup_options);
            assert optionsGroup != null;
            optionsGroup.setVisibility(View.GONE);
            button.setText(R.string.coming_soon);
            button.setEnabled(false);
            return;
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mFilteredCards.size() == 0) {
                    new AlertDialog.Builder(TopicDetailActivity.this)
                            .setTitle("No flashcards here!")
                            .setMessage("There are no flashcards available for the options you have chosen. " +
                                    "Change your options and try again.")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                }
                            })
                            .show();
                    return;
                }

                int cardListSize = mFilteredCards.size();
                if (cardListSize > mSelectedNumCards) {
                    mFilteredCards.subList(0, mSelectedNumCards).clear();
                } else if (cardListSize < mSelectedNumCards) {
                    mSelectedNumCards = cardListSize;
                }

                Intent intent = new Intent(TopicDetailActivity.this, FlashCardActivity.class);
                intent.putExtra(FlashCardActivity.EXTRA_TOPIC, mTopic);
                intent.putExtra(FlashCardActivity.EXTRA_NUM_CARDS, mSelectedNumCards);
                intent.putExtra(FlashCardActivity.EXTRA_CARD_LIST, mFilteredCards);
                startActivity(intent);
            }
        });
    }

    private void updateCardNumSpinner(ArrayList<FlashCard> flashCards) {
        final int numOfCards = flashCards.size();

        ArrayList<String> spinnerCardItems = new ArrayList<>();
        for (int i = 5; i < numOfCards-1; i += 5) {
            spinnerCardItems.add(String.valueOf(i));
        }
        spinnerCardItems.add("All flashcards");
        final ArrayList<String> finalSpinnerCardItems = spinnerCardItems;

        mCardNumSpinner.setItemsArray(spinnerCardItems);
        mCardNumSpinner.setOnItemChosenListener(new LabelledSpinner.OnItemChosenListener() {
            @Override
            public void onItemChosen(View labelledSpinner, AdapterView<?> adapterView, View itemView,
                                     int position, long id) {
                mSelectedNumCards = (position == finalSpinnerCardItems.size()-1) ?
                        numOfCards : Integer.parseInt(finalSpinnerCardItems.get(position));
            }

            @Override
            public void onNothingChosen(View labelledSpinner, AdapterView<?> adapterView) {}
        });
    }

    private @ArrayRes int getCardContentOptions() {
        String courseType = mTopic.getCourse().getType();
        switch (courseType) {
            case FlashCard.STANDARD:
                return R.array.spinner_content_items_std;
            case FlashCard.LANGUAGE:
                return R.array.spinner_content_items_lang;
            default:
                throw new IllegalArgumentException("the course type identifier '" +
                        courseType + "' is invalid");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
