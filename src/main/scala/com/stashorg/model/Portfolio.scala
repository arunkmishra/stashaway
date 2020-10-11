package com.stashorg.model

trait Portfolio {
  def amount: Money
  val name: String = this.getClass.getSimpleName

  override def toString: String = name.split("Portfolio").mkString(" ") + s" [$amount]"

  def addMoney(amount: Money): Portfolio

  def sum(that: Money): Money = amount + that

  def updateAmountInPortfolios(portfolios: Seq[Portfolio]): Seq[Portfolio] =
    portfolios.map {
      case folio if folio.name == this.name =>
        folio.addMoney(this.amount)
      case folio => folio
    }

}


case class RetirementPortfolio(amount: Money) extends Portfolio {

  override def addMoney(amount: Money): RetirementPortfolio =
    this.copy(amount = this.amount + amount)
}

case class EmergencyPortfolio(amount: Money) extends Portfolio {

  override def addMoney(amount: Money): EmergencyPortfolio =
    this.copy(amount = this.amount + amount)
}

case class HighRiskPortfolio(amount: Money) extends Portfolio {

  override def addMoney(amount: Money): HighRiskPortfolio =
    this.copy(amount = this.amount + amount)
}

