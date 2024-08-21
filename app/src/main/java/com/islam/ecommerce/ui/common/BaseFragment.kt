package com.islam.ecommerce.ui.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.islam.ecommerce.BR
import com.islam.ecommerce.ui.common.views.ProgressDialog

abstract class BaseFragment<DB : ViewDataBinding, VM : ViewModel> : Fragment() {
    val progressDialog by lazy {
        ProgressDialog.createProgressDialog(requireActivity())
    }
    protected abstract val viewModel: VM
    private var _binding: DB? = null
    protected val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        doDataBinding()
        init()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, getResId(), container, false)
        return _binding?.root
    }

    @LayoutRes
    abstract fun getResId(): Int

    private fun doDataBinding() {
        _binding?.lifecycleOwner = viewLifecycleOwner
        _binding?.setVariable(BR.ViewModel, viewModel)
        _binding?.executePendingBindings()
    }
    protected abstract fun init()
}