package com.islam.ecommerce.ui.auth.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.islam.ecommerce.R
import com.islam.ecommerce.data.models.Resource
import com.islam.ecommerce.data.repository.auth.FirebaseAuthRepositoryImpl
import com.islam.ecommerce.databinding.FragmentForgetPasswordBinding
import com.islam.ecommerce.ui.auth.viewmodel.ForgetPasswordViewModel
import com.islam.ecommerce.ui.auth.viewmodel.ForgetPasswordViewModelFactory
import com.islam.ecommerce.ui.common.views.AlertDialog
import com.islam.ecommerce.ui.common.views.ProgressDialog
import com.islam.ecommerce.ui.showSnakeBarError
import kotlinx.coroutines.launch


class ForgetPasswordFragment : BottomSheetDialogFragment() {

    var _binding: FragmentForgetPasswordBinding? = null
    val binding get() = _binding
    val forgetPasswordViewModel: ForgetPasswordViewModel by viewModels {
        ForgetPasswordViewModelFactory(FirebaseAuthRepositoryImpl())
    }
    val progressDialog by lazy {
        ProgressDialog.createProgressDialog(requireContext())
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentForgetPasswordBinding.inflate(layoutInflater, container, false)
        _binding?.forgetPasswordViewModel = forgetPasswordViewModel
        _binding?.lifecycleOwner = this
        initViewModel()
        return _binding?.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    fun initViewModel() =
        lifecycleScope.launch {
            forgetPasswordViewModel.forgetPasswordState.collect { state ->
                state?.let { resources ->
                    when (resources) {
                        is Resource.Loading -> {
                            progressDialog.show()
                        }

                        is Resource.Success -> {
                            progressDialog.dismiss()
                            AlertDialog.showRestPasswordAlertDialog(
                                requireContext(),
                                this@ForgetPasswordFragment
                            )
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