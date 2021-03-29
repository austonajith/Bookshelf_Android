package com.app.bookshelf.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.bookshelf.R
import com.app.bookshelf.databinding.RowBooksListBinding
import com.app.bookshelf.extension.getFormattedDate
import com.app.bookshelf.model.BooksListModel
import com.app.bookshelf.ui.listener.OnBookClickListener
import com.bumptech.glide.Glide

class BooksListAdapter(var listener: OnBookClickListener) : RecyclerView.Adapter<BooksListAdapter.MyViewHolder>() {

    var modelList: ArrayList<BooksListModel>? = null
    var modelListNoFilter: ArrayList<BooksListModel>? = null

    class MyViewHolder(private val binding: RowBooksListBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(model: BooksListModel, listener: OnBookClickListener){
            binding.apply {
                tvBookName.text = model.name
                tvAuthor.text = model.author
                tvPgCount.text = "${clRoot.context.getString(R.string.pgcount)} ${model.pageCount}"
                tvPubDate.text = model.publishedDate?.getFormattedDate()
                Glide.with(clRoot.context).load(model.imgUrl).into(ivBook)
                clRoot.setOnClickListener {
                    listener.onBookClicked(model)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            RowBooksListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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

    fun updateAdapter(newModelList: ArrayList<BooksListModel>?, isLocalSearch: Boolean=false) {
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