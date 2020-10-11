package com.stashorg.model
import org.scalatest.flatspec.AnyFlatSpec

class MoneySpec extends AnyFlatSpec {

  val tenSGP = Money(10, SGP)

  "def toString" should "return in expected format" in {
    val expectedString = "10 SGP"
    assert(tenSGP.toString == expectedString)
  }

  "def +" should "return sum of two amount" in {
    val fiveSGP = Money(5, SGP)
    val actualAmount = tenSGP + fiveSGP
    val expectedAmount = Money(15, SGP)
    assert(actualAmount == expectedAmount)
  }

  "def +" should "return subtraction of two amount" in {
    val fiveSGP = Money(5, SGP)
    val actualAmount = tenSGP - fiveSGP
    val expectedAmount = Money(5, SGP)
    assert(actualAmount == expectedAmount)
  }

}
