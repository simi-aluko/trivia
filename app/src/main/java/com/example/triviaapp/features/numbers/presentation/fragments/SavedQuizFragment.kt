package com.example.triviaapp.features.numbers.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.triviaapp.R

/**
 * A simple [Fragment] subclass.
 * Use the [SavedQuizFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SavedQuizFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_saved_quiz, container, false)
    }
}