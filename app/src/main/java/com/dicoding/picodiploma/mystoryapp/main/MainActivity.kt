package com.dicoding.picodiploma.mystoryapp.main

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.picodiploma.mystoryapp.ViewModelFactory
import com.dicoding.picodiploma.mystoryapp.addstory.AddStoryActivity
import com.dicoding.picodiploma.mystoryapp.data.ResultState
import com.dicoding.picodiploma.mystoryapp.welcome.WelcomeActivity
import com.mystoryapp.R
import com.mystoryapp.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.show()

        binding.fabUpload.setOnClickListener {
            startActivity(Intent(this, AddStoryActivity::class.java))
        }

        viewModel.getSession().observe(this){
            if (!it.isLogin){
                val intent = Intent(this, WelcomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }else{
                setupAction()
            }
        }
        val layoutManager = LinearLayoutManager(this)
        binding.rvReview.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvReview.addItemDecoration(itemDecoration)
    }

    private fun setupAction(){
        lifecycleScope.launch {
            viewModel.getStories().observe(this@MainActivity) { story ->
                when (story) {
                    is ResultState.Error -> {
                        binding.progressBar.visibility = View.INVISIBLE
                        val error = story.error
                        Toast.makeText(this@MainActivity, error, Toast.LENGTH_SHORT).show()
                    }

                    is ResultState.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }

                    is ResultState.Success -> {
                        binding.progressBar.visibility = View.INVISIBLE
                        val adapter = MainAdapter()
                        adapter.submitList(story.data)
                        binding.rvReview.adapter = adapter
                    }
                }
            }
        }
    }



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.logout -> {
                lifecycleScope.launch {
                    viewModel.logout()
                    val intent = Intent(this@MainActivity,WelcomeActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
            R.id.setting->{
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
            }
        }
        return super.onOptionsItemSelected(item)
    }
}