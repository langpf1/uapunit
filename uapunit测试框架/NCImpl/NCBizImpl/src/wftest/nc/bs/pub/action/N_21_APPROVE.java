/*// Decompiled by DJ v2.9.9.60 Copyright 2000 Atanas Neshkov  Date: 2012-9-2 19:07:19
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: fullnames ansi 
// Source File Name:   N_21_APPROVE.java

package nc.bs.pub.action;

import nc.itf.pu.m21.IOrderApprove;

public class N_21_APPROVE extends nc.bs.pub.compiler.AbstractCompiler2
{

    public N_21_APPROVE()
    {
    }

    public java.lang.String getCodeRemark()
    {
        return "\tnc.vo.pu.m21.entity.OrderVO[] inObject  =(nc.vo.pu.m21.entity.OrderVO[])getVos ();\nObject retValue=nc.bs.framework.common.NCLocator.getInstance().lookup(nc.itf.pu.m21.IOrderApprove.class).approve(inObject, this);\nreturn retValue;\n";
    }

    public java.lang.Object runComClass(nc.vo.pub.compiler.PfParameterVO vo)
        throws nc.vo.pub.BusinessException
    {
        java.lang.Object retValue;
        super.m_tmpVo = vo;
        nc.vo.pu.m21.entity.OrderVO inObject[] = (nc.vo.pu.m21.entity.OrderVO[])(nc.vo.pu.m21.entity.OrderVO[])getVos();
        //retValue = ((nc.itf.pu.m21.IOrderApprove)nc.bs.framework.common.NCLocator.getInstance().lookup(IOrderApprove.class)).approve(inObject, this);
        //return retValue;
        try {
			procActionFlow(vo);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return inObject;
    }
}*/