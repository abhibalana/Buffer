package com.example.buffer.helper

import com.example.buffer.Models.ItemsItem

class constant {
    companion object {
        const val Base_url = "https://soundcloud-scraper.p.rapidapi.com/v1/"

        const val host="soundcloud-scraper.p.rapidapi.com"
        const val limit=10

        const val termsconditionurl =
            "https://abhibalana.github.io/TermsAndCondition/"
        const val privacy="https://www.privacypolicygenerator.info/live.php?token=090vXmG2LNEQxC9QCbSnkXEfra1dJthH"
        const val api_key=""


        const val playlist1="https://soundcloud.com/abhishek-balana/sets/top-50-country"
        const val playlist2="https://soundcloud.com/abhishek-balana/sets/top-50-hip-hop-rap"
        const val playlist3="https://soundcloud.com/abhishek-balana/sets/top-50-pop"
        const val playlist4="https://soundcloud.com/abhishek-balana/sets/top-50-latin"
        const val playlist5="https://soundcloud.com/abhishek-balana/sets/top-50-audiobooks"
        const val requestIdToken ="267797881978-ag49fktguv7dcni243jaddgh006fb397.apps.googleusercontent.com"
        var likeSong = ArrayList<ItemsItem>()
        const val MUSIC_SERVICE_ACTION_NEXT="com.example.buffer.next"
        const val MUSIC_SERVICE_ACTION_PAUSE="com.example.buffer.pause"
        const val MUSIC_SERVICE_ACTION_STOP="com.example.buffer.stop"
        const val MUSIC_SERVICE_ACTION_PREV="com.example.buffer.prev"




    }
}
