package com.coach.walker.fragments

import android.content.Context
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.coach.walker.R
import com.coach.walker.listeners.WMainActivityListener
import com.coach.walker.utils.WApplicationConstants
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.maps.MapView


/**
 * Created by pierre-alexandrevezinet on 09/01/2018.
 */

class WMapChallengesFragment : Fragment() {

    var wFragment1Listener: WMainActivityListener? = null
    private var mapView: MapView? = null

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

        Mapbox.getInstance(context!!, WApplicationConstants.WALKER_TOKEN_MAPBOX)
        mapView = rootView.findViewById(R.id.mapview) as MapView
        mapView!!.onCreate(savedInstanceState);
        mapView!!.getMapAsync {

        }

        return rootView

    }

    override fun onStart() {
        super.onStart()
        mapView!!.onStart()
    }


    override fun onResume() {
        super.onResume()
        mapView!!.onResume()
    }


    override fun onDestroy() {
        super.onDestroy()
        mapView!!.onDestroy()
    }


    override fun onPause() {
        super.onPause()
        mapView!!.onPause()
    }


    override fun onLowMemory() {
        super.onLowMemory()
        mapView!!.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView!!.onSaveInstanceState(outState)
    }
}