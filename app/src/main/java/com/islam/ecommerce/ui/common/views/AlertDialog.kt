package com.islam.ecommerce.ui.common.views

import android.content.Context
import android.view.View
import androidx.navigation.Navigation
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class AlertDialog( ) {
    companion object{
    fun showMaterialAlertDialog(context: Context,view:View){
        MaterialAlertDialogBuilder(context)
            .setTitle("Login success")
            .setMessage("we have sent you a verification link, please verify your email")
            .setPositiveButton("ok"){
                dialog,wich->
                dialog.dismiss()
                Navigation.findNavController(view).popBackStack()
            }.create().show()
    }
    }
}