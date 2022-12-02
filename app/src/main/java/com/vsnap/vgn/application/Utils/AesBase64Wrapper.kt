package com.vsnap.vgn.application.Utils

import android.util.Base64
import com.vsnap.vgn.application.Utils.AesBase64Wrapper
import java.io.UnsupportedEncodingException
import java.lang.Exception
import java.lang.RuntimeException
import java.security.Key
import java.security.spec.KeySpec
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec
import kotlin.Throws

object AesBase64Wrapper {
    //    private static char[] password = "EF737CC29DAE7C80644A5B01544CBA61".toCharArray();
    //    private static byte iv[];
    //    private static String salt = "12345";
    private val password = "EVO7IC37SNAPCC29DAE7PVTC80M6U4R4UAG5ANB01544CBA61LTD".toCharArray()
    private lateinit var iv: ByteArray
    private const val salt = "120495030296"
    fun encryptAndEncode(raw: String): String {
        return try {
            val c = getCipher(1, salt)
            val encryptedVal = c.doFinal(getBytes(raw))
            //return Base64.getEncoder().encodeToString(encryptedVal);
            Base64.encodeToString(encryptedVal, Base64.DEFAULT)
        } catch (t: Throwable) {
            throw RuntimeException(t)
        }
    }

    @Throws(Exception::class)
    fun decodeAndDecrypt(encrypted: String?): String {
        var decodedValue: ByteArray? = ByteArray(0)

        //  decodedValue = Base64.getDecoder().decode(encrypted);
        decodedValue = Base64.decode(encrypted, Base64.DEFAULT)
        val c = getCipher(2, salt)
        val decValue = c.doFinal(decodedValue)
        return String(decValue)
    }

    @Throws(UnsupportedEncodingException::class)
    private fun getBytes(str: String): ByteArray {
        return str.toByteArray(charset("UTF-8"))
    }

    @Throws(Exception::class)
    private fun getCipher(mode: Int, salt: String): Cipher {
        val c = Cipher.getInstance("AES/CBC/PKCS5Padding")
        c.init(mode, generateKey(salt), IvParameterSpec(iv))
        return c
    }

    @Throws(Exception::class)
    private fun generateKey(salt: String): Key {
        val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1")
        val saltb = getBytes(salt)
        val spec: KeySpec = PBEKeySpec(password, saltb, 2000, 256)
        val tmp = factory.generateSecret(spec)
        return SecretKeySpec(tmp.encoded, "AES")
    }

    init {
        try {
            //iv = getBytes("79994A6EF73DA76C");
            iv = getBytes("120495V6OICMUSNA")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}