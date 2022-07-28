package com.example.triviaapp.features.numbers.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.triviaapp.databinding.FragmentSavedTriviaBinding
import com.example.triviaapp.features.numbers.domain.entities.NumberTriviaEntity
import com.example.triviaapp.features.numbers.presentation.adapters.SavedNumberTriviaListAdapter
import com.example.triviaapp.features.numbers.presentation.viewmodels.NumbersViewModel

/**
 * A simple [Fragment] subclass.
 * Use the [SavedNumberTriviaFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SavedNumberTriviaFragment : Fragment(), SavedNumberTriviaListAdapter.SavedNumberTriviaItemClickListener {
    private var _binding: FragmentSavedTriviaBinding? = null
    private val binding get() = _binding!!
    private val numbersViewModel: NumbersViewModel by activityViewModels()
    private lateinit var savedNumberTriviaAdapter: SavedNumberTriviaListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentSavedTriviaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()
        setUpObservers()
    }

    private fun setUpObservers(){
        numbersViewModel.getAllSavedTrivia().observe(viewLifecycleOwner){
            it?.let {
                if(::savedNumberTriviaAdapter.isInitialized){
                    savedNumberTriviaAdapter.submitList(it)
                }
            }
        }
    }

    private fun setUpRecyclerView(){
        savedNumberTriviaAdapter = SavedNumberTriviaListAdapter(this)
        binding.savedNumberTriviaRecyclerView.apply {
            adapter = savedNumberTriviaAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    override fun onClick(trivia: NumberTriviaEntity) {
        numbersViewModel.deleteTriviaFromDB(trivia)
    }

}