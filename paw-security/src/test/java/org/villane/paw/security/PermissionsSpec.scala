package org.villane.paw.security

import org.scalatest._
import org.scalatest.matchers.ShouldMatchers
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class PermissionsSpec extends FunSuite with ShouldMatchers with ImplyMatchers {

  object Simple1 extends SimplePermission
  object Simple2 extends SimplePermission
  object Simple3 extends SimplePermission

  test("AllPermissions") {
    val it = AllPermissions
    it should imply()
    it should imply(NoPermissions)
    it should imply(NoPermissions, NoPermissions)
    it should imply(AllPermissions)
    it should imply(Simple1)
    it should imply(Simple2)
    it should imply(Simple1, Simple2)
  }

  test("NoPermissions") {
    val it = NoPermissions
    it should imply()
    it should imply(NoPermissions)
    it should imply(NoPermissions, NoPermissions)
    it should not(imply(AllPermissions))
    it should not(imply(Simple1))
    it should not(imply(Simple2))
  }

  test("Custom Permission Simple1") {
    val it = Simple1
    it should not(imply(NoPermissions))
    it should not(imply(AllPermissions))
    it should imply(Simple1)
    it should not(imply(Simple2))
  }

  test("PermissionSet of Simple1, Simple2") {
    val it = new PermissionSet(Set(Simple1, Simple2))
    it should imply()
    it should imply(NoPermissions)
    it should imply(NoPermissions, NoPermissions)
    it should not(imply(AllPermissions))
    it should imply(Simple1)
    it should imply(Simple2)
    it should imply(Simple1, Simple2, NoPermissions)
    it should not(imply(Simple3))
    it should not(imply(Simple1, Simple2, Simple3, NoPermissions))
    it should not(imply(Simple3, NoPermissions))
  }

}
