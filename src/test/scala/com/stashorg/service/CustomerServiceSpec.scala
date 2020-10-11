package com.stashorg.service
import com.stashorg.model._
import com.stashorg.service.repository.{
  InMemoryRepository,
  InMemoryRepositoryForCustomer
}
import org.scalatest.flatspec.AnyFlatSpec

class CustomerServiceSpec extends AnyFlatSpec {

  val retirementPortfolio = RetirementPortfolio(Money(5, SGP))
  val emergencyPortfolio = EmergencyPortfolio(Money(10, SGP))

  val customer = Customer(
    ReferenceNumber(1),
    StashAwaySimpleWallet(Money(0)),
    Some(OneTimePlan(List(retirementPortfolio, emergencyPortfolio))),
    None,
    List(
      retirementPortfolio.copy(amount = Money(20, SGP)),
      emergencyPortfolio.copy(amount = Money(30, SGP))
    )
  )
  val customerRepository: InMemoryRepository[ReferenceNumber, Customer] =
    InMemoryRepositoryForCustomer(customer)
  val customerService = new CustomerService(customerRepository)

  "def findCustomerAndDeposit" should "return deposit failure when no customer found in records" in {
    val actualResult =
      customerService.findCustomerAndDeposit(ReferenceNumber(2), Money(5))
    val expectedResult =
      DepositFailure("No customer found with ref no. : ReferenceNumber(2)")

    assert(actualResult == expectedResult)
  }

  "def findCustomerAndDeposit" should "return updated customer repository if customer found in record" in {
    val customerService = new CustomerService(customerRepository)
    val actualResult = customerService
      .findCustomerAndDeposit(ReferenceNumber(1), Money(15, SGP))

    val expectedResult = DepositSuccess()

    val updatedCustomer =
      customerRepository.findByKey(ReferenceNumber(1))

    val expectedCustomer = Some(
      customer.copy(
        portfolios = List(
          retirementPortfolio.copy(amount = Money(25, SGP)),
          emergencyPortfolio.copy(amount = Money(40, SGP))
        )
      )
    )

    assert(actualResult == expectedResult)
    assert(updatedCustomer == expectedCustomer)
  }
}
