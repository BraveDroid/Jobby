package com.bravedroid.jobby.presentation.util

import android.view.LayoutInflater
import android.view.ViewGroup

object ViewGroupExt {
    fun ViewGroup.getLayoutInflater(): LayoutInflater = LayoutInflater.from(context)
}
