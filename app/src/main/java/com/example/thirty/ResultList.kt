package com.example.thirty

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.thirty.databinding.FragmentResultListBinding
import kotlinx.coroutines.NonDisposableHandle.parent


class ResultList : Fragment() {

    private var binding : FragmentResultListBinding ?= null
    private val viewModels: DiceViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("DiceViewModel", "Hello from Result List")
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentResultListBinding.inflate(inflater, container, false)
        Log.d("DiceViewModel", "Hello from Result List")
        return binding?.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = binding?.resultList
        recyclerView?.layoutManager = LinearLayoutManager(context)

        val adapter = DiceAdapter(viewModels.roundsAndScore)
        Log.d("DiceViewModel", "Total Item in the hashMap == ${adapter.itemCount}")
        recyclerView?.adapter = adapter

        binding?.playAgainButton?.setOnClickListener {
            viewModels.clearInformationOfRoundsAndScore()
            findNavController().navigate(R.id.action_result_list_to_home_page)
        }
    }

}