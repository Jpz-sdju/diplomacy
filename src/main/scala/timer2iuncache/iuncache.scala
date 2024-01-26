package timer2iuncache

import chisel3._
import chisel3.util._
import freechips.rocketchip.diplomacy.{IdRange, LazyModule, LazyModuleImp}
import chipsalliance.rocketchip.config._
import freechips.rocketchip.tilelink.TLMasterParameters
import freechips.rocketchip.tilelink.TLMasterPortParameters
import freechips.rocketchip.tilelink._


class IUnCache()(implicit p: Parameters) extends LazyModule {

  val clientParameters = TLMasterPortParameters.v1(
    clients = Seq(
      TLMasterParameters.v1(
        "Iuncache",
        sourceId = IdRange(0, 1 << 1)
      )
    )
  )
  val clientNode = TLClientNode(Seq(clientParameters))

  lazy val module = new IUncacheImp(this)
}

class IUncacheImp(wo: IUnCache) extends LazyModuleImp(wo) {
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