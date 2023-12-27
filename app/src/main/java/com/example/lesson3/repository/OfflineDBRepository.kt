package com.example.lesson3.repository

import com.example.lesson3.data.Manager
import com.example.lesson3.data.Client
import com.example.lesson3.data.Invoice
import com.example.lesson3.database.ListDAO
import kotlinx.coroutines.flow.Flow
import java.util.UUID

class OfflineDBRepository(val dao: ListDAO): DBRepository {
    override fun getManager(): Flow<List<Manager>> =dao.getFaculty()
    override suspend fun insertManager(manager: Manager) = dao.insertFaculty(manager)
    override suspend fun updateManager(manager: Manager) =dao.updateFaculty(manager)
    override suspend fun insertAllManagers(managerList: List<Manager>) =dao.insertAllFaculty(managerList)
    override suspend fun deleteManager(manager: Manager) =dao.deleteFaculty(manager)
    override suspend fun deleteAllManagers() =dao.deleteAllFaculty()

    override fun getAllClients(): Flow<List<Client>> =dao.getAllGroups()
    override fun getManagerClients(restaurantId : UUID): Flow<List<Client>> =dao.getFacultyGroups(restaurantId)
    override suspend fun insertClient(client: Client) =dao.insertGroup(client)
    override suspend fun deleteClient(client: Client) =dao.deleteGroup(client)
    override suspend fun deleteAllClients() =dao.deleteAllGroups()

    override fun getAllInvoices(): Flow<List<Invoice>> =dao.getAllStudents()
    override fun getClientInvoices(courseId : UUID): Flow<List<Invoice>> =dao.getGroupStudents(courseId)
    override suspend fun insertInvoice(invoice: Invoice) =dao.insertStudent(invoice)
    override suspend fun deleteInvoice(invoice: Invoice) =dao.deleteStudent(invoice)
    override suspend fun deleteAllInvoices() =dao.deleteAllStudents()
}