package com.app.bookshelf.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.bookshelf.Constants.BOOK_DATA
import com.app.bookshelf.Constants.BOOK_ISBN
import com.app.bookshelf.Constants.CATEGORY_NAME
import com.app.bookshelf.R
import com.app.bookshelf.databinding.ActivityBookBinding
import com.app.bookshelf.db.dao.BooksDatabase
import com.app.bookshelf.model.BooksListModel
import com.app.bookshelf.model.CategorySelectionModel
import com.app.bookshelf.network.Status
import com.app.bookshelf.ui.adapter.BooksListAdapter
import com.app.bookshelf.ui.listener.OnBookClickListener
import com.app.bookshelf.utils.AppUtils
import com.app.bookshelf.viewmodel.MainViewModel

class BooksActivity : AppCompatActivity(), OnBookClickListener {

    private val TAG = "BooksActivity"
    var category: String?= null
    var adapter: BooksListAdapter?= null
    private lateinit var binding: ActivityBookBinding
    lateinit var viewModel: MainViewModel
    private var database: BooksDatabase? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        initViews()
    }

    private fun initViews() {
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        database = BooksDatabase.getInstance(this)
        category= intent?.getStringExtra(CATEGORY_NAME)
        binding.tvCategoryName.text = category
        adapter = BooksListAdapter(this)
        binding.rvBooksList.apply {
            layoutManager = LinearLayoutManager(this@BooksActivity)
            adapter = this@BooksActivity.adapter
        }
        //adapter?.updateAdapter(getDummyData())

        binding.etSearch.addTextChangedListener {
            adapter?.doLocalSearch(it.toString())
        }
        binding.btnBack.setOnClickListener {
            onBackPressed()
        }

        attachObserver()
        viewModel.getBooksDbData(database)
    }


    private fun attachObserver() {
        viewModel.booksDbLiveData.observe(this, {
            when (it.status) {
                Status.LOADING -> {
                }
                Status.SUCCESS -> {
                    it.data?.let{data->

                        val filteredBooks = data.filter {
                            it.categories?.contains(category?:return@observe) == true
                        }

                      val booksList =  filteredBooks.map {
                            BooksListModel(it.title,it.authors,it.publishedDate,it.pageCount,it.imgUrl,it.categories,it.isbn,it.shortDescription,it.longDescription,it.status)
                        }
                        adapter?.updateAdapter(ArrayList(booksList))

                    }
                }
                Status.ERROR -> {
                    Toast.makeText(this, it.message?:"error", Toast.LENGTH_SHORT).show()
                    Log.d(TAG, "responseBooks: ${it.message}")
                }
            }
        })

    }

    override fun onBookClicked(model: BooksListModel?) {
        model?:return
        val intent = Intent(this, BookDetailActivity::class.java)
        intent.putExtra(BOOK_DATA, model )
        startActivity(intent)
    }


}