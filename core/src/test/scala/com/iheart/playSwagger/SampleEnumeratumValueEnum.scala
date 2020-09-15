package com.iheart.playSwagger

import enumeratum.values.{StringEnum, StringEnumEntry}

sealed abstract class SampleEnumeratumValueEnum(val value: String) extends StringEnumEntry

object SampleEnumeratumValueEnum extends StringEnum[SampleEnumeratumValueEnum] {
  val values = findValues

  case object ValueOne extends SampleEnumeratumValueEnum("valueOne")
  case object ValueTwo extends SampleEnumeratumValueEnum("valueTwo")
}
