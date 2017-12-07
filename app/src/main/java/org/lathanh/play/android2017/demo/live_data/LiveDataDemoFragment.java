package org.lathanh.play.android2017.demo.live_data;

import android.arch.lifecycle.LifecycleFragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.lathanh.play.android2017.R;
import org.lathanh.play.android2017.services.livedata.LiveDataUserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by rlathanh on 2017-10-05.
 */

public class LiveDataDemoFragment extends LifecycleFragment {

  private static final String LOG_TAG = LiveDataDemoFragment.class.getSimpleName();


  //== Constants ==============================================================

  /**
   * The number of elements in the list to show.
   * Because some approaches will process every item in this list before
   * displaying and of the items, the larger this number the longer it will take
   * for those approaches to be displaying any items.
   */
  protected static final int NUM_LIST_ELEMENTS = 500;

  /**
   * The amount of time it should take to "load" the data.
   * An artificial delay, this allows us to see what the loading UI looks like.
   */
  protected static final int LOAD_DELAY_MS = 500;

  /** Inclusive. See {@link #ITEM_ADAPTING_COST_RANDOM_SEED} */
  protected static final int ITEM_ADAPTING_COST_MIN_MS = 6;

  /** Inclusive. See {@link #ITEM_ADAPTING_COST_RANDOM_SEED} */
  protected static final int ITEM_ADAPTING_COST_MAX_MS = 15;

  protected static final int ITEM_ADAPTING_COST_DIFF_MS =
      ITEM_ADAPTING_COST_MAX_MS - ITEM_ADAPTING_COST_MIN_MS + 1;

  /**
   * Each item will have a "random" adapting cost between
   * {@link #ITEM_ADAPTING_COST_MIN_MS} and {@link #ITEM_ADAPTING_COST_MAX_MS}.
   *
   * By using a fixed seed for the "randomization," each item will be assigned
   * the same cost for every run.
   * This results in all demos getting the same sequence of costs for its items,
   * so comparisons are more fair.
   */
  protected static final long ITEM_ADAPTING_COST_RANDOM_SEED = 0xABCDEF;


  //== Dependencies ===========================================================

  private LiveDataUserService liveDataUserService = new LiveDataUserService();


  //== Instance fields ========================================================

  protected RecyclerView recyclerView;

  private long debugLoadStartTime;


  //== Instantiation ==========================================================


  //== Instance methods =======================================================

  //-- 'Fragment' methods ----------------------------------------------------

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Start a task to "load" the list items
    Runnable onLoadedCallback = new Runnable() {
      @Override
      public void run() {
        getActivity().findViewById(R.id.loadingView).setVisibility(View.GONE);
        getActivity().findViewById(R.id.recycler_view).setVisibility(View.VISIBLE);

        long debugElapsedMs = System.currentTimeMillis() - debugLoadStartTime;
        Toast.makeText(getActivity(),
            "Load completed in " + debugElapsedMs + "ms",
            Toast.LENGTH_SHORT).show();
      }
    };
    getLoadTask(onLoadedCallback).execute();
    debugLoadStartTime = System.currentTimeMillis();
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.live_data_demo_fragment, container,
        false);

    recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
    RecyclerView.LayoutManager layoutManager =
        new LinearLayoutManager(getActivity());
    recyclerView.setLayoutManager(layoutManager);

    return view;
  }


  /**
   * Emulates loading of strings from a place that takes
   * {@link #LOAD_DELAY_MS} ms, for example from a disk or remote server.
   *
   * It will determine how much processing cost any particular item should take
   * to "adapt" (that is, to create a {@link ViewModel} from it.
   * Since a fixed seed is used for the cost "randomizer", all implementations
   * will end up with items having the same sequence of costs.
   */
  protected List<DataModel> loadData() {
    //-- "Load" the strings
    Random random = new Random(ITEM_ADAPTING_COST_RANDOM_SEED);
    List<DataModel> dataModels = new ArrayList<>(NUM_LIST_ELEMENTS);
    for (int i = 0; i < NUM_LIST_ELEMENTS; i++) {
      int delayForItem =
          ITEM_ADAPTING_COST_MIN_MS +
              random.nextInt(ITEM_ADAPTING_COST_DIFF_MS);
      dataModels.add(new DataModel(i, delayForItem));
    }

    // artificial delay to overall "loading" so it feels more real
    try {
      Thread.sleep(LOAD_DELAY_MS);
    } catch (InterruptedException e) {
      Log.e(LOG_TAG, "Load sleep interrupted!");
    }

    return dataModels;
  }

  /** Create - "adapt" - a ViewModel from a DataModel. */
  public static ViewModel adaptDataModelToViewModel(DataModel dataModel) {
    //-- A handful of string operations (e.g., concat, parse) until delay
    //   elapsed
    String string = dataModel.getName();
    String delayDescription = AdaptingDemo_Models.adaptForDelay(dataModel);
    return new ViewModel(string, delayDescription);
  }


  @NonNull
  protected AsyncTask<Void, Void, List<LoadingViewModel>>
  getLoadTask(@NonNull final Runnable onLoadedCallback) {
    return new AsyncTask<Void, Void, List<LoadingViewModel>>() {
      @Override
      protected List<LoadingViewModel> doInBackground(Void... params) {
        List<DataModel> dataModels = loadData();

        // create a LoadingViewModel for each DataModel
        // the actual ViewModel isn't adapted here
        List<LoadingViewModel> loadingViewModels =
            new ArrayList<>(dataModels.size());
        for (DataModel dataModel : dataModels) {
          loadingViewModels.add(new LoadingViewModel(dataModel));
        }
        return loadingViewModels;
      }

      @Override
      protected void onPostExecute(List<LoadingViewModel> loadingViewModels) {
        // Set up adapter and call callback
        Adapter adapter = new Adapter(getActivity(), loadingViewModels);
        recyclerView.setAdapter(adapter);
        onLoadedCallback.run();
      }
    };
  } // getLoadTask()


  //== Inner classes ==========================================================

  private class Adapter extends RecyclerView.Adapter<LoadingViewHolder> {

    private final List<LoadingViewModel> loadingViewModels;
    private LayoutInflater inflater;

    public Adapter(Context context, List<LoadingViewModel> loadingViewModels) {
      this.inflater = LayoutInflater.from(context);
      this.loadingViewModels = loadingViewModels;
    }

    @Override
    public LoadingViewHolder onCreateViewHolder(ViewGroup parent, int i) {
      AdaptingDemoDataBindingImprovedListItemBinding binding =
          AdaptingDemoDataBindingImprovedListItemBinding.inflate(inflater,
              parent, false);
      return new LoadingViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(final LoadingViewHolder viewHolder,
                                 int position) {
      final LoadingViewModel loadingViewModel = loadingViewModels.get(position);
      viewHolder.setLoadingViewModel(loadingViewModel);

      if (loadingViewModel.getViewModel() == null) {
        // start task to make this view available
        new AsyncTask<Void, Void, ViewModel>() {
          @Override
          protected ViewModel doInBackground(Void... params) {
            return adaptDataModelToViewModel(loadingViewModel.getDataModel());
          }

          @Override
          protected void onPostExecute(ViewModel viewModel) {
            loadingViewModel.setOnBindTimeNanos(System.nanoTime());
            loadingViewModel.setViewModel(viewModel);
          }
        }.executeOnExecutor(threadPoolExecutor);
      } else {
        loadingViewModel.setOnBindTimeNanos(System.nanoTime());
      }
    } // onBindViewHolder()

    @Override
    public int getItemCount() {
      return loadingViewModels.size();
    }
  } // class Adapter


  //== Inner classes ==========================================================

  /**
   * This base Adapter implementation simplifies (centralizes) the creation of
   * the View and ViewHolder for each item.
   */
  protected static abstract class BaseAdapter
      extends RecyclerView.Adapter<ViewHolder> {

    private final Context context;

    protected BaseAdapter(Context context) {
      this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                         int i) {
      View v =
          LayoutInflater.from(context).inflate(R.layout.adapting_demo_list_item,
              parent, false);
      return new ViewHolder(v);
    }
  }

}
