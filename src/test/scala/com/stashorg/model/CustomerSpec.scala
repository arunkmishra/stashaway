package com.stashorg.model

import org.scalatest.flatspec.AnyFlatSpec

class CustomerSpec extends AnyFlatSpec {

  val customer = Customer(
    ReferenceNumber(1),
    StashAwaySimpleWallet(Money(50)),
    Some(OneTimePlan(List(RetirementPortfolio(Money(10))))),
    None,
    List(
      RetirementPortfolio(Money(10)), EmergencyPortfolio(Money(5))
    )
  )

  "def toString" should "return in string in required format" in {

    val expectedString =
      """
        |Customer ref no. -> ReferenceNumber(1)
        |Wallet balance -> 50 SGP
        |Portfolios with Balance -> List(Retirement [10 SGP], Emergency [5 SGP])
        |Deposit Plan -> OneTimePlan(List(Retirement [10 SGP]))
        |Total balance: 15 SGP
        |""".stripMargin

    assert(customer.toString == expectedString)
  }

  it should "contain to choose plan if no deposit plan is there" in {
    val actualString = customer.copy(oneTimePlan = None).toString

    val expectedString = "No deposit plan. Please select at least one plan"
    assert(actualString.contains(expectedString))
  }

}