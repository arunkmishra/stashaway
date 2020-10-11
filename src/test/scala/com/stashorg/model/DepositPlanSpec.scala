package com.stashorg.model

import org.scalatest.flatspec.AnyFlatSpec

class DepositPlanSpec extends AnyFlatSpec {

  "def getTotalDepositAllocation" should "return sum of all amount specified in given portfolios" in {
    val depositPlan = OneTimePlan(
      List(RetirementPortfolio(Money(10)), EmergencyPortfolio(Money(100)))
    )
    val actualAmount = depositPlan.getTotalDepositAllocation
    val expectedAmount = Money(110)
    assert(actualAmount == expectedAmount)
  }

}
