package com.stashorg.service.deposit
import com.stashorg.model._
import org.scalatest.flatspec.AnyFlatSpec

class DepositAllocatorSpec extends AnyFlatSpec {

  val dummyAllocator: DepositAllocator = new DepositAllocator {
    override val plan: DepositPlan = OneTimePlan(
      List(RetirementPortfolio(Money(10)))
    )
    override def runPlanForDepositPlan(
      depositAmount: Money,
      portfolio: Seq[Portfolio]
    ): (StashAwaySimpleWallet, Seq[Portfolio]) = ???
  }

  "def addMoneyInDepositPlan" should "add money as given in plan to given portfolio" in {
    val actualResult = dummyAllocator.addMoneyInDepositPlan(
      List(RetirementPortfolio(Money(5)), HighRiskPortfolio(Money(11)))
    )
    val expectedResult =
      List(RetirementPortfolio(Money(15)), HighRiskPortfolio(Money(11)))
    assert(actualResult == expectedResult)
  }

}
