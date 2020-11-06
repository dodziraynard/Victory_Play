package com.innova.victoryplay.ui.welfare;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.innova.victoryplay.adapters.WelfareAdapter;
import com.innova.victoryplay.databinding.ActivityWelfareBinding;
import com.innova.victoryplay.ui.momo.MomoActivity;

import static com.innova.victoryplay.utils.Functions.getFullUsername;

public class WelfareActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityWelfareBinding binding = ActivityWelfareBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        WelfareActivityViewModel viewModel = new ViewModelProvider(this).get(WelfareActivityViewModel.class);

        viewModel.getIsLoading().observe(this, isLoading -> {
            if (isLoading) {
                binding.progressBar.setVisibility(View.VISIBLE);
            } else {
                binding.progressBar.setVisibility(View.GONE);
            }
        });

        WelfareAdapter adapter = new WelfareAdapter();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(layoutManager);
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(this,
                        layoutManager.getOrientation());
        binding.recyclerView.addItemDecoration(dividerItemDecoration);

        binding.arrears.setOnClickListener(view -> startActivity(new Intent(WelfareActivity.this, MomoActivity.class)));
        binding.addWelfare.setOnClickListener(view -> startActivity(new Intent(WelfareActivity.this, MomoActivity.class)));

        binding.tvFullUsername.setText(getFullUsername(this));

        binding.btnRefresh.setOnClickListener(view -> viewModel.loadWelfare());

        viewModel.loadWelfare();
        viewModel.getWelfareList().observe(this, welfare -> {
            if (welfare.size() > 0)
                binding.tvLastPaid.setText(welfare.get(0).getReadableAmount());
            adapter.setData(welfare);
        });

        viewModel.getArrears().observe(this, amount -> {
            amount = amount * 100;
            amount = (double) Math.round(amount) / 100;
            binding.tvArrears.setText(String.format("GH¢%s", amount));
        });
    }
}