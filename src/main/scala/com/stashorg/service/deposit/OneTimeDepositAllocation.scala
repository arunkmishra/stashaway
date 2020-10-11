package com.stashorg.service.deposit
import com.stashorg.model._
import com.stashorg.service.WalletWithPortfolio

case class OneTimeDepositAllocation(plan: OneTimePlan)
    extends DepositAllocation {

  def runPlanForDepositPlan(actualDeposit: Money,
                            portfolios: Seq[Portfolio]): WalletWithPortfolio =
    if (actualDeposit >= plan.getTotalDepositAllocation)
      (
        StashAwaySimpleWallet(actualDeposit - plan.getTotalDepositAllocation),
        addMoneyInDepositPlan(actualDeposit, portfolios)
      )
    else
      (StashAwaySimpleWallet(actualDeposit), portfolios)

}
