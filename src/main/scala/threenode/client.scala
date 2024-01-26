package threenode

import freechips.rocketchip.tilelink.TLMasterPortParameters
import chipsalliance.rocketchip.config._
import freechips.rocketchip.tilelink.TLMasterParameters
import freechips.rocketchip.diplomacy._
import freechips.rocketchip.tilelink.TLClientNode
import chisel3._
import chisel3.util._


class myClient()(implicit p: Parameters) extends LazyModule {

  val clientParameters = TLMasterPortParameters.v1(
    clients = Seq(
      TLMasterParameters.v1(
        "myClient",
        sourceId = IdRange(0, 1 << 1)
      )
    )
  )
  val clientNode = TLClientNode(Seq(clientParameters))

  lazy val module = new myClientImp(this)
}

class myClientImp(wo: myClient) extends LazyModuleImp(wo) {
  val io = IO {
    new Bundle {
      val ss = Output(UInt(64.W))

    }
  }
  val cnt = RegInit(0.U(8.W))
  val (bus, edge) = wo.clientNode.out.head

  val mem_acquire = bus.a
  val mem_grant = bus.d

  when(true.B){
    cnt := cnt +1.U
  }
  val load = edge.Get(
    fromSource      = 3.U,
    toAddress       = 3.U,
    lgSize          = 3.U
  )._2

  val store = edge.Put(
    fromSource      = 3.U,
    toAddress       = 3.U,
    lgSize          = 3.U,
    data            = 3.U,
    mask            = 3.U
  )._2

  mem_acquire.bits := load
  mem_acquire.valid := Mux(cnt(1),true.B,false.B)


  mem_grant.ready := Mux(cnt(1),true.B,false.B)
  io.ss := mem_grant.bits.data
}