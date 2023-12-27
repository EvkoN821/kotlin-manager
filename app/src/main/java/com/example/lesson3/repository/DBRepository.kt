package com.example.lesson3.repository

import com.example.lesson3.data.Manager
import com.example.lesson3.data.Client
import com.example.lesson3.data.Invoice
import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface DBRepository {
    fun getFaculty(): Flow<List<Manager>>
    suspend fun insertFaculty(manager: Manager)
    suspend fun updateFaculty(manager: Manager)
    suspend fun insertAllFaculty(managerList: List<Manager>)
    suspend fun deleteFaculty(manager: Manager)
    suspend fun deleteAllFaculty()

    fun getAllGroups(): Flow<List<Client>>
    fun getFacultyGroups(restaurantId: UUID): Flow<List<Client>>
    suspend fun insertGroup(client: Client)
    suspend fun deleteGroup(client: Client)
    suspend fun deleteAllGroups()

    fun getAllStudents(): Flow<List<Invoice>>
    fun getGroupStudents(courseID: UUID): Flow<List<Invoice>>
    suspend fun insertStudent(invoice: Invoice)
    suspend fun deleteStudent(invoice: Invoice)
    suspend fun deleteAllStudents()
}
