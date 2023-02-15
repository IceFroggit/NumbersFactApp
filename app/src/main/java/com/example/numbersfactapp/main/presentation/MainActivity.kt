package com.example.numbersfactapp.main.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.numbersfactapp.R
import com.example.numbersfactapp.main.numbers.presentation.NumbersFragment

class MainActivity : AppCompatActivity(), ShowFragment {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null)
            show(NumbersFragment(), false)
    }

    override fun show(fragment: Fragment) {
        show(fragment, true)
    }

    private fun show(fragment: Fragment, add: Boolean) {
        //todo сделать ООП'шно
        val transaction = supportFragmentManager.beginTransaction()
        if (add) {
            transaction
                .add(R.id.container, fragment)
                .addToBackStack(fragment.javaClass.simpleName)
        } else
            transaction.replace(R.id.container, NumbersFragment())
        transaction.commit()
    }
}

interface ShowFragment {

    fun show(fragment: Fragment)

    class Empty : ShowFragment {
        override fun show(fragment: Fragment) = Unit
    }
}