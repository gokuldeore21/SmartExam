package com.example.smartexam

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.smartexam.databinding.QuestionItemRecyclerViewBinding

class questionListAdapter (private val questionModelList: List<QuestionModel>) :
    RecyclerView.Adapter<questionListAdapter.myViewHolder>() {
    class myViewHolder(private val binding: QuestionItemRecyclerViewBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(model: QuestionModel){
            binding.apply {
                questionTitleText.text = model.title
                questionSubtitleText.text = model.subtitle
                questionTimerText.text = model.time + " min"


                root.setOnClickListener {
                    val intent = Intent(root.context,QuestionActivity::class.java)
                    QuestionActivity.questionModelList = model.questionList
                    QuestionActivity.time = model.time
                    root.context.startActivity(intent)
                }

            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myViewHolder {
        val binding = QuestionItemRecyclerViewBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return myViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return questionModelList.size
    }

    override fun onBindViewHolder(holder: myViewHolder, position: Int) {
        holder.bind(questionModelList[position])
    }
}