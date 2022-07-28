package com.example.triviaapp.features.numbers.presentation.fragments

import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.triviaapp.R
import com.example.triviaapp.core.NumberTriviaType.*
import com.example.triviaapp.core.NumberTriviaType.Number
import com.example.triviaapp.databinding.FragmentNumbersBinding
import com.example.triviaapp.features.numbers.presentation.viewmodels.NumbersViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import kotlin.getValue
import kotlin.let

/**
 * A simple [Fragment] subclass.
 * Use the [NumbersFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class NumbersFragment : Fragment() {
    private var _binding: FragmentNumbersBinding? = null
    private val binding get() = _binding!!
    private val numbersViewModel: NumbersViewModel by activityViewModels()

    @Inject lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        _binding = FragmentNumbersBinding.inflate(inflater, container, false)
        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpTypeOptionsView()
        setUpNumbersInputField()
        setUpClickListeners()
        setUpObservers()
        setUpSharedPreferencesListener()
    }

    private fun setUpSharedPreferencesListener() {
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener)
    }

    private val listener =
        OnSharedPreferenceChangeListener { sharedPreferences, key ->
            sharedPreferences?.let {
                val currentTrivia = it.getString(key, "")
                currentTrivia?.let { numbersViewModel.updateCurrentNumberTrivia(it) }
            }
        }

    private fun setUpObservers(){
        numbersViewModel.currentNumberTriviaLD.observe(viewLifecycleOwner) {
            it?.let {
                binding.number.text = it.number.toString()
                binding.numberTriviaText.text = it.text
            }
        }

        numbersViewModel.currentTriviaType.observe(viewLifecycleOwner){
            it?.let {
                if(it == Date){
                    binding.dateTriviaContainer.visibility = View.VISIBLE
                    binding.enterNumberTextField.visibility = View.GONE
                }else {
                    binding.dateTriviaContainer.visibility = View.GONE
                    if(it == Random){
                        binding.enterNumberTextField.visibility = View.GONE
                    }else{
                        binding.enterNumberTextField.visibility = View.VISIBLE
                    }
                }
            }
        }

        numbersViewModel.isGetTriviaBtnEnabled.observe(viewLifecycleOwner){
            it?.let { binding.getTriviaBtn.isEnabled = it}
        }

        numbersViewModel.isFavourite.observe(viewLifecycleOwner){
            it?.let {
                if(it) {
                    binding.favouriteBtn.setImageResource(R.drawable.ic_star_24)
                    numbersViewModel.saveTriviaToDB()
                } else {
                    binding.favouriteBtn.setImageResource(R.drawable.ic_star_outline_24)
                    numbersViewModel.deleteTriviaFromDB()
                }
            }
        }
    }

    private fun setUpClickListeners(){
        binding.getTriviaBtn.setOnClickListener { numbersViewModel.getTrivia() }
        binding.getTriviaBtn.isEnabled = false
        binding.favouriteBtn.setOnClickListener {
            numbersViewModel.toggleFavourite()
        }
    }

    private fun setUpNumbersInputField(){
        binding.enterNumberTextField.doAfterTextChanged {
            it?.let {
                if(it.toString().isNotBlank()) {
                    numbersViewModel.setCurrentNumberLD(it.toString().toInt())
                    numbersViewModel.setGetTriviaBtnEnabled(true)
                }else {
                    numbersViewModel.setGetTriviaBtnEnabled(false)
                }
            }
        }
        val dayStateFlow = MutableStateFlow(1)
        val monthStateFlow = MutableStateFlow(1)
        binding.enterDayTextField.doAfterTextChanged {
            it?.let {
                if(it.isNotBlank()) {
                    dayStateFlow.value = it.toString().toInt()
                    numbersViewModel.setCurrentDateLD(dayStateFlow.asStateFlow(), monthStateFlow.asStateFlow())
                }
                else numbersViewModel.setGetTriviaBtnEnabled(false)
            }
        }
        binding.enterMonthTextField.doAfterTextChanged {
            it?.let {
                if(it.isNotBlank()) {
                    monthStateFlow.value = it.toString().toInt()
                    numbersViewModel.setCurrentDateLD(dayStateFlow.asStateFlow(), monthStateFlow.asStateFlow())
                }
                else numbersViewModel.setGetTriviaBtnEnabled(false)
            }
        }
    }

    private fun setUpTypeOptionsView(){
        val items = values().map { it.name }
        val adapter = ArrayAdapter(requireContext(), R.layout.view_numbers_option, items)
        binding.numbersOptionsDropdown.setAdapter(adapter)
        binding.numbersOptionsDropdown.setText(items.first(), false)

        binding.numbersOptionsDropdown.doOnTextChanged { text, _, _, _ ->
            when(text?.toString()){
                Number.name -> numbersViewModel.setCurrentTriviaType(Number)
                Random.name -> numbersViewModel.setCurrentTriviaType(Random)
                Math.name -> numbersViewModel.setCurrentTriviaType(Math)
                Date.name -> numbersViewModel.setCurrentTriviaType(Date)
            }
            numbersViewModel.getTrivia()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDetach() {
        super.onDestroy()
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener)
    }
}