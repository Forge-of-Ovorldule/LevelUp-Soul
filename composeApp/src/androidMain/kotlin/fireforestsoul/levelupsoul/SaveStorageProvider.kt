package fireforestsoul.levelupsoul

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle

@SuppressLint("StaticFieldLeak")
object SaveStorageProvider {
    private var appContext: Context? = null
    private var currentActivity: Activity? = null
    private var activityTrackingStarted = false

    fun init(context: Context) {
        appContext = context

        val app = context.applicationContext as? Application
        if (app != null && !activityTrackingStarted) {
            activityTrackingStarted = true
            app.registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
                override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}
                override fun onActivityStarted(activity: Activity) {}
                override fun onActivityResumed(activity: Activity) {
                    currentActivity = activity
                }
                override fun onActivityPaused(activity: Activity) {}
                override fun onActivityStopped(activity: Activity) {}
                override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
                override fun onActivityDestroyed(activity: Activity) {
                    if (currentActivity === activity) currentActivity = null
                }
            })
        }
    }

    fun getContext(): Context {
        return appContext ?: throw IllegalStateException(
            "SaveStorageProvider not initialized! Call SaveStorageProvider.init(context) in Application.onCreate()"
        )
    }

    fun currentActivity(): Activity? = currentActivity
}
