package asykur.khamid.award.adapter

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import asykur.khamid.award.R
import asykur.khamid.award.model.AwardsModel
import asykur.khamid.award.utils.CurrencyIDRFormatter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import kotlinx.android.synthetic.main.item_awards.view.*
import kotlinx.android.synthetic.main.item_loadmore.view.*

class AwardsAdapter (private var awardsList: List<AwardsModel>?): RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    companion object{
        var isEmptyAwards: Boolean = false
    }
    private val viewTypeItem = 0
    private val viewTypeLoading = 1

    fun setAdapterData(list: List<AwardsModel>?){
        this.awardsList = list
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == awardsList?.size){
            viewTypeLoading
        }else{
            viewTypeItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == viewTypeItem){
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_awards,parent,false)
            VHItem(view)
        }else{
            val viewLoading = LayoutInflater.from(parent.context).inflate(R.layout.item_loadmore,parent,false)
            VHLoadMore(viewLoading)
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is VHItem){
            holder.onBind(awardsList?.get(position))
        }else if(holder is VHLoadMore){
            holder.onBind()
        }
    }

    override fun getItemCount(): Int {
        return if (awardsList == null){
            0
        }else{
            awardsList?.size!! + 1
        }
    }

    fun noMoreAwards(isEmpty: Boolean) {
        isEmptyAwards = isEmpty
    }


    inner class VHLoadMore(view: View):RecyclerView.ViewHolder(view){
        private val layoutEmpty = view.layoutEmpty
        private val progress = view.pgLoadMore

        fun onBind() {
            val iss = isEmptyAwards
            if (isEmptyAwards){
                progress.visibility = View.GONE
                layoutEmpty.visibility = View.VISIBLE
            }else{
                progress.visibility = View.VISIBLE
                layoutEmpty.visibility = View.GONE
            }
        }
    }

    inner class VHItem(view: View): RecyclerView.ViewHolder(view){
        private val name = view.tvAwardName
        private val type = view.tvAwardType
        private val point = view.tvAwardPoint
        private val img = view.imgAward

        fun onBind(awardsModel: AwardsModel?) {
            Glide.with(img.context)
                .asBitmap()
                .load(awardsModel?.imageUrl)
                .dontAnimate()
//                .transition(DrawableTransitionOptions.withCrossFade(1000))
                .placeholder(R.drawable.logo)
                .into(object : SimpleTarget<Bitmap>() {
                    override fun onResourceReady(bitmap: Bitmap, transition: Transition<in Bitmap>?) {
                        val w = bitmap.width
                        val h = bitmap.height
                        img.post {
                            val ivW = img.measuredWidth
                            val ivH = ivW * h / w
                            img.layoutParams.height = ivH
                            img.requestLayout()
                            img.setImageBitmap(bitmap)
                        }
                    }
                })

            if (awardsModel?.type.equals("Vouchers",ignoreCase = true)){
                type.background = type.context.resources.getDrawable(R.drawable.small_rounded_blue)
            }else if(awardsModel?.type.equals("Products",ignoreCase = true)){
                type.background = type.context.resources.getDrawable(R.drawable.small_rounded_brown)
            }
            name.text = awardsModel?.name
            type.text = awardsModel?.type
            val poin = CurrencyIDRFormatter().formatIDR(awardsModel?.point,false)
            point.text = "$poin Point"
        }

    }
}