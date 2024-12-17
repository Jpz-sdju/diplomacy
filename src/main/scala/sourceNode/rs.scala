package sourceNode

import org.chipsalliance.cde.config.Parameters
import chisel3._
import chisel3.util.Valid
import freechips.rocketchip.diplomacy.{LazyModule, LazyModuleImp}
import org.chipsalliance.cde.config.Parameters
import chisel3.experimental.SourceInfo
import freechips.rocketchip.diplomacy._


class IntegerReservationStation (num : Int)(implicit p: Parameters) extends LazyModule {

  private val rsParam = RsParam(num)
  val issueNode = new RsIssueNode(rsParam)


  lazy val module = new IntegerReservationStationImpl(this, rsParam)
}

class IntegerReservationStationImpl(outer:IntegerReservationStation, param:RsParam) extends LazyModuleImp(outer){


  val io = IO(new Bundle{
    val redirect = Output(Valid(UInt(2.W)))
  })

  io.redirect := DontCare

}

object RsIssueNodeImpl extends SimpleNodeImp[RsParam, Seq[UpwardParam], (RsParam, Seq[UpwardParam], Parameters), Vec[IssueBundle]]{
  override def edge(pd: RsParam, pu: Seq[UpwardParam], p: Parameters, sourceInfo: SourceInfo): (RsParam, Seq[UpwardParam], Parameters) = {
    if(pd.width == 123){
      (pd, pu.filter(_.width == 666), p)
    }else{
      (pd, pu.filter(_.width == 777), p)
    }
  }
  override def bundle(e: (RsParam, Seq[UpwardParam], Parameters)): Vec[IssueBundle] = {
    Vec(e._2.length, new IssueBundle())}
  override def render(e: (RsParam, Seq[UpwardParam], Parameters)): RenderedEdge = {
    RenderedEdge("#00ff00" + "Issue")
  }
}


class RsIssueNode(param:RsParam)(implicit valName: ValName) extends SourceNode(RsIssueNodeImpl)(Seq(param))
