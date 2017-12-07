package org.lathanh.play.android2017.services.livedata;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;

import org.lathanh.play.android2017.services.ThingService;

/**
 * Created by rlathanh on 2017-10-04.
 */

public class LiveDataThingService {

  //== Dependencies ===========================================================

  private final ThingService thingService = new ThingService();

  public LiveData<ThingService.Thing> getThingById(final long thingId) {
    final MutableLiveData<ThingService.Thing> thingLiveData =
        new MutableLiveData<>();
    AsyncTask.THREAD_POOL_EXECUTOR.execute(new Runnable() {
      @Override
      public void run() {
        thingLiveData.postValue(thingService.getThing(thingId));
      }
    });

    return thingLiveData;
  }

}
