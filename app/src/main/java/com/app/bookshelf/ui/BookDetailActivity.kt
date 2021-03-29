package com.app.bookshelf.ui

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.app.bookshelf.Constants
import com.app.bookshelf.R
import com.app.bookshelf.databinding.ActivityBookDetailBinding
import com.app.bookshelf.databinding.FragmentInterestedBooksBinding
import com.app.bookshelf.db.dao.BooksDatabase
import com.app.bookshelf.db.dao.entity.FavoriteBooksEntity
import com.app.bookshelf.extension.getFormattedDate
import com.app.bookshelf.model.BooksListModel
import com.app.bookshelf.network.Status
import com.app.bookshelf.utils.AppUtils
import com.app.bookshelf.viewmodel.MainViewModel
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar

class BookDetailActivity : AppCompatActivity() {

    private val TAG = "BookDetailActivity"
    private lateinit var binding: ActivityBookDetailBinding
    private var database: BooksDatabase? = null
    lateinit var viewModel: MainViewModel
    var bookData: BooksListModel? =null
    var isFavorite = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookDetailBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        initViews()
    }

    private fun initViews() {
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        database = BooksDatabase.getInstance(this)
        bookData = intent?.getSerializableExtra(Constants.BOOK_DATA) as BooksListModel
        binding.apply {
            btnBack.setOnClickListener {
                onBackPressed()
            }
            bookData?.apply {
                Glide.with(this@BookDetailActivity).load(imgUrl).into(ivBook)
                tvBookName.text = name
                tvAuthor.text = author
                tvPubDate.text = publishedDate?.getFormattedDate()
                tvIsbn.text = isbn
                tvShortDesc.text =shortDescription
                tvLongDesc.text = longDescription
            }


            attachObserver()
            viewModel.getFavBook(bookData?.isbn?:return@apply, database)
            fabBookmark.setOnClickListener {


                bookData?.apply {
                    if (isFavorite){
                        viewModel.delFavBook(isbn?:return@apply, database)
                    }else{
                        val model = FavoriteBooksEntity(isbn?:return@apply,name,pageCount,publishedDate,longDescription,status,author,category,imgUrl, shortDescription)
                        viewModel.insertFavBook(model, database)
                    }


                }
            }
        }
    }

    private fun attachObserver() {
        viewModel.deleteFavBookDbWithIdLiveData.observe(this, {
            when (it.status) {
                Status.LOADING -> {
                }
                Status.SUCCESS -> {
                    viewModel.getFavBook(bookData?.isbn?:return@observe, database)
                    Snackbar.make(binding.clRoot, "Succesfully Removed", Snackbar.LENGTH_LONG).show()
                }
                Status.ERROR -> {
                    Toast.makeText(this, it.message?:"error", Toast.LENGTH_SHORT).show()
                    Log.d(TAG, "responseBooks: ${it.message}")
                }
            }
        })

        viewModel.retriveFavBookDbLiveData.observe(this, {
            when (it.status) {
                Status.LOADING -> {
                }
                Status.SUCCESS -> {
                    it.data?.let { data ->
                        isFavorite = true
                        binding.fabBookmark.setColorFilter(ContextCompat.getColor(this@BookDetailActivity, R.color.blue_500))
                    } ?: kotlin.run {
                        isFavorite = false
                        binding.fabBookmark.setColorFilter(ContextCompat.getColor(this@BookDetailActivity, R.color.gray))
                    }
                }
                Status.ERROR -> {
                    Toast.makeText(this, it.message?:"error", Toast.LENGTH_SHORT).show()
                    Log.d(TAG, "responseBooks: ${it.message}")
                }
            }
        })
        viewModel.insertFavBookDbLiveData.observe(this, {
            when (it.status) {
                Status.LOADING -> {
                }
                Status.SUCCESS -> {
                    Snackbar.make(binding.clRoot, "Succesfully Added", Snackbar.LENGTH_LONG).show()
                    viewModel.getFavBook(bookData?.isbn?:return@observe, database)
                }
                Status.ERROR -> {
                    Toast.makeText(this, it.message?:"error", Toast.LENGTH_SHORT).show()
                    Log.d(TAG, "responseBooks: ${it.message}")
                }
            }
        })
    }


}