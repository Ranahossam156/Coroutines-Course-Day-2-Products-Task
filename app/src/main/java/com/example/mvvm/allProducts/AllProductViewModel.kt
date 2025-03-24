package com.example.mvvm.allProducts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.mvvm.Response
import com.example.mvvm.data.models.Product
import com.example.mvvm.data.repo.ProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class AllProductViewModel(private val repo:ProductRepository):ViewModel (){
//    private val mutableProducts:MutableLiveData<List<Product>?> = MutableLiveData()
//    val products:LiveData<List<Product>?> =mutableProducts
    private val _productList = MutableStateFlow<Response>(Response.Loading)
    val productList=_productList.asStateFlow()
//    private val mutableMessage:MutableLiveData<String> = MutableLiveData()
//    val message:LiveData<String> =mutableMessage
    private val _mutableMessage= MutableSharedFlow<String>()
    val mutableMessage=_mutableMessage.asSharedFlow()

    private val mutableLoading: MutableLiveData<Boolean> = MutableLiveData()
    val loading: LiveData<Boolean> = mutableLoading
//    fun getProducts(){
//        viewModelScope.launch (Dispatchers.IO){
//            try {
//                val result=repo.getAllProducts(true)
//                if(result!=null){
//                    val list:List<Product> =result
//                    mutableProducts.postValue(list)
//                }
//                else{
//                    mutableMessage.postValue("please try again later")
//                }
//            }catch (ex:Exception){
//                mutableMessage.postValue("An error occurred, ${ex.message}")
//            }
//        }
//    }
fun getProducts() {
    viewModelScope.launch(Dispatchers.IO) {
        try {
            mutableLoading.postValue(true)
            val products = repo.getAllProducts()
            products.catch { ex ->
                _productList.value=Response.Failure(ex)
                _mutableMessage.emit("Error From API: ${ex.message}")
//                mutableLoading.postValue(false)
//                mutableMessage.postValue("An error occurred: ${ex.message}")
            }
            .collect {
                _productList.value=Response.Success(it)
//                result ->
//                mutableLoading.postValue(false)
//                if (result != null && result.isNotEmpty()) {
//                    mutableProducts.postValue(result)
//                } else {
//                    mutableMessage.postValue("Please try again later")
//                }
            }
        } catch (e: Exception) {
            _productList.value=Response.Failure(e)
            _mutableMessage.emit("Error:${e.message}")
        }
    }
}
    fun addToFavorites(product:Product?){
        if(product!=null){
            viewModelScope.launch (Dispatchers.IO){
                try{
                    val result =repo.addProduct(product)
                    if(result>0){
                        _mutableMessage.emit("Added successfully!")
                    }
                    else{
                        _mutableMessage.emit("Product is already in favorites!")
                    }

                }catch (ex:Exception){
                    _mutableMessage.emit("Couldn't add Product, missing data")

                }
            }

        }
    }
}
class AllProductViewModelFactory(private val repo: ProductRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(AllProductViewModel::class.java)) {
            AllProductViewModel(repo) as T
        }
        else{
            throw IllegalArgumentException("viewModel Class not found")

        }
    }
}
