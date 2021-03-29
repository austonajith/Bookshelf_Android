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
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.bookshelf.Constants.CATEGORY_NAME
import com.app.bookshelf.R
import com.app.bookshelf.databinding.FragmentMyCategoriesBinding
import com.app.bookshelf.db.dao.BooksDatabase
import com.app.bookshelf.model.CategorySelectionModel
import com.app.bookshelf.model.MyCategoryModel
import com.app.bookshelf.network.Status
import com.app.bookshelf.ui.adapter.MyCategoryAdapter
import com.app.bookshelf.ui.listener.OnCategoryClickListener
import com.app.bookshelf.viewmodel.MainViewModel

class MyCategoriesFragment : Fragment(), OnCategoryClickListener {

    private val TAG = "MyCategoriesFragment"
    var adapter: MyCategoryAdapter?= null
    private lateinit var binding:FragmentMyCategoriesBinding
    var viewModel: MainViewModel?=null
    private var database: BooksDatabase? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding =  FragmentMyCategoriesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        database = BooksDatabase.getInstance(requireContext())
        adapter = MyCategoryAdapter(this)
        binding.rvMyCategory.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = this@MyCategoriesFragment.adapter
        }

        viewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        binding.etSearch.addTextChangedListener {
            adapter?.doLocalSearch(it.toString())
        }
        binding.fabAdd.setOnClickListener {
            startActivity(Intent(requireContext(), CategorySelectionActivity::class.java))
        }

    }

    override fun onResume() {
        super.onResume()
        attachObserver()
        viewModel?.getMyCategory(database)
    }

    private fun attachObserver() {
        viewModel?.retriveMyCategoryDbLiveData?.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.LOADING -> {
                }
                Status.SUCCESS -> {
                    val categories = it.data?.map {
                        MyCategoryModel(it.name)
                    }

                    adapter?.updateAdapter(ArrayList(categories))
                }
                Status.ERROR -> {
                    Toast.makeText(requireContext(), it.message?:"error", Toast.LENGTH_SHORT).show()
                    Log.d(TAG, "responseBooks: ${it.message}")
                }
            }
        })
    }

    override fun onCategoryClick(category: String?) {
        val intent = Intent(requireContext(), BooksActivity::class.java)
        intent.putExtra(CATEGORY_NAME, category)
        startActivity(intent)
    }



}