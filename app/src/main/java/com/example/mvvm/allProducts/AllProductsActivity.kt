package com.example.mvvm.allProducts

import android.content.res.Configuration
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.mvvm.Response
import com.example.mvvm.data.local.AppDatabase
import com.example.mvvm.data.local.ProductDao
import com.example.mvvm.data.local.ProductsLocalDataSource
import com.example.mvvm.data.models.Product
import com.example.mvvm.data.remote.ProductsRemoteDataSource
import com.example.mvvm.data.remote.RetrofitHelper
import com.example.mvvm.data.repo.ProductRepository
import com.example.mvvm.data.repo.ProductRepositoryImplementation

class AllProductsActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {
            ProductScreen(viewModel(factory = AllProductViewModelFactory(ProductRepositoryImplementation.getInstance( ProductsRemoteDataSource(RetrofitHelper.retrofitService),
                ProductsLocalDataSource(AppDatabase.getDatabase(this@AllProductsActivity).productDao())))))
//            ProductScreen(
//                ViewModelProvider(this,AllProductViewModelFactory(ProductRepositoryImplementation.getInstance( ProductsRemoteDataSource(RetrofitHelper.retrofitService),
//                    ProductsLocalDataSource(AppDatabase.getDatabase(this@AllProductsActivity).productDao()))
//                )).get(AllProductViewModel::class.java)
//                viewModel
//            )

        }
    }
}
@Composable
fun ProductScreen(viewModel: AllProductViewModel) {
    val uiState by viewModel.productList.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.getProducts()
        viewModel.mutableMessage.collect { message ->
            snackBarHostState.showSnackbar(message = message, duration = SnackbarDuration.Short)
        }
    }

    Scaffold(snackbarHost = { SnackbarHost(snackBarHostState) }) { contentPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding),
            contentAlignment = Alignment.Center
        ) {
            when (uiState) {
                is Response.Loading -> {
                    CircularProgressIndicator()
                }

                is Response.Failure -> {
                    Text(text = "No products available")
                }

                is Response.Success -> {
                    val productList = (uiState as Response.Success).data ?: emptyList()
                    LazyColumn(modifier = Modifier.fillMaxWidth()) {
                        items(productList.size) { index ->
                            ProductRow(
                                product = productList[index],
                                actionName = "Add To Favorite",
                                action = { viewModel.addToFavorites(productList[index]) }
                            )
                        }
                    }
                }
            }
        }
    }
}
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ProductRow(product: Product?, actionName:String, action:() -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(modifier = Modifier.padding(12.dp)) {
            GlideImage(
                model = product?.thumbnail,
                contentDescription = "Product Image",
                modifier = Modifier.size(80.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = product?.title ?: "Unknown Product",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "$${product?.price ?: 0}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Button(
                onClick = { action()},
                modifier = Modifier.align(alignment = androidx.compose.ui.Alignment.CenterVertically)
            ) {
                Text(text = actionName)
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ProductDetails(product: Product, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .padding(16.dp)
    ) {
        GlideImage(
            model = product.thumbnail,
            contentDescription = "Product Image",
            modifier = Modifier
                .height(200.dp)
                .fillMaxWidth()
        )
        Text(text = product.title, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 8.dp))
        Text(text = "Price: $${product.price}", modifier = Modifier.padding(top = 4.dp))
        Text(text = product.description, modifier = Modifier.padding(top = 8.dp))
    }
}


//@Composable
//fun ProductScreen(viewModel: AllProductViewModel) {
//    val uiState by viewModel.productList.collectAsStateWithLifecycle()
//    val context= LocalContext.current
////    val productState = viewModel.products.observeAsState()
////    val messageState = viewModel.message.observeAsState()
//    val loadingState = viewModel.loading.observeAsState(initial = false)
//
//    val snackBarHostState = remember { SnackbarHostState() }
//
//    LaunchedEffect(Unit) {
//        viewModel.mutableMessage.collect{message->Toast.makeText(context,message,Toast.LENGTH_SHORT).show()}
//    }
//
//    Scaffold(snackbarHost = { SnackbarHost(snackBarHostState) }) { contentPadding ->
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(contentPadding)
//        ) {
//            when(uiState) {
//                is Response.Loading -> {
//                    CircularProgressIndicator(
//                        modifier = Modifier.align(Alignment.Center)
//                    )
//                }
//                is Response.Failure -> {
//                    Text(
//                        text = "No products available",
//                        modifier = Modifier.align(Alignment.Center)
//                    )
//                }
//                is Response.Success -> {
//                    LazyColumn(modifier = Modifier.fillMaxWidth()) {
//
//                        items(productState.value?.size ?: 0) {
//                            ProductRow(
//                                productState.value?.get(it),
//                                "Add To Favorite",
//                                { viewModel.addToFavorites(productState.value?.get(it)) }
//                            )
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//    LaunchedEffect(messageState.value) {
//        if (!messageState.value.isNullOrEmpty()) {
//            snackBarHostState.showSnackbar(
//                message = messageState.value.toString(),
//                duration = SnackbarDuration.Short
//            )
//        }
//    }
//}



//@Composable
//fun ProductScreen(viewModel: AllProductViewModel) {
//    val loadingState = viewModel.loading.observeAsState(initial = false)
//    val productState=viewModel.products.observeAsState()
//    val messageState=viewModel.message.observeAsState()
//    viewModel.getProducts()
//    val snackBarHostState = remember{SnackbarHostState()}
//
//    val scope= rememberCoroutineScope()
//
//    val context = LocalContext.current
//    val isPortrait = context.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT
//    val selectedProduct = remember { mutableStateOf<Product?>(null) }
//
//    Scaffold (snackbarHost = {SnackbarHost(snackBarHostState)}){contentPadding->
//        LazyColumn(modifier = Modifier.fillMaxWidth().padding(contentPadding)) {
//            items(productState.value?.size?:0) {
//                ProductRow(productState.value?.get(it),"Add To Favorite",{viewModel.addToFavorites(productState.value?.get(it))
//
//                })
//            }
//        }
//        LaunchedEffect (messageState.value){
//            if(!messageState.value.isNullOrEmpty())
//                snackBarHostState.showSnackbar(message = messageState.value.toString()
//                    , duration = SnackbarDuration.Short)
//        }
//
//    }
//
//}

