/*
 * Copyright (C) 2021 The Android Open Source Project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.lunchtray.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.example.lunchtray.data.DataSource
import java.text.NumberFormat

class OrderViewModel : ViewModel() {

    // Map of menu items
    val menuItems = DataSource.menuItems

    // Default values for item prices
//    private var previousEntreePrice = 0.0
//    private var previousSidePrice = 0.0
//    private var previousAccompanimentPrice = 0.0

    // Default tax rate
    private val taxRate = 0.08

    // Entree for the order
    private val _entree = MutableLiveData<MenuItem?>()
    val entree: LiveData<MenuItem?> = _entree

    // Side for the order
    private val _side = MutableLiveData<MenuItem?>()
    val side: LiveData<MenuItem?> = _side

    // Accompaniment for the order.
    private val _accompaniment = MutableLiveData<MenuItem?>()
    val accompaniment: LiveData<MenuItem?> = _accompaniment

    // Subtotal for the order
    private val _subtotal = MutableLiveData(0.0)
    val subtotal: LiveData<String> = _subtotal.map {
        NumberFormat.getCurrencyInstance().format(it)
    }

    // Total cost of the order
    private val _total = MutableLiveData(0.0)
    val total: LiveData<String> = _total.map {
        NumberFormat.getCurrencyInstance().format(it)
    }

    // Tax for the order
    private val _tax = MutableLiveData(0.0)
    val tax: LiveData<String> = _tax.map {
        NumberFormat.getCurrencyInstance().format(it)
    }

    /**
     * Set the entree for the order.
     */
    fun setEntree(entree: String) {
        menuItems.get(entree)?.run {
            _entree.setValue(this)
            updateSubtotal()
        }
        Log.d("DUMMY", entree)
    }

    /**
     * Set the side for the order.
     */
    fun setSide(side: String) {
        menuItems.get(side)?.run {
            _side.setValue(this)
            updateSubtotal()
        }
    }

    /**
     * Set the accompaniment for the order.
     */
    fun setAccompaniment(accompaniment: String) {
        menuItems.get(accompaniment)?.run {
            _accompaniment.setValue(this)
            updateSubtotal()
        }
    }

    /**
     * Update subtotal value.
     */
    private fun updateSubtotal() {
        val entreePrice = entree.value?.price ?: 0.0
        val sidePrice = side.value?.price ?: 0.0
        val accompanimentPrice = accompaniment.value?.price ?: 0.0
        _subtotal.value = entreePrice + sidePrice + accompanimentPrice
    }

    /**
     * Calculate tax and update total.
     */
    fun calculateTaxAndTotal() {
        _subtotal.value?.run {
            _tax.value = this.times(taxRate)
            _total.value = this + this.times(taxRate)
        }
    }

    /**
     * Reset all values pertaining to the order.
     */
    fun resetOrder() {
        // DONE: Reset all values associated with an order
        _entree.value = null
        _side.value = null
        _accompaniment.value = null
        _subtotal.value = 0.0
        _total.value = 0.0
        _tax.value = 0.0
    }
}
