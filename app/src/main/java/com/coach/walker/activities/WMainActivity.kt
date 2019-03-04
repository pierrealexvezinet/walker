package com.coach.walker.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.coach.walker.R
import com.coach.walker.controllers.WServiceController
import com.coach.walker.events.WEvent
import com.coach.walker.utils.WApplicationConstants
import com.coach.walker.utils.WPrefsManager
import com.walker.walker_android_sdk.models.WGitHubMember
import kotlinx.android.synthetic.main.w_main_activity.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import android.support.v4.view.ViewPager
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.coach.walker.fragments.WMapChallengesFragment
import com.coach.walker.fragments.WFragment2
import com.coach.walker.fragments.WFragment3
import com.coach.walker.listeners.WMainActivityListener
import kotlinx.android.synthetic.main.layout_fragment2.*

/**
 * Created by pierre-alexandrevezinet on 10/08/2018.
 */

class WMainActivity : AppCompatActivity(), WMainActivityListener {

    private val GITHUB_MEMBERS = arrayOf("mojombo", "JakeWharton", "mattt")
    private var wServiceController: WServiceController? = null
    private var prefsManager: WPrefsManager? = null
    private val bus = EventBus.getDefault()
    private var listGitMember : ArrayList<WGitHubMember> = ArrayList()

    private var toolbar: android.support.v7.widget.Toolbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.w_main_activity)
        prefsManager = WPrefsManager(this)
        //wServiceController = WServiceController(this, prefsManager!!)
        //wServiceController!!.execute(GITHUB_MEMBERS, WApplicationConstants.GET_MEMBER_GITHUB)


        toolbar =  findViewById(R.id.mytoolbar);
        setSupportActionBar(toolbar)


        setViewPager(viewpager)
        tabs!!.setupWithViewPager(viewpager)

    }

     fun setViewPager(viewPager: ViewPager) {
        val adapter = ViewPagerAdapter(supportFragmentManager)

        adapter.addFragment(WMapChallengesFragment(), "Challenges Map")
        adapter.addFragment(WFragment2(), "Other 2")
        adapter.addFragment(WFragment3(), "Other 3")
        viewPager.adapter = adapter
    }

    internal inner class ViewPagerAdapter(manager: FragmentManager) : FragmentPagerAdapter(manager) {
        private val mFragmentList = ArrayList<Fragment>()
        private val mFragmentTitleList = ArrayList<String>()

        override fun getItem(position: Int): Fragment {
            return mFragmentList.get(position)
        }

        override fun getCount(): Int {
            return mFragmentList.size
        }

        fun addFragment(fragment: Fragment, title: String) {
            mFragmentList.add(fragment)
            mFragmentTitleList.add(title)
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return mFragmentTitleList.get(position)
        }
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
        if (event.eventName.equals(WApplicationConstants.GET_CITY_WEATHER)) {
            try {

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
