/*
 * Copyright 2020 BDK Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.bdk.app

import android.app.Application
import android.util.Log
import org.bdk.jni.*
import java.nio.file.Path
import java.util.*
import kotlin.concurrent.thread

private const val TAG = "ExampleApp"

class ExampleApp : Application() {

    private val bdkApi = BdkApi()
    private val network = Network.Testnet
    //private val network = Network.Regtest

    private var bdkThread: Thread? = null;

    init {
        bdkApi.initLogger()
    }

    private fun getWorkDir(): Path {
        return filesDir.toPath()
    }

    fun getConfig(): Optional<Config> {
        return bdkApi.loadConfig(getWorkDir(), network)
    }

    fun startBdk() {
        if (getConfig().isPresent && bdkThread?.isAlive != true) {
            Log.d(TAG, "starting bdk thread")
            val workDir = filesDir.toPath()
            bdkThread = thread {
                bdkApi.start(workDir, network, false)
                Log.d(TAG, "bdk thread stopped")
            }
        }
    }

    fun stopBdk() {
        Log.d(TAG, "stopping bdk thread")
        bdkApi.stop()
    }

    fun initConfig(): Optional<InitResult> {
        val initResult = bdkApi.initConfig(getWorkDir(), network, "test passphrase", "")
        val bitcoinPeers = emptyArray<String>() //arrayOf("127.0.0.1:9333", "127.0.0.1:19333")
        bdkApi.updateConfig(getWorkDir(), network, bitcoinPeers, 10, true)

        return initResult
    }

    fun getBalance(): String {
        return if (getConfig().isPresent) {
            if (bdkThread?.isAlive == true) {
                val balance = bdkApi.balance().map(BalanceAmt::getBalance)
                if (balance.isPresent) {
                    balance.get().toString()
                } else {
                    "NO VALUE"
                }
            } else {
                "DEAD THREAD"
            }
        } else {
            "NO CONFIG"
        }
    }

    fun getDepositAddress(): String {
        return if (getConfig().isPresent) {
            if (bdkThread?.isAlive == true) {
                bdkApi.depositAddress().address
            } else {
                "DEAD THREAD"
            }
        } else {
            "NO CONFIG"
        }
    }

    fun withdrawToAddress(
        passphrase: String,
        address: String,
        feePerVByte: Long,
        amount: Long
    ): String {
        return if (getConfig().isPresent) {
            if (bdkThread?.isAlive == true) {
                val withdrawTx = bdkApi.withdraw(passphrase, address, feePerVByte, amount)
                "DONE"
            } else {
                "DEAD THREAD"
            }
        } else {
            "NO CONFIG"
        }
    }
}