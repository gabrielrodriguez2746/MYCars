package com.mycars.base.navigation

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import dagger.MapKey
import javax.inject.Inject
import kotlin.reflect.KClass

class AppNavigationProvider @Inject constructor(
    private val navigationMap: Map<NavigationType, NavigationClassIntent>
) : NavigationProvider {

    override fun navigate(context: Context, navigationType: NavigationType) {
        context.startActivity(navigationMap.getValue(navigationType)(context))
    }

}

interface NavigationProvider {
    fun navigate(context: Context, navigationType: NavigationType)
}

enum class NavigationType {
    HOME, LAUNCHER
}

@MustBeDocumented
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@MapKey
annotation class NavigationKey(
    val type: NavigationType
)

data class NavigationClassIntent(
    val value: KClass<out AppCompatActivity>
) {
    operator fun invoke(context: Context) = Intent(context, value.java)
}

