package org.lathanh.play.android2017.demo.scheduler;

import android.arch.core.util.Function;
import android.arch.lifecycle.LifecycleFragment;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.lathanh.play.android2017.BR;
import org.lathanh.play.android2017.databinding.SchedulerDemoFragmentBinding;
import org.lathanh.play.android2017.services.UserService;
import org.lathanh.play.android2017.services.livedata.LiveDataUserService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Demonstrates:
 *
 * Implementation Notes:
 *   * There is a single button, "Load Data", which schedules an
 *     Observable/Observer.
 *   * As the Observer's onStart and onComplete are called, the View Model's
 *     LoadingState is updated appropriately.
 *   * The View is automatically updated according to the LoadingState (using
 *     Android Data Binding).
 *
 * @author Robert LaThanh
 * @since 2017-02-21
 */
public class SchedulerDemoFragment extends LifecycleFragment {

  //== Dependencies ===========================================================

  private LiveDataUserService liveDataUserService = new LiveDataUserService();

  //== Operating fields ========================================================

  private UserViewModel userViewModel;
  private LiveData<UserService.User> userLiveData;

//  private CompositeDisposable disposables = new CompositeDisposable();
//  private DisposableObserver<Long> observer = new DataObserver();
//  private RandomNumberLoadableViewModel viewModel;


  //== 'Fragment' methods =====================================================

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater,
                           @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {
    final SchedulerDemoFragmentBinding fragmentSchedulerDemoBinding =
        SchedulerDemoFragmentBinding.inflate(inflater, container, false);

    fragmentSchedulerDemoBinding.button.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            LiveData<UserService.User> userLiveData =
                liveDataUserService.getUserById(1);

            UserViewModel userVm = new UserViewModel(userLiveData);
            fragmentSchedulerDemoBinding.setUserVm(userVm);
          }
        }
    );

    return fragmentSchedulerDemoBinding.getRoot();
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
  }


  //== Inner classes ==========================================================

  public class UserViewModel extends BaseObservable {

    private String text1;
    private String text2;

    public UserViewModel(LiveData<UserService.User> userLiveData) {
      userLiveData.observe(
          SchedulerDemoFragment.this,
          new Observer<UserService.User>() {
            @Override
            public void onChanged(@Nullable UserService.User user) {
              text1 = String.format(Locale.US,
                                    "%d: [%s] %s",
                                    user.getId(),
                                    user.getLanguage(),
                                    user.getName());
              DateFormat dateFormat =
                  SimpleDateFormat.getDateTimeInstance(DateFormat.MEDIUM,
                                                       DateFormat.LONG);
              text2 = String.format(Locale.US, "(%s)",
                                    dateFormat.format(user.getLastUpdate()));
              UserViewModel.this.notifyPropertyChanged(BR.text1);
              UserViewModel.this.notifyPropertyChanged(BR.text2);
            }
          });
    } // ctor

    @Bindable
    public String getText1() {
      return text1;
    }

    @Bindable
    public String getText2() {
      return text2;
    }
  }

  public class UserLiveViewModel extends ViewModel {

    public final LiveData<String> text1;
    public final LiveData<String> text2;

    UserLiveViewModel(LiveData<UserService.User> userLiveData,
                      final SchedulerDemoFragmentBinding fragmentSchedulerDemoBinding) {
      Log.v("XXXX", "userLiveData.hasObservers-1=" + userLiveData.hasActiveObservers() + ", .hasActiveObservers-1=" + userLiveData.hasActiveObservers());

      this.text1 =
          Transformations.map(
              userLiveData,
              new Function<UserService.User, String>() {
                @Override
                public String apply(UserService.User input) {
                  Log.v("XXX", "userLiveData map 1");
                  return String.format(Locale.US,
                                       "%d: %s, %s",
                                       input.getId(),
                                       input.getLanguage(),
                                       input.getName());
                }
              }
          );
      Log.v("XXXX", "text1.hasObservers-1 = " + text1.hasObservers() + ", .hasActiveObservers-1=" + text1.hasActiveObservers());
      Log.v("XXXX", "userLiveData.hasObservers-2=" + userLiveData.hasActiveObservers() + ", .hasActiveObservers-2=" + userLiveData.hasActiveObservers());

      this.text2 =
          Transformations.switchMap(
              userLiveData,
              new Function<UserService.User, LiveData<String>>() {
                @Override
                public LiveData<String> apply(UserService.User input) {
                  Log.v("XXX", "userLiveData switchMap 2");
                  DateFormat dateFormat =
                      SimpleDateFormat.getDateTimeInstance(DateFormat.MEDIUM,
                                                           DateFormat.LONG);

                  MutableLiveData<String> data = new MutableLiveData<>();
                  data.postValue(
                      String.format(Locale.US, "(%s)",
                                    dateFormat.format(input.getLastUpdate())));
                  return data;
                }
              });

      userLiveData.observe(
          SchedulerDemoFragment.this,
          new Observer<UserService.User>() {
            @Override
            public void onChanged(
                @Nullable UserService.User user) {
              Log.v("XXX", "UserViewModel userLiveData onChanged; " +
                  "user.language=" + user.getLanguage() + "; " +
                  "user.name=" + user.getName());

//              text1.postValue(user.getName());
              fragmentSchedulerDemoBinding.notifyChange();
              fragmentSchedulerDemoBinding.notifyPropertyChanged(BR.text1);
//              text2.postValue("ZZZZZZZZZZZZZZZ");
            }
          });

      Log.v("XXXX", "userLiveData.hasObservers-3=" + userLiveData.hasActiveObservers() + ", .hasActiveObservers-3=" + userLiveData.hasActiveObservers());
    } // constructor
  } // class UserLiveViewModel

}