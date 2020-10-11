package com.stashorg.service.repository
import com.stashorg.model.{Customer, ReferenceNumber}

object InMemoryRepositoryForCustomer {

  def apply(xs: Customer*): InMemoryRepository[ReferenceNumber, Customer] = {
    val repo = InMemoryRepositoryForCustomer.empty
    xs.foreach(customer => repo.add(customer.referenceNumber, customer))
    repo
  }

  def empty: InMemoryRepository[ReferenceNumber, Customer] = new InMemoryRepository[ReferenceNumber, Customer]()

}
