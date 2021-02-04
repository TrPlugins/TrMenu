package me.arasple.mc.trmenu.util

import java.text.SimpleDateFormat

/**
 * @author Arasple
 * @date 2021/2/4 10:58
 */
object Time {

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

    val formatDate: () -> String = { dateFormat.format(System.currentTimeMillis()) }

}