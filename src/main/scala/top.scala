import timer2iuncache.{IUnCache, TLTimer}

import chisel3._
import freechips.rocketchip.diplomacy._
import freechips.rocketchip.tilelink._
import chipsalliance.rocketchip.config._


class top()(implicit p : Parameters) extends Module{
    val io = IO(new Bundle {})
    val l_soc = LazyModule(new sfs())
    val soc = Module(l_soc.module)


}

class sfs()(implicit p: Parameters) extends LazyModule {
    val iun = LazyModule(new IUnCache())
    val timer = LazyModule(new TLTimer())
//    val xbar = TLXbar()
//    xbar := TLTempNode() := iun.clientNode
    timer.node := iun.clientNode

    lazy val module = new sfsImpl(this)

}

class sfsImpl(outer: sfs) extends LazyModuleImp(outer) {

    val iun = outer.iun.module
    val timer = outer.timer.module

}
