package com.example.lesson3.repository

import com.example.lesson3.data.Manager
import com.example.lesson3.data.Client
import com.example.lesson3.data.Invoice
import com.example.lesson3.database.ListDAO
import kotlinx.coroutines.flow.Flow
import java.util.UUID

class OfflineDBRepository(val dao: ListDAO): DBRepository {
    override fun getFaculty(): Flow<List<Manager>> =dao.getFaculty()
    override suspend fun insertFaculty(manager: Manager) = dao.insertFaculty(manager)
    override suspend fun updateFaculty(manager: Manager) =dao.updateFaculty(manager)
    override suspend fun insertAllFaculty(managerList: List<Manager>) =dao.insertAllFaculty(managerList)
    override suspend fun deleteFaculty(manager: Manager) =dao.deleteFaculty(manager)
    override suspend fun deleteAllFaculty() =dao.deleteAllFaculty()

    override fun getAllGroups(): Flow<List<Client>> =dao.getAllGroups()
    override fun getFacultyGroups(restaurantId : UUID): Flow<List<Client>> =dao.getFacultyGroups(restaurantId)
    override suspend fun insertGroup(client: Client) =dao.insertGroup(client)
    override suspend fun deleteGroup(client: Client) =dao.deleteGroup(client)
    override suspend fun deleteAllGroups() =dao.deleteAllGroups()

    override fun getAllStudents(): Flow<List<Invoice>> =dao.getAllStudents()
    override fun getGroupStudents(courseId : UUID): Flow<List<Invoice>> =dao.getGroupStudents(courseId)
    override suspend fun insertStudent(invoice: Invoice) =dao.insertStudent(invoice)
    override suspend fun deleteStudent(invoice: Invoice) =dao.deleteStudent(invoice)
    override suspend fun deleteAllStudents() =dao.deleteAllStudents()
}