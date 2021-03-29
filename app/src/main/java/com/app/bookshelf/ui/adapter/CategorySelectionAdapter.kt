package com.app.bookshelf.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.app.bookshelf.R
import com.app.bookshelf.databinding.RowCategorySelectionBinding
import com.app.bookshelf.model.CategorySelectionModel
import com.app.bookshelf.ui.listener.OnCategorySelectionClickListener
import com.app.bookshelf.utils.AppUtils

class CategorySelectionAdapter(var listener: OnCategorySelectionClickListener): RecyclerView.Adapter<CategorySelectionAdapter.MyViewHolder>(){

    var modelList: ArrayList<CategorySelectionModel>? = ArrayList()
    var modelListNoFilter: ArrayList<CategorySelectionModel>? = null
    var isFilterEnabled = false

    init {
        setHasStableIds(true)
    }

    inner class MyViewHolder(private val binding: RowCategorySelectionBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(model: CategorySelectionModel, position: Int, listener: OnCategorySelectionClickListener){
            binding.apply {
                clRoot.setOnClickListener {
                    checkBoxCategory.performClick()
                    listener.onCategorySelected(model.name, position)
                    val idx = modelListNoFilter?.indexOf(model)
                    idx?.let {
                        modelListNoFilter?.set(idx, CategorySelectionModel(model.name, model.booksCount, checkBoxCategory.isChecked))
                    }
                    modelList?.set(position, CategorySelectionModel(model.name, model.booksCount, checkBoxCategory.isChecked))
                    updateAdapter(modelList, isFilterEnabled)
                }
                ivPattern.setImageResource(AppUtils.getRandomPattern())
                cardCategory.setCardBackgroundColor(ContextCompat.getColor(clRoot.context, AppUtils.getRandomColors()))
                tvCategoryName.text = model.name
                tvCategoryLabel.text = model.name
                //tvNoOfBooks.text = "${model.booksCount} ${clRoot.context.getString(R.string.books)}"
                checkBoxCategory.isChecked = model.isChecked

            }
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = RowCategorySelectionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val model = modelList?.get(position)
        model?.let {
            holder.bind(it, position, listener)
        }
    }

    override fun getItemCount(): Int {
        return modelList?.size ?: 0
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    fun updateAdapter(newModelList: ArrayList<CategorySelectionModel>?, isLocalSearch: Boolean=false) {

        newModelList?:return
        //val diff = DiffUtil.calculateDiff(MyDiffUtil(modelList?:return, newModelList))
        modelList = newModelList
        if (!isLocalSearch){
            modelListNoFilter = newModelList
        }
        notifyDataSetChanged()
        //diff.dispatchUpdatesTo(this)
    }

    fun doLocalSearch(p0: String) {

        if (p0.isNotEmpty()) {
            val searchList = modelListNoFilter?.filter {
                it.name?.contains(p0, true) ?: false
            }
            updateAdapter(ArrayList(searchList), true)
            isFilterEnabled = true
        } else {
            updateAdapter(modelListNoFilter)
            isFilterEnabled = false
        }
    }

    fun getSelectedCategories(): List<CategorySelectionModel>? {
        return modelListNoFilter?.filter { it.isChecked }
    }


    class MyDiffUtil(var oldList: ArrayList<CategorySelectionModel>, var newList: ArrayList<CategorySelectionModel>) :
        DiffUtil.Callback() {

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].name == newList[newItemPosition].name
        }

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].name != newList[newItemPosition].name
        }

    }
}