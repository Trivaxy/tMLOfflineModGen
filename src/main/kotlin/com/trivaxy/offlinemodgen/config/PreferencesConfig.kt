package com.trivaxy.offlinemodgen.config

import com.trivaxy.offlinemodgen.KotlinApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.prefs.Preferences


@Configuration
class PreferencesConfig {
    @Bean(name = ["AppPreferences"])
    fun getPreferences(): Preferences {
        return Preferences.userRoot().node(KotlinApplication::class.java.name)
    }
}