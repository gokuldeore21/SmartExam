package com.example.smartexam

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smartexam.databinding.ActivityMainBinding
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var questionModelList : MutableList<QuestionModel>
    lateinit var adpater : questionListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        questionModelList = mutableListOf()
        getDataFromFirebase()

    }

    private fun setUpRecyclerView(){
        binding.progressBar.visibility = View.GONE
        adpater = questionListAdapter(questionModelList)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adpater

    }



    private fun getDataFromFirebase(){
        binding.progressBar.visibility = View.VISIBLE
        FirebaseDatabase.getInstance().reference
            .get()
            .addOnSuccessListener { dataSnapshot ->
                if(dataSnapshot.exists()){
                    for(snapshot in dataSnapshot.children){
                        val questionModel = snapshot.getValue(QuestionModel::class.java)
                        if (questionModel != null) {
                            questionModelList.add(questionModel)
                        }
                    }
                }
                setUpRecyclerView()
            }

    }
}




