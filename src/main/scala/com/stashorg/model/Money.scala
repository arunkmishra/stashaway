package com.stashorg.model

sealed trait Currency
case object SGP extends Currency
case object USD extends Currency

case class Money(amount: BigDecimal, currency: Currency = SGP) {

  def +(amount2: Money) = Money(amount + amount2.amount, currency)

  def -(amount2: Money) = Money(amount - amount2.amount, currency)

  override def toString: String = s"$amount $currency"

}
//TODO: add a method to check currency before operating on amount
object Money {

  def zero(currency: Currency = SGP) = Money(0, currency)

  implicit def moneyAsNumber(amt: Money): BigDecimal = amt.amount

  implicit val ordering: Ordering[Money] = new Ordering[Money] {
    override def compare(x: Money, y: Money): Int =
      if (x.amount > y.amount)
        1
      else if (x.amount < y.amount)
        -1
      else
        0
  }
}
