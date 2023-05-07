package ru.zavgorodnev.googletasks.utils

import androidx.fragment.app.Fragment

interface Navigator {

    fun launch(fragment: Fragment)

    fun goBack()

}