package hu.bme.aut.android.mysporttrackerapp.utils

import android.graphics.Color

object Const {
    const val LINE_COLOR = Color.RED
    const val LINE_WIDTH = 8f
    const val MAP_ZOOM = 15f
    const val ACTION_START_OR_RESUME_SERVICE = "ACTION_START_OR_RESUME_SERVICE"
    const val ACTION_PAUSE_SERVICE = "ACTION_PAUSE_SERVICE"
    const val ACTION_STOP_SERVICE = "ACTION_STOP_SERVICE"
    const val CHANNEL_ID = "mapview_channel"
    const val NOTIFICATION_ID = 1
    const val REQUEST_CODE_LOCATION_PERMISSION = 0
    const val SHARED_PREFERENCES_NAME = "sharedPref"
    const val KEY_FIRST_TIME = "KEY_FIRST_TIME"
    const val KEY_NAME = "KEY_NAME"
    const val KEY_WEIGHT = "KEY_WEIGHT"
    const val TIMER_UPDATE_INTERVAL = 50L
}