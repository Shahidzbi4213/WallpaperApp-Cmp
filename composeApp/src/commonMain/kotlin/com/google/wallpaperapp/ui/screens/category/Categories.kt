package com.google.wallpaperapp.ui.screens.category

data class Category(
    val name: String,
    val query: String,
    val thumbnail:String,
)

val categories = listOf(
    Category(name = "Earth & Landscapes", query = "landscape", thumbnail = "https://images.pexels.com/photos/814499/pexels-photo-814499.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1"),
    Category(name = "Abstract & Fluid", query = "abstract", thumbnail = "https://images.pexels.com/photos/2850287/pexels-photo-2850287.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1"),
    Category(name = "Macro & Textures", query = "macro", thumbnail = "https://images.pexels.com/photos/1031641/pexels-photo-1031641.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1"),
    Category(name = "Minimal & Geometry", query = "minimal", thumbnail = "https://images.unsplash.com/photo-1550684848-fac1c5b4e853?auto=format&fit=crop&w=1260&h=750&q=80"),
    Category(name = "Light & Shadow", query = "shadow", thumbnail = "https://images.pexels.com/photos/290595/pexels-photo-290595.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1"),
    Category(name = "Art & Illustration", query = "art", thumbnail = "https://images.pexels.com/photos/415574/pexels-photo-415574.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1"),
    Category(name = "Seasonal", query = "flowers", thumbnail = "https://images.pexels.com/photos/1158961/pexels-photo-1158961.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1")
)
