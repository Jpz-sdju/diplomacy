package sourceNode

import org.chipsalliance.cde.config.Parameters
import chisel3._
import chisel3.util.Valid
import freechips.rocketchip.diplomacy.{LazyModule, LazyModuleImp}
import org.chipsalliance.cde.config.Parameters
import chisel3.experimental.SourceInfo
import freechips.rocketchip.diplomacy._


class ExecuteBlock(implicit p:Parameters) extends LazyModule  {
  
  val integerReservationStation: IntegerReservationStation = LazyModule(new IntegerReservationStation)
  val integerReservationStation1: IntegerReservationStation = LazyModule(new IntegerReservationStation)

  val integerBlock: IntegerBlock = LazyModule(new IntegerBlock)
  // val integerBlock1: IntegerBlock = LazyModule(new IntegerBlock)

  val exuBlocks = Seq(integerBlock )

  val regFile = LazyModule(new RegFileTop(2))


  regFile.issueNode :*= integerReservationStation.issueNode
  regFile.issueNode :*= integerReservationStation1.issueNode



  for (eb <- exuBlocks) {
    eb.issueNode :*= regFile.issueNode
    // println("sep")
  }

  lazy val module = new ExecuteBlockImp(this)
}

class ExecuteBlockImp(outer:ExecuteBlock) extends LazyModuleImp(outer){
  val io = IO(new Bundle {
    val s = Output(UInt(1.W))
  })

  io.s := 0.U

  val a = outer.integerBlock.module
  val b = outer.exuBlocks.head.module
  val c = outer.integerReservationStation.module



  dontTouch(a.io)
}