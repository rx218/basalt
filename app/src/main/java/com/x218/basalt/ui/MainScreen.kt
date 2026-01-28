package com.x218.basalt

@Composable
fun MainScreen() {
    Column() {
	Header()
	Divider()
	Compass()
	LocationBar()
	PrayerTimeDrawer()
    }
}

@Composable
fun Header() {
    Row() {
	BasaltIcon()
	HelpButton()
    }
}
