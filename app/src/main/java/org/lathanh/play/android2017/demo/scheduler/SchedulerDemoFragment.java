package org.lathanh.play.android2017.demo.scheduler;

import android.arch.core.util.Function;
import android.arch.lifecycle.LifecycleFragment;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
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
 *   * Using the Rx Scheduler (and some of its basic Operators)
 *   * A View Model with Android Data Binding to have the view automatically
 *     update when the View Model updates
 *   * Having the Observable update the View Model.
 *
 * Implementation Notes:
 *   * There is a single button, "Load Data", which schedules an
 *     Observable/Observer.
 *   * The Observable/Scheduler stuff happens in {@link #handleButtonOnClick()},
 *     which is bound to the "Load Data" button.
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

    // When this Fragment is created, we'll also have our data ("Data Model")
    // initialized as though it is not yet loaded (the new
    // DataLoadingStateContainer has no Data and is not yet loading).
//    viewModel = new RandomNumberLoadableViewModel();
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
            Log.v("X", "onClick. setUserVm");
            final LiveData<UserService.User> userLiveData =
                liveDataUserService.getUserById(1);

            final UserViewModel userVm = new UserViewModel(userLiveData,
                                                           fragmentSchedulerDemoBinding);
            fragmentSchedulerDemoBinding.setUserVm(userVm);
//            userVm.userLiveData.observe(
//                SchedulerDemoFragment.this,
//                new Observer<UserService.User>() {
//                  @Override
//                  public void onChanged(@Nullable UserService.User user) {
//                    Log.v("XXX", "userVm.userLiveData.observe");
//                    userVm.userLiveData = userLiveData;
//                  }
//                });

            userLiveData.observe(SchedulerDemoFragment.this,
                                 new Observer<UserService.User>() {
                                   @Override
                                   public void onChanged(
                                       @Nullable UserService.User user) {
                                     Log.v("XXX", "onClick userLiveData onChanged; " +
                                         "user.langurage=" + user.getLanguage() + "; " +
                                         "user.name=" + user.getName());

                                     fragmentSchedulerDemoBinding.notifyChange();
//                                     PropertyChanged(
//                                         BR.userVm);
//                               text1.setValue("YYYYYYYYYYYYY");
//                               text1.postValue("ZZZZZZZZZZZZZZZ");
                                   }
                                 });
          }
        }
    );


//    fragmentSchedulerDemoBinding.setUserVm(viewModel);
    return fragmentSchedulerDemoBinding.getRoot();
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
//    disposables.clear();
  }


  //== Private methods ========================================================

  /**
   * This is where we'll see some Rx Operators in action, along with a
   * Scheduler.
   *
   * This "simulates" loading data.
   *
   * When the "Load Data" button is clicked, subscribe the {@link #observer} to
   * an Observable that will emit a single value after one second.
   */
//  private void handleButtonOnClick() {
//    Observable<Long> observable = Observable
//        .just(true)
//        .map(new Function<Boolean, Long>() {
//          @Override
//          public Long apply(Boolean aBoolean) throws Exception {
//            try {
//              Thread.sleep(1000);
//            } catch (InterruptedException e) {
//              // baby's crying again
//            }
//
//            return new Random().nextLong();
//          }
//        })
//        .subscribeOn(Schedulers.io())
//        .observeOn(AndroidSchedulers.mainThread());
//
//    observable.subscribe(observer);
//  }


  //== Inner classes ==========================================================

  public class UserViewModel extends ViewModel {

    private LiveData<UserService.User> userLiveData;
    private final SchedulerDemoFragmentBinding fragmentSchedulerDemoBinding;
    public final MutableLiveData<String> text1;
    public final LiveData<String> text2;

    UserViewModel(LiveData<UserService.User> userLiveData,
                  final SchedulerDemoFragmentBinding fragmentSchedulerDemoBinding) {
      this.userLiveData = userLiveData;
      this.fragmentSchedulerDemoBinding = fragmentSchedulerDemoBinding;

      Log.v("XXXX", "userLiveData.hasObservers-1=" + userLiveData.hasActiveObservers() + ", .hasActiveObservers-1=" + userLiveData.hasActiveObservers());

//      this.text1 =
//          Transformations.map(
//              userLiveData,
//              new Function<UserService.User, String>() {
//                @Override
//                public String apply(UserService.User input) {
//                  Log.v("XXX", "userLiveData map 1");
//                  return String.format(Locale.US,
//                                       "%d: %s, %s",
//                                       input.getId(),
//                                       input.getLanguage(),
//                                       input.getName());
//                }
//              }
//          );

      MutableLiveData<String> stringMutableLiveData = new MutableLiveData<>();
      stringMutableLiveData.setValue("XXXXXXXXXXXXXXXXXXXXXX");
      this.text1 = stringMutableLiveData;
//          Transformations.switchMap(
//              userLiveData,
//              new Function<UserService.User, LiveData<String>>() {
//                @Override
//                public LiveData<String> apply(UserService.User input) {
//                  Log.v("XXX", "userLiveData switchMap 1");
//                  MutableLiveData<String> data = new MutableLiveData<>();
//                  data.postValue(String.format(Locale.US,
//                                               "%d: %s, %s",
//                                               input.getId(),
//                                               input.getLanguage(),
//                                               input.getName()));
//                  return data;
//                }
//              });

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

      userLiveData.observe(SchedulerDemoFragment.this,
                           new Observer<UserService.User>() {
                             @Override
                             public void onChanged(
                                 @Nullable UserService.User user) {
                               Log.v("XXX", "UserViewModel userLiveData onChanged; " +
                                   "user.langurage=" + user.getLanguage() + "; " +
                                   "user.name=" + user.getName());

                               text1.postValue(user.getName());
                               fragmentSchedulerDemoBinding.notifyChange();
//                               text2.postValue("ZZZZZZZZZZZZZZZ");
                             }
                           });
//      userLiveData.observeForever(new Observer<UserService.User>() {
//        @Override
//        public void onChanged(@Nullable UserService.User user) {
//          Log.v("XXX", "userLiveData onChanged; user.name=" + user.getName());
//        }
//      });

      Log.v("XXXX", "userLiveData.hasObservers-3=" + userLiveData.hasActiveObservers() + ", .hasActiveObservers-3=" + userLiveData.hasActiveObservers());
    } // constructor


  } // class UserViewModel

}