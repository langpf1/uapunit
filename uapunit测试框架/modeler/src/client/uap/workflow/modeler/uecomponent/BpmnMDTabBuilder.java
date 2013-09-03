package uap.workflow.modeler.uecomponent;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import javax.swing.JScrollPane;
import javax.swing.ScrollPaneLayout;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.logging.Logger;
import nc.bs.pf.pub.BillTypeCacheKey;
import nc.bs.pf.pub.PfDataCache;
import nc.md.MDBaseQueryFacade;
import nc.md.model.IComponent;
import nc.uap.pf.metadata.PfMetadataTools;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.formula.dialog.FormulaEventSource;
import nc.ui.pub.formula.dialog.IFormulaEventListener;
import nc.ui.pub.formula.dialog.IFormulaTabBuilder;
import nc.ui.pub.formula.dialog.FormulaEventSource.FormulaEventType;
import nc.ui.pub.formula.manager.IKeyWord;
import nc.ui.pub.query.tools.MDVisitTree;
import nc.ui.pub.tree.TreeNodeLocalizer;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pub.BusinessException;
import nc.vo.pub.billtype.BilltypeVO;
import nc.vo.pub.formulaedit.FormulaItem;
import nc.vo.uap.pf.PFBusinessException;
import nc.vo.uap.pf.PFRuntimeException;

public class BpmnMDTabBuilder extends UIPanel implements IFormulaTabBuilder {

	private static final long serialVersionUID = -7274738454797768113L;

	private JScrollPane scrollPanel;

	private String objectType = "10GY";

	private MDVisitTree mdTree;

	int wordType = IKeyWord.WORD_TYPE;
	private List<IFormulaEventListener> listeners;

	public BpmnMDTabBuilder(String objectType) {
		this.objectType = objectType;
	}

	@Override
	public String getTabName() {
		return "元数据";
	}

	public static void checkBilltypeRelatedMeta(String strTypeCode) {
		BilltypeVO btVO = PfDataCache.getBillTypeInfo(new BillTypeCacheKey().buildBilltype(strTypeCode).buildPkGroup(InvocationInfoProxy.getInstance().getGroupId()));
		if (btVO == null)
			throw new PFRuntimeException(NCLangRes4VoTransl.getNCLangRes().getStrByID("busitype", "busitypehint-000026", null, new String[] { strTypeCode }));

		boolean hasMeta = false;
		if (btVO.getIstransaction() != null && btVO.getIstransaction().booleanValue()) {
			// 如果为交易类型，则获取其单据类型关联的元数据组件，因为交易类型中没有这个信息
			btVO = PfDataCache.getBillTypeInfo(new BillTypeCacheKey().buildBilltype(btVO.getParentbilltype()));
			hasMeta = !StringUtil.isEmptyWithTrim(btVO.getComponent());
		} else
			hasMeta = !StringUtil.isEmptyWithTrim(btVO.getComponent());
		if (!hasMeta)
			throw new PFRuntimeException(NCLangRes4VoTransl.getNCLangRes().getStrByID("busitype", "busitypehint-000027", null, new String[] { strTypeCode }));
	}

	public static IComponent queryComponentOfBilltype(String objectType) throws BusinessException {

		// 必须保证单据类型关联了元数据实体
		checkBilltypeRelatedMeta(objectType);

		BilltypeVO btVO = PfDataCache.getBillType(new BillTypeCacheKey().buildBilltype(objectType).buildPkGroup(InvocationInfoProxy.getInstance().getGroupId()));
		String strComp = btVO.getComponent();
		if (btVO.getIstransaction() != null && btVO.getIstransaction().booleanValue()) {
			// 如果为交易类型，则获取其单据类型关联的元数据组件，因为交易类型中没有这个信息
			btVO = PfDataCache.getBillTypeInfo(new BillTypeCacheKey().buildBilltype(btVO.getParentbilltype()));
			strComp = btVO.getComponent();
		}

		if (StringUtil.isEmptyWithTrim(strComp))
			throw new PFBusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("busitype", "busitypehint-000028", null, new String[] { objectType }));

		return MDBaseQueryFacade.getInstance().getComponentByName(strComp.trim());
	}

	private Component getMetadataTree() {
		if (mdTree == null) {
			DefaultMutableTreeNode root = new DefaultMutableTreeNode("root");
			mdTree = new MDVisitTree(root);
			mdTree.setRootVisible(false);
			mdTree.setShowsRootHandles(true);
			mdTree.setEntityLeaf(true);
			TreeNodeLocalizer tns = new TreeNodeLocalizer(mdTree);
			tns.setWildcardEnabled(false);
			// tns.setInputHint("search")/* @res "输入名称搜索:" */;
			// tns.setInputHint(NCLangRes.getInstance().getStrByID("smcomm",
			// "UPP1005-000139"))/* @res "输入名称搜索:" */;
			// 查询出某个组件
			try {
				IComponent comp = null;
				if (objectType.lastIndexOf(".")>0){
					String mdName = null;
					Class<?> clz = Class.forName(objectType);
					Method method = clz.getMethod("getDefaultTableName", new Class<?>[0]);
					mdName = (String)method.invoke(clz.newInstance(), new Object[0]);
					if(mdName != null)
						comp = MDBaseQueryFacade.getInstance().getComponentByName(mdName);
				}else{
					comp = PfMetadataTools.queryComponentOfBilltype(objectType);
				}
				mdTree.initUI(comp);
			} catch (Exception e) {
				e.printStackTrace();
				Logger.error(e.getMessage(), e);
			}
		}

		mdTree.setRootVisible(false);
		mdTree.setShowsRootHandles(true);

		// 鼠标双击事件处理器
		MouseListener ml = new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				TreePath selPath = mdTree.getPathForLocation(e.getX(), e.getY());
				FormulaItem formulaItem = getFormulaItem(selPath);
				if (formulaItem != null) {
					int count = e.getClickCount();
					FormulaEventSource eventSource = new FormulaEventSource();
					eventSource.setEventSource(this);
					if (count == 1) {
						eventSource.setEventType(FormulaEventType.MESSAGE_TO_HINTPANEL);
						eventSource.setNewString(formulaItem.getHintMsg());
					} else if (count == 2) {
						eventSource.setEventType(FormulaEventType.INSERT_TO_EDITOR);
						eventSource.setNewString(formulaItem.getInputSig());
					}
					if (getListeners() != null) {
						for (IFormulaEventListener listener : getListeners()) {
							listener.notifyFormulaEvent(eventSource);
						}
					}

				}

			}

			// 双击时打开条件编辑对话框
			private FormulaItem getFormulaItem(TreePath selPath) {
				if (selPath == null)
					return null;
				FormulaItem fitem = null;
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) selPath.getLastPathComponent();
				// if (node.isLeaf()) {
				String[] realPath = MDVisitTree.createNodeCodeAndName(node);
				fitem = new FormulaItem(realPath[1], realPath[0], realPath[1]);
				// }
				return fitem;
			}

		};
		mdTree.addMouseListener(ml);

		return mdTree;
	}

	@Override
	public List<IFormulaEventListener> getListeners() {
		return listeners;
	}

	@Override
	public Map<String, FormulaItem> getName2FormulaItemMap() {
		return null;
	}

	@Override
	public void initUI() {
		setLayout(new BorderLayout());
		getMetadataTree();
		mdTree.setVisible(true);

		scrollPanel = new JScrollPane();
		scrollPanel.setLayout(new ScrollPaneLayout());
		scrollPanel.setViewportView(mdTree);
		scrollPanel.setVisible(true);
		scrollPanel.validate();
		add(scrollPanel, BorderLayout.CENTER);
	}

	@Override
	public void setListeners(List<IFormulaEventListener> listeners) {
		this.listeners = listeners;
	}

	@Override
	public int getWordType() {
		return wordType;
	}

	@Override
	public void setWordType(int wordType) {
		this.wordType = wordType;
	}

}
