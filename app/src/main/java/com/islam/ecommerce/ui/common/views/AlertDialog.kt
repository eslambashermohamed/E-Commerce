package com.islam.ecommerce.ui.common.views

import android.content.Context
import android.view.View
import androidx.navigation.Navigation
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class AlertDialog( ) {
    companion object{
    fun showMaterialAlertDialog(context: Context,view:View){
        MaterialAlertDialogBuilder(context)
            .setTitle("Register Success")
            .setMessage("we have sent you a verification link, please verify your email")
            .setPositiveButton("ok"){
                dialog,wich->
                dialog.dismiss()
                Navigation.findNavController(view).popBackStack()
            }.create().show()
    }
        fun showRestPasswordAlertDialog(context: Context,fragment:BottomSheetDialogFragment){
            MaterialAlertDialogBuilder(context)
                .setTitle("Rest Password")
                .setMessage("check your email to rest your password")
                .setPositiveButton("ok"){
                        dialog,wich->
                    dialog.dismiss()
                    fragment.dismiss()
                }.create().show()
        }
    }
}