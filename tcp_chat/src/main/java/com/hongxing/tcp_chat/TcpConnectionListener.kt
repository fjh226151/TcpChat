package com.hongxing.tcp_chat

import java.io.IOException
import java.lang.Exception

interface TcpConnectionListener {
    fun connectionIoException(io: IOException)
    fun connectionSuccess()
    fun exception(e:Exception)
    fun getMsg(msg:String)
}