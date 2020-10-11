package com.stashorg.service.deposit
import com.stashorg.model.{Money, MonthlyPlan, Portfolio, StashAwaySimpleWallet}
import com.stashorg.service.WalletWithPortfolio

import scala.annotation.tailrec

case class MonthlyPlanDepositAllocation(plan: MonthlyPlan)
    extends DepositAllocation {

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
          addMoneyInDepositPlan(remAmt, resultFolio)
        )
      else
        (StashAwaySimpleWallet(remAmt), resultFolio)

    tailrec(depositAmount, portfolios)
  }

}
