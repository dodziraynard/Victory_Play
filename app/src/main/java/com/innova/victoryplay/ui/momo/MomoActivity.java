package com.innova.victoryplay.ui.momo;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.innova.victoryplay.R;
import com.innova.victoryplay.databinding.ActivityMomoBinding;

import static com.innova.victoryplay.utils.Functions.getUserId;


public class MomoActivity extends AppCompatActivity {
    String type = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMomoBinding binding = ActivityMomoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        MomoActivityViewModel viewModel = new ViewModelProvider(this).get(MomoActivityViewModel.class);
        viewModel.getIsLoading().observe(this, isLoading -> {
            if (isLoading) {
                binding.progressBar.setVisibility(View.VISIBLE);
            } else {
                binding.progressBar.setVisibility(View.GONE);
            }
        });

        setSupportActionBar(binding.toolbar);
        setTitle(getString(R.string.momo));

        binding.fab.setOnClickListener(view -> {
            String fullName = binding.tvFullName.getText().toString();
            String mobile = binding.momoNumber.getText().toString();
            String strAmount = binding.amount.getText().toString();
//            String strAmount = binding.amount.getText().toString();
            if (strAmount.isEmpty() || fullName.isEmpty() || mobile.isEmpty() || type.isEmpty()) {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
                return;
            }
            double amount = Double.parseDouble(strAmount);
            long userId = getUserId(MomoActivity.this);
            viewModel.postTransaction(userId, fullName, type, mobile, amount);
        });

        binding.radioButtonGroup.setOnCheckedChangeListener((group, id) -> {
            switch (id) {
                case R.id.rb_tithe:
                    type = "tithe";
                    break;
                case R.id.rb_welfare:
                    type = "welfare";
                    break;

                case R.id.rb_donation:
                    type = "donation";
                    break;
            }
            Toast.makeText(MomoActivity.this, type, Toast.LENGTH_SHORT).show();
        });
    }
}