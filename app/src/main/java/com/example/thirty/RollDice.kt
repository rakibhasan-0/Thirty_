package com.example.thirty

import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.thirty.databinding.FragmentRollDiceBinding
import com.google.android.material.snackbar.Snackbar

class RollDice : Fragment() {
    private var binding: FragmentRollDiceBinding? = null
    private var throwCount = 0
    private var isFirstSelection = true
    private val viewModels: DiceViewModel by activityViewModels()



    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("throwCount", throwCount)
        outState.putBoolean("isFirstSelection", isFirstSelection)
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRollDiceBinding.inflate(inflater,container,false)
        return binding?.root
    }




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        savedInstanceState?.let {
            throwCount = it.getInt("throwCount", 0)
            isFirstSelection = it.getBoolean("isFirstSelection", true)
        }

        binding?.throwButton?.setOnClickListener {
            if (throwCount < 3) {
                rollDices()
                throwCount++
            }
            if(throwCount == 3){
                Toast.makeText(context,"You cannot throw no more",Toast.LENGTH_SHORT).show()
            }
        }

        setDiceClickListeners(binding?.dices)
        val categories = listOf("Select a Category") + Category.values().map { it.toString() }
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, categories)
        binding?.categorySpinner?.adapter = adapter


        binding?.categorySpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                if (isFirstSelection) {
                    isFirstSelection = false
                    return
                }
                val selectedCategoryValue = Category.values()[position-1]
                handleSelectedCategory(selectedCategoryValue)
                binding?.categorySpinner?.isEnabled = false
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                Toast.makeText(context, "Please select a category", Toast.LENGTH_SHORT).show()
            }
        }


        binding?.Calculatebutton?.setOnClickListener{
            if(viewModels.selectedDiceValues.size == 6 && viewModels.category!= null){
                throwCount = 0
                resetInfo()
                resetDicesVisibility(binding?.dices)
                findNavController().navigate(R.id.action_rollDice2_to_calculateScore)
            }
        }
    }






    private fun resetInfo() {
        isFirstSelection = true
        binding?.categorySpinner?.isEnabled = true
        binding?.categorySpinner?.setSelection(0)
        binding?.SelectedDices?.removeAllViews()
    }






    private fun resetDicesVisibility(container: ViewGroup?) {
        val childCount = container?.childCount ?: 0
        for (i in 0 until childCount) {
            val child = container?.getChildAt(i)
            if (child is ImageView) {
                child.visibility = View.VISIBLE
            } else if (child is ViewGroup) {
                resetDicesVisibility(child)
            }
        }
    }





    private fun handleSelectedCategory(category: Category) {
        viewModels.setCategory(category.toString())
        binding?.categorySpinner?.isEnabled = false
    }





    private fun setDiceClickListeners(container: ViewGroup?) {
        val childCount = container?.childCount ?: 0
        for (i in 0 until childCount) {
            val child = container?.getChildAt(i)
            if (child is ImageView) {
                child.setOnClickListener {
                    selectDice(child)
                }
            } else if (child is ViewGroup) {
                setDiceClickListeners(child)
            }
        }
    }





    private fun selectDice(selectedImageView: ImageView) {
        when {
            canSelectDice() -> handleDiceSelection(selectedImageView)
            viewModels.selectedDiceValues.size >= 6 -> showSnackbar("You can't select more than 6 dices!")
            throwCount == 0 -> showSnackbar("You Need To Throw First")
        }
    }




    private fun canSelectDice(): Boolean {
        return viewModels.selectedDiceValues.size <= 6 && throwCount > 0
    }





    private fun handleDiceSelection(selectedImageView: ImageView) {
        val diceValue = selectedImageView.tag?.toString()?.toInt() ?: return
        val newDiceView = createNewDiceView(selectedImageView, diceValue)
        removeViewFromParent(selectedImageView)
        viewModels.addDiceValue(diceValue)

        binding?.SelectedDices?.addView(newDiceView)
    }




    private fun createNewDiceView(selectedImageView: ImageView, diceValue: Int): ImageView {
        val newDiceView = ImageView(context)
        newDiceView.setImageDrawable(selectedImageView.drawable)
        val sizeInPixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 70f, resources.displayMetrics).toInt()
        newDiceView.layoutParams = LinearLayout.LayoutParams(sizeInPixels, sizeInPixels)
        newDiceView.tag = diceValue
        return newDiceView
    }








    private fun removeViewFromParent(selectedImageView: ImageView) {
        var parent = selectedImageView.parent
        while (parent is ViewGroup && parent.id != R.id.dices) {
            if (parent.indexOfChild(selectedImageView) != -1) {
                selectedImageView.visibility = View.INVISIBLE
                break
            }
            parent = parent.parent
        }
    }






    private fun showSnackbar(message: String) {
        binding?.root?.let {
            Snackbar.make(it, message, Snackbar.LENGTH_SHORT).show()
        }
    }










    private fun rollDices() {
        val diceImageMap = mapOf(
            R.drawable.white1 to 1,
            R.drawable.white2 to 2,
            R.drawable.white3 to 3,
            R.drawable.white4 to 4,
            R.drawable.white5 to 5,
            R.drawable.white6 to 6
        )
        rollAllDicesInContainer(binding?.dices, diceImageMap)
    }













    private fun rollAllDicesInContainer(container: ViewGroup?, diceImageMap: Map<Int, Int>) {
        val childCount = container?.childCount ?: 0
        for (i in 0 until childCount) {
            val child = container?.getChildAt(i)
            if (child is ImageView) {
                val (randomDiceImage, diceValue) = diceImageMap.entries.random()
                child.setImageResource(randomDiceImage)
                child.tag = diceValue.toString()
                Log.d("RollDice", diceValue.toString())
            } else if (child is ViewGroup) {
                rollAllDicesInContainer(child, diceImageMap)
            }
        }
    }













    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}
