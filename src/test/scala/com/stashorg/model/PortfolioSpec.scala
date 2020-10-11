package com.stashorg.model
import org.scalatest.flatspec.AnyFlatSpec

class PortfolioSpec extends AnyFlatSpec {

  "def updateAmountInPortfolios" should "find itself from list of portfolios and double the amount for itself" in {
    val retirementPortfolio = RetirementPortfolio(Money(10))
    val actualResult = retirementPortfolio
      .updateAmountInPortfolios(List(retirementPortfolio))
    val expectedResult = List(RetirementPortfolio(Money(20)))
    assert(actualResult == expectedResult)
  }

  "def sum" should "add the given amount in portfolio" in {
    val retirementPortfolio = RetirementPortfolio(Money(10))
    val actualResult = retirementPortfolio.sum(Money(11))
    val expectedResult = Money(21)
    assert(actualResult == expectedResult)
  }
}
