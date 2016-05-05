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
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.satsumasoftware.flashcards.R;
import com.satsumasoftware.flashcards.object.Course;
import com.satsumasoftware.flashcards.object.FlashCard;
import com.satsumasoftware.flashcards.object.Subject;
import com.satsumasoftware.flashcards.object.Topic;
import com.satsumasoftware.flashcards.util.PrefUtils;
import com.satsumasoftware.flashcards.util.ThemeUtils;
import com.satsuware.usefulviews.FlippableView;

import java.util.ArrayList;
import java.util.Collections;

public class FlashCardActivity extends AppCompatActivity {

    protected static final String EXTRA_TOPIC = "extra_topic";
    protected static final String EXTRA_NUM_CARDS = "extra_number_of_cards";

    protected static final String SAVED_CARD_LIST = "saved_card_list";
    protected static final String SAVED_CARD_COUNT = "saved_card_count";

    private Topic mTopic;
    private ArrayList<FlashCard> mFlashCards;

    private int mNumOfCards;

    private TextView mCounterText;
    private Button mButtonNext, mButtonPrevious;

    private FlippableView mFlippableView;

    private int mCardCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mTopic = getIntent().getParcelableExtra(EXTRA_TOPIC);
        if (mTopic == null) {
            throw new NullPointerException("parcelable object from intent is null");
        }
        setTheme(ThemeUtils.getMduTheme(this, mTopic.getCourse()));
        setContentView(R.layout.activity_flash_card);

        mNumOfCards = getIntent().getExtras().getInt(EXTRA_NUM_CARDS);

        if (savedInstanceState == null) {
            mFlashCards = mTopic.getFlashCards(this);
            Collections.shuffle(mFlashCards);
            mCardCount = 1;
        } else {
            mFlashCards = savedInstanceState.getParcelableArrayList(SAVED_CARD_LIST);
            mCardCount = savedInstanceState.getInt(SAVED_CARD_COUNT);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        assert toolbar != null;
        assert getSupportActionBar() != null;

        getSupportActionBar().setTitle(mTopic.getName());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        View root = findViewById(R.id.layout_root);
        assert root != null;

        int color = PrefUtils.useDarkFlashcardBackground(this) ?
                R.color.mdu_grey_800 :
                mTopic.getCourse().getSubject(this).getColorRes(this, Subject.VARIANT_400);
        root.setBackgroundColor(ContextCompat.getColor(this, color));

        final LayoutInflater inflater = LayoutInflater.from(this);

        mCounterText = (TextView) findViewById(R.id.text_count);
        mButtonNext = (Button) findViewById(R.id.button_next);
        mButtonPrevious = (Button) findViewById(R.id.button_previous);

        mFlippableView = (FlippableView) findViewById(R.id.flippableView);

        mButtonPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCardCount-1 < 1) {
                    finish();
                } else {
                    mCardCount--;
                    showFlashCardPair(inflater);
                }
            }
        });
        mButtonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCardCount+1 > mNumOfCards) {
                    finish();
                } else {
                    mCardCount++;
                    showFlashCardPair(inflater);
                }
            }
        });

        showFlashCardPair(inflater);
    }

    private void showFlashCardPair(LayoutInflater inflater) {
        mCounterText.setText(getResources().getString(R.string.flashcards_count, mCardCount, mNumOfCards));
        mButtonPrevious.setText(getResources().getString((mCardCount == 1) ?
                R.string.flashcards_quit : R.string.flashcards_previous));
        mButtonNext.setText(getResources().getString((mCardCount == mNumOfCards) ?
                R.string.flashcards_finish : R.string.flashcards_next));

        FlashCard flashCard = mFlashCards.get(mCardCount-1);

        final View questionCard = inflater.inflate(R.layout.flashcard_question, mFlippableView, false);
        TextView questionText = (TextView) questionCard.findViewById(R.id.text);
        questionText.setText(flashCard.getQuestion());

        final View answerCard = inflater.inflate(R.layout.flashcard_answer, mFlippableView, false);
        TextView answerText = (TextView) answerCard.findViewById(R.id.text);
        answerText.setText(flashCard.getAnswer());

        mFlippableView.setFrontAndBackViews(questionCard, answerCard);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelableArrayList(SAVED_CARD_LIST, mFlashCards);
        outState.putInt(SAVED_CARD_COUNT, mCardCount);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_flashcards, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.reference:
                FlashCard flashCard = mFlashCards.get(mCardCount-1);
                int pageNo = flashCard.getPageReference();
                Course course = flashCard.getTopic().getCourse();
                String examBoard = course.getExamBoard(this).getName();
                String subject = course.getSubject(this).getName();
                new AlertDialog.Builder(this)
                        .setTitle(R.string.flashcard_guide_ref_title)
                        .setMessage(Html.fromHtml(getResources().getString(R.string.flashcard_guide_ref, pageNo, examBoard, subject)))
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Do nothing
                            }
                        })
                        .show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
