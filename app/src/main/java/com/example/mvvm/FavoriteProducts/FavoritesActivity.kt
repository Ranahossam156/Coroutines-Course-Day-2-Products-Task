package com.example.mvvm.FavoriteProducts

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mvvm.allProducts.AllProductViewModelFactory
import com.example.mvvm.allProducts.ProductRow
import com.example.mvvm.allProducts.ProductScreen
import com.example.mvvm.data.local.AppDatabase
import com.example.mvvm.data.local.ProductsLocalDataSource
import com.example.mvvm.data.remote.ProductsRemoteDataSource
import com.example.mvvm.data.remote.RetrofitHelper
import com.example.mvvm.data.repo.ProductRepositoryImplementation

class FavoritesActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FavoritesScreen(
                viewModel(factory = FavoriteProductsViewModelFactory(ProductRepositoryImplementation.getInstance( ProductsRemoteDataSource(RetrofitHelper.retrofitService),
                ProductsLocalDataSource(AppDatabase.getDatabase(this@FavoritesActivity).productDao())))
                )
            )
//            FavoritesScreen(
//                ViewModelProvider(this, FavoriteProductsViewModelFactory(
//                    ProductRepositoryImplementation.getInstance( ProductsRemoteDataSource(
//                        RetrofitHelper.retrofitService),
//                    ProductsLocalDataSource(AppDatabase.getDatabase(this@FavoritesActivity).productDao())
//                    )
//                )
//                ).get(FavoriteProductsViewModel::class.java)
//            )

        }
    }
    @Composable
    fun FavoritesScreen(viewModel: FavoriteProductsViewModel) {
        val productState=viewModel.products.observeAsState()
        val messageState=viewModel.message.observeAsState()
        viewModel.getFavorites()
        val snackBarHostState = remember{ SnackbarHostState() }

        val scope= rememberCoroutineScope()

        val context = LocalContext.current

        Scaffold (snackbarHost = { SnackbarHost(snackBarHostState) }){ contentPadding->
            LazyColumn(modifier = Modifier.fillMaxWidth().padding(contentPadding)) {
                items(productState.value?.size?:0) {
                    ProductRow(productState.value?.get(it),"Delete",{viewModel.deleteProduct(productState.value?.get(it))

                    })
                }
            }
            LaunchedEffect (messageState.value){
                if(!messageState.value.isNullOrEmpty())
                    snackBarHostState.showSnackbar(message = messageState.value.toString()
                        , duration = SnackbarDuration.Short)
            }

        }
    }
}