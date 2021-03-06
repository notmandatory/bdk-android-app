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

package org.bdk.app.ui.init

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

private const val TAG = "INIT_MODEL"

class InitViewModel(application: Application) : AndroidViewModel(application) {

    private val _words = MutableLiveData<List<String>>().apply {
        value = listOf("", "", "", "", "", "", "", "", "", "", "", "")
    }

    val words: LiveData<List<String>> = _words

    fun setWords(words: List<String>) {
        _words.value = words
    }
}
