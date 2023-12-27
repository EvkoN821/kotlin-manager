package com.example.lesson3.repository

import android.util.Log
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.example.lesson3.API.ListAPI
import com.example.lesson3.API.ListConnection
import com.example.lesson3.API.PostId
import com.example.lesson3.API.PostResult
import com.example.lesson3.API.PostUser
import com.example.lesson3.MyApplication
import com.example.lesson3.data.Managers
import com.example.lesson3.data.Manager
import com.example.lesson3.data.Client
import com.example.lesson3.data.Clients
import com.example.lesson3.data.Invoice
import com.example.lesson3.data.Invoices
import com.example.lesson3.data.Spasibo
import com.example.lesson3.database.ListDatabase
import com.example.lesson3.myConsts.TAG
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AppRepository {
    companion object{
        private var INSTANCE: AppRepository?=null

        fun getInstance(): AppRepository {
            if (INSTANCE==null){
                INSTANCE= AppRepository()
            }
            return INSTANCE?:
            throw IllegalStateException("реп не иниц")
        }
    }

    var manager: MutableLiveData<Manager> = MutableLiveData()
    var client: MutableLiveData<Client> = MutableLiveData()
    var invoice: MutableLiveData<Invoice> = MutableLiveData()


    fun getRestaurantPosition(manager: Manager): Int=listOfManager.value?.indexOfFirst {
        it.id==manager.id } ?:-1

    fun getRestaurantPosition()=getRestaurantPosition(manager.value?: Manager())

    fun setCurrentRestaurant(position:Int){
        if (position<0 || (listOfManager.value?.size!! <= position))
            return setCurrentRestaurant(listOfManager.value!![position])
    }

    fun setCurrentRestaurant(_manager: Manager){
        manager.postValue(_manager)
    }

    fun saveData(){

    }

    fun loadData(){
        fetchRestaurants()
    }

    fun getCoursePosition(client: Client): Int=listOfClient.value?.indexOfFirst {
        it.id==client.id } ?:-1

    fun getCoursePosition()=getCoursePosition(client.value?: Client())

    fun setCurrentCourse(position:Int){
        if (listOfClient.value==null || position<0 ||
            (listOfClient.value?.size!! <=position))
            return setCurrentCourse(listOfClient.value!![position])
    }

    fun setCurrentCourse(_client: Client){
        client.postValue(_client)
    }

    val restaurantCourses
        get()= listOfClient.value?.filter { it.mangerID == (manager.value?.id ?: 0) }?.sortedBy { it.name }?: listOf()

    fun getFoodPosition(invoice: Invoice): Int=listOfInvoice.value?.indexOfFirst {
        it.id==invoice.id } ?:-1

    fun getFoodPosition()=getFoodPosition(invoice.value?: Invoice())

    fun setCurrentFood(position:Int){
        if (listOfInvoice.value==null || position<0 ||
            (listOfInvoice.value?.size!! <=position))
            return setCurrentFood(listOfInvoice.value!![position])
    }

    fun setCurrentFood(_invoice: Invoice){
        invoice.postValue(_invoice)
    }

    fun getCourseFoods(courseID: Int) =
        (listOfInvoice.value?.filter { it.clientID == courseID }?.sortedBy { it.shortName.lowercase() }?: listOf())
    fun getCourseFoodsByPrice(courseID: Int) =
        (listOfInvoice.value?.filter { it.clientID == courseID }?.sortedBy { it.getPrice }?: listOf())
    fun getCourseFoodsByWeight(courseID: Int) =
        (listOfInvoice.value?.filter { it.clientID == courseID }?.sortedBy { it.getWeight }?: listOf())

    private val listDB by lazy {OfflineDBRepository(ListDatabase.getDatabase(MyApplication.context).listDAO())}

    private val myCoroutineScope = CoroutineScope(Dispatchers.Main)

    fun getSearch(s: String, courseID: Int) =
        (listOfInvoice.value?.filter { ((s in it.name.toString() ) or (s in it.price.toString()) or (s in it.weight.toString()) ) and (it.clientID == courseID) }?.sortedBy { it.shortName }?: listOf())

    fun onDestroy(){
        myCoroutineScope.cancel()
    }

    val listOfManager: LiveData<List<Manager>> = listDB.getFaculty().asLiveData()
    val listOfClient: LiveData<List<Client>> = listDB.getAllGroups().asLiveData()
    val listOfInvoice: LiveData<List<Invoice>> = listDB.getAllStudents().asLiveData()


    private var listAPI = ListConnection.getClient().create(ListAPI::class.java)

    fun fetchRestaurants(){
        listAPI.getRestaurants().enqueue(object: Callback<Managers> {
            override fun onFailure(call: Call<Managers>, t :Throwable){
                Log.d(TAG,"Ошибка получения списка факультетов", t)
            }
            override fun onResponse(
                call : Call<Managers>,
                response: Response<Managers>
            ){
                if (response.code()==200){
                    val restaurants = response.body()
                    val items = restaurants?.items?:emptyList()
                    Log.d(TAG,"Получен список факультетов $items")
                    for (f in items){
                        println(f.id::class.java.typeName)
                        println(f.name::class.java.typeName)
                    }
                    myCoroutineScope.launch{
                        listDB.deleteAllFaculty()
                        for (f in items){
                            listDB.insertFaculty(f)
                        }
                    }
                    fetchCourses()
                }
            }
        })
    }

    fun addRestaurant(manager: Manager){
        listAPI.insertRestaurant(manager)
            .enqueue(object : Callback<PostResult>{
                override fun onResponse(call:Call<PostResult>,response: Response<PostResult>){
                    if (response.code()==200) fetchRestaurants()
                }
                override fun onFailure(call:Call<PostResult>,t: Throwable){
                    Log.d(TAG,"Ошибка добавления факультета",t)
                }
            })
    }

    fun updateRestaurant(manager: Manager){
        listAPI.updateRestaurant(manager)
            .enqueue(object : Callback<PostResult>{
                override fun onResponse(call:Call<PostResult>,response: Response<PostResult>){
                    if (response.code()==200) fetchRestaurants()
                }
                override fun onFailure(call:Call<PostResult>,t: Throwable){
                    Log.d(TAG,"Ошибка обновления факультета",t)
                }
            })
    }

    fun deleteRestaurant(manager: Manager){
        listAPI.deleteRestaurant(PostId(manager.id))
            .enqueue(object : Callback<PostResult>{
                override fun onResponse(call:Call<PostResult>,response: Response<PostResult>){
                    if (response.code()==200) fetchRestaurants()
                }
                override fun onFailure(call:Call<PostResult>,t: Throwable){
                    Log.d(TAG,"Ошибка удаления факультета",t)
                }
            })
    }

    fun fetchCourses(){
        listAPI.getCourses().enqueue(object: Callback<Clients> {
            override fun onFailure(call: Call<Clients>, t: Throwable) {
                Log.d(TAG, "Ошибка получения списка групп", t)
            }

            override fun onResponse(
                call: Call<Clients>,
                response: Response<Clients>
            ) {
                if (response.code() == 200) {
                    val courses = response.body()
                    val items = courses?.items ?: emptyList()
                    Log.d(TAG, "Получен список групп $items")
                    myCoroutineScope.launch {
                        listDB.deleteAllGroups()
                        for (g in items) {
                            listDB.insertGroup(g)
                        }
                    }
                    fetchFoods()
                }
            }
        })
    }

    fun addCourse(client: Client){
        listAPI.insertCourse(client)
            .enqueue(object : Callback<PostResult>{
                override fun onResponse(call:Call<PostResult>,response:Response<PostResult>){
                    if (response.code()==200) fetchCourses()
                }
                override fun onFailure(call:Call<PostResult>,t:Throwable){
                    Log.d(TAG,"Ошибка обновления группы", t)
                }
            })
    }

    fun updateCourse(client: Client){
        listAPI.updateCourse(client)
            .enqueue(object : Callback<PostResult>{
                override fun onResponse(call:Call<PostResult>,response:Response<PostResult>){
                    if (response.code()==200) fetchCourses()
                }
                override fun onFailure(call:Call<PostResult>,t:Throwable){
                    Log.d(TAG,"Ошибка записи группы", t)
                }
            })
    }

    fun deleteCourse(client: Client){
        listAPI.deleteCourse(PostId(client.id))
            .enqueue(object : Callback<PostResult>{
                override fun onResponse(call:Call<PostResult>,response:Response<PostResult>){
                    if (response.code()==200) fetchCourses()
                }
                override fun onFailure(call:Call<PostResult>,t:Throwable){
                    Log.d(TAG,"Ошибка удаления группы", t)
                }
            })
    }

    fun fetchFoods(){
        listAPI.getFoods().enqueue(object : Callback<Invoices>{
            override fun onFailure(call:Call<Invoices>, t : Throwable){
                Log.d(TAG,"Ошибка получения списка студентов",t)
            }
            override fun onResponse(
                call:Call<Invoices>,
                response: Response<Invoices>
            ){
                if(response.code()==200){
                    val foods = response.body()
                    val items = foods?.items?: emptyList()
                    Log.d(TAG,"Получен список студентов $items")
                    myCoroutineScope.launch {
                        listDB.deleteAllStudents()
                        for (s in items){
                            listDB.insertStudent(s)
                        }
                    }
                }
            }
        })
    }

    fun addFood(invoice: Invoice){
        listAPI.insertFood(invoice)
            .enqueue(object : Callback<PostResult>{
                override fun onResponse(call:Call<PostResult>,response:Response<PostResult>){
                    if (response.code()==200) fetchFoods()
                }
                override fun onFailure(call:Call<PostResult>,t:Throwable){
                    Log.d(TAG,"Ошибка записи студента", t.fillInStackTrace())
                }
            })
    }

    fun updateFood(invoice: Invoice){
        listAPI.updateFood(invoice)
            .enqueue(object : Callback<PostResult>{
                override fun onResponse(call:Call<PostResult>,response:Response<PostResult>){
                    if (response.code()==200) fetchFoods()
                }
                override fun onFailure(call:Call<PostResult>,t:Throwable){
                    Log.d(TAG,"Ошибка обновления студента", t.fillInStackTrace())
                }
            })
    }

    fun deleteFood(invoice: Invoice){
        listAPI.deleteFood(PostId(invoice.id))
            .enqueue(object : Callback<PostResult>{
                override fun onResponse(call:Call<PostResult>,response:Response<PostResult>){
                    if (response.code()==200) fetchFoods()
                }
                override fun onFailure(call:Call<PostResult>,t:Throwable){
                    Log.d(TAG,"Ошибка удаления студента", t.fillInStackTrace())
                }
            })
    }

    fun login(userName: String, pwd : String, dickandballs : TextView){
        listAPI.login(PostUser(userName,pwd)).enqueue(object: Callback<Spasibo> {
            override fun onFailure(call: Call<Spasibo>, t: Throwable) {
                Log.d(TAG, "Ошибка получения списка групп", t)
            }

            override fun onResponse(
                call: Call<Spasibo>,
                response: Response<Spasibo>
            ) {
                if (response.code() == 200) {
                    val resp = response.body()
                    dickandballs.text = resp?.items.toString()
                    dickandballs.callOnClick()
                }
            }
        })
    }

}





















