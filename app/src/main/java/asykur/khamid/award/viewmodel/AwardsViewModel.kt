package asykur.khamid.award.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import asykur.khamid.award.model.AwardsModel
import asykur.khamid.award.utils.SetDataAwards
import asykur.khamid.award.view.MainActivity

class AwardsViewModel: ViewModel() {

    private var awardsViewModel: MutableLiveData<ArrayList<AwardsModel>> = MutableLiveData()
    private var start = 0
    private var end = 10
    private var list: ArrayList<AwardsModel> = ArrayList()
    private val allData = SetDataAwards().getDataAwards()
    private var dataSize: Int = allData.size
    private var isEnd = false

    fun getDataAwards(){
        if (dataSize >= end){
            subList(start,end)
            end += 10
            start += 10
        }else{
            subList(start,dataSize)
            isEnd = true
        }
    }

    private fun subList(start: Int, end: Int){
        if (!isEnd){
            list.addAll(allData.subList(start,end))
            awardsViewModel.value = list
        }else{
            MainActivity().noMoreData()
        }
    }
//    private fun loopData(ends: Int){
//        if (start > 0){
//            start += 1
//        }
//        val a = start
//        val b = ends
//        for (i in start+1..ends){
//            list.add(allData[i])
//            awardsViewModel.value = list
//            start = i
//        }
//    }

    fun observeAwards(): LiveData<ArrayList<AwardsModel>>{
        return awardsViewModel
    }
}