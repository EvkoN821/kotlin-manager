package com.example.lesson3.fragments

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lesson3.data.Client
import com.example.lesson3.data.Invoice
import com.example.lesson3.repository.AppRepository

class InvoiceViewModel : ViewModel() {
    var invoiceList: MutableLiveData<List<Invoice>> = MutableLiveData()

    private var _invoice: Invoice?= null
    val student
        get()= _invoice

    var client: Client? = null
    fun update_info(typeSort: Int){
        if (typeSort == 1)
            AppRepository.getInstance().listOfInvoice.observeForever{
                invoiceList.postValue(AppRepository.getInstance().getClientInvoices(this.client!!.id))
            }
        if (typeSort == 2)
            AppRepository.getInstance().listOfInvoice.observeForever{
                invoiceList.postValue(AppRepository.getInstance().getClientInvoicesId(client!!.id))
            }
        if (typeSort == 3)
            AppRepository.getInstance().listOfInvoice.observeForever{
                invoiceList.postValue(AppRepository.getInstance().getclientInvoicesSum(client!!.id))
            }
        AppRepository.getInstance().invoice.observeForever{
            _invoice=it
        }
    }
    fun search(s: String){
        AppRepository.getInstance().listOfInvoice.observeForever{
            invoiceList.postValue(AppRepository.getInstance().getSearch(s, client!!.id))
        }
    }
    fun set_Group(client: Client) {
        this.client = client
        AppRepository.getInstance().listOfInvoice.observeForever {
            invoiceList.postValue(AppRepository.getInstance().getClientInvoices(client.id))
        }
        AppRepository.getInstance().invoice.observeForever{
            _invoice=it
        }
    }

    fun deleteStudent() {
        if(student!=null)
            AppRepository.getInstance().deleteInvoice(student!!)
    }
    fun update_info(){
        AppRepository.getInstance().fetchInvoice()
    }

    fun appendStudent(name:String, weight:Int, price:Int, calories:Int, info:String, comp:String, prep:Int){
        val invoice = Invoice()
        invoice.name = name
        invoice.weight = weight
        invoice.price = price
        invoice.calories = calories
        invoice.info = info
        invoice.clientID = client!!.id
        invoice.comp = comp
        invoice.prep = prep
        AppRepository.getInstance().addInvoice(invoice)
    }

    fun updateStudent(name:String, weight:Int, price:Int, calories:Int, info:String, comp:String, prep:Int){
        if (_invoice!=null){
            _invoice!!.name = name
            _invoice!!.weight = weight
            _invoice!!.price = price
            _invoice!!.calories = calories
            _invoice!!.info = info
            _invoice!!.comp = comp
            _invoice!!.prep = prep
            AppRepository.getInstance().updateInvoice(_invoice!!)
        }
    }

    fun setCurrentStudent(invoice: Invoice){
        AppRepository.getInstance().setCurrentInvoice(invoice)
    }

}