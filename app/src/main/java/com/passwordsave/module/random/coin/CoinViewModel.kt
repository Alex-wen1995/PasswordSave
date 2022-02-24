package com.passwordsave.module.random.coin

import androidx.lifecycle.ViewModel

class CoinViewModel : ViewModel() {
    var count:Int=1
    var numberList= mutableListOf((1..6).random())

}