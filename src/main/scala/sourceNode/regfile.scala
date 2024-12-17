package sourceNode

import org.chipsalliance.cde.config.Parameters
import chisel3._
import chisel3.util.Valid
import freechips.rocketchip.diplomacy.{LazyModule, LazyModuleImp}
import org.chipsalliance.cde.config.Parameters
import chisel3.experimental.SourceInfo
import freechips.rocketchip.diplomacy._
object RegFileNodeInwardImpl extends InwardNodeImp[RsParam,Seq[UpwardParam],(RsParam, Seq[UpwardParam], Parameters),Vec[IssueBundle]]{

  override def edgeI(pd: RsParam, pu: Seq[UpwardParam], p: Parameters, sourceInfo: SourceInfo): (RsParam, Seq[UpwardParam], Parameters) = {
    if(pd.width == 123){
      (pd, pu.filter(_.width == 666), p)
    }else{
      (pd, pu.filter(_.width == 777), p)
    }
  }
  override def bundleI(ei: (RsParam, Seq[UpwardParam], Parameters)): Vec[IssueBundle] = {

    Vec(ei._2.length, new IssueBundle())}
  override def render(e: (RsParam, Seq[UpwardParam], Parameters)): RenderedEdge = RenderedEdge("#0000ff", "Issue")
}
object RegFileNodeOutwardImpl extends OutwardNodeImp[Seq[RsParam], UpwardParam, (RsParam, UpwardParam, Parameters), IssueBundle]{
  override def edgeO(pd: Seq[RsParam], pu: UpwardParam, p: Parameters, sourceInfo: SourceInfo): (RsParam, UpwardParam, Parameters) = {
    if(pu.width == 666){
      (pd.filter(_.width == 123).head, pu, p)
    }else {
      (pd.filter(_.width == 456).head, pu, p)
    }
  }
  override def bundleO(eo: (RsParam, UpwardParam, Parameters)): IssueBundle = new IssueBundle()
}


class RegFileNode(implicit valName: ValName) extends MixedNexusNode(
  inner = RegFileNodeInwardImpl, outer = RegFileNodeOutwardImpl
)(
  { pd => pd }, { pu => pu }
)

class RegFileTop(extraScalarRfReadPort: Int)(implicit p:Parameters) extends LazyModule{
  val issueNode = new RegFileNode

  lazy val module = new Impl
  class Impl extends LazyModuleImp(this) {
    val redirectIn = IO(Output(Valid(UInt(2.W))))
    redirectIn := DontCare
  }

}
