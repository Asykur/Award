package asykur.khamid.award.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import asykur.khamid.award.R
import asykur.khamid.award.adapter.AwardsAdapter
import asykur.khamid.award.database.AppDatabase
import asykur.khamid.award.model.AwardsModel
import asykur.khamid.award.utils.AppPreference
import asykur.khamid.award.utils.GeneralDialog
import asykur.khamid.award.utils.SetDataAwards
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.custom_toolbar.*
import org.jetbrains.anko.intentFor

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private var point = 0
    private var mapType: ArrayList<String>? = null
    private var isFiltered = false
    private var allDataSize = 0
    private lateinit var database: AppDatabase
    private var isLoading: Boolean = false
    private lateinit var adapter: AwardsAdapter
    private val awardList: ArrayList<AwardsModel> = ArrayList()
    private var offset = 1
    private var limit = 10
    lateinit var drawerLayout: DrawerLayout
    private lateinit var progress: GeneralDialog
    private val liveDataAwards: MutableLiveData<List<AwardsModel>> = MutableLiveData()

    companion object{
        const val request_code_filter = 100
        const val result_code_filter = 101
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initSQLite()
        initProgressDialog()
        initRecycler()
        getData()
        rvListener()
        initOnClick()
    }

    private fun initSQLite() {
        database = AppDatabase.getAppDatabase(this)
        setData()// insert data to SQLite
    }

    private fun initProgressDialog() {
        progress = GeneralDialog(getString(R.string.process_logout))
        drawerLayout = drawer
    }

    private fun initRecycler() {
        adapter = AwardsAdapter(awardList)
        rvawards.adapter = adapter
        rvawards.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
    }

    private fun initOnClick() {
        imgFilter.setOnClickListener {
            drawer.openDrawer(GravityCompat.START)
        }

        imgMenu.setOnClickListener {
            startActivityForResult(intentFor<FilterActivity>(),request_code_filter)
            overridePendingTransition(
                R.anim.in_right,
                R.anim.out_left
            )
        }
        nav_view.setNavigationItemSelectedListener(this)
    }

    private fun setData() {
        if (database.getAwardsDAO().getAllAwards().isEmpty()) {
            val dataList = SetDataAwards().getDataAwards()
            for (i in dataList.indices) {
                database.getAwardsDAO().insertNewAwards(dataList[i])
            }
        }
        allDataSize = database.getAwardsDAO().getAllAwards().size
    }

    private fun rvListener() {
        rvawards.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val manager = rvawards.layoutManager as LinearLayoutManager
                val firstVisible = manager.findLastVisibleItemPosition()

                if (dy > 0) {
                    if (firstVisible == awardList.size) {
                        if (!isLoading) {
                            Handler().postDelayed({
                                loadData()
                            }, 2000)
                            isLoading = true
                        }
                    }
                }
            }

        })
    }

    private fun loadData() {
        awardList.addAll(emptyList())
        adapter.notifyItemInserted(awardList.size - 1)
        adapter.notifyItemRemoved(awardList.size)
        getData()

    }

    private fun getData() {

        if (isFiltered){
            if (point != 0 && mapType != null && mapType?.size != 0){
                liveDataAwards.value = database.getAwardsDAO().getAwardsByTypePoint(mapType?.toTypedArray(),FilterActivity.minSB,point)
            }else if (point != 0){
                liveDataAwards.value = database.getAwardsDAO().getAwardsByPoint(FilterActivity.minSB, point)
            }else if(mapType != null && mapType?.size != 0){
                liveDataAwards.value = database.getAwardsDAO().getAwardsByType(mapType?.toTypedArray())
            }
        }else{
            if (allDataSize > 0) {
                if (allDataSize > limit) {
                    liveDataAwards.value = database.getAwardsDAO().getPagingAwards(limit, offset)
                } else {
                    liveDataAwards.value = database.getAwardsDAO().getPagingAwards(allDataSize, offset)
                }

            } else {
                adapter.noMoreAwards(true)
                adapter.notifyDataSetChanged()
                isLoading = true
            }
        }

        liveDataAwards.observe(this, Observer { data ->
            if (data != null) {
                if (data.isNotEmpty()){
                    isLoading = false

                    if (isFiltered){
                        awardList.clear()
                    }
                    awardList.addAll(data)
                    adapter.setAdapterData(awardList)
                    adapter.notifyDataSetChanged()
                    offset = (offset - 1) * limit
                    allDataSize -= limit
                }else{
                    adapter.noMoreAwards(true)
                    adapter.notifyDataSetChanged()
                }

            } else {
                adapter.noMoreAwards(true)
                adapter.notifyDataSetChanged()
            }

        })

    }

    fun noMoreData() {
        AwardsAdapter.isEmptyAwards = true
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> gotoHome()
            R.id.nav_Logout -> doLogout()
        }
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    private fun doLogout() {
        progress.show(this.supportFragmentManager, null)
        Handler().postDelayed({
            progress.dismissAllowingStateLoss()
            AppPreference.setPreference(this).putBoolean(AppPreference.isLogin, false).commit()
            startActivity(intentFor<SplashActivity>())
            finishAffinity()
        }, 2000)
    }

    private fun gotoHome() {
        drawer.closeDrawer(GravityCompat.START)
    }

    override fun onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == request_code_filter){
            if (resultCode == result_code_filter){
                val bundle = data?.extras
                if (bundle != null){
                    mapType = ArrayList()
                    isFiltered = true
                    point = bundle.getInt(FilterActivity.filter_point)
                    mapType = bundle.getStringArrayList(FilterActivity.filter_type) as? ArrayList<String>
                    Log.i("TYPELIST",mapType.toString())
                    getData()
                }
            }
        }
    }
}
