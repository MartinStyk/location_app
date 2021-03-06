package sk.styk.martin.location.ui.main

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.view.MenuItem
import dagger.android.AndroidInjection
import dagger.android.DispatchingAndroidInjector
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import sk.styk.martin.location.R
import sk.styk.martin.location.ui.map.MapFragment
import sk.styk.martin.location.ui.settings.SettingsFragment
import javax.inject.Inject
import dagger.android.AndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import sk.styk.martin.location.ui.statistics.StatisticsFragment


class MainActivity : LocationServiceBindableActivity(), LocationTrackingController, NavigationView.OnNavigationItemSelectedListener, HasSupportFragmentInjector {

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Fragment>

    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return androidInjector
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        // only on first run redirect to default fragment
        if (savedInstanceState == null) {
            nav_view.setCheckedItem(R.id.nav_map)
            supportFragmentManager.beginTransaction().replace(R.id.main_content, MapFragment()).commit()
        }
    }

    override fun onBackPressed() = if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
        drawer_layout.closeDrawer(GravityCompat.START)
    } else {
        super.onBackPressed()
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val fragment: Fragment = when (item.itemId) {
            R.id.nav_map -> {
                toolbar.title = getString(R.string.app_name)
                MapFragment()
            }
            R.id.nav_statistics -> {
                toolbar.title = getString(R.string.action_statistics)
                StatisticsFragment()
            }
            R.id.nav_settings -> {
                toolbar.title = getString(R.string.action_settings)
                SettingsFragment()
            }

            else -> return false
        }

        supportFragmentManager.beginTransaction()
                .replace(R.id.main_content, fragment)
                .commit()


        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
