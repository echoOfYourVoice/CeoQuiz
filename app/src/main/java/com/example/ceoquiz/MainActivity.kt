package com.example.ceoquiz

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    companion object {
        const val TAG = "QuizActivity"
        const val KEY_INDEX = "index"
        const val REQUEST_CODE_CHEAT = 0
        private var misCheater = false
    }

    private lateinit var mTrueButton: Button
    private lateinit var mFalseButton: Button
    private lateinit var mCheatButton: Button
    private lateinit var mNextButton: ImageButton
    private lateinit var mPrevButton: ImageButton
    private lateinit var mQuestionTextView: TextView

    private val mQuestionBank = arrayListOf(
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_africa, false),
        Question(R.string.question_americas, true),
        Question(R.string.question_asia, true)
    )

    private var mCurrentIndex = 0
    private var mRightAnswers = 0

    private val mPastQuestions = mutableListOf<Question>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle) called")
        setContentView(R.layout.activity_main)

        if (savedInstanceState != null) mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0)

        mQuestionTextView = findViewById(R.id.question_text_view)

        mTrueButton = findViewById(R.id.true_button)

        mTrueButton.setOnClickListener {
            checkAnswer(true)
        }

        mFalseButton = findViewById(R.id.false_button)

        mFalseButton.setOnClickListener {
            checkAnswer(false)
        }

        mNextButton = findViewById(R.id.next_button)
        mNextButton.setOnClickListener {
            misCheater = false
            mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.size
            if (mCurrentIndex % mQuestionBank.size == 0) {
                Toast.makeText(this, "Right answers: $mRightAnswers", Toast.LENGTH_SHORT).show()
                mRightAnswers = 0
                mPastQuestions.clear()
            }
            updateQuestion()
        }

        mPrevButton = findViewById(R.id.prev_button)
        mPrevButton.setOnClickListener {
            mCurrentIndex = (if (mCurrentIndex == 0) 0 else mCurrentIndex - 1) % mQuestionBank.size
            updateQuestion()

        }

        mCheatButton = findViewById(R.id.cheat_button)
        mCheatButton.setOnClickListener {
            //val intent = Intent(this, CheatActivity::class.java)
            val answerIsTrue: Boolean = mQuestionBank[mCurrentIndex].mAnswerTrue
            val intent = (CheatActivity::newIntent)(CheatActivity(),this, answerIsTrue)
            //startActivity(intent)
            startActivityForResult(intent, REQUEST_CODE_CHEAT)
        }

        updateQuestion()
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume() called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause() called")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() called")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.i("dataResult", data.toString())
        if (resultCode != Activity.RESULT_OK) return
        if (requestCode == REQUEST_CODE_CHEAT) {
            if (data == null) return
            misCheater = (CheatActivity::wasAnswerShown)(CheatActivity(), data)
        }
    }

    /*
        Для сохранения в Bundle и восстановления из него подходят примитивные типы и классы, реализующие интерфейс Serializable или Parcelable.
        Впрочем, обычно сохранение объектов пользовательских типов в Bundle считается нежелательным, потому что данные могут потерять актуальность к моменту их
        извлечения. Лучше организовать для данных другой тип хранилища и сохранить
        в Bundle примитивный идентификатор
         */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.i(TAG, "onSaveInstanceState")
        outState.putInt(KEY_INDEX, mCurrentIndex)
    }

    private fun updateQuestion() {
        val question = mQuestionBank[mCurrentIndex].mTextResID
        mQuestionTextView.setText(question)
        setEnabledButtons()
    }

    private fun checkAnswer(userPressedTrue: Boolean) {
        val answerIsTrue = mQuestionBank[mCurrentIndex].mAnswerTrue

        val messageResId: Int
        messageResId = if (misCheater) R.string.judgment_toast
        else {
            if (userPressedTrue == answerIsTrue) {
                R.string.correct_toast
            } else {
                R.string.incorrect_toast
            }
        }

        if (messageResId == R.string.correct_toast) mRightAnswers++



        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).apply {
            setGravity(Gravity.TOP, 0, 0)
            show()
        }
        mPastQuestions.add(mQuestionBank[mCurrentIndex])
        setEnabledButtons()
    }

    private fun setEnabledButtons() {
        val enabled = mPastQuestions.contains(mQuestionBank[mCurrentIndex])
        mFalseButton.isEnabled = !enabled
        mTrueButton.isEnabled = !enabled
    }
}
