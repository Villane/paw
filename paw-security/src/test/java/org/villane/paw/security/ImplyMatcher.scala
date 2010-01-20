package org.villane.paw.security

import org.scalatest.matchers.Matchers
import org.scalatest.matchers.Matcher
import org.scalatest.matchers.MatchResult

trait ImplyMatchers {

  class ImplyOneMatcher(right: Permission) extends Matcher[Permission] {
    def apply(left: Permission) = MatchResult(
      left implies right,
      left + " did not imply " + right,
      left + " implied " + right
    )
  }

  class ImplyMultipleMatcher(right: Seq[Permission]) extends Matcher[Permissions] {
    def apply(left: Permissions) = MatchResult(
      left implies right,
      left + " did not imply " + right,
      left + " implied " + right
    )
  }

  def imply(right: Permission) = new ImplyOneMatcher(right)
  def imply(right: Permission*) = new ImplyMultipleMatcher(right)

  /*implicit def resNot(r: ResultOfNotWordForAnyRef[Permission]) = new {
    def imply(right: Permission) = {
      val matcherResult = self.imply(right)(r.left)
      if (matcherResult.matches != r.shouldBeTrue) {
        throw newTestFailedException(
          if (shouldBeTrue) matcherResult.failureMessage else matcherResult.negatedFailureMessage
        )
      }
    }
    def imply(right: Permission*) = new ImplyMultipleMatcher(right)
  }*/
}