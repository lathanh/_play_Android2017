package org.lathanh.play.android2017.services.livedata;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import org.lathanh.play.android2017.services.UserService;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by rlathanh on 2017-07-02.
 */

public class LiveDataUserService {

  //== Dependencies ===========================================================

  private final UserService userService = new UserService();

  //== 'LiveDataUserService' methods ==========================================

  public LiveData<UserService.User> getUserById(final long userId) {
    final MutableLiveData<UserService.User> userLiveData = new MutableLiveData<>();
    Callable<UserService.User> callable = new Callable<UserService.User>() {
      @Override
      public UserService.User call() throws Exception {
        UserService.User user = userService.getUserById(userId);
        userLiveData.postValue(user);
        return user;
      }
    };

    ExecutorService executor = Executors.newSingleThreadExecutor();
    executor.submit(callable);
    executor.shutdown();

    return userLiveData;
  }


}
