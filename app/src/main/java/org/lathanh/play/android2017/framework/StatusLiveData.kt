package org.lathanh.play.android2017.framework

import android.arch.lifecycle.MutableLiveData

/**
 * Created by rlathanh on 2017-10-04.
 */

class StatusLiveData<T> : MutableLiveData<T>() {

  var dataStatus: DataStatus? = null

  override fun postValue(value: T) {
    super.postValue(value)
    dataStatus = DataStatus.LOADED
  }

  override fun setValue(value: T) {
    super.setValue(value)
    dataStatus = DataStatus.LOADED
  }
}
