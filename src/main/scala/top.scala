

import chisel3._
import freechips.rocketchip.diplomacy._
import freechips.rocketchip.tilelink._
import scala.reflect.runtime.universe._
import freechips.rocketchip.diplomacy.AdapterNode
import sourceNode._
import org.chipsalliance.cde.config.Parameters
class top()() extends Module {
  val io = IO(new Bundle {})
  
  val l_soc = LazyModule(new ExecuteBlock()(Parameters.empty))
  val soc = Module(l_soc.module)
  dontTouch(soc.io)
  println(l_soc.integerReservationStation.issueNode.out)
  println(l_soc.regFile.issueNode.in)
}

