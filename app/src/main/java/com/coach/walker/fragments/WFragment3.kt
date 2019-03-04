package com.coach.walker.fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.coach.walker.R
import com.coach.walker.listeners.WMainActivityListener

/**
 * Created by pierre-alexandrevezinet on 09/01/2018.
 */


class WFragment3 : Fragment() {

    var wFragment1Listener: WMainActivityListener? = null


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try {
            if (context is WMainActivityListener) {
                this.wFragment1Listener = context
            }
        } catch (e: ClassCastException) {
            Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        var rootView = inflater.inflate(R.layout.layout_map_challenge_fragment, container, false)

        return rootView as View
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
    }


}