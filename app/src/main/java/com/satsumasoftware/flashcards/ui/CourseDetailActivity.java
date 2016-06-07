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
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.satsumasoftware.flashcards.R;
import com.satsumasoftware.flashcards.framework.Course;
import com.satsumasoftware.flashcards.framework.topic.FullContentTopic;
import com.satsumasoftware.flashcards.framework.topic.Topic;
import com.satsumasoftware.flashcards.ui.adapter.TopicsAdapter;
import com.satsumasoftware.flashcards.util.ThemeUtils;

import java.util.ArrayList;

public class CourseDetailActivity extends AppCompatActivity {

    protected static final String EXTRA_COURSE = "extra_course";

    private Course mCourse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mCourse = getIntent().getParcelableExtra(EXTRA_COURSE);
        if (mCourse == null) {
            throw new NullPointerException("parcelable object received from intent is null");
        }
        setTheme(ThemeUtils.getMduTheme(this, mCourse));
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;

        getSupportActionBar().setTitle(mCourse.getSubject(this).getName());
        getSupportActionBar().setSubtitle(mCourse.getExamBoard(this).getName());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        assert recyclerView != null;
        recyclerView.setLayoutManager(new GridLayoutManager(this, getResources().getInteger(R.integer.columns)));
        ArrayList<Topic> courseTopics = mCourse.getTopics(this);

        if (courseTopics.size() == 0) {
            return;
        }

        if (courseTopics.size() > 1) {
            FullContentTopic fullContentTopic = new FullContentTopic(mCourse);
            courseTopics.add(0, fullContentTopic);
        }
        final ArrayList<Topic> topics = courseTopics;

        TopicsAdapter adapter = new TopicsAdapter(this, topics);
        adapter.setOnEntryClickListener(new TopicsAdapter.OnEntryClickListener() {
            @Override
            public void onEntryClick(View view, int position) {
                Intent intent = new Intent(CourseDetailActivity.this, TopicDetailActivity.class);
                intent.putExtra(TopicDetailActivity.EXTRA_TOPIC, topics.get(position));
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);
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
