package uap.workflow.modeler.uecomponent;

import nc.ui.ml.NCLangRes;

public class DefaultCellLib implements IGraphCellLib {//

	// Adds some template cells for dropping into the graph

	private static String[][] imageCells = new String[][] {////
			new String[] { "Bell", "/com/mxgraph/examples/swing/images/bell.png", "image;image=/com/mxgraph/examples/swing/images/bell.png", "50", "50", "Bell", "Y" },
			new String[] { "Box", "/com/mxgraph/examples/swing/images/box.png", "image;image=/com/mxgraph/examples/swing/images/box.png", "50", "50", "Box", "Y" },
			new String[] { "Cube", "/com/mxgraph/examples/swing/images/cube_green.png", "image;image=/com/mxgraph/examples/swing/images/cube_green.png", "50", "50", "Cube", "Y" },
			new String[] { "User", "/com/mxgraph/examples/swing/images/dude3.png", "roundImage;image=/com/mxgraph/examples/swing/images/dude3.png", "50", "50", "User", "Y" },
			new String[] { "Earth", "/com/mxgraph/examples/swing/images/earth.png", "roundImage;image=/com/mxgraph/examples/swing/images/earth.png", "50", "50", "Earth", "Y" },
			new String[] { "Gear", "/com/mxgraph/examples/swing/images/gear.png", "roundImage;image=/com/mxgraph/examples/swing/images/gear.png", "50", "50", "Gear", "Y" },
			new String[] { "Home", "/com/mxgraph/examples/swing/images/house.png", "image;image=/com/mxgraph/examples/swing/images/house.png", "50", "50", "Home", "Y" },
			new String[] { "Package", "/com/mxgraph/examples/swing/images/package.png", "image;image=/com/mxgraph/examples/swing/images/package.png", "50", "50", "Package", "Y" },
			new String[] { "Printer", "/com/mxgraph/examples/swing/images/printer.png", "image;image=/com/mxgraph/examples/swing/images/printer.png", "50", "50", "Printer", "Y" },
			new String[] { "Server", "/com/mxgraph/examples/swing/images/server.png", "image;image=/com/mxgraph/examples/swing/images/server.png", "50", "50", "Server", "Y" },

			new String[] { "Workplace", "/com/mxgraph/examples/swing/images/workplace.png", "image;image=/com/mxgraph/examples/swing/images/workplace.png", "50", "50", "Workplace", "Y" },
			new String[] { "Wrench", "/com/mxgraph/examples/swing/images/wrench.png", "roundImage;image=/com/mxgraph/examples/swing/images/wrench.png", "50", "50", "Wrench", "Y" } ,
			
			
			new String[] { "BillMaker", "/nc/ui/wfengine/designer/resources/user_m.gif", "roundImage;image=/nc/ui/wfengine/designer/resources/user_m.gif", "50", "50", "BillMaker", "Y" },
			new String[] { "Starter", "/nc/ui/wfengine/designer/resources/start.gif", "roundImage;image=/nc/ui/wfengine/designer/resources/start.gif", "50", "50", "Start", "Y" },
			new String[] { "Ender", "/nc/ui/wfengine/designer/resources/end.gif", "roundImage;image=/nc/ui/wfengine/designer/resources/end.gif", "50", "50", "End", "Y" }
			
			
	};

	private static String[][] shapeCells = new String[][] {

	new String[] { "Container", "/com/mxgraph/examples/swing/images/swimlane.png", "swimlane", "280", "280", "", "Y" },
			new String[] { "Rectangle", "/com/mxgraph/examples/swing/images/rectangle.png", "rectangle", "160", "120", "", "Y" },
			new String[] { "Rounded Rectangle", "/com/mxgraph/examples/swing/images/rounded.png", "rounded=1", "160", "120", "", "Y" },
			new String[] { "Ellipse", "/com/mxgraph/examples/swing/images/ellipse.png", "ellipse", "160", "160", NCLangRes.getInstance().getStrByID("pfworkflow1", "BuziflowObjectTreeCellRender-000000")/*½áÊø*/, "Y" },
			new String[] { "Double Ellipse", "/com/mxgraph/examples/swing/images/doubleellipse.png", "ellipse;shape=doubleEllipse", "160", "160", "", "Y" },
			new String[] { "Triangle", "/com/mxgraph/examples/swing/images/triangle.png", "triangle", "120", "160", NCLangRes.getInstance().getStrByID("pfworkflow1", "BuziflowObjectTreeCellRender-000001")/*¿ªÊ¼*/, "Y" },
			new String[] { "Rhombus", "/com/mxgraph/examples/swing/images/rhombus.png", "rhombus", "160", "160", "", "Y" },
			new String[] { "Horizontal Line", "/com/mxgraph/examples/swing/images/hline.png", "line", "160", "10", "", "Y" },
			new String[] { "Hexagon", "/com/mxgraph/examples/swing/images/hexagon.png", "shape=hexagon", "160", "120", "", "Y" },
			new String[] { "Cylinder", "/com/mxgraph/examples/swing/images/cylinder.png", "shape=cylinder", "120", "160", "", "Y" },
			new String[] { "Actor", "/com/mxgraph/examples/swing/images/actor.png", "shape=actor", "120", "160", "", "Y" },
			new String[] { "Cloud", "/com/mxgraph/examples/swing/images/cloud.png", "ellipse;shape=cloud", "160", "120", "", "Y" },

			new String[] { "Straight", "/com/mxgraph/examples/swing/images/straight.png", "straight", "120", "120", "", "N" },
			new String[] { "Horizontal Connector", "/com/mxgraph/examples/swing/images/connect.png", null, "100", "100", "", "N" },
			new String[] { "Vertical Connector", "/com/mxgraph/examples/swing/images/vertical.png", "vertical", "100", "100", "", "N" },
			new String[] { "Entity Relation", "/com/mxgraph/examples/swing/images/entity.png", "entity", "100", "100", "", "N" },
			new String[] { "Arrow", "/com/mxgraph/examples/swing/images/arrow.png", "arrow", "120", "120", "", "N" } 
	
	       };

	private static String[][] smybolCells = new String[][] {
			new String[] { "Timer", "/com/mxgraph/examples/swing/images/timer.png", "roundImage;image=/com/mxgraph/examples/swing/images/timer.png", "80", "80", "Timer", "Y" },
			new String[] { "Terminate", "/com/mxgraph/examples/swing/images/timer.png", "roundImage;image=/com/mxgraph/examples/swing/images/terminate.png", "80", "80", "Terminate", "Y" },
			new String[] { "Rule", "/com/mxgraph/examples/swing/images/rule.png", "roundImage;image=/com/mxgraph/examples/swing/images/rule.png", "80", "80", "Rule", "Y" },
			new String[] { "Multiple", "/com/mxgraph/examples/swing/images/multiple.png", "roundImage;image=/com/mxgraph/examples/swing/images/multiple.png", "80", "80", "Multiple", "Y" },//
			new String[] { "Message", "/com/mxgraph/examples/swing/images/message.png", "roundImage;image=/com/mxgraph/examples/swing/images/message.png", "80", "80", "Message", "Y" },
			new String[] { "Merge", "/com/mxgraph/examples/swing/images/merge.png", "roundImage;image=/com/mxgraph/examples/swing/images/merge.png", "80", "80", "Merge", "Y" },
			new String[] { "Link", "/com/mxgraph/examples/swing/images/link.png", "roundImage;image=/com/mxgraph/examples/swing/images/link.png", "80", "80", "Link", "Y" },
			new String[] { "Fork", "/com/mxgraph/examples/swing/images/fork.png", "roundImage;image=/com/mxgraph/examples/swing/images/fork.png", "80", "80", "Fork", "Y" },
			new String[] { "Event", "/com/mxgraph/examples/swing/images/event.png", "roundImage;image=/com/mxgraph/examples/swing/images/event.png", "80", "80", "Event", "Y" },
			new String[] { "Error", "/com/mxgraph/examples/swing/images/error.png", "roundImage;image=/com/mxgraph/examples/swing/images/error.png", "80", "80", "Error", "Y" },
			new String[] { "Cancel", "/com/mxgraph/examples/swing/images/cancel_end.png", "roundImage;image=/com/mxgraph/examples/swing/images/cancel_end.png", "80", "80", "Cancel", "Y" },
			new String[] { "Inclusive", "/com/mxgraph/examples/swing/images/inclusive.png", "roundImage;image=/com/mxgraph/examples/swing/images/inclusive.png", "80", "80", "Inclusive", "Y" } };

	/**
	 * @param strss
	 * @return
	 */
	private GraphElementMeta[] getArray(String[][] strss) {
		GraphElementMeta[] metas = new GraphElementMeta[strss.length];
		for (int i = 0; i < metas.length; i++) {
			metas[i] = new StringCellMeta(strss[i]);
		}
		return metas;
	}

	@Override
	public GraphElementMeta[] getImageCellsMeta() {
		// TODO Auto-generated method stub
		return getArray(getImageCellDesriptor());
	}

	@Override
	public GraphElementMeta[] getShapeCellsMeta() {
		// TODO Auto-generated method stub
		return getArray(getShapeCellDesriptor());
	}

	@Override
	public GraphElementMeta[] getSymbolCellsMeta() {
		// TODO Auto-generated method stub
		return getArray(getSymbolsCellDesriptor());
	}

	// design for override
	public String[][] getImageCellDesriptor() {
		return imageCells;
	}

	public String[][] getShapeCellDesriptor() {
		return shapeCells;
	}

	public String[][] getSymbolsCellDesriptor() {
		return smybolCells;
	}

}
