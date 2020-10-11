package com.stashorg.service
import com.stashorg.model._
import org.scalatest.flatspec.AnyFlatSpec

class DepositServiceSpec extends AnyFlatSpec {

  val planAmountAllocation =
    List(RetirementPortfolio(Money(5)), EmergencyPortfolio(Money(10)))

  val customer = Customer(
    ReferenceNumber(1),
    StashAwaySimpleWallet(Money(0)),
    Some(OneTimePlan(planAmountAllocation)),
    None,
    List(
      RetirementPortfolio(Money(20, SGP)),
      EmergencyPortfolio(Money(30, SGP))
    )
  )
  val depositService = new DepositService()

  "def depositInCustomerAccount" should "return updated customer with one time deposit amount allocated according to portfolio" in {
    val actualResult =
      depositService.depositInCustomerAccount(customer, Money(15))
    val expectedResult = Right(
      customer.copy(
        portfolios = List(
          RetirementPortfolio(Money(25, SGP)),
          EmergencyPortfolio(Money(40, SGP))
        )
      )
    )
    assert(actualResult == expectedResult)
  }

  it should "return updated customer with monthly plan deposit amount allocated according to portfolio" in {
    val newCustomer = customer.copy(
      oneTimePlan = None,
      monthlyPlan = Some(MonthlyPlan(planAmountAllocation))
    )
    val actualResult =
      depositService.depositInCustomerAccount(newCustomer, Money(15))
    val expectedResult = Right(
      newCustomer.copy(
        portfolios = List(
          RetirementPortfolio(Money(25, SGP)),
          EmergencyPortfolio(Money(40, SGP))
        )
      )
    )
    assert(actualResult == expectedResult)
  }

  it should "return updated customer with one time and monthly deposit amount allocated to portfolios" in {
    val newCustomer =
      customer.copy(monthlyPlan = Some(MonthlyPlan(planAmountAllocation)))
    val actualResult =
      depositService.depositInCustomerAccount(newCustomer, Money(45))
    val expectedResult = Right(
      newCustomer.copy(
        portfolios = List(
          RetirementPortfolio(Money(35, SGP)),
          EmergencyPortfolio(Money(60, SGP))
        )
      )
    )
    assert(actualResult == expectedResult)
  }

  it should "return updated customer and deposit amount portfolio and remaining amount to wallet if multiple amount is not deposited to portfolios" in {
    val actualResult =
      depositService.depositInCustomerAccount(customer, Money(20))
    val expectedResult = Right(
      customer.copy(
        wallet = StashAwaySimpleWallet(Money(5)),
        portfolios = List(
          RetirementPortfolio(Money(25, SGP)),
          EmergencyPortfolio(Money(40, SGP))
        )
      )
    )
    assert(actualResult == expectedResult)
  }

  it should "return deposit failure in case no deposit plan is mentioned" in {
    val newCustomer = customer.copy(oneTimePlan = None)
    val actualResult =
      depositService.depositInCustomerAccount(newCustomer, Money(45))
    val expectedResult =
      Left(DepositFailure("No plan declared. Please declare at least one plan"))
    assert(actualResult == expectedResult)
  }
}
