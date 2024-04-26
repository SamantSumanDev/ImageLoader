package com.samant.acharyaassignment.ui.view

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.samant.acharyaassignment.R
import com.samant.acharyaassignment.api.ApiClient.apiService
import com.samant.acharyaassignment.databinding.ActivityMainBinding
import com.samant.acharyaassignment.ui.Model.Coverage
import com.samant.acharyaassignment.ui.ViewModel.MainViewModel
import com.samant.acharyaassignment.ui.adapter.MediaCoveragesAdapter
import com.samant.acharyaassignment.utils.ApiState

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var mediaRecyclerView: RecyclerView
    private lateinit var layoutManager: GridLayoutManager
    private lateinit var adapter: MediaCoveragesAdapter
    private val mediaData: MutableList<Coverage> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Enable edge-to-edge display
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Apply window insets to adjust padding
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize views and data
        initView()
        fetchMediaCoverages(100)
        observeMediaCoverages()
    }

    private fun initView() {
        mediaRecyclerView = binding.mediaRecyclerView
    }

    private fun initializeRecyclerView(mediaData: MutableList<Coverage>) {
        try {
            mediaRecyclerView.setHasFixedSize(true)
            layoutManager = GridLayoutManager(applicationContext, 3)
            mediaRecyclerView.layoutManager = layoutManager
            adapter = MediaCoveragesAdapter(applicationContext, mediaData)
            mediaRecyclerView.adapter = adapter
        }catch (e:Exception){
            Log.i("MainActivity: RecyclerView",e.toString())
        }


    }

    private fun fetchMediaCoverages(limit: Int) {
        mainViewModel.getMediaCoverage(limit, apiService)
    }

    private fun observeMediaCoverages() {
        mainViewModel.MediaCoverageLiveData.observe(this) { state ->
            when (state) {
                is ApiState.Loading -> {
                    binding.pbr.visibility = View.VISIBLE
                    mediaRecyclerView.visibility = View.GONE
                }

                is ApiState.Success -> {
                    try {
                        Log.d("tag", "success")
                        Log.d("tag", state.data.toString())
                      //  Toast.makeText(applicationContext, "Success", Toast.LENGTH_SHORT).show()
                        mediaData.clear()
                        mediaData.addAll(state.data)
                        initializeRecyclerView(mediaData)

                        binding.pbr.visibility = View.GONE
                        binding.mediaRecyclerView.visibility = View.VISIBLE

                    } catch (e: Exception) {
                        Log.d("tag", e.toString())
                    }

                }

                is ApiState.Error -> {
                    Log.e("error_response", state.message)
                    Toast.makeText(
                        applicationContext,
                        "something went wrong",
                        Toast.LENGTH_SHORT
                    ).show()

                    binding.pbr.visibility = View.VISIBLE
                    mediaRecyclerView.visibility = View.GONE
                }

                else -> {
                    // Handle other states
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
    }
}
