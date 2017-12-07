package org.lathanh.play.android2017.services.livedata;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;

import org.lathanh.play.android2017.framework.StatusLiveData;
import org.lathanh.play.android2017.services.LikeService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rlathanh on 2017-10-04.
 */

public class LiveDataLikeService {

  private final LikeService likeService = new LikeService();

  private final Map<Long, StatusLiveData<List<Long>>> likesByUserLds = new HashMap<>();

  public StatusLiveData<List<Long>> getLikesByUser(final long userId) {
    final StatusLiveData<List<Long>> likesByUserLiveData =
        new StatusLiveData<>();
    AsyncTask.THREAD_POOL_EXECUTOR.execute(new Runnable() {
      @Override
      public void run() {
        likesByUserLiveData.postValue(likeService.getLikesByUser(userId));
      }
    });
    return likesByUserLiveData;
  }

  public StatusLiveData<List<Long>> getLikersOfThing(final long thingId) {
    final StatusLiveData<List<Long>> likersOfThingLiveData =
        new StatusLiveData<>();
    AsyncTask.THREAD_POOL_EXECUTOR.execute(new Runnable() {
      @Override
      public void run() {
        likersOfThingLiveData.postValue(likeService.getLikersOfThing(thingId));
      }
    });
    return likersOfThingLiveData;
  }

  public StatusLiveData<Integer> getNumLikersOfThing(final long thingId) {
    final StatusLiveData<Integer> numLikersOfThingLiveData =
        new StatusLiveData<>();
    AsyncTask.THREAD_POOL_EXECUTOR.execute(new Runnable() {
      @Override
      public void run() {
        numLikersOfThingLiveData.postValue(
            likeService.getNumLikersOfThing(thingId));
      }
    });
    return numLikersOfThingLiveData;
  }

}
