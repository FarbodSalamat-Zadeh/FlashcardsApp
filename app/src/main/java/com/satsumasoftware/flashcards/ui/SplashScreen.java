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

import com.satsumasoftware.flashcards.R;
import com.satsumasoftware.flashcards.util.PrefUtils;

public class SplashScreen extends AppCompatActivity {

    public static final int SLEEP_TIME = 0; // TODO

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Thread timer = new Thread() {
            public void run() {
                try {
                    sleep(SLEEP_TIME);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    Intent intent;
                    if (!PrefUtils.hasDatabaseInitialised(getBaseContext())) {
                        intent = new Intent(getBaseContext(), InitializeDbActivity.class);
                    } else {
                        intent = new Intent(getBaseContext(), MainActivity.class);
                    }
                    startActivity(intent);
                }
            }
        };
        timer.start();
    }
}
