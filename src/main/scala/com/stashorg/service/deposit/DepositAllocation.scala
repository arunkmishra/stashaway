package com.stashorg.service.deposit
import com.stashorg.model.{DepositPlan, Money, Portfolio}
import com.stashorg.service.WalletWithPortfolio
import com.stashorg.util.ConsoleLogger

import scala.annotation.tailrec

trait DepositAllocation {

  val plan: DepositPlan

  private val logger = new ConsoleLogger(this.getClass.getCanonicalName)

  def runPlanForDepositPlan(depositAmount: Money,
                            portfolio: Seq[Portfolio]): WalletWithPortfolio

  def addMoneyInDepositPlan(deposit: Money,
                            folios: Seq[Portfolio]): Seq[Portfolio] = {
    logger.info(
      s"adding amount ${plan.getTotalDepositAllocation} to plan : ${plan.getClass.getSimpleName}"
    )
    @tailrec
    def tailDiv(res: Seq[Portfolio],
                distFolio: Seq[Portfolio]): Seq[Portfolio] = distFolio match {
      case h :: tail => tailDiv(h.updateAmountInPortfolios(res), tail)
      case Nil       => res
    }

    tailDiv(folios, plan.portfolios)
  }

}
