package com.stashorg.service.deposit
import com.stashorg.model.{Money, MonthlyPlan, Portfolio, StashAwaySimpleWallet}
import com.stashorg.service.WalletWithPortfolio

import scala.annotation.tailrec

case class MonthlyPlanDepositAllocator(plan: MonthlyPlan)
    extends DepositAllocator {

  override def runPlanForDepositPlan(
    depositAmount: Money,
    portfolios: Seq[Portfolio]
  ): WalletWithPortfolio = {

    @tailrec
    def tailrec(remAmt: Money,
                resultFolio: Seq[Portfolio]): WalletWithPortfolio =
      if (remAmt >= plan.getTotalDepositAllocation && plan.getTotalDepositAllocation != Money
            .zero())
        tailrec(
          remAmt - plan.getTotalDepositAllocation,
          addMoneyInDepositPlan(resultFolio)
        )
      else
        (StashAwaySimpleWallet(remAmt), resultFolio)

    tailrec(depositAmount, portfolios)
  }

}
