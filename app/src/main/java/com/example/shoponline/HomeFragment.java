package com.example.shoponline;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.shoponline.adapter.CategoryAdapter;
import com.example.shoponline.adapter.ProductAdapter;
import com.example.shoponline.adapter.ProductHotAdapter;
import com.example.shoponline.constant.Ustils;
import com.example.shoponline.model.Category;
import com.example.shoponline.model.Product;
import com.example.shoponline.retrofit.APIShop;
import com.example.shoponline.retrofit.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class HomeFragment extends Fragment {
    ImageView bannerimg;
    RecyclerView listcategory, listProductHot, listProduct;
    CategoryAdapter categoryAdapter;
    ProductAdapter productAdapter;
    ProductHotAdapter productHotAdapter;

    List<Category> arrCategory;
    List<Product> arrProduct, arrProductHot;
    APIShop apiShop;
    CompositeDisposable disposable = new CompositeDisposable();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        init(view);
        category();
        productHot();
        product();
        return view;
    }

    private void product() {
        disposable.add(apiShop.getProduct()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        productModel -> {
                            if(productModel.isSuccess()){
                                arrProduct = productModel.getData();
                                productAdapter = new ProductAdapter(getActivity(), arrProduct);
                                listProduct.setAdapter(productAdapter);
                            }
                        },
                        throwable -> {
                            Toast.makeText(getActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                ));
    }

    private void productHot() {
        disposable.add(apiShop.getProduct()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        productModel -> {
                            if(productModel.isSuccess()){
                                arrProductHot = productModel.getData();
                                productHotAdapter = new ProductHotAdapter(getActivity(), arrProduct);
                                listProductHot.setAdapter(productHotAdapter);
                            }
                        },
                        throwable -> {
                            Toast.makeText(getActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                ));
    }

    private void category() {
        disposable.add(apiShop.getCategory()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        categoryModel -> {
                            if(categoryModel.isSuccess()){
                                //Toast.makeText(getActivity(), categoryModel.getMessage(), Toast.LENGTH_SHORT).show();
                                arrCategory = categoryModel.getData();
                                categoryAdapter = new CategoryAdapter(HomeFragment.this, arrCategory);
                                listcategory.setAdapter(categoryAdapter);
                            }
                        },
                        throwable -> {
                            Toast.makeText(getActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                ));
    }

    private void init(View view){
        bannerimg = view.findViewById(R.id.banner);
        bannerimg.setImageResource(R.drawable.banner);
        listcategory = view.findViewById(R.id.listcategory);
        listProductHot = view.findViewById(R.id.listProductHot);
        listProduct = view.findViewById(R.id.listproduct);
        arrCategory = new ArrayList<>();
        arrProduct = new ArrayList<>();
        arrProductHot = new ArrayList<>();
        apiShop = RetrofitClient.getInstance(Ustils.BASE_URL).create(APIShop.class);
    }
}