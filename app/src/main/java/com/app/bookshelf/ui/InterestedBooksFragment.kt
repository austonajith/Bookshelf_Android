package com.app.bookshelf.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.bookshelf.Constants
import com.app.bookshelf.R
import com.app.bookshelf.databinding.FragmentInterestedBooksBinding
import com.app.bookshelf.databinding.FragmentMyCategoriesBinding
import com.app.bookshelf.db.dao.BooksDatabase
import com.app.bookshelf.model.BooksListModel
import com.app.bookshelf.model.MyCategoryModel
import com.app.bookshelf.network.Status
import com.app.bookshelf.ui.adapter.BooksListAdapter
import com.app.bookshelf.ui.listener.OnBookClickListener
import com.app.bookshelf.viewmodel.MainViewModel

class InterestedBooksFragment : Fragment(), OnBookClickListener {

    var adapter: BooksListAdapter?= null
    private val TAG = "InterestedBooksFragment"
    private lateinit var binding: FragmentInterestedBooksBinding
    var viewModel: MainViewModel?=null
    private var database: BooksDatabase? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =  FragmentInterestedBooksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        database = BooksDatabase.getInstance(requireContext())
        viewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        adapter = BooksListAdapter(this)
        binding.rvBooksList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@InterestedBooksFragment.adapter
        }
        //adapter?.updateAdapter(getDummyData())

        binding.etSearch.addTextChangedListener {
            adapter?.doLocalSearch(it.toString())
        }


    }

    private fun attachObserver() {
        viewModel?.retriveAllFavBookDbLiveData?.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.LOADING -> {
                }
                Status.SUCCESS -> {
                    it?.data?.let {
                        val result = it.map {
                            BooksListModel(it.title,it.authors,it.publishedDate,it.pageCount,it.imgUrl,it.categories,it.isbn,it.shortDescription, it.longDescription,it.status)

                        }
                        adapter?.updateAdapter(ArrayList(result))
                    }
                }
                Status.ERROR -> {
                    Toast.makeText(requireContext(), it.message?:"error", Toast.LENGTH_SHORT).show()
                    Log.d(TAG, "responseBooks: ${it.message}")
                }
            }
        })
    }

    override fun onBookClicked(model: BooksListModel?) {
        model?:return
        val intent = Intent(requireContext(), BookDetailActivity::class.java)
        intent.putExtra(Constants.BOOK_DATA, model )
        startActivity(intent)

    }


    override fun onResume() {
        super.onResume()
        attachObserver()
        viewModel?.getAllFavBook(database)
    }



}