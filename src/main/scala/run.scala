import chipsalliance.rocketchip.config._
import chisel3._
import chisel3.stage.ChiselStage

object run extends App{
    println("safasfd")
    (new ChiselStage).emitVerilog(new top()(Parameters.empty), args)
}