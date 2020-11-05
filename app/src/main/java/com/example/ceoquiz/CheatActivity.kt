package com.example.ceoquiz

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class CheatActivity : AppCompatActivity() {

    companion object{
        const val KEY_INDEX = "index"
        const val EXTRA_ANSWER_IS_TRUE = "com.android.geoquiz.answer_is_true"
        const val EXTRA_ANSWER_SHOWN = "com.android.geoquiz.answer_shown"
    }

    private var mAnswerIsTrue = false
    private lateinit var mAnswerTextView: TextView
    private lateinit var mShowAnswerButton: Button
    private var  mIsAnswerShown = false

    fun newIntent(packageContext: Context, answerIsTrue: Boolean): Intent {
        val intent = Intent(packageContext, CheatActivity::class.java)
        intent.putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue)
        return intent
    }

    fun wasAnswerShown(result: Intent): Boolean {
        return result.getBooleanExtra(EXTRA_ANSWER_SHOWN, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cheat)
        mIsAnswerShown = false
        if (savedInstanceState != null) {
            mIsAnswerShown = savedInstanceState.getBoolean(KEY_INDEX)
            setAnswerShownResult()
        }

        mAnswerIsTrue = intent.getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false)
        mAnswerTextView = findViewById(R.id.answer_text_view)
        mShowAnswerButton = findViewById(R.id.show_answer_button)
        mShowAnswerButton.setOnClickListener {
            if (mAnswerIsTrue) mAnswerTextView.setText(R.string.true_button)
            else mAnswerTextView.setText(R.string.false_button)
            mIsAnswerShown = true
            setAnswerShownResult()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(KEY_INDEX, mIsAnswerShown)
    }

    private fun setAnswerShownResult() {
        val data = Intent()
        Log.i("mIsAnswerShown", mIsAnswerShown.toString())
        data.putExtra(EXTRA_ANSWER_SHOWN, mIsAnswerShown)
        setResult(Activity.RESULT_OK, data)
    }

}
