package com.mycars.base.listeners

interface OnFragmentInteraction {

    fun onItemClicked(fragment: String, id: String)
    fun onNavigateUp(fragment: String)
    fun onMessageToShow(message: String)
}