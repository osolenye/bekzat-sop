package com.example.shopapi.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.shopapi.models.ModelM;
import com.example.shopapi.repositories.ShopRepository;

import java.util.List;

public class HomeViewModel extends ViewModel {
    private ShopRepository jemRepository;
    private LiveData<List<ModelM>> modelMResponseLiveData;

    public HomeViewModel() {
        ShopRepository jemRepository = new ShopRepository();
        modelMResponseLiveData= jemRepository.getDashJeminyList();
    }

    public LiveData<List<ModelM>> getModelMResponseLiveData() {
        return modelMResponseLiveData;
    }

}