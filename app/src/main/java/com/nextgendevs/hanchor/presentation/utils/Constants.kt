package com.nextgendevs.hanchor.presentation.utils

class Constants {
    companion object {

        const val TODO_INSERT_ID_LOCAL = "com.nextgendevs.constants_TODO_INSERT_ID_LOCAL"
        const val TODO_UPDATE_ID_LOCAL = "com.nextgendevs.constants_TODO_UPDATE_ID_LOCAL"

        const val MY_PREF = "com.nextgendevs.constants_MY_PREF"
        const val IS_NIGHT_MODE = "com.nextgendevs.constants_IS_NIGHT_MODE"
        const val FIRST_TIME_LOADING = "com.nextgendevs.constants_FIRST_TIME_LOADING"

        const val AUTH_TOKEN = "com.nextgendevs.constants_AUTH_TOKEN"
        const val USERID = "com.nextgendevs.constants_AUTH_TOKEN"
        const val USERNAME = "com.nextgendevs.constants_USERNAME"
        const val FIRSTNAME = "com.nextgendevs.constants_FIRSTNAME"
        const val LASTNAME = "com.nextgendevs.constants_LASTNAME"
        const val EMAIL = "com.nextgendevs.constants_EMAIL"



        const val FCM_TOKEN = "com.nextgendevs.constants_FCM_TOKEN"
        const val SUBSCRIBE_TO_HANCHOR = "/topics/deals"
        const val FCM_TITLE_KEY = "title"
        const val FCM_BODY_KEY = "body"
        const val FCM_LINK_KEY = "link"
        const val NOTIFICATION_CHANNEL_ID = "NOTIFICATION_CHANNEL_ID_HANCHOR"
        const val NOTIFICATION_NAME = "New notification hanchor"
        const val NOTIFICATION_ID = 63533

        const val REMINDER_NOTIFICATION_ALARM_TIME = "com.nextgendevs.constants_REGULAR_NEXT_ALARM_TIME"
        const val REMINDER_NOTIFICATION_SNOOZE_ID = "com.nextgendevs.constants_REGULAR_ALARM_SNOOZE_ID"
        const val REMINDER_NOTIFICATION_SNOOZE_TITLE = "com.nextgendevs.constants_REGULAR_ALARM_SNOOZE_TITLE"
        const val REMINDER_NOTIFICATION_SNOOZE_PICKED_DATE_TIME = "com.nextgendevs.constants_REGULAR_ALARM_SNOOZE_PICKED_DATE_TIME"
        const val REMINDER_NOTIFICATION_SNOOZE_DATE_CALENDAR = "com.nextgendevs.constants_REGULAR_ALARM_SNOOZE_DATE_CALENDAR"
        const val REMINDER_NOTIFICATION_SNOOZE_TIME_CALENDAR = "com.nextgendevs.constants_REGULAR_ALARM_SNOOZE_TIME_CALENDAR"
        const val REMINDER_NOTIFICATION_SNOOZE_HAS_TIME_PAST = "com.nextgendevs.constants_REGULAR_ALARM_SNOOZE_HAS_TIME_PAST"
        const val REMINDER_NOTIFICATION_SNOOZE_IS_ALARM_DONE = "com.nextgendevs.constants_REGULAR_ALARM_SNOOZE_IS_ALARM_DONE"

        const val STOP_SELF_SERVICE_KEY = "com.nextgendevs.constants_STOP_SELF_SERVICE_KEY"


        const val ERROR_REMINDER_ID = -1
        const val ZERO_REMINDER_ID = 0
        const val REMINDER_PICKED_DATE_TIME_STRING = "com.nextgendevs.constants_REMINDER_PICKED_DATE_TIME_STRING"
        const val REMINDER_HOUR_STRING = "com.nextgendevs.constants_REMINDER_HOUR_STRING"
        const val REMINDER_MINUTE_STRING = "com.nextgendevs.constants_REMINDER_MINUTE_STRING"
        const val REMINDER_TITLE_STRING = "com.nextgendevs.constants_REMINDER_TITLE_STRING"
        const val REMINDER_DATE_STRING = "com.nextgendevs.constants_REMINDER_DATE_STRING"
        const val REMINDER_TIME_STRING = "com.nextgendevs.constants_REMINDER_TIME_STRING"
        const val REMINDER_HAS_TIME_PAST_STRING = "com.nextgendevs.constants_REMINDER_HAS_TIME_PAST_STRING"
        const val REMINDER_IS_REMINDER_DONE_STRING = "com.nextgendevs.constants_REMINDER_IS_REMINDER_DONE_STRING"


        const val REMINDER_TABLE = "reminder_table"
        const val REMINDER_ID = "_id"
        const val REMINDER_TITLE = "reminder_title"
        const val REMINDER_TIME = "reminder_time_long"
        const val REMINDER_DATE_CALENDAR = "reminder_date_calendar"
        const val REMINDER_TIME_CALENDAR = "reminder_time_calendar"
        const val REMINDER_HAS_TIME_PAST = "reminder_has_time_past"
        const val REMINDER_IS_ALARM_DONE = "reminder_is_alarm_done"

        const val REMINDER_NOTIFICATION_ID = "com.nextgendevs.constants_REMINDER_NOTIFICATION_ID"
        const val REMINDER_NOTIFICATION_TITLE = "com.nextgendevs.constants_REMINDER_NOTIFICATION_TITLE"
        const val REMINDER_NOTIFICATION_PICKED_DATE_TIME =  "com.nextgendevs.constants_REMINDER_NOTIFICATION_PICKED_DATE_TIME"
        const val REMINDER_NOTIFICATION_DATE_CALENDAR = "com.nextgendevs.constants_REMINDER_NOTIFICATION_DATE_CALENDAR"
        const val REMINDER_NOTIFICATION_TIME_CALENDAR = "com.nextgendevs.constants_REMINDER_NOTIFICATION_TIME_CALENDAR"
        const val REMINDER_NOTIFICATION_HAS_TIME_PAST = "com.nextgendevs.constants_REMINDER_NOTIFICATION_HAS_TIME_PAST"
        const val REMINDER_NOTIFICATION_IS_REMINDER_DONE = "com.nextgendevs.constants_REMINDER_NOTIFICATION_IS_REMINDER_DONE"

        const val FCM_NOTIFICATION = 300557

        const val PI_REMINDER_NOTIFICATION = 3000
        const val PI_MORNING_ALARM = 3001
        const val PI_REPEATING_MORNING_ALARM = 30011
        const val PI_NIGHT_ALARM = 3002
        const val PI_REPEATING_NIGHT_ALARM = 30022

        const val PI_REMINDER_SNOOZE_NOTIFICATION = 3003
        const val PI_MORNING_SNOOZE_ALARM = 3004
        const val PI_NIGHT_SNOOZE_ALARM = 3005

        const val PI_REMINDER_BROADCAST = 3006
        const val PI_MORNING_ALARM_START = 3007
        const val PI_NIGHT_ALARM_START = 3008

        const val PI_SNOOZE_REMINDER_BROADCAST = 3009
        const val PI_SNOOZE_MORNING_ALARM_FOREGROUND_SERVICE = 3010
        const val PI_SNOOZE_NIGHT_ALARM_FOREGROUND_SERVICE = 3011

        const val REMINDER_WORKER_ID = "com.nextgendevs.constants_REMINDER_WORKER_ID"
        const val REMINDER_WORKER_EXISTING_TITLE = "com.nextgendevs.constants_REMINDER_WORKER_EXISTING_TITLE"
        const val REMINDER_WORKER_PRESENT_TITLE = "com.nextgendevs.constants_REMINDER_WORKER_PRESENT_TITLE"
        const val REMINDER_WORKER_EXISTING_LONG_TIME = "com.nextgendevs.constants_REMINDER_WORKER_EXISTING_LONG_TIME"
        const val REMINDER_WORKER_PRESENT_LONG_TIME = "com.nextgendevs.constants_REMINDER_WORKER_PRESENT_LONG_TIME"
        const val REMINDER_WORKER_KEY = "com.nextgendevs.constants_REMINDER_WORKER_KEY"
        const val REMINDER_WORKER_INSERT = "com.nextgendevs.constants_REMINDER_WORKER_INSERT"
        const val REMINDER_WORKER_UPDATE = "com.nextgendevs.constants_REMINDER_WORKER_UPDATE"
        const val REMINDER_WORKER_DELETE = "com.nextgendevs.constants_REMINDER_WORKER_DELETE"

    }
}