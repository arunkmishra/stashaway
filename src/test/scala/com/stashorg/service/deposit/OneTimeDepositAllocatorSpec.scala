package com.stashorg.service.deposit

import com.stashorg.model._
import org.scalatest.flatspec.AnyFlatSpec

class OneTimeDepositAllocatorSpec extends AnyFlatSpec {

  "def runPlanForDepositPlan" should "allocate deposit according to plan mentioned" in {
    val oneTimePlan = OneTimePlan(List(RetirementPortfolio(Money(10))))
    val oneTimeDepositAllocator = OneTimeDepositAllocator(oneTimePlan)
    val (actualWalletBalance, actualPortfolio) =
      oneTimeDepositAllocator.runPlanForDepositPlan(
        Money(10),
        List(RetirementPortfolio(Money(22)), HighRiskPortfolio(Money.zero()))
      )
    val (expectedWalletBalance, expectedPortfolio) = (
      StashAwaySimpleWallet(Money.zero()),
      List(RetirementPortfolio(Money(32)), HighRiskPortfolio(Money.zero()))
    )
    assert(actualWalletBalance == expectedWalletBalance)
    assert(actualPortfolio == expectedPortfolio)
  }

  it should "allocate balance according to plan and money left from plan updated in Wallet" in {
    val oneTimePlan = OneTimePlan(List(RetirementPortfolio(Money(10))))
    val oneTimeDepositAllocator = OneTimeDepositAllocator(oneTimePlan)
    val (actualWalletBalance, actualPortfolio) =
      oneTimeDepositAllocator.runPlanForDepositPlan(
        Money(20),
        List(RetirementPortfolio(Money(22)), HighRiskPortfolio(Money.zero()))
      )
    val (expectedWalletBalance, expectedPortfolio) = (
      StashAwaySimpleWallet(Money(10)),
      List(RetirementPortfolio(Money(32)), HighRiskPortfolio(Money.zero()))
    )
    assert(actualWalletBalance == expectedWalletBalance)
    assert(actualPortfolio == expectedPortfolio)
  }

}
