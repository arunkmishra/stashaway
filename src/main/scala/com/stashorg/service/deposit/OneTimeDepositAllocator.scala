package com.stashorg.service.deposit

import com.stashorg.model._
import com.stashorg.service.WalletWithPortfolio

case class OneTimeDepositAllocator(plan: OneTimePlan) extends DepositAllocator {

  def runPlanForDepositPlan(actualDeposit: Money,
                            portfolios: Seq[Portfolio]): WalletWithPortfolio =
    if (actualDeposit >= plan.getTotalDepositAllocation)
      (
        StashAwaySimpleWallet(actualDeposit - plan.getTotalDepositAllocation),
        addMoneyInDepositPlan(portfolios)
      )
    else
      (StashAwaySimpleWallet(actualDeposit), portfolios)

}
