package com.passwordsave.module.random.dice

import androidx.lifecycle.ViewModel

class DiceViewModel : ViewModel() {
    var count:Int=1
    var numberList= mutableListOf<Int>((1..6).random())
}