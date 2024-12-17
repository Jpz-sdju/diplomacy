package sourceNode

import org.chipsalliance.cde.config.Parameters
import chisel3._
import chisel3.util.Valid
import freechips.rocketchip.diplomacy.{LazyModule, LazyModuleImp}
import org.chipsalliance.cde.config.Parameters
import chisel3.experimental.SourceInfo
import freechips.rocketchip.diplomacy._


class ExecuteBlock(implicit p:Parameters) extends LazyModule  {
  
  val integerReservationStation: IntegerReservationStation = LazyModule(new IntegerReservationStation(123))
  val integerReservationStation1: IntegerReservationStation = LazyModule(new IntegerReservationStation(456))

  val regFile = LazyModule(new RegFileTop(2))


  val integerBlock: IntegerBlock = LazyModule(new IntegerBlock)
  // val integerBlock1: IntegerBlock = LazyModule(new IntegerBlock)

  val exuBlocks = Seq(integerBlock )



  regFile.issueNode :*= integerReservationStation.issueNode
  regFile.issueNode :*= integerReservationStation1.issueNode



  // for (eb <- exuBlocks) {
    exuBlocks.head.issueNode :*= regFile.issueNode
  // }

  lazy val module = new ExecuteBlockImp(this)


}

class ExecuteBlockImp(outer:ExecuteBlock) extends LazyModuleImp(outer){
  val io = IO(new Bundle {
    val s = Output(UInt(1.W))
  })

  io.s := 0.U

  val ib1 = outer.integerBlock.module
  // val ib2 = outer.integerBlock1.module
  val exu = outer.exuBlocks.head.module
  val rs1 = outer.integerReservationStation.module
  val rs2 = outer.integerReservationStation1.module



  dontTouch(rs1.io)
  dontTouch(rs2.io)
}