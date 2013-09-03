/*// Decompiled by DJ v2.9.9.60 Copyright 2000 Atanas Neshkov  Date: 2012-8-30 14:30:57
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: fullnames ansi 
// Source File Name:   N_20_SAVE.java

package nc.bs.pub.action;


public class N_20_SAVE extends nc.bs.pub.compiler.AbstractCompiler2
{

    public N_20_SAVE()
    {
    }

    public java.lang.String getCodeRemark()
    {
        return "\tObject retObj=null;\n\tnc.vo.pu.m20.entity.PraybillVO[] inObject = (nc.vo.pu.m20.entity.PraybillVO[]) getVos();\n          Object retValue =nc.bs.framework.common.NCLocator.getInstance().lookup(nc.itf.pu.m20.IPraybillApprove.class).sendapprove(inObject,this);\n          return retValue;\n";
    }

    public java.lang.Object runComClass(nc.vo.pub.compiler.PfParameterVO vo)
        throws nc.vo.pub.BusinessException
    {
        java.lang.Object retValue;
        super.m_tmpVo = vo;
        nc.vo.pu.m20.entity.PraybillVO inObject[] = (nc.vo.pu.m20.entity.PraybillVO[])(nc.vo.pu.m20.entity.PraybillVO[])getVos();
        retValue = null;//((nc.itf.pu.m20.IPraybillApprove)nc.bs.framework.common.NCLocator.getInstance().lookup(IPraybillApprove.class)).sendapprove(inObject, this);
        //return retValue;
        return inObject;
    }
}*/