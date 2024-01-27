package kz.zhanarys.data.configurations

import java.security.MessageDigest

class Md5 {
    fun md5(input: String): String {
        val md = MessageDigest.getInstance("MD5")
        val byteArray = md.digest(input.toByteArray())

        // Convert the byte array to a hexadecimal string
        val result = StringBuilder()
        for (byte in byteArray) {
            result.append(String.format("%02x", byte))
        }
        return result.toString()
    }
}