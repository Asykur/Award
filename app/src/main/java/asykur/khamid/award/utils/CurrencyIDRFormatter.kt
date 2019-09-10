package asykur.khamid.award.utils

import android.text.TextUtils
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*


class CurrencyIDRFormatter {

    private val lang = "in"
    private val country = "ID"

    fun formatIDR(amount: Int?, isIDR: Boolean): String {

        var idrCurrency = ""
        val formatRp: DecimalFormatSymbols

        if (!TextUtils.isEmpty(amount.toString())) {

            val kursIndonesia = DecimalFormat.getCurrencyInstance() as DecimalFormat
            val localeID = Locale(lang, country)
            formatRp = DecimalFormatSymbols(localeID)

            if (isIDR){
                formatRp.currencySymbol = "IDR "
            }else{
                formatRp.currencySymbol = ""
            }
            formatRp.groupingSeparator = '.'
            kursIndonesia.decimalFormatSymbols = formatRp
            idrCurrency = kursIndonesia.format(amount)
        }

        return if (idrCurrency.contains(",")) {
            idrCurrency.substring(0, idrCurrency.length - 3)
        } else {
            idrCurrency
        }
    }
}