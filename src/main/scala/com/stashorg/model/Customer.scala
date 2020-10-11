package com.stashorg.model

final case class ReferenceNumber(number: Int)
final case class StashAwaySimpleWallet(amount: Money)

case class Customer(referenceNumber: ReferenceNumber,
                    wallet: StashAwaySimpleWallet,
                    oneTimePlan: Option[OneTimePlan],
                    monthlyPlan: Option[MonthlyPlan],
                    portfolios: Seq[Portfolio]) {
  override def toString: String =
    s"""
      |Customer ref no. -> $referenceNumber
      |Wallet balance -> ${wallet.amount}
      |Portfolios with Balance -> ${portfolios.map(_.toString)}
      |Deposit Plan -> ${if (oneTimePlan.isDefined && monthlyPlan.isDefined)
         oneTimePlan.get.toString + ", " + monthlyPlan.get.toString
       else if (monthlyPlan.isDefined) monthlyPlan.get.toString
       else if (oneTimePlan.isDefined) oneTimePlan.get.toString
       else "No deposit plan. Please select at least one plan"}
      |Total balance: ${portfolios.foldLeft(Money.zero()) {
         case (acc, p) => p.amount + acc
       }}
      |""".stripMargin
}
