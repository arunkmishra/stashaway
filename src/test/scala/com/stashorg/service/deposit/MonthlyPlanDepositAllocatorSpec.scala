package com.stashorg.service.deposit
import com.stashorg.model._
import org.scalatest.flatspec.AnyFlatSpec

class MonthlyPlanDepositAllocatorSpec extends AnyFlatSpec {

  "def runPlanForDepositPlan" should "allocate deposit according to plan mentioned" in {
    val monthlyPlan = MonthlyPlan(List(RetirementPortfolio(Money(10))))
    val monthlyDepositAllocator = MonthlyPlanDepositAllocator(monthlyPlan)
    val (actualWalletBalance, actualPortfolio) =
      monthlyDepositAllocator.runPlanForDepositPlan(
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

  it should "allocate balance according to plan and reallocate if money left till it is zero" in {
    val monthlyPlan = MonthlyPlan(List(RetirementPortfolio(Money(10))))
    val monthlyDepositAllocator = MonthlyPlanDepositAllocator(monthlyPlan)
    val (actualWalletBalance, actualPortfolio) =
      monthlyDepositAllocator.runPlanForDepositPlan(
        Money(20),
        List(RetirementPortfolio(Money(22)), HighRiskPortfolio(Money.zero()))
      )
    val (expectedWalletBalance, expectedPortfolio) = (
      StashAwaySimpleWallet(Money.zero()),
      List(RetirementPortfolio(Money(42)), HighRiskPortfolio(Money.zero()))
    )
    assert(actualWalletBalance == expectedWalletBalance)
    assert(actualPortfolio == expectedPortfolio)
  }

  it should "allocate balance according to plan and money left from plan updated in Wallet" in {
    val monthlyPlan = MonthlyPlan(List(RetirementPortfolio(Money(10))))
    val monthlyDepositAllocator = MonthlyPlanDepositAllocator(monthlyPlan)
    val (actualWalletBalance, actualPortfolio) =
      monthlyDepositAllocator.runPlanForDepositPlan(
        Money(25),
        List(RetirementPortfolio(Money(22)), HighRiskPortfolio(Money.zero()))
      )
    val (expectedWalletBalance, expectedPortfolio) = (
      StashAwaySimpleWallet(Money(5)),
      List(RetirementPortfolio(Money(42)), HighRiskPortfolio(Money.zero()))
    )
    assert(actualWalletBalance == expectedWalletBalance)
    assert(actualPortfolio == expectedPortfolio)
  }
}
