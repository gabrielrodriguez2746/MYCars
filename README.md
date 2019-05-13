# MYCars

MYCars is a simple app to practice with MapView google maps component

* * *

## Pending
- Manage android.graphics.SurfaceTexture exception on map fist time load.
- Add support for instrumented test.
- Add ui component to show the user data has been loading.
- Clean database with a schedule job after a amont of time.
- Improve animations.
- More unit test support.

* * *

## Integrations
r has integrations for CD an CI
- CI integration has been made with [CircleCI](https://circleci.com/) running all unit test and static code anlizers as a condition to merge.
- CD integration runs with [bitrise](https://www.bitrise.io/). It has the api key for the project on its secrets `env` variables
and it compile a build for distribution after a push on master branch. 
To compile the app after clone the repo yo need to provide you `api_key` on the `api_key.xml` file. 
    
## Distribution
App is distributed to [Play Store Beta Channel](https://play.google.com/apps/testing/com.mycars.maps) or to a list of tester
with Bitrise (user get an email notifying about the new available version).
    
## Architecture
The project follows a MVVM architecture defined by this components

* [Activity/Fragments](#activity/Fragments)
* [ViewModel](#viewmodel)
* [Model](#model)
* [Repository](#repository)

The project make each feature independent and the project holds on three base module `base`, `data`, `baseui`. The feature module 
should just know this three and all the dependencies as mapper and repository will be provided by the `app` module through 
`Dagger`

* * *

- ### Activity/Fragments

**Responsibilities:** This class is responsible only for rendering and view binding. Create `Views`.

**What don't do:** 
* Don't make any async call like Service Calls, get data from persistence or any heavy operation. 
* Don't use singletons to save this reference.
* Don't allocate a reference of this class in any static method like listeners or asyncTasks

````
#!kotlin
class MainActivity : AppCompatActivity(), HasSupportFragmentInjector, OnFragmentInteraction {

    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>

    private lateinit var navController: NavController
    private val isNavControllerInitialized: Boolean get() = ::navController.isInitialized

    private var toast: Toast? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)

        binding.bnHome.setupWithNavController(
            listOf(R.navigation.main, R.navigation.map),
            supportFragmentManager,
            R.id.flNavController
        ).observe(this, Observer { navController ->
            setupActionBarWithNavController(navController)
            this.navController = navController
        })
    }

    override fun onItemClicked(fragment: String, id: String) {
        when (fragment) {
            CarListFragment.CAR_LIST -> {
                val data = bundleOf(CarDetailFragment.CAR_ID to id.toInt())
                navController.navigate(R.id.action_detail, data)
            }
        }
    }

    override fun onBackPressed() {
        if (isNavControllerInitialized && !navController.navigateUp()) {
            super.onBackPressed()
        }
    }

    override fun onNavigateUp(fragment: String) {
        onSupportNavigateUp()
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = fragmentInjector
}
````


* * *

- ### ViewModel

**Responsibilities:** A ViewModel provides the data for a specific UI component, such as a fragment or activity, and handles the communication with the business part of data handling, such as calling other components to load the data or forwarding user modifications. The ViewModel does not know about the View.
**What don't do:** 
* Don't reference the View for example `Activity` or `Fragment` never.

````
#!kotlin
class CarMapsViewModel @Inject constructor(
    private val repository: @JvmSuppressWildcards BaseRepository<Any, Int, Car>
) : ViewModel(), LifecycleObserver {

    private val initialDisposables = CompositeDisposable()
    private val _events = MutableLiveData<CarMapsViewModelEvents>()

    val events: LiveData<CarMapsViewModelEvents> get() = _events

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        initialDisposables += repository.getSingleListData(null)
            .subscribeOn(Schedulers.computation())
            .flatMapObservable { Observable.fromIterable(it) }
            .map { with(it.coordinate) { MarkerMap(latitude, longitude, it.type) } }
            .toList()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(onError = {
                _events.postValue(OnRequestError(it.message))
            }, onSuccess = {
                if (it.isEmpty()) {
                    _events.postValue(OnEmptyResults)
                } else {
                    _events.postValue(OnMapItems(it))
                }

            })
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        dispose()
    }

    internal fun dispose() {
        initialDisposables.clear()
    }

    sealed class CarMapsViewModelEvents {
        object OnEmptyResults : CarMapsViewModelEvents()
        class OnRequestError(val errorMessage: String?) : CarMapsViewModelEvents()
        class OnMapItems(val items: List<MarkerMap>) : CarMapsViewModelEvents()
    }

}
````

* * *

- ### Models

**Responsibilities:** The Models are the entities necessaries to work with this module, represent a business logic unit and are the result of the `Repository`.

The models should be data classes.

**What don't do:** 
* Don't perform any kind of logic here.

````
#!kotlin
@Entity(tableName = "car")
data class Car(
    @PrimaryKey
    @ColumnInfo(name = "car_id")
    val id: Int,
    val type: String,
    val heading: Double,
    @Embedded(prefix = "car_")
    val coordinate: Coordinate
)
````


- ### Repository

**Responsibilities:** Module responsible for handling data operations. They provide a clean API to the rest of the app. 
They know where to get the data from and what API calls to make when data is updated. 
You can consider them as mediators between different data sources (persistent model, web service, cache, etc.).

````
#!kotlin
class CarRepository @Inject constructor(
    private val service: CarsService,
    private val dao: CarsDao
) : BaseRepository<Any, Int, Car>() {

    override fun getSingleListData(parameters: Any?): Single<List<Car>> {
        return dao.getCarsPersistenceList()
            .subscribeOn(Schedulers.io())
            .toSingle()
            .flatMap(::validateEmptyPersistence)
            .onErrorResumeNext { getDataFromServer() }
    }

    override fun getSingleDataByIdentifier(identifier: Int): Single<Car> {
        return dao.getCarById(identifier)
            .subscribeOn(Schedulers.io())
            .toSingle()
            .onErrorResumeNext {
                getDataFromServer()
                    .map { it.first { car -> car.id == identifier } }
            }
    }

    internal fun validateEmptyPersistence(cars: List<Car>): Single<List<Car>> {
        return if (cars.isEmpty()) {
            getDataFromServer()
        } else {
            Single.just(cars)
        }
    }

    internal fun getDataFromServer(): Single<List<Car>> {
        return service.getCarWrapper()
            .subscribeOn(Schedulers.io())
            .map { it.cars }
            .observeOn(Schedulers.io())
            .doOnSuccess { dao.insert(it) }
    }

}
````

* * *



