package com.app.bookshelf.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.app.bookshelf.databinding.RowMyCategoryBinding
import com.app.bookshelf.model.MyCategoryModel
import com.app.bookshelf.ui.listener.OnCategoryClickListener
import com.app.bookshelf.utils.AppUtils.getRandomColors
import com.app.bookshelf.utils.AppUtils.getRandomPattern

class MyCategoryAdapter(var listener: OnCategoryClickListener) : RecyclerView.Adapter<MyCategoryAdapter.MyViewHolder>() {

    var modelList: ArrayList<MyCategoryModel>? = null
    var modelListNoFilter: ArrayList<MyCategoryModel>? = null

    class MyViewHolder(private val binding: RowMyCategoryBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(model: MyCategoryModel, listener: OnCategoryClickListener){
            binding.apply {
                cardMyCategory.setOnClickListener {
                    listener.onCategoryClick(model.name)
                }
                ivPattern.setImageResource(getRandomPattern())
                cardMyCategory.setCardBackgroundColor(ContextCompat.getColor(cardMyCategory.context, getRandomColors()))
                tvCategoryName.text = model.name
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            RowMyCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val model = modelList?.get(position)
        model?.let {
            holder.bind(it, listener)
        }

    }

    override fun getItemCount(): Int {
        return modelList?.size ?: 0
    }

    fun updateAdapter(newModelList: ArrayList<MyCategoryModel>?, isLocalSearch: Boolean=false) {
        newModelList?:return
        modelList = newModelList
        if (!isLocalSearch){
            modelListNoFilter = newModelList
        }
        notifyDataSetChanged()
    }

    fun doLocalSearch(p0: String) {

        if (p0.isNotEmpty()) {
            val searchList = modelListNoFilter?.filter {
                it.name?.contains(p0, true) ?: false
            }
            updateAdapter(ArrayList(searchList), true)
        } else {
            updateAdapter(modelListNoFilter)
        }
    }
}