package com.coach.walker.controllers

/*import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.view.View
import android.widget.Toast
import com.rey.material.widget.ProgressView
import org.greenrobot.eventbus.EventBus


import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.view.View
import android.widget.Toast
import com.coach.umanlife.*
import com.coach.umanlife.events.ULEvent
import com.coach.umanlife.utils.ULApplication
import com.umanlife.library.models.*
import com.coach.umanlife.utils.ULApplicationConstants
import com.coach.umanlife.utils.ULPrefsManager
import org.greenrobot.eventbus.EventBus
import com.rey.material.widget.ProgressView
import com.umanlife.library.callbacks.ULSDKServiceCallback
import com.umanlife.library.services.ws.ULSDKService

/**
 * Created by pierre-alexandrevezinet on 14/08/2018.
 */



/**
 * Created by pierre-alexandrevezinet <pax@umanlife.com> on 09/01/2018.
 */
class ULServiceController() : ULSDKServiceCallback {
    private var ulPrefsManager: ULPrefsManager? = null
    private var eventName = ""
    private var progressViewCircular: ProgressView? = null
    private var context: Context? = null
    private var token: String = ""
    private var accessToken: String = ""
    private var ulService: ULSDKService = ULSDKService(ULApplicationConstants.BASE_URL_UMANLIFE_STAGING, this)
    private val bus = EventBus.getDefault()

    constructor(mContext: Context, mULPrefsManager: ULPrefsManager, mProgressViewCircular: ProgressView) : this() {
        ulPrefsManager = mULPrefsManager
        context = mContext
        if (mProgressViewCircular != null) {
            progressViewCircular = mProgressViewCircular
        }
    }

    constructor(mContext: Context, mULPrefsManager: ULPrefsManager) : this() {
        ulPrefsManager = mULPrefsManager
        context = mContext
    }

    /**
     * @param param1
     * *
     * @param wsName
     */
    fun execute(`param1`: Any, wsName: String) {
        eventName = wsName
        if (progressViewCircular != null) {
            progressViewCircular!!.visibility = View.VISIBLE
        }

        token = ulPrefsManager!!.getStringFromPreferences(ULApplicationConstants.TOKEN)
        accessToken = ULApplication.getAccessToken()
        if (ULApplication.isNetworkAvailable(context!!)) {
            when (wsName) {
            //HERMES
                ULApplicationConstants.LOGIN -> ulService.login(`param1` as ULSDKLogin)
                ULApplicationConstants.PASSWORD_CHANGE -> ulService.changePassword(token, `param1` as ULSDKPasswordChange)
                ULApplicationConstants.PROFILE_GET_ALL -> ulService.getAllUserProfiles(`param1` as String)
                ULApplicationConstants.PROFILE_GET_MASTER -> ulService.getMasterProfile(token)
                ULApplicationConstants.PROFILE_GET_ONE -> ulService.getOneProfile(token, `param1` as String)
                ULApplicationConstants.REGISTER -> ulService.register(`param1` as ULSDKRegister)
                ULApplicationConstants.ULP_REGISTER -> ulService.registerULP(accessToken, `param1` as ULSDKRegisterULP)
                ULApplicationConstants.PASSWORD_FORGET -> ulService.forgetPassword(`param1` as ULSDKForgetPassword)
                ULApplicationConstants.PASSWORD_RESET -> ulService.resetPassword(`param1` as ULSDKPasswordReset)
                ULApplicationConstants.VALIDATE -> ulService.validateRegisteredUser(`param1` as ULSDKToken)
                ULApplicationConstants.PROFILE_CREATE_NEW_ONE -> ulService.createNewProfile(token, `param1` as ULSDKProfile)
                ULApplicationConstants.VALIDATE_NEW_EMAIL -> ulService.validateNewEmail(`param1` as ULSDKToken)
                ULApplicationConstants.PASSWORD_VERIFY -> ulService.passwordVerify(`param1` as ULSDKToken)
                ULApplicationConstants.USER_GET_ONE -> ulService.userGetOne(`param1` as String)
                ULApplicationConstants.PROFILE_DELETE -> ulService.deleteProfile(token, `param1` as String)
                ULApplicationConstants.GET_ALL_USERS_PROFILES -> ulService.getAllUserProfiles(token)
                ULApplicationConstants.GET_ONE_USER_PROFILE -> ulService.getOneProfile(token, `param1` as String)
                ULApplicationConstants.DESACTIVATE_USER_ACCOUNT -> ulService.desactivateUserAccount(token)
            //PYTHONISSE
                ULApplicationConstants.GET_QUESTIONS_CONFIGURATOR -> ulService.getConfiguratorQuestions(token, `param1` as String)
                ULApplicationConstants.GET_QUESTIONS_BY_THEME -> ulService.getQuestionByTheme(token, `param1` as String)
                ULApplicationConstants.GET_QUESTIONS_BY_PROFILE_ID -> ulService.getQuestionByProfileId(token, `param1` as String)
                ULApplicationConstants.GET_QUESTIONS_BY_NB_DAY -> ulService.getQuestionByNbDay(token, `param1` as Number)
                ULApplicationConstants.CREATE_USER_CHALLENGE -> ulService.createUserChallenge(token, `param1` as ULSDKUserChallenge)
                ULApplicationConstants.GET_ALL_USER_CHALLENGES -> ulService.getAllUserChallenges(token)
                ULApplicationConstants.GET_LIST_GOAL_ALL -> ulService.getGoalAll(token)
                ULApplicationConstants.GET_SERIE_QUESTIONS -> ulService.getSerieQuestions(token, `param1` as String)
            //ESCULAPE
                ULApplicationConstants.GET_APPOINTMENTS -> ulService.getAppointments(token)
                ULApplicationConstants.GET_APPOINTMENTS_BY_PROFILE_ID -> ulService.getAppointmentsByProfileID(token, `param1` as String)
                ULApplicationConstants.DELETE_AN_APPOINTMENT_BY_ID -> ulService.deleteAppointmentById(token, `param1` as String)
                ULApplicationConstants.GET_PROFILE_BRANDS -> ulService.getProfileBrands(token)
                ULApplicationConstants.LIST_EXTERNALS -> ulService.getListExternals(token)
                ULApplicationConstants.GET_HEALTH_CAUSES -> ulService.getHealthCauses(token)
                ULApplicationConstants.GET_PROFILE_GOALS -> ulService.getProfileGoals(token, `param1` as String)
                ULApplicationConstants.LINK_AVOCADOO_ACCOUNT -> ulService.linkAvocadooAccount(token, `param1` as String)
            //KUIPER
                ULApplicationConstants.GET_ALL_ADVICES -> ulService.getAllAdvices(token)
                ULApplicationConstants.GET_ADVICES_BY_THEME -> ulService.getAdvicesByTheme(token, `param1` as String)
                ULApplicationConstants.GET_ALL_CONTENTS -> ulService.getAllContents(token)
                ULApplicationConstants.GET_CONTENTS_BY_THEME -> ulService.getContentsByTheme(token, `param1` as String)
                ULApplicationConstants.GET_CONTENT_BY_ID -> ulService.getContentById(token, `param1` as String)
                ULApplicationConstants.GET_NOTIFICATIONS_OF_THE_USER -> ulService.getNotificationsOfTheUser(token)
                ULApplicationConstants.GET_ALL_PROGRAMS_BY_PROFILE_ID -> ulService.getAllProgramsByProfileId(token, `param1` as String)
                ULApplicationConstants.GET_TAGS -> ulService.getTags(token, `param1` as String)
                ULApplicationConstants.GET_CONTENTS_LIKED -> ulService.getContentsLiked(token)
                ULApplicationConstants.GET_USER_CONTENTS -> ulService.getUserContents(token)
            //BRIGHID
                ULApplicationConstants.GET_ALL_DOCUMENTS_BY_PROFILE_ID -> ulService.getAllDocumentsByProfileId(token, `param1` as String)
                ULApplicationConstants.DOWNLOAD_A_FILE -> ulService.downloadFile(token, `param1` as String)
                ULApplicationConstants.DELETE_A_FILE -> ulService.deleteFile(token, `param1` as String)
                ULApplicationConstants.MARK_NOTIFICATION_AS_READ -> ulService.markNotificationAsRead(token, `param1` as String)
            //UMANLIFEPLAY
                ULApplicationConstants.GET_LIST_GOALS_AND_THEMES -> ulService.getListGoals(token)
            //ESCULAPE
                ULApplicationConstants.GET_APPOINTMENTS_BY_PROFILE_ID -> ulService.getAppointmentsByProfileID(token, param1 as String)
                ULApplicationConstants.ADD_NEW_DEVICE -> ulService.addNewDevice(token, `param1` as ULSDKDevice)
                ULApplicationConstants.DELETE_DEVICE_BY_ID -> ulService.deleteDevice(token, `param1` as String)
                ULApplicationConstants.GET_DEVICE_BY_ID -> ulService.getDeviceById(token, `param1` as String)
                ULApplicationConstants.REGISTER_VALIDIC_WITH_ULP -> ulService.registerValidic(`param1` as String, null, token)
            //MELKART
                ULApplicationConstants.GET_CLIENT -> ulService.getClient(token)
            }
        } else {
            if (progressViewCircular != null) {
                progressViewCircular!!.stop()
                progressViewCircular!!.visibility = View.GONE
            }
            if (!wsName.equals(ULApplicationConstants.GET_ALL_USER_CHALLENGES)) {
                Toast.makeText(context!!, context!!.getString(R.string.NETWORK_connectionErrorMessage), Toast.LENGTH_SHORT).show()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
            /**
             * @param param1
             * *
             * @param param2
             *
             * @param wsName
             */
    fun execute(`param1`: Any, `param2`: Any, wsName: String) {
        eventName = wsName
        if (progressViewCircular != null) {
            progressViewCircular!!.visibility = View.VISIBLE
        }
        token = ulPrefsManager!!.getStringFromPreferences(ULApplicationConstants.TOKEN)
        accessToken = ULApplication.getAccessToken()
        if (ULApplication.isNetworkAvailable(context!!)) {
            when (wsName) {
            //UMANLIFE
                ULApplicationConstants.USER_UPDATE -> ulService.userUpdate(token, `param1` as String, `param2` as ULSDKUserUpdate)
                ULApplicationConstants.PROFILE_UPDATE -> ulService.updateProfile(token, `param1` as String, `param2` as ULSDKProfile)
                ULApplicationConstants.DELETE_AN_APPOINTMENT_BY_ID_AND_PROFILE_ID -> ulService.deleteAppointmentByIdAndProfileId(token, `param1` as String, `param2` as String)
                ULApplicationConstants.CREATE_EXTERNAL -> ulService.createExternal(token, `param1` as String, `param2` as ULSDKExternal)
                ULApplicationConstants.GET_AN_APPOINTMENT_BY_ID -> ulService.getOnAppointmentsById(token, `param1` as String, `param2` as String)
                ULApplicationConstants.GET_DATAS -> ulService.getDatas(token, `param1` as String, `param2` as String)
                ULApplicationConstants.GET_ALL_PROGRAMS_BY_PROFILE_ID_AND_BY_DATE -> ulService.getAllProgramsByProfileIdAndByDate(token, `param1` as String, `param2` as String)
                ULApplicationConstants.STORE_DATA -> ulService.storeData(token, `param1` as String, `param2`)
            //PYTHONISSE
                ULApplicationConstants.UPDATE_USER_CHALLENGE -> ulService.updateUserChallenges(token, `param1` as String, `param2` as ULSDKUserChallenge)
            //ESCULAPE
                ULApplicationConstants.CREATE_A_NEW_APPOINTMENT -> ulService.createNewAppointment(token, `param1` as String, `param2`)
            //SYNC VALIDIC FOR GRAPH
                ULApplicationConstants.SYNC_VALIDIC_WITH_ULP -> ulService.registerValidic(`param1` as String, `param2` as Int, token)

            }
        } else {
            if (progressViewCircular != null) {
                progressViewCircular!!.stop()
                progressViewCircular!!.visibility = View.GONE
            }
            Toast.makeText(context!!, context!!.getString(R.string.NETWORK_connectionErrorMessage), Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * @param param1
     * *
     * @param param2
     * *
     * @param param3
     *
     * @param wsName
     */
    fun execute(`param1`: Any, `param2`: Any, `param3`: Any, wsName: String) {
        eventName = wsName
        if (progressViewCircular != null) {
            progressViewCircular!!.visibility = View.VISIBLE
        }
        token = ulPrefsManager!!.getStringFromPreferences(ULApplicationConstants.TOKEN)
        accessToken = ULApplication.getAccessToken()
        if (ULApplication.isNetworkAvailable(context!!)) {
            when (wsName) {
                ULApplicationConstants.UPDATE_EXTERNAL -> ulService.updateExternal(token, `param1` as String, `param2` as String, `param3` as ULSDKExternal)
                ULApplicationConstants.UPDATE_APPOINTMENT_BY_ID_AND_PROFILE_ID -> ulService.updateAppointmentByIdAndProfileId(token, `param1` as String, `param2` as String, `param3`)
                ULApplicationConstants.GET_ALL_PROGRAMS_BY_PROFILE_ID_AND_FROM_DATE_AND_TO_DATE -> ulService.getAllProgramsByProfileIdWithFromDateAntoDate(token, `param1` as String, `param2` as String, `param3` as String)
            //MOLOCH
                ULApplicationConstants.UPDATE_USER_CONTENTS -> ulService.updateUserContent(token, `param1` as String, `param2` as String, `param3` as ULSDKLike)
            //BRIGHID
                ULApplicationConstants.UPLOAD_A_FILE_BY_PROFILE_ID -> ulService.uploadFileByProfileId(token, `param1` as String, `param2` as ULSDKMetadata, `param3` as String)
            //ESCULAPE
                ULApplicationConstants.ADD_PROFILE_GOAL -> ulService.addProfileGoal(token, `param1` as String, `param2` as String, `param3` as ULSDKFollowedVariables)
            }
        } else {
            if (progressViewCircular != null) {
                progressViewCircular!!.stop()
                progressViewCircular!!.visibility = View.GONE
            }
            Toast.makeText(context!!, context!!.getString(R.string.NETWORK_connectionErrorMessage), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onServiceStart(response: Any) {
        if (progressViewCircular != null) {
            progressViewCircular!!.visibility = View.VISIBLE
            progressViewCircular!!.start()
        }
    }

    override fun onServiceFailed(response: Any) {
        bus.post(ULEvent(response, eventName))
        if (progressViewCircular != null) {
            progressViewCircular!!.stop()
            progressViewCircular!!.visibility = View.GONE
        }
    }

    override fun onServiceSuccess(response: Any) {
        bus.post(ULEvent(response, eventName))
        if (progressViewCircular != null) {
            progressViewCircular!!.stop()
            progressViewCircular!!.visibility = View.GONE
        }
    }
}
*/