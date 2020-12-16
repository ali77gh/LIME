package com.ali77gh.lime.ui.fragment

interface Backable {
    /**
     * @return true if used , false if not used
     * */
    fun onBack():Boolean
}