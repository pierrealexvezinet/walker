package com.coach.walker.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.coach.walker.R
import com.coach.walker.controllers.WServiceController
import com.coach.walker.events.WEvent
import com.coach.walker.utils.WApplicationConstants
import com.coach.walker.utils.WPrefsManager
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * Created by pierre-alexandrevezinet on 10/08/2018.
 */

class WMainActivity : AppCompatActivity() {

    private val GITHUB_MEMBERS = arrayOf("mojombo", "JakeWharton", "mattt")
    private var wServiceController: WServiceController? = null
    private var prefsManager: WPrefsManager? = null
    private val bus = EventBus.getDefault()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.w_main_activity)
        prefsManager = WPrefsManager(this)
        wServiceController = WServiceController(this, prefsManager!!)

        wServiceController!!.execute(GITHUB_MEMBERS, WApplicationConstants.GET_MEMBER_GITHUB)

    }

    /**
     * This method is automatically called when a eventbus event is worked
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMainEvent(event: WEvent) {

        var mObject: Any? = event.wEvent
        var jsonObject: Any? = null

        //LOGIN USER
        if (event.eventName.equals(WApplicationConstants.GET_MEMBER_GITHUB)) {

            try {
                Toast.makeText(this, mObject.toString(), Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }


    override fun onResume() {
        super.onResume()
        if (!bus.isRegistered(this)) {
            bus.register(this)
        }
    }

    override fun onStop() {
        super.onStop()
        if (bus.isRegistered(this)) {
            bus.unregister(this)
        }
    }


}
