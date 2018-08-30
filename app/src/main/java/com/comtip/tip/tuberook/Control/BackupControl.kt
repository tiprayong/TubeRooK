package com.comtip.tip.tuberook.Control

import android.app.Activity
import android.content.DialogInterface
import android.os.Build
import android.support.v7.app.AlertDialog
import com.comtip.tip.mydictk.RoomDatabaseKotlin.BackupDatabase
import com.comtip.tip.tuberook.Presenter.CustomSnackBar
import com.comtip.tip.tuberook.R
import com.comtip.tip.tuberook.RoomDatabase.AppDatabase.Companion.DATABASE

/**
 * Created by TipRayong on 17/4/2561 11:16
 * TubeRooK
 */
/*--------------------------------------------------------------------------------------
        Method:  alertBackup
        Description:  Backup and Restore Database
-------------------------------------------------------------------------------------*/
fun alertBackup(activity: Activity) {
    val alertBackup = AlertDialog.Builder(activity)
    alertBackup.setTitle("Backup");
    alertBackup.setPositiveButton("External") { _, _ ->

        if (BackupDatabase.checkWritePermission(activity)) {
            databaseManagementDialog(activity, true);
        } else {
            CustomSnackBar.setMessage(activity, "Please Grant STORAGE Permission.");
            //Toast.makeText(activity,"Please Grant STORAGE Permission.",Toast.LENGTH_SHORT).show()
        }
    }

    alertBackup.setNegativeButton("Internal") { _, _ ->
        if (BackupDatabase.checkWritePermission(activity)) {
            databaseManagementDialog(activity, false);
        } else {
            CustomSnackBar.setMessage(activity, "Please Grant STORAGE Permission.");
            //Toast.makeText(activity,"Please Grant STORAGE Permission.",Toast.LENGTH_SHORT).show()
        }
    }

    val alertView = alertBackup.create()
    alertView.show()

    val externalImage = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        activity.resources.getDrawable(android.R.drawable.stat_notify_sdcard_usb, null)
    } else {
        activity.resources.getDrawable(android.R.drawable.stat_notify_sdcard_usb)
    }
    externalImage.setBounds(0, 0, externalImage.intrinsicWidth, externalImage.intrinsicHeight)
    alertView.getButton(DialogInterface.BUTTON_POSITIVE)
            .setCompoundDrawables(externalImage, null, null, null)


    val internalImage = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        activity.resources.getDrawable(android.R.drawable.stat_notify_sdcard, null)
    } else {
        activity.resources.getDrawable(android.R.drawable.stat_notify_sdcard)
    }
    internalImage.setBounds(0, 0, internalImage.intrinsicWidth, internalImage.intrinsicHeight)
    alertView.getButton(DialogInterface.BUTTON_NEGATIVE)
            .setCompoundDrawables(internalImage, null, null, null)

}

/*--------------------------------------------------------------------------------------
        Method:  databaseManagementDialog
        Description:  Backup and Restore Database
-------------------------------------------------------------------------------------*/
fun databaseManagementDialog(activity: Activity, isExt: Boolean) {
    val alertDB = AlertDialog.Builder(activity)
    if (isExt) {
        alertDB.setTitle("External Storage");
    } else {
        alertDB.setTitle("Internal Storage");
    }
    alertDB.setIcon(R.mipmap.ic_launcher);

    alertDB.setPositiveButton("Backup") { _, _ ->
        if (isExt) {
            BackupDatabase.backupEXT(activity, DATABASE)
        } else {
            BackupDatabase.backup(activity, DATABASE)
        }
    }

    alertDB.setNeutralButton("Restore") { _, _ ->
        if (isExt) {
            if (BackupDatabase.restoreEXT(activity, DATABASE)) {
                activity.recreate()
            }
        } else {
            if (BackupDatabase.restore(activity, DATABASE)) {
                activity.recreate()
            }
        }
    }

    val alertView = alertDB.create()
    alertView.show()

    val backupImage = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        activity.resources.getDrawable(android.R.drawable.ic_menu_save, null)
    } else {
        activity.resources.getDrawable(android.R.drawable.ic_menu_save)
    }
    backupImage.setBounds(0, 0, backupImage.intrinsicWidth, backupImage.intrinsicHeight)
    alertView.getButton(DialogInterface.BUTTON_POSITIVE)
            .setCompoundDrawables(backupImage, null, null, null)

    val restoreImage = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        activity.resources.getDrawable(android.R.drawable.ic_menu_upload, null)
    } else {
        activity.resources.getDrawable(android.R.drawable.ic_menu_upload)
    }
    restoreImage.setBounds(0, 0, restoreImage.intrinsicWidth, restoreImage.intrinsicHeight)
    alertView.getButton(DialogInterface.BUTTON_NEUTRAL)
            .setCompoundDrawables(restoreImage, null, null, null)

}

