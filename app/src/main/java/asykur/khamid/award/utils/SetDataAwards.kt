package asykur.khamid.award.utils

import asykur.khamid.award.model.AwardsModel

class SetDataAwards {
    private val awardsList: ArrayList<AwardsModel> = ArrayList()
    fun getDataAwards(): List<AwardsModel>{
        awardsList.add(AwardsModel("Vouchers",100000,"Gift Card IDR 200.000",""))
        awardsList.add(AwardsModel("Products",200000,"Old Fashion Cake",""))
        awardsList.add(AwardsModel("Vouchers",200000,"Gift Card IDR 300.000",""))
        awardsList.add(AwardsModel("Gift Card",400000,"Gift Card IDR 400.000",""))
        awardsList.add(AwardsModel("Vouchers",500000,"Gift Card IDR 500.000",""))
        awardsList.add(AwardsModel("Vouchers",600000,"Gift Card IDR 600.000",""))
        awardsList.add(AwardsModel("Products",700000,"Old Fashion Cake",""))
        awardsList.add(AwardsModel("Vouchers",800000,"Gift Card IDR 700.000",""))
        awardsList.add(AwardsModel("Gift Card",900000,"Gift Card IDR 800.000",""))
        awardsList.add(AwardsModel("Vouchers",1000000,"Gift Card IDR 900.000",""))
        awardsList.add(AwardsModel("Vouchers",100000,"Gift Card IDR 200.000",""))
        awardsList.add(AwardsModel("Products",200000,"Old Fashion Cake",""))
        awardsList.add(AwardsModel("Vouchers",200000,"Gift Card IDR 300.000",""))
        awardsList.add(AwardsModel("Gift Card",400000,"Gift Card IDR 400.000",""))
        awardsList.add(AwardsModel("Vouchers",500000,"Gift Card IDR 500.000",""))
        awardsList.add(AwardsModel("Vouchers",600000,"Gift Card IDR 600.000",""))
        awardsList.add(AwardsModel("Products",700000,"Old Fashion Cake",""))
        awardsList.add(AwardsModel("Vouchers",800000,"Gift Card IDR 700.000",""))
        awardsList.add(AwardsModel("Gift Card",900000,"Gift Card IDR 800.000",""))
        awardsList.add(AwardsModel("Vouchers",1000000,"Gift Card IDR 900.000",""))
        return awardsList
    }
}