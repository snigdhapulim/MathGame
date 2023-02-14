package com.example.mathgame

import androidx.lifecycle.SavedStateHandle
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ActivityScenario.launch
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import org.junit.Rule
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
public class QuizActivityTest {

    private lateinit var scenario: ActivityScenario<QuizActivity>

    @Before
    fun setUp() {
        scenario = launch(QuizActivity::class.java)
    }
    @After
    fun tearDown() {
        scenario.close()
    }

    @Test
    fun checkIfQuestionGenerated() {
        val quizViewModel = QuizViewModel()
        onView(withId(R.id.question)).check(matches(withText(quizViewModel.getCurrentQuestion)))
    }
}