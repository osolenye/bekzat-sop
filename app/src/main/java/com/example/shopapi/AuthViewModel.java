package com.example.shopapi;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AuthViewModel extends ViewModel {
    MutableLiveData<String> token = new MutableLiveData<>();
}
