package com.example.budgetwise.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.budgetwise.R
import com.example.budgetwise.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(), View.OnClickListener {

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeButtonClick()
    }

    private fun initializeButtonClick() {
        binding.fbFloatingButton.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.fb_floating_button -> {
//                val action = HomeFragmentDirections.actionHomeFragmentToAddBudget()
//                Navigation.findNavController(requireView()).navigate(action)
                val bottomSheet = AddBudgetFragment()
                bottomSheet.show(parentFragmentManager, "AddBudgetBottomSheet")
            }
        }
    }
}