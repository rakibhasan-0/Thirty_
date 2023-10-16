package com.example.thirty

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class DiceViewModel: ViewModel() {

    // LiveData to hold the current round number.
    private var _totalRound = MutableLiveData(0)
    val totalRound: LiveData<Int> get() = _totalRound

    //LiveData for the current Score
    private var _playerScore = MutableLiveData(0)
    val playerScore: LiveData<Int> get() = _playerScore

    // LiveData to hold the values of the selected dices.
    private var _selectedDiceValues = mutableListOf<Dice>()
    val selectedDiceValues: List<Dice> get() = _selectedDiceValues

    // LiveData to hold the mapping of round number to score.
    private var _roundsAndScore = HashMap<Int, Int>()
    val roundsAndScore : HashMap<Int, Int> get() = _roundsAndScore

    // Variable to keep track of the current round.
    private var currentRound: Int = 1


    private var _category : String? = null
    val category: String? get() = _category

    /**
     * Updates the total round by increasing it by one.
     */
    fun updateTotalRound(){
        _totalRound.value = (_totalRound.value ?: 0 ) + 1
    }


    /**
     * set category for the game
     */

    fun setCategory(value: String ){
        _category = value
    }


    /**
     * Clears the list of selected dice values.
     */
    fun removeDices(){
        _selectedDiceValues.clear()
    }


    /**
     * players current score
     */
    fun playerScore(score: Int){
       _playerScore.value = _playerScore.value?.plus(score)
    }


    /**
     * Adds a dice value to the list of selected dice values.
     * @param value The value of the dice to be added.
     */
    fun addDiceValue(value: Int) {
       _selectedDiceValues.add(Dice(value, false))
    }

    /**
     * Records the score for the current round and moves to the next round.
     * @param score The score to be recorded for the current round.
     */
    fun addTheScoreOfEachRound(score: Int){
        roundsAndScore[currentRound] = score
        currentRound++
    }

    /**
     * it will set the player score as 0
     */

    fun setPlayerScore(){
        _playerScore.value = 0
    }

    /**
     * clear hashmap's information
     */

    fun clearInformationOfRoundsAndScore(){
        roundsAndScore.clear()
    }
}
