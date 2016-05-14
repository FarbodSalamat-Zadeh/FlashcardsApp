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

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.satsumasoftware.flashcards.R;
import com.satsumasoftware.flashcards.object.Topic;
import com.satsumasoftware.flashcards.util.ThemeUtils;
import com.satsuware.usefulviews.LabelledSpinner;

import java.util.ArrayList;

public class TopicDetailActivity extends AppCompatActivity {

    protected static final String EXTRA_TOPIC = "extra_topic";
    private Topic mTopic;

    private ArrayList<String> mSpinnerItems;
    private int mSelectedItem;

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

        final int numOfCards = mTopic.getFlashCards(this).size();

        LabelledSpinner spinner = (LabelledSpinner) findViewById(R.id.spinner);
        assert spinner != null;
        mSpinnerItems = new ArrayList<>();
        for (int i = 5; i < numOfCards-1; i += 5) {
            mSpinnerItems.add(String.valueOf(i));
        }
        mSpinnerItems.add("All flashcards");

        spinner.setItemsArray(mSpinnerItems);
        spinner.setOnItemChosenListener(new LabelledSpinner.OnItemChosenListener() {
            @Override
            public void onItemChosen(View labelledSpinner, AdapterView<?> adapterView, View itemView, int position, long id) {
                mSelectedItem = (position == mSpinnerItems.size()-1) ?
                        numOfCards : Integer.parseInt(mSpinnerItems.get(position));
            }

            @Override
            public void onNothingChosen(View labelledSpinner, AdapterView<?> adapterView) {}
        });


        Button button = (Button) findViewById(R.id.button);
        assert button != null;

        if (numOfCards == 0) {
            LinearLayout linearLayout = (LinearLayout) findViewById(R.id.viewGroup_options);
            assert linearLayout != null;
            linearLayout.setVisibility(View.GONE);
            button.setText(R.string.coming_soon);
            button.setEnabled(false);
            return;
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TopicDetailActivity.this, FlashCardActivity.class);
                intent.putExtra(FlashCardActivity.EXTRA_TOPIC, mTopic);
                intent.putExtra(FlashCardActivity.EXTRA_NUM_CARDS, mSelectedItem);
                startActivity(intent);
            }
        });
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
