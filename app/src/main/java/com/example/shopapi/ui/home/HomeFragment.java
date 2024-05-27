package com.example.shopapi.ui.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.shopapi.R;
import com.example.shopapi.databinding.FragmentHomeBinding;
import com.example.shopapi.models.ModelM;
import com.example.shopapi.remote_data.RetrofitClient;

import android.view.MenuItem;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    ShopAdapter adapter;
    HomeViewModel homeViewModel;
    NavController navController;
    String emailUserIdentify;
    SharedPreferences preferences;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        // Загрузка данных пользователя из SharedPreferences
        preferences = getActivity().getSharedPreferences("prefs", Context.MODE_PRIVATE);
        emailUserIdentify = preferences.getString("email", null);

        // Отображение данных пользователя в tvIdentify
        if (emailUserIdentify != null && !emailUserIdentify.isEmpty()) {
            binding.textViewIdentify.setVisibility(View.VISIBLE);
            binding.textViewIdentify.setText(emailUserIdentify);
        }
//        preferences=getActivity()
//                .getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
//        if(getArguments()!=null){
//            emailUserIdentify=getArguments().getString("identify");
//        }
//        if(preferences.getBoolean("loggedin",true)){
//            binding.textViewIdentify.setVisibility(View.VISIBLE);
//            binding.textViewIdentify.setText(emailUserIdentify);
//
//
//        }
        Call<List<ModelM>> apiCall= RetrofitClient.getInstance().getApi().getStoreProducts();
        apiCall.enqueue(new Callback<List<ModelM>>() {
            @Override
            public void onResponse(Call<List<ModelM>> call, Response<List<ModelM>> response) {
                if(response.body()!=null){
                    binding.progressBar.setVisibility(View.INVISIBLE);
                    adapter=new ShopAdapter();
                    binding.rvCatalogM.setAdapter(adapter);
                    adapter.setList(response.body());
                }else {
                    Toast.makeText(requireActivity(),"No ability to get data",Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<List<ModelM>> call, Throwable throwable) {
                Log.e("TAG","NO DATA"+throwable.getLocalizedMessage());
                Toast.makeText(requireActivity(),"NO DATA",Toast.LENGTH_SHORT).show();
            }
        });

//        homeViewModel.getModelMResponseLiveData().observe(getViewLifecycleOwner(), new Observer<List<ModelM>>() {
//            @Override
//            public void onChanged(List<ModelM> modelMS) {
//                binding.progressBar.setVisibility(View.INVISIBLE);
//
//                adapter = new ShopAdapter(requireActivity(), modelMS);
//                binding.rvCatalogM.setAdapter(adapter);
//            }
//        });
//        setUpOnBackPressed();
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.basketBtn.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(requireActivity(), binding.basketBtn);
            popup.getMenuInflater().inflate(R.menu.action_menu, popup.getMenu());

            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getTitle().toString()) {
                        case "Добавить в корзину":
                            navController = Navigation.findNavController(requireActivity(), R.id.nav_host);

                            Bundle bundle = new Bundle();
                            bundle.putParcelableArrayList("keysss_basket", adapter.getSelected_intoBasketList());
                            navController.navigate(R.id.navigation_basket, bundle);
                            break;
                        case "Пометить":
                            Toast.makeText(requireContext(), "Marked", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(requireContext(), "default", Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }
            });
            popup.show();
        });
        binding.signInSignOut.setOnClickListener(v1->{
            navController=Navigation.findNavController(requireActivity(),R.id.nav_host);
            navController.navigate(R.id.action_navigation_home_to_registerFragment);
        });
    }

    private void setUpOnBackPressed() {
        requireActivity().getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (isEnabled()) {
                    requireActivity().finish();
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}