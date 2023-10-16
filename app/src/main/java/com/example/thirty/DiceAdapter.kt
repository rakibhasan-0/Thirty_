package com.example.thirty

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.thirty.databinding.ResultItemBinding

class DiceAdapter(private val roundsAndScores: HashMap<Int, Int>): RecyclerView.Adapter<DiceAdapter.DiceViewHolder>() {

    private val roundsList = roundsAndScores.keys.toList()
    private val scoresList = roundsAndScores.values.toList()

    class DiceViewHolder(private val binding: ResultItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(round: Int, score: Int) {
            binding.roundView.text = "Round ${round.toString()}"
            binding.pointView.text = "Score ${score.toString()}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiceViewHolder {
        val view = ResultItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return DiceViewHolder(view)
    }

    override fun getItemCount(): Int {
        return roundsAndScores.size
    }

    override fun onBindViewHolder(holder: DiceViewHolder, position: Int) {
        holder.bind(roundsList[position],scoresList[position])
    }
}
