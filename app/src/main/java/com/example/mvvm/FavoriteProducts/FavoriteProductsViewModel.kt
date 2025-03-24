package com.example.mvvm.FavoriteProducts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.mvvm.allProducts.AllProductViewModel
import com.example.mvvm.data.models.Product
import com.example.mvvm.data.repo.ProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoriteProductsViewModel(private val repo:ProductRepository):ViewModel() {
    private val mutableProducts :MutableLiveData<List<Product>> = MutableLiveData()
    val products: LiveData<List<Product>> = mutableProducts
    private val mutableMessage: MutableLiveData<String> = MutableLiveData("")
    val message: LiveData<String> =mutableMessage
    fun deleteProduct(product: Product?){
        if(product!=null){
            viewModelScope.launch (Dispatchers.IO){
                try{
                    val result =repo.removeProduct(product)
                    if(result>0){
                        mutableMessage.postValue("Removed successfully!")
                        getFavorites()
                    }
                    else{
                        mutableMessage.postValue("Product is already not in favorites!")
                    }

                }catch (ex:Exception){
                    mutableMessage.postValue("Couldn't remove product, missing data")

                }
            }

        }
    }
//    fun getFavorites(){
//        viewModelScope.launch (Dispatchers.IO){
//            try {
//                val result=repo.getAllProducts(false)
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
    fun getFavorites(){
        viewModelScope.launch {
            val localProducts=repo.getStoredProducts()
            if (localProducts != null) {
                localProducts.collect{
                    mutableProducts.value=it
                }
            }
        }
    }

}
class FavoriteProductsViewModelFactory(private val repo: ProductRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(FavoriteProductsViewModel::class.java)) {
            FavoriteProductsViewModel(repo) as T
        }
        else{
            throw IllegalArgumentException("viewModel Class not found")

        }
    }
}