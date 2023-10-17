package com.example.thirty

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.thirty.databinding.FragmentCalculateScoreBinding


class CalculateScore : Fragment() {

    private var binding: FragmentCalculateScoreBinding ? = null
    private val viewModels: DiceViewModel by activityViewModels()
    private var diceLists: MutableList<Dice> = mutableListOf()
    private var category: String ?= null



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCalculateScoreBinding.inflate(inflater,container,false)
        return binding?.root
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("category", category)
        outState.putIntegerArrayList("diceValues", ArrayList(diceLists.map { it.value }))
    }




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        savedInstanceState?.let {
            category = it.getString("category")
            diceLists.addAll(it.getIntegerArrayList("diceValues")
                ?.map { value -> Dice(value, true) } ?: listOf())
        }


        binding?.category?.text = "Category: ${viewModels.category}"
        category = viewModels.category
        diceLists.addAll(viewModels.selectedDiceValues)

        viewModels.removeDices()
        setDices()

        userSelectionOfDices()
        calculateScore()

        submitButton()
    }



    private fun submitButton() {
        binding?.submitButton2?.setOnClickListener {
            viewModels.addTheScoreOfEachRound(viewModels.playerScore.value ?: 0)
            viewModels.setPlayerScore()

            if (viewModels.totalRound.value!! >= 3) {
                findNavController().navigate(R.id.action_calculateScore_to_resultList)
            } else {
                viewModels.updateTotalRound()
                findNavController().navigate(R.id.action_calculateScore_to_rollDice)
            }
        }
    }


    private fun calculateScore() {
        binding?.calcButton?.setOnClickListener {
            when (val selectedCategory = category) {
                "LOW" -> {
                    calculateScoreForCategory(1)
                }
                "FOUR", "FIVE", "SIX", "SEVEN", "EIGHT", "NINE", "TEN", "ELEVEN", "TWELVE" -> {
                     when (selectedCategory) {
                        "FOUR" -> calculateScoreForCategory(4)
                        "FIVE" -> calculateScoreForCategory(5)
                        "SIX" -> calculateScoreForCategory(6)
                        "SEVEN" -> calculateScoreForCategory(7)
                        "EIGHT" -> calculateScoreForCategory(8)
                        "NINE" -> calculateScoreForCategory(9)
                        "TEN" -> calculateScoreForCategory(10)
                        "ELEVEN" -> calculateScoreForCategory(11)
                        "TWELVE" -> calculateScoreForCategory(12)
                        else -> null
                    }
                }
            }
        }
    }











    private fun calculateScoreForCategory(category: Int) {
        val sumOfSelectedDice = diceLists?.sumBy { it.value } ?: 0

        val sum = when (category) {
            in 1..3 -> diceLists?.filter { it.value < 4 }?.sumBy { it.value } ?: 0
            else -> sumOfSelectedDice
        }

        if (sum > 0 && sum % category == 0) {
            viewModels.playerScore(sum)
            setupObservers()
            removeImageViewFromTheList()

        } else {
            restoreDiceVisibility()
        }

        diceLists?.clear()
    }









    private fun removeImageViewFromTheList(){
        for (i in 0 until (binding?.diceView?.childCount ?: 0)) {
            val child = binding?.diceView?.getChildAt(i)
            val diceNumberOfChild = child?.tag?.toString()?.toIntOrNull()



            // Check if diceLists contains a Dice object with the matching value and is not used
            val matchingDice = diceLists?.firstOrNull { dice -> dice.value == diceNumberOfChild && dice.used }
            if (matchingDice != null) {
                binding?.diceView?.removeView(child)
            }
        }


    }









    private fun restoreDiceVisibility() {
        for (i in 0 until (binding?.diceView?.childCount ?: 0)) {
            val child = binding?.diceView?.getChildAt(i)
            val diceNumberOfChild = child?.tag.toString().toInt()

            // Check if diceLists contains a Dice object with the matching value and is not used
            val matchingDice = diceLists?.firstOrNull { dice -> dice.value == diceNumberOfChild && dice.used }
            if (matchingDice != null) {
                child?.visibility = View.VISIBLE
            }
        }
    }








    // Set this up in onViewCreated or similar lifecycle method
    private fun setupObservers() {
        viewModels.playerScore.observe(viewLifecycleOwner) { score ->
            binding?.currentScore?.text = score.toString()
        }
    }






    private fun setDices() {
        val map = hashMapOf(
            1 to R.drawable.red1,
            2 to R.drawable.red2,
            3 to R.drawable.red3,
            4 to R.drawable.red4,
            5 to R.drawable.red5,
            6 to R.drawable.red6
        )
        createViewForDices(map)
    }






    private fun createViewForDices(map: HashMap<Int, Int>) {
        for (i in 0 until diceLists!!.size){
            val currentValue = diceLists?.get(i)?.value
            val imageId = map[currentValue]

            imageId?.let{
                val newDice = ImageView(requireContext())
                newDice.setImageResource(imageId)

                val sizeInPixels = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    70f,
                    resources.displayMetrics
                ).toInt()
                newDice.layoutParams = ViewGroup.LayoutParams(sizeInPixels, sizeInPixels)
                newDice.tag = currentValue.toString()

                binding?.diceView?.addView(newDice)
            }
        }
        diceLists?.clear()
    }



    private fun userSelectionOfDices() {
        for (i in 0 until (binding?.diceView?.childCount ?: 0)) {
            val child = binding?.diceView?.getChildAt(i)
            if (child is ImageView) {
                child.setOnClickListener {
                    diceLists?.add(Dice(child.tag.toString().toInt(),true))
                    child.visibility = View.INVISIBLE
                }
            }
        }
    }




}

