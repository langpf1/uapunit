/*// Decompiled by DJ v2.9.9.60 Copyright 2000 Atanas Neshkov  Date: 2012-8-26 18:09:18
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: fullnames ansi 
// Source File Name:   N_21_SAVE.java

package nc.bs.pub.action;

import nc.itf.pu.m21.IOrderApprove;

public class N_21_SAVE extends nc.bs.pub.compiler.AbstractCompiler2
{

    public N_21_SAVE()
    {
    }

    public java.lang.String getCodeRemark()
    {
        return "\tObject retValue=null;\nnc.vo.pu.m21.entity.OrderVO[] vos=(nc.vo.pu.m21.entity.OrderVO[]) getVos();\nretValue=nc.bs.framework.common.NCLocator.getInstance().lookup(nc.itf.pu.m21.IOrderApprove.class).sendapprove(vos,this);\nreturn retValue;\n";
    }

    public java.lang.Object runComClass(nc.vo.pub.compiler.PfParameterVO vo)
        throws nc.vo.pub.BusinessException
    {
    	
        java.lang.Object retValue;
        super.m_tmpVo = vo;
        retValue = null;
        nc.vo.pu.m21.entity.OrderVO vos[] = (nc.vo.pu.m21.entity.OrderVO[])(nc.vo.pu.m21.entity.OrderVO[])getVos();
        IOrderApprove approve = ((nc.itf.pu.m21.IOrderApprove)nc.bs.framework.common.NCLocator.getInstance().lookup(IOrderApprove.class));
        retValue = null;//approve.sendapprove(vos, this);
        return vos;
    }
}*/