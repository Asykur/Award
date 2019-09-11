package asykur.khamid.award.view

import android.os.Bundle
import android.os.Handler
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import asykur.khamid.award.R
import asykur.khamid.award.adapter.AwardsAdapter
import asykur.khamid.award.model.AwardsModel
import asykur.khamid.award.utils.AppPreference
import asykur.khamid.award.viewmodel.AwardsViewModel
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.custom_toolbar.*
import org.jetbrains.anko.intentFor


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private var isLoading: Boolean = false
    private lateinit var viewModel: AwardsViewModel
    private lateinit var adapter: AwardsAdapter
    private val awardList: ArrayList<AwardsModel> = ArrayList()
    private var page = 0
    private var limit = 9
    lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tb = toolbar as Toolbar
        setSupportActionBar(tb)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_filter)

        drawerLayout = drawer
        viewModel = ViewModelProviders.of(this).get(AwardsViewModel::class.java)
        getData()
        rvListener()

        adapter = AwardsAdapter(awardList)
        rvawards.adapter = adapter
        rvawards.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        imgFilter.setOnClickListener {
            drawer.openDrawer(GravityCompat.START)
        }

        imgMenu.setOnClickListener {
            startActivity(intentFor<FilterActivity>())
            overridePendingTransition(
                R.anim.in_right,
                R.anim.out_left
            )
        }
        nav_view.setNavigationItemSelectedListener(this)
    }

    private fun rvListener() {
        rvawards.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val manager = rvawards.layoutManager as LinearLayoutManager
                val firstVisible = manager.findLastVisibleItemPosition()

                if (dy > 0) {
                    if (firstVisible == awardList.size) {
                        Handler().postDelayed({
                            if (!isLoading) {
                                page = limit + 1
                                isLoading = true
                                loadData()
                            }
                        }, 1500)
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
        adapter.notifyDataSetChanged()
    }

    private fun getData() {
        viewModel.getDataAwards()
        viewModel.observeAwards().observe(this, Observer { data ->
            if (data != null) {
                awardList.clear()
                awardList.addAll(data)
                adapter.setAdapterData(awardList)
                adapter.notifyDataSetChanged()
                isLoading = false
            }
        })
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
        AppPreference.setPreference(this).putBoolean(AppPreference.isLogin, false).commit()
        startActivity(intentFor<SplashActivity>())
        finishAffinity()
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
}
