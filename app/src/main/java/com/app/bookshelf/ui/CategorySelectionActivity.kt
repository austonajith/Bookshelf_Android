package com.app.bookshelf.ui

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.bookshelf.databinding.ActivityCategoriesSelectionBinding
import com.app.bookshelf.db.dao.BooksDatabase
import com.app.bookshelf.db.dao.entity.BooksEntity
import com.app.bookshelf.db.dao.entity.MyCategoryEntity
import com.app.bookshelf.model.CategorySelectionModel
import com.app.bookshelf.network.Status
import com.app.bookshelf.ui.adapter.CategorySelectionAdapter
import com.app.bookshelf.ui.listener.OnCategorySelectionClickListener
import com.app.bookshelf.utils.AppUtils.disableDarkMode
import com.app.bookshelf.viewmodel.MainViewModel


class CategorySelectionActivity : AppCompatActivity(), OnCategorySelectionClickListener {

    private val TAG = "CategorySelectionActivi"
    var adapter: CategorySelectionAdapter?= null
    lateinit var viewModel: MainViewModel
    private var database: BooksDatabase? = null
    private lateinit var binding: ActivityCategoriesSelectionBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoriesSelectionBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        initViews()
    }

    private fun initViews() {
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        database = BooksDatabase.getInstance(this)
        adapter = CategorySelectionAdapter(this)
        binding.rvCategorySelection.apply {
            layoutManager = LinearLayoutManager(this@CategorySelectionActivity)
            adapter = this@CategorySelectionActivity.adapter
        }

        binding.etSearch.addTextChangedListener {
            adapter?.doLocalSearch(it.toString())
        }
        binding.btnBack.setOnClickListener {
            onBackPressed()
        }
        binding.btnSave.setOnClickListener {
            val selectedCategories = adapter?.getSelectedCategories()?.map {
                MyCategoryEntity(0, it.name)
            }
            viewModel.insertMyCategory(ArrayList(selectedCategories), database)
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
                    it.data?.let { data ->
                      val formatted =  getCategories(data).map {
                          CategorySelectionModel(it)
                        }
                        adapter?.updateAdapter(ArrayList(formatted))
                        viewModel.getMyCategory(database)
                    }
                }
                Status.ERROR -> {
                    Toast.makeText(this, it.message?:"error", Toast.LENGTH_SHORT).show()
                    Log.d(TAG, "responseBooks: ${it.message}")
                }
            }
        })

        viewModel.myCategoryDbLiveData.observe(this, {
            when (it.status) {
                Status.LOADING -> {
                }
                Status.SUCCESS -> {
                    onBackPressed()
                }
                Status.ERROR -> {
                    Toast.makeText(this, it.message?:"error", Toast.LENGTH_SHORT).show()
                    Log.d(TAG, "responseBooks: ${it.message}")
                }
            }
        })

        viewModel.retriveMyCategoryDbLiveData.observe(this, {
            when (it.status) {
                Status.LOADING -> {
                }
                Status.SUCCESS -> {
                    it.data?.let { data ->
                        adapter?.modelListNoFilter?.forEachIndexed { index, categorySelectionModel ->
                            data.forEach {
                                if (categorySelectionModel.name == it.name){
                                    adapter?.modelListNoFilter?.set(index, CategorySelectionModel(it.name, isChecked = true))
                                    adapter?.updateAdapter(adapter?.modelListNoFilter)
                                }
                            }

                        }
                    }
                }
                Status.ERROR -> {
                    Toast.makeText(this, it.message?:"error", Toast.LENGTH_SHORT).show()
                    Log.d(TAG, "responseBooks: ${it.message}")
                }
            }
        })



    }

    private fun getCategories(data: List<BooksEntity>): List<String> {
        val category = ArrayList<String>()
        data.forEach {
            val convertToList = it.categories?.split(",")?.map { it.trim() }
            convertToList?.let {
                category.addAll(convertToList)
            }
        }
        return category.distinct()

    }

    private fun getDummyMyCategoryData(): ArrayList<CategorySelectionModel>? {
        val data = arrayListOf(
            CategorySelectionModel("Open Source", "15"),
            CategorySelectionModel("Mobile", "8"),
            CategorySelectionModel("Java", "7"),
            CategorySelectionModel("Software Engineering", "16"),
            CategorySelectionModel("Internet", "56"),
            CategorySelectionModel("Web Development", "35"),
            CategorySelectionModel("Miscellaneous", "89"),
            CategorySelectionModel("Microsoft .NET", "54"),
            CategorySelectionModel("Microsoft", "76"),
            CategorySelectionModel("Next Generation Databases", "19"),
            CategorySelectionModel("PowerBuilder", "28"),

            )
        return data
    }

    override fun onCategorySelected(name: String?, position: Int?) {

    }


}