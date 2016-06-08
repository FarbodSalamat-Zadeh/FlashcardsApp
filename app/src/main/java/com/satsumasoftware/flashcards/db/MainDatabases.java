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

package com.satsumasoftware.flashcards.db;

import android.content.Context;

import com.satsumasoftware.flashcards.framework.Course;
import com.satsumasoftware.flashcards.framework.ExamBoard;
import com.satsumasoftware.flashcards.framework.Subject;
import com.satsumasoftware.flashcards.framework.flashcard.FlashCard;
import com.satsumasoftware.flashcards.util.CsvUtils;
import com.univocity.parsers.csv.CsvParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainDatabases {

    public static ArrayList<Course> getCourses(Context context) {
        ArrayList<Course> list = new ArrayList<>();
        CsvParser parser = CsvUtils.getMyParser();
        try {
            List<String[]> allRows = parser.parseAll(context.getAssets().open(CsvUtils.COURSES));
            for (String[] line : allRows) {
                // int id = Integer.parseInt(line[0]); // (not needed)
                String subjectIdentifier = line[1];
                String examBoardIdentifier = line[2];
                int courseType = Integer.parseInt(line[3]);
                String revisionGuide = line[4];

                Course course = new Course(
                        subjectIdentifier,
                        examBoardIdentifier,
                        courseTypeFromInt(courseType),
                        (revisionGuide == null || revisionGuide.equals("")) ? null : revisionGuide
                );
                list.add(course);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    private static @FlashCard.CourseType String courseTypeFromInt(int x) {
        switch (x) {
            case 0:
                return FlashCard.STANDARD;
            case 1:
                return FlashCard.LANGUAGE;
            default:
                throw new IllegalArgumentException("the course type identifier '" + x +
                        "' is invalid");
        }
    }

    public static ExamBoard findExamBoard(Context context, String identifierToFind) {
        ExamBoard examBoard = null;
        CsvParser parser = CsvUtils.getMyParser();
        try {
            List<String[]> allRows = parser.parseAll(context.getAssets().open(CsvUtils.EXAM_BOARDS));
            for (String[] line : allRows) {
                String identifier = line[1];
                if (identifier.equals(identifierToFind)) {
                    int id = Integer.parseInt(line[0]);
                    String name = line[2];

                    examBoard = new ExamBoard(id, identifier, name);
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (examBoard == null) {
            throw new NullPointerException("unable to find the identifier given");
        }
        return examBoard;
    }

    public static Subject findSubject(Context context, String identifierToFind) {
        Subject subject = null;
        CsvParser parser = CsvUtils.getMyParser();
        try {
            List<String[]> allRows = parser.parseAll(context.getAssets().open(CsvUtils.SUBJECTS));
            for (String[] line : allRows) {
                String identifier = line[1];
                if (identifier.equals(identifierToFind)) {
                    int id = Integer.parseInt(line[0]);
                    String name = line[2];
                    String mduColorIdentifier = line[3];

                    subject = new Subject(id, identifier, name, mduColorIdentifier);
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (subject == null) {
            throw new NullPointerException("unable to find the identifier given");
        }
        return subject;
    }

}
