package com.example.triviaapp.features.numbers.presentation.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import com.example.triviaapp.R
import com.example.triviaapp.core.NumberTriviaType.*
import com.example.triviaapp.core.NumberTriviaType.Number
import com.example.triviaapp.databinding.FragmentNumbersBinding
import com.example.triviaapp.features.numbers.presentation.viewmodels.NumbersViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

/**
 * A simple [Fragment] subclass.
 * Use the [NumbersFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class NumbersFragment : Fragment() {
    private var _binding: FragmentNumbersBinding? = null
    private val binding get() = _binding!!
    private val numbersViewModel: NumbersViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        _binding = FragmentNumbersBinding.inflate(inflater, container, false)
        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpTypeOptionsView()
        setUpNumbersInputField()
        setUpGetTriviaBtn()
        setUpObservers()
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
    }

    private fun setUpGetTriviaBtn(){
        binding.getTriviaBtn.setOnClickListener { numbersViewModel.getTrivia() }
        binding.getTriviaBtn.isEnabled = false
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

        binding.numbersOptionsDropdown.doOnTextChanged { text, start, before, count ->
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
}