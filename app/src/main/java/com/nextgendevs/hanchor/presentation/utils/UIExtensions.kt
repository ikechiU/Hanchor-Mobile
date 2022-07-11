package com.nextgendevs.hanchor.presentation.utils

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.callbacks.onDismiss
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.nextgendevs.hanchor.R
import com.nextgendevs.hanchor.business.domain.utils.*
import com.nextgendevs.hanchor.presentation.auth.AuthActivity

private val TAG = "AppDebug"

fun processQueue(
    context: Context?,
    queue: Queue<StateMessage>,
    stateMessageCallback: StateMessageCallback
) {
    context?.let { ctx ->
        if(!queue.isEmpty()){
            queue.peek()?.let { stateMessage ->
                ctx.onResponseReceived(
                    response = stateMessage.response,
                    stateMessageCallback = stateMessageCallback
                )
            }
        }
    }
}

private fun Context.onResponseReceived(
    response: Response,
    stateMessageCallback: StateMessageCallback
) {
    when(response.uiComponentType){

        is UIComponentType.AreYouSureDialog -> {

            response.message?.let {
                areYouSureDialog(
                    message = it,
                    callback = response.uiComponentType.callback,
                    stateMessageCallback = stateMessageCallback
                )
            }
        }

        is UIComponentType.Toast -> {
            response.message?.let {
                displayToast(
                    message = it,
                    stateMessageCallback = stateMessageCallback
                )
            }
        }

        is UIComponentType.Dialog -> {
            displayDialog(
                response = response,
                stateMessageCallback = stateMessageCallback
            )
        }

        is UIComponentType.None -> {
            // This would be a good place to send to your Error Reporting
            // software of choice (ex: Firebase crash reporting)
            Log.i(TAG, "onResponseReceived: ${response.message}")
            stateMessageCallback.removeMessageFromStack()
        }
    }
}


private fun Context.displayDialog(
    response: Response,
    stateMessageCallback: StateMessageCallback
){
    response.message?.let { message ->

        when (response.messageType) {

            is MessageType.Error -> {
                displayErrorDialog(
                    message = message,
                    stateMessageCallback = stateMessageCallback
                )
            }

            is MessageType.Success -> {
                displaySuccessDialog(
                    message = message,
                    stateMessageCallback = stateMessageCallback
                )
            }

            is MessageType.Info -> {
                displayInfoDialog(
                    message = message,
                    stateMessageCallback = stateMessageCallback
                )
            }

            else -> {
                // do nothing
                stateMessageCallback.removeMessageFromStack()
                null
            }
        }
    }?: stateMessageCallback.removeMessageFromStack()
}

private fun Context.displaySuccessDialog(
    message: String?,
    stateMessageCallback: StateMessageCallback
) {
    MaterialDialog(this)
        .show{
            cornerRadius(10F)
            title(R.string.text_success)
            message(text = message)
            positiveButton(R.string.text_ok){
                stateMessageCallback.removeMessageFromStack()
                dismiss()
            }
            onDismiss {
            }
            cancelable(false)
        }
}

private fun Context.displayErrorDialog(
    message: String?,
    stateMessageCallback: StateMessageCallback
) {
    MaterialDialog(this)
        .show{
            cornerRadius(10F)
            title(R.string.text_error)
            message(text = message)
            positiveButton(R.string.text_ok){
                stateMessageCallback.removeMessageFromStack()
                dismiss()
            }
            onDismiss {
            }
            cancelable(false)
        }
}

private fun Context.displayInfoDialog(
    message: String?,
    stateMessageCallback: StateMessageCallback
) {
    MaterialDialog(this)
        .show{
            cornerRadius(10F)
            title(R.string.text_info)
            message(text = message)
            positiveButton(R.string.text_ok){
                stateMessageCallback.removeMessageFromStack()
                dismiss()
            }
            onDismiss {
            }
            cancelable(false)
        }
}

private fun Context.areYouSureDialog(
    message: String,
    callback: AreYouSureCallback,
    stateMessageCallback: StateMessageCallback
) {
    MaterialDialog(this)
        .show{
            cornerRadius(10F)
            title(R.string.are_you_sure)
            message(text = message)
            negativeButton(R.string.text_cancel){
                callback.cancel()
                stateMessageCallback.removeMessageFromStack()
                dismiss()
            }
            positiveButton(R.string.text_yes){
                callback.proceed()
                stateMessageCallback.removeMessageFromStack()
                dismiss()
            }
            onDismiss {
            }
            cancelable(false)
        }
}

fun Context.displayToast(
    @StringRes message:Int,
    stateMessageCallback: StateMessageCallback
){
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    stateMessageCallback.removeMessageFromStack()
}

fun Context.displayToast(
    message:String,
    stateMessageCallback: StateMessageCallback
){
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    stateMessageCallback.removeMessageFromStack()
}

fun Context.displayToast(
    message:String
){
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun NavController.safeNavigate(direction: NavDirections) {
    currentDestination?.getAction(direction.actionId)?.run { navigate(direction) }
}

fun NavController.safeNavigate(
    @IdRes currentDestinationId: Int,
    @IdRes id: Int,
    args: Bundle? = null
) {
    if (currentDestinationId == currentDestination?.id) {
        navigate(id, args)
    }
}

fun Context.toastMessage(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

private fun Context.displayErrorDialog(
    message: String?
) {
    MaterialDialog(this)
        .show{
            cornerRadius(10F)
            title(R.string.text_error)
            message(text = message)
            positiveButton(R.string.text_ok){
                dismiss()
            }
            onDismiss {
            }
            cancelable(false)
        }
}

fun Context.displayInfoDialog(
    message: String?
) {
    MaterialDialog(this)
        .show{
            cornerRadius(10F)
            title(R.string.text_info)
            message(text = message)
            positiveButton(R.string.text_ok){
                dismiss()
            }
            onDismiss {
            }
            cancelable(false)
        }
}

fun Context.areYouSureDialog(
    message: String,
    callback: AreYouSureCallback
) {
    MaterialDialog(this)
        .show{
            //https://stackoverflow.com/questions/55255130/android-afollestad-material-dialog-action-button-text-color
            val yesText = "<font color='#1B1ED8'>$message</font>"
            val cancelText = "<font color='#44D81B'>$message</font>"

            cornerRadius(10F)
            title(R.string.are_you_sure)
            message(text = message)
            negativeButton(R.string.text_cancel){
                callback.cancel()
                dismiss()
            }
            positiveButton(R.string.text_yes){
                callback.proceed()
                dismiss()
            }
            onDismiss {
            }
            cancelable(false)
        }
}


fun isNotEmpty(textView: TextView): Boolean {
    return textView.text.toString().trim().isNotEmpty()
}

fun isNotDay(textView: TextView, daysInMonth: Int): Boolean {
    return (Integer.parseInt(textView.text.toString().trim()) > daysInMonth)
}

fun isDayToHighlight(textView: TextView, currentDay: Int) : Boolean {
    return (Integer.parseInt(textView.text.toString()) == currentDay)
}

fun highlightDay(textView: TextView, dayOfMonthColor: String) {
    textView.setTextColor(Color.parseColor(dayOfMonthColor))
    textView.setTypeface(textView.typeface, Typeface.BOLD)
}

fun setEmptyText(textView: TextView) {
    textView.text = ""
}

fun hideTableRow(tableRow: TableRow) {
    tableRow.visibility = View.GONE
}

fun showTableRow(tableRow: TableRow) {
    tableRow.visibility = View.VISIBLE
}

fun Context.isInternetAvailable(): Boolean {
    val mConMgr = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    return (mConMgr.activeNetworkInfo != null && mConMgr.activeNetworkInfo!!.isAvailable
            && mConMgr.activeNetworkInfo!!.isConnected)
}

fun DatePickerDialog.makeButtonTextPurple() {
    this.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(context, R.color.primary_color_light))
    this.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(context, R.color.primary_color_light))
}

fun TimePickerDialog.makeButtonTextPurple() {
    this.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(context, R.color.primary_color_light))
    this.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(context, R.color.primary_color_light))
}

fun AlertDialog.makeButtonTextPurple() {
    this.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(context, R.color.primary_color_light))
    this.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(context, R.color.primary_color_light))
    this.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(ContextCompat.getColor(context, R.color.primary_color_light))
}

fun RecyclerView.hideFabOnScroll(fab: FloatingActionButton) {
    var scrollingPosition = 0

    this.addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)

            if(scrollingPosition > 0 && fab.isVisible) { //scrolling up
                fab.visibility = View.INVISIBLE
            } else  {
                fab.visibility = View.VISIBLE
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            scrollingPosition = dy
        }
    })
}