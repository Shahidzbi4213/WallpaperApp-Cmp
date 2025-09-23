package com.google.wallpaperapp.core.platform


import platform.Foundation.NSUserDefaults
import platform.UIKit.UIApplication
import platform.UIKit.UIViewController
import platform.UIKit.UIWindow

actual class LocaleManager actual constructor() {
    actual fun changeLocale(languageCode: String) {
        NSUserDefaults.standardUserDefaults.setObject(
            listOf(languageCode),
            forKey = "AppleLanguages"
        )

        // Reload root VC
        val window: UIWindow? = UIApplication.sharedApplication.keyWindow
        val rootVC: UIViewController? = window?.rootViewController
        window?.rootViewController = rootVC
    }

}
