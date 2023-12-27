package com.hongxing.tcp_chat

import android.app.Application
import android.content.Context
import android.text.TextUtils
import java.io.BufferedReader
import java.io.IOError
import java.io.IOException
import java.io.InputStreamReader
import java.io.PrintWriter
import java.lang.Exception
import java.net.Socket

class TcpClient(val context: Context, val listener: TcpConnectionListener? = null) {

    var mBufferReader: BufferedReader? = null
    var mPrintWriter: PrintWriter? = null
    var ServerMsg: String = ""
    fun connect(ip: String, port: Int): TcpClient {
        if (TextUtils.isEmpty(ip) || TextUtils.isEmpty(port.toString())) {
            return this
        }
        try {
            val socket = Socket(ip, port)
            mBufferReader = BufferedReader(InputStreamReader(socket.getInputStream()))
            mPrintWriter = PrintWriter(socket.getOutputStream(), true)
            listener?.connectionSuccess()
        } catch (e: IOException) {
            listener?.connectionIoException(e)
        }
        return this
    }

    /**
     * 发送消息
     */
    fun postMsg(string: String) {
        mPrintWriter?.apply {
            write(string)
            flush()
        }
    }

    /**
     * 监听服务器发过来的消息
     */
    fun listenerMessage(thread: Thread) {
        var runnable = Runnable {
            while (true) {
                try {
                    ServerMsg = mBufferReader?.readLine().toString()
                    if (ServerMsg != null) {
                        ServerMsg += "\n"
                        listener?.getMsg(ServerMsg)
                    }
                } catch (e: Exception) {
                    listener?.exception(e)
                }
            }
        }
        Thread(runnable).start()
    }

}