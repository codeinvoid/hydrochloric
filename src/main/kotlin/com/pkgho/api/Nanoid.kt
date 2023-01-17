package com.pkgho.api

import com.aventrix.jnanoid.jnanoid.NanoIdUtils
import java.security.SecureRandom

class NanoIds {
    val random = SecureRandom()
    val alphabet = charArrayOf('0','1','2','3',
        '4','5','6','7','8','9','A','B','C','D','E',
        'F','G','H','I','J','K','L','M','N','O','P','Q',
        'R','S','T','U','V','W','X','Y','Z')

    fun nanoid():String{
        return NanoIdUtils.randomNanoId(random,alphabet,8);
    }

    fun inanoid():String{
        return NanoIdUtils.randomNanoId(random,alphabet,12);
    }
}