package com.islam.ecommerce.ui.auth.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.islam.ecommerce.R
import com.islam.ecommerce.data.models.Resource
import com.islam.ecommerce.databinding.FragmentRegisterBinding
import com.islam.ecommerce.ui.auth.viewmodel.RegisterViewModel
import com.islam.ecommerce.ui.auth.viewmodel.RegisterviewModelFactory
import com.islam.ecommerce.ui.common.views.AlertDialog
import com.islam.ecommerce.ui.common.views.ProgressDialog
import com.islam.ecommerce.ui.home.MainActivity
import com.islam.ecommerce.ui.showSnakeBarError
import kotlinx.coroutines.launch


class RegisterFragment : Fragment() {

    lateinit var binding: FragmentRegisterBinding;
    val registerViewModel: RegisterViewModel by viewModels {
        RegisterviewModelFactory(requireContext())
    }

    val progressDialog by lazy {
        ProgressDialog.createProgressDialog(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        binding.registerViewModel = registerViewModel
        binding.lifecycleOwner = this
        iniListeners()
        initViewModel()
        return binding.root
    }

    private fun iniListeners() {
        binding.signInTx.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    fun initViewModel() {
        lifecycleScope.launch {
            registerViewModel.registerState.collect { state ->
                state?.let { resources ->
                    when (resources) {
                        is Resource.Loading -> {
                            progressDialog.show()
                        }

                        is Resource.Success -> {
                            progressDialog.dismiss()
                            AlertDialog.showMaterialAlertDialog(requireActivity(),requireView())
                        }

                        is Resource.Error -> {
                            progressDialog.dismiss()
                            val msg =
                                resources.exception?.message ?: getString(R.string.generic_err_msg)
                            view?.showSnakeBarError(
                                resources.exception?.message ?: getString(R.string.generic_err_msg)
                            )
                        }
                    }
                }
            }
        }
    }


}