package com.stashorg
import com.stashorg.model.{Portfolio, StashAwaySimpleWallet}

package object service {

  type WalletWithPortfolio = (StashAwaySimpleWallet, Seq[Portfolio])

}
