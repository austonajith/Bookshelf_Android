package com.app.bookshelf.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.app.bookshelf.R
import com.app.bookshelf.databinding.ActivityHomeBinding
import com.app.bookshelf.db.dao.BooksDatabase
import com.app.bookshelf.db.dao.entity.BooksEntity
import com.app.bookshelf.model.BooksResponseModel
import com.app.bookshelf.network.Status
import com.app.bookshelf.utils.AppUtils.disableDarkMode
import com.app.bookshelf.viewmodel.MainViewModel

class HomeActivity : AppCompatActivity() {

    private val TAG = "HomeActivity"
    private lateinit var binding: ActivityHomeBinding
    lateinit var viewModel: MainViewModel
    private var database: BooksDatabase? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        initViews()
    }

    private fun initViews() {
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        database = BooksDatabase.getInstance(this)
        val navHostFragment =
                supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNav.setupWithNavController(navController)

        attachObserver()

        viewModel.fetchBooksData()

    }

    private fun attachObserver() {
        viewModel.booksResponseLiveData.observe(this, {
            when (it.status) {
                Status.LOADING -> {
                    Log.d(TAG, "responseBooks: loading")
                }
                Status.SUCCESS -> {
                    it.data?.let { data ->
                        storeResponseData(data)
                    }

                    Log.d(TAG, "responseBooks: ${it.data?.toString()}")
                }
                Status.ERROR -> {
                    Toast.makeText(this, it.message?:"error", Toast.LENGTH_SHORT).show()
                    Log.d(TAG, "responseBooks: ${it.message}")
                }
            }
        })

        viewModel.storeMyCategoryDbLiveData.observe(this, {
            when (it.status) {
                Status.LOADING -> {
                    Log.d(TAG, "responseBooks: loading")
                }
                Status.SUCCESS -> {


                    Log.d(TAG, "responseBooks: ${it.data?.toString()}")
                }
                Status.ERROR -> {
                    Toast.makeText(this, it.message?:"error", Toast.LENGTH_SHORT).show()
                    Log.d(TAG, "responseBooks: ${it.message}")
                }
            }
        })


    }


    private fun storeResponseData(data: BooksResponseModel?) {
        val entityList = ArrayList<BooksEntity>()
        data?.forEach {

            it.apply {
                val booksEntity = BooksEntity(
                        0,
                        title,
                        isbn,
                        pageCount?.toString(),
                        publishedDate?.`$date`,
                        longDescription,
                        status,
                        authors?.joinToString(),
                        categories?.joinToString(),
                        thumbnailUrl,
                        shortDescription
                )
                entityList.add(booksEntity)
            }

        }
        viewModel.storeBookData(entityList, database ?: return)
    }

}