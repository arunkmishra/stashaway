package com.stashorg.service
import com.stashorg.model._
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
  val customerRepository: CustomerRepository = CustomerRepository(customer)

  "def findCustomerAndDeposit" should "return deposit failure when no customer found in records" in {
    val customerService = new CustomerService(customerRepository)
    val actualResult =
      customerService.findCustomerAndDeposit(ReferenceNumber(2), Money(5))
    val expectedResult = Left(
      DepositFailure("No customer found with ref no. : ReferenceNumber(2)")
    )
    assert(actualResult == expectedResult)
  }

  "def findCustomerAndDeposit" should "return updated customer repository if customer found in record" in {
    val customerService = new CustomerService(customerRepository)
    val actualResult = customerService
      .findCustomerAndDeposit(ReferenceNumber(1), Money(15, SGP))
      .fold(err => err, c => c.findCustomerByRefNo(ReferenceNumber(1)))
    val expectedResult = Some(
      customer.copy(
        portfolios = List(
          retirementPortfolio.copy(amount = Money(25, SGP)),
          emergencyPortfolio.copy(amount = Money(40, SGP))
        )
      )
    )

    assert(actualResult == expectedResult)
  }
}
