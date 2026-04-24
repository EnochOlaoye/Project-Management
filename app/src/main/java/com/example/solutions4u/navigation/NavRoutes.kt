package com.example.solutions4u.navigation

// All the screen routes used for navigation throughout the app.
// Each constant is a unique path that the navigation system uses to know which screen to show.
object NavRoutes {
    const val HOME = "home"
    const val ELECTRICITY = "electricity"
    const val GAS = "gas"
    const val INSURANCE = "insurance"
    const val BROADBAND = "broadband"
    const val MOBILE = "mobile"
    const val NEWS = "news"
    const val SIGN_IN = "sign_in"
    const val REGISTER = "register"
    // Profile route takes the user's id, name, and email as parameters in the URL
    const val PROFILE = "profile/{id}/{name}/{email}"
    const val FAQ = "faq"
    const val CONTACT = "contact"
    const val ABOUT = "about"
    const val SEARCH = "search"
    const val REVIEWS = "reviews"
}
