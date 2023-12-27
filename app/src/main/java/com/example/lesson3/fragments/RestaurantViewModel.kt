package com.example.lesson3.fragments

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.lesson3.data.Manager
import com.example.lesson3.myConsts.TAG
import com.example.lesson3.repository.AppRepository

class RestaurantViewModel : ViewModel() {

    var managerList: LiveData<List<Manager>> = AppRepository.getInstance().listOfManager
    private var _manager : Manager = Manager()
    val faculty
        get()=_manager

    init{
        AppRepository.getInstance().manager.observeForever{
            _manager=it
            Log.d(TAG, "получен student v studlistviewmodel")
        }
    }

    fun deleteFaculty(){
        if (faculty!=null)
            AppRepository.getInstance().deleteRestaurant(faculty!!)
    }

    fun appendFaculty(facultyName: String){
        val manager=Manager()
        manager.name=facultyName
        AppRepository.getInstance().addRestaurant(manager)
    }

    fun updateFaculty(facultyName: String){
        if (_manager!=null){
            _manager!!.name=facultyName
            AppRepository.getInstance().updateRestaurant(_manager!!)
        }
    }

    fun setFaculty(manager: Manager){
        AppRepository.getInstance().setCurrentRestaurant(manager)
    }
}






