package com.app.bookshelf.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.bookshelf.db.dao.BooksDatabase
import com.app.bookshelf.db.dao.entity.BooksEntity
import com.app.bookshelf.db.dao.entity.FavoriteBooksEntity
import com.app.bookshelf.db.dao.entity.MyCategoryEntity
import com.app.bookshelf.model.BooksResponseModel
import com.app.bookshelf.network.Resource
import com.app.bookshelf.repo.MainRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    var booksResponseLiveData = MutableLiveData<Resource<BooksResponseModel>>()
    var booksDbLiveData = MutableLiveData<Resource<List<BooksEntity>>>()
    var myCategoryDbLiveData = MutableLiveData<Resource<List<MyCategoryEntity>>>()
    var retriveMyCategoryDbLiveData = MutableLiveData<Resource<List<MyCategoryEntity>>>()
    var storeMyCategoryDbLiveData = MutableLiveData<Resource<List<MyCategoryEntity>>>()
    var retriveFavBookDbLiveData = MutableLiveData<Resource<FavoriteBooksEntity>>()
    var retriveAllFavBookDbLiveData = MutableLiveData<Resource<List<FavoriteBooksEntity>>>()
    var insertFavBookDbLiveData = MutableLiveData<Resource<List<FavoriteBooksEntity>>>()
    var deleteFavBookDbWithIdLiveData = MutableLiveData<Resource<List<FavoriteBooksEntity>>>()


    fun fetchBooksData() {

        viewModelScope.launch(Dispatchers.IO) {
            try {
                booksResponseLiveData.postValue(Resource.loading(null))
                MainRepository.getBooksData().let {
                    if (it.isSuccessful) {
                        booksResponseLiveData.postValue(Resource.success(it.body()))
                    } else {
                        booksResponseLiveData.postValue(Resource.error(it.errorBody().toString(), null))
                    }
                }
            }catch (e: Exception){
                booksResponseLiveData.postValue(Resource.error(e.message?:"Error", null))
                e.printStackTrace()
            }

        }
    }

    fun storeBookData(payload: ArrayList<BooksEntity>, database: BooksDatabase){

        viewModelScope.launch(Dispatchers.IO) {
            try {
                MainRepository.storeBookData(payload, database)
                storeMyCategoryDbLiveData.postValue(Resource.success(null))
            }catch (e:Exception){
                storeMyCategoryDbLiveData.postValue(Resource.error(e.message?:"Error", null))
            }

        }
    }

    fun getBooksDbData(database: BooksDatabase?){
        database?:return
        viewModelScope.launch (Dispatchers.IO){
            booksDbLiveData.postValue(Resource.loading(null))
            try {
                val result = MainRepository.getBooksDbData(database)
                booksDbLiveData.postValue(Resource.success(result))
            }catch (e: java.lang.Exception){
                booksDbLiveData.postValue(Resource.error(e.message?:"Error", null))
            }
        }
    }

    fun insertMyCategory(payload: ArrayList<MyCategoryEntity>, database: BooksDatabase?){
        database?:return
        viewModelScope.launch (Dispatchers.IO){
            myCategoryDbLiveData.postValue(Resource.loading(null))
            try {
                MainRepository.deleteMyCategory( database)
                MainRepository.insertMyCategory(payload, database)
                myCategoryDbLiveData.postValue(Resource.success(null))
            }catch (e: java.lang.Exception){
                myCategoryDbLiveData.postValue(Resource.error(e.message?:"Error", null))
            }
        }
    }

    fun getMyCategory(database: BooksDatabase?){
        database?:return
        viewModelScope.launch (Dispatchers.IO){
            retriveMyCategoryDbLiveData.postValue(Resource.loading(null))
            try {
                val result = MainRepository.getMyCategory(database)
                retriveMyCategoryDbLiveData.postValue(Resource.success(result))
            }catch (e: java.lang.Exception){
                retriveMyCategoryDbLiveData.postValue(Resource.error(e.message?:"Error", null))
            }
        }
    }

    fun insertFavBook(payload: FavoriteBooksEntity, database: BooksDatabase?){
        database?:return
        viewModelScope.launch (Dispatchers.IO){
            insertFavBookDbLiveData.postValue(Resource.loading(null))
            try {
                MainRepository.insertFavoriteBooks(payload, database)
                insertFavBookDbLiveData.postValue(Resource.success(null))
            }catch (e: java.lang.Exception){
                insertFavBookDbLiveData.postValue(Resource.error(e.message?:"Error", null))
            }
        }
    }

    fun getFavBook(isbn: String, database: BooksDatabase?){
        database?:return
        viewModelScope.launch (Dispatchers.IO){
            retriveFavBookDbLiveData.postValue(Resource.loading(null))
            try {
                val result = MainRepository.getFavoriteBooks(isbn, database)
                retriveFavBookDbLiveData.postValue(Resource.success(result))
            }catch (e: java.lang.Exception){
                retriveFavBookDbLiveData.postValue(Resource.error(e.message?:"Error", null))
            }
        }
    }

    fun getAllFavBook(database: BooksDatabase?){
        database?:return
        viewModelScope.launch (Dispatchers.IO){
            retriveAllFavBookDbLiveData.postValue(Resource.loading(null))
            try {
                val result = MainRepository.getAllFavoriteBooks(database)
                retriveAllFavBookDbLiveData.postValue(Resource.success(result))
            }catch (e: java.lang.Exception){
                retriveAllFavBookDbLiveData.postValue(Resource.error(e.message?:"Error", null))
            }
        }
    }

    fun delFavBook(isbn: String, database: BooksDatabase?){
        database?:return
        viewModelScope.launch (Dispatchers.IO){
            deleteFavBookDbWithIdLiveData.postValue(Resource.loading(null))
            try {
                val result = MainRepository.deleteFavBookWithId(isbn, database)
                deleteFavBookDbWithIdLiveData.postValue(Resource.success(null))
            }catch (e: java.lang.Exception){
                deleteFavBookDbWithIdLiveData.postValue(Resource.error(e.message?:"Error", null))
            }
        }
    }

}