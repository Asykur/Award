package asykur.khamid.award.view

import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import asykur.khamid.award.R
import asykur.khamid.award.utils.CurrencyIDRFormatter
import kotlinx.android.synthetic.main.activity_filter.*
import org.jetbrains.anko.toast


class FilterActivity : AppCompatActivity() {
    private var cbProgressValue = 0
    private val minSB = 10000
    private var map: HashMap<String, String> = HashMap()
    var isPointVisible = false
    var isTypeVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter)

        tvMinSB.text = CurrencyIDRFormatter().formatIDR(seekbar.progress, true)
        seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                cbProgressValue = progress
                tvMaxSB.text = CurrencyIDRFormatter().formatIDR(progress, true)
            }

            override fun onStartTrackingTouch(seekbar: SeekBar?) {
                Log.i("START", "START")
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                if (seekBar != null) {
                    if (seekBar.progress > minSB) {
                        linGroupPoint.visibility = View.VISIBLE
                        isPointVisible = true
                        tvFilteredPoint.text = "Point: " + CurrencyIDRFormatter().formatIDR(
                            minSB,
                            false
                        ) + " - " + CurrencyIDRFormatter().formatIDR(seekBar.progress, false)
                    } else {
                        linGroupPoint.visibility = View.GONE
                        isPointVisible = false
                    }
                }
            }

        })

        imgCloseFilter.setOnClickListener {
            finish()
            overridePendingTransition(
                R.anim.in_left,
                R.anim.out_right
            )
        }
        btnClosePoint.setOnClickListener {
            linGroupPoint.visibility = View.GONE
        }
        btnCloseType.setOnClickListener {
            linGroupType.visibility = View.GONE
        }

        linClearAll.setOnClickListener {
            linGroupType.visibility = View.GONE
            linGroupPoint.visibility = View.GONE
            linClearAll.visibility = View.GONE
            cbAllType.isChecked = false
            cbVouchers.isChecked = false
            cbProducts.isChecked = false
            cbOthers.isChecked = false
            map.clear()
            seekbar.progress = minSB
            toast("Filter Canceled")
            Handler().postDelayed({
                finish()
            },1000)
        }

        linGroupPoint.viewTreeObserver.addOnGlobalLayoutListener {
            if (linGroupPoint.isVisible) {
                isPointVisible = true
                isBothSelected(isPointVisible,isTypeVisible)
            }else{
                isPointVisible = false
                isBothSelected(isPointVisible,isTypeVisible)
            }
        }

        linGroupType.viewTreeObserver.addOnGlobalLayoutListener {
            if (linGroupType.isVisible) {
                isTypeVisible = true
                isBothSelected(isPointVisible,isTypeVisible)
            }else{
                isTypeVisible = false
                isBothSelected(isPointVisible,isTypeVisible)
            }
        }

        btnFilter.setOnClickListener {
            finish()
            toast("Filter Confirmed")
        }

    }


    fun onCBClicked(view: View) {

        when(view.id){
            R.id.cbAllType -> checkAllType()
            R.id.cbVouchers -> checkVouchers()
            R.id.cbProducts -> checkProducts()
            R.id.cbOthers -> checkOthers()
        }

    }

    private fun checkOthers() {
        if (cbOthers.isChecked) {
            linGroupType.visibility = View.VISIBLE
            map[cbOthers.text.toString()] = cbOthers.text.toString()
            setTextTypeFiltered(map[cbOthers.text.toString()].toString())
        } else if (!cbOthers.isChecked) {
            map.remove(cbOthers.text.toString())
            if (TextUtils.isEmpty(map[cbOthers.text.toString()])) {
                setTextTypeFiltered("")
                checkMap()
            } else {
                setTextTypeFiltered(map[cbOthers.text.toString()].toString())
            }
        }
    }

    private fun checkProducts() {
        if (cbProducts.isChecked) {
            linGroupType.visibility = View.VISIBLE
            map[cbProducts.text.toString()] = cbProducts.text.toString()
            setTextTypeFiltered(map[cbProducts.text.toString()].toString())
        } else if (!cbProducts.isChecked) {
            map.remove(cbProducts.text.toString())
            if (TextUtils.isEmpty(map[cbProducts.text.toString()])) {
                setTextTypeFiltered("")
                checkMap()
            } else {
                setTextTypeFiltered(map[cbProducts.text.toString()].toString())
            }
        }
    }

    private fun checkVouchers() {
        if (cbVouchers.isChecked) {
            linGroupType.visibility = View.VISIBLE
            map[cbVouchers.text.toString()] = cbVouchers.text.toString()
            setTextTypeFiltered(map[cbVouchers.text.toString()].toString())
        } else if (!cbVouchers.isChecked) {
            map.remove(cbVouchers.text.toString())
            if (TextUtils.isEmpty(map[cbVouchers.text.toString()])) {
                setTextTypeFiltered("")
                checkMap()
            } else {
                setTextTypeFiltered(map[cbVouchers.text.toString()].toString())
            }
        }
    }

    private fun checkAllType() {
        if (cbAllType.isChecked) {
            linGroupType.visibility = View.VISIBLE
            map[cbAllType.text.toString()] = cbAllType.text.toString()
            val allType = map[cbAllType.text.toString()].toString()
            setTextTypeFiltered(allType)
        } else if (!cbAllType.isChecked) {
            map.remove(cbAllType.text.toString())
            if (TextUtils.isEmpty(map[cbAllType.text.toString()])) {
                setTextTypeFiltered("")
                checkMap()
            } else {
                setTextTypeFiltered(map[cbAllType.text.toString()].toString())
            }
        }
    }

    private fun checkMap() {
        if (map.isEmpty()) {
            linGroupType.visibility = View.GONE
            isTypeVisible = false
        } else {
            linGroupType.visibility = View.VISIBLE
            isTypeVisible = true
        }
    }

    private fun isBothSelected(isPointVisible: Boolean, isTypeVisible: Boolean){
        if (isPointVisible && isTypeVisible){
            linClearAll.visibility = View.VISIBLE
        }else{
            linClearAll.visibility = View.GONE
        }
    }


    private fun setTextTypeFiltered(string: String) {
        val stringBuilder = StringBuilder()
        for ((key, value) in map) {
            println("$key = $value")
            stringBuilder.append("$value ")
        }
        tvFilteredType.text = stringBuilder
    }

//    private fun animateGone(view: View,isGone: Boolean){
//        view.animate()
//            .translationY(0f)
//            .alpha(0f)
//            .setListener(object : AnimatorListenerAdapter() {
//                override fun onAnimationEnd(animation: Animator) {
//                    super.onAnimationEnd(animation)
//                    if (isGone){
//                        view.visibility = View.GONE
//                    }else{
//                        view.visibility = View.VISIBLE
//                    }
//                }
//            })
//    }
}
