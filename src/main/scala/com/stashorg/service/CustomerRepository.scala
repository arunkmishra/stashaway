package com.stashorg.service
import com.stashorg.model.{Customer, ReferenceNumber}

class CustomerRepository(customers: List[Customer]) {

  def isEmpty: Boolean = customers.isEmpty

  def addCustomer(customer: Customer): CustomerRepository = new CustomerRepository(customer :: customers)

  def updateCustomer(referenceNumber: ReferenceNumber, updatedCustomer: Customer): CustomerRepository =
    new CustomerRepository(customers.map{
      case Customer(refNo, _, _, _, _) if refNo == referenceNumber => updatedCustomer
      case customer => customer
    })

  def findCustomerByRefNo(refNo: ReferenceNumber): Option[Customer] =
    customers.find(_.referenceNumber == refNo)

  def showAllCustomersInRepository(): Unit = customers.foreach(println)

}

object CustomerRepository {

  def empty: CustomerRepository = new CustomerRepository(Nil)

  def apply(xs: Customer*): CustomerRepository =
    xs.foldLeft(CustomerRepository.empty)((r,a) => r.addCustomer(a))

}