package sk.styk.martin.location.util

import java.text.DecimalFormat

object UnitUtils {
    @JvmStatic
    fun speedConvert(metresSecond: Float?): Float {
        return metresSecond?.times(3.6f) ?: 0f
    }

    @JvmStatic
    fun speedConvertToString(metresSecond: Float?): String {
        return metresSecond?.let {
            DecimalFormat.getInstance().format(
                    speedConvert(it)
            )
        } ?: "0"
    }

}