package com.example.shopapi;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.shopapi.databinding.FragmentRegisterBinding;
import com.example.shopapi.models.User;
import com.example.shopapi.remote_data.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterFragment extends Fragment {
    FragmentRegisterBinding binding;

    User newUser;

    NavController navController;

//    public RegisterFragment() {
//        // Required empty public constructor
//    }

//    public static RegisterFragment newInstance(String param1, String param2) {
//        RegisterFragment fragment = new RegisterFragment();
//
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding=FragmentRegisterBinding
                .inflate(inflater,container,false);

        View root=binding.getRoot();
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstancesState){
        super.onViewCreated(view,savedInstancesState);
        binding.register.setOnClickListener(v -> {
            binding.progressBar.setVisibility(View.VISIBLE);
            if (!IsEmptyEditTextRegistration()){
                registerToTable();
            }
        });
        binding.toLogin.setOnClickListener(v -> {
            navController= Navigation.findNavController(requireActivity(),R.id.nav_host);
            navController.navigate(R.id.action_registerFragment_to_loginFragment);
        });
    }

    private void registerToTable() {
        newUser=new User(binding.nameUser.getText().toString(),
                binding.email.getText().toString(),
                binding.password.getText().toString());
        Call<User> callCreateUser = RetrofitClient.getInstance().getApi().registrationNewUser(newUser);
        callCreateUser.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()){
                    if(response.body()!=null){
                        binding.progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(requireActivity(),"Registration was successful",Toast.LENGTH_LONG).show();
                        SharedPreferences preferences= getActivity().getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
                        SharedPreferences.Editor prefLoginEdit=preferences.edit();
                        prefLoginEdit.putBoolean("registration",true);
                        prefLoginEdit.commit();
                    }
                }else {
                    Log.e("fail","fail");
                    Toast.makeText(requireActivity(),"Registration is not available now",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable throwable) {
                Log.e("failure","Registration is failure");
                Toast.makeText(requireActivity(),"Registration was failed",Toast.LENGTH_LONG).show();
            }
        });
    }
    private boolean IsEmptyEditTextRegistration(){
        if(binding.nameUser.getText().toString().isEmpty() ||
        binding.email.getText().toString().isEmpty() ||
        binding.password.getText().toString().isEmpty()){
            Toast toast=Toast.makeText(getActivity(),
                    "Заполните поля для регистрации",Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();
            return true;
        }else {
            return false;
        }
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
    }
}