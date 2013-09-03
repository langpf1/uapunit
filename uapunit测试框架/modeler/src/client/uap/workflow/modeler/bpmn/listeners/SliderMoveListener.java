package uap.workflow.modeler.bpmn.listeners;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JSlider;
import javax.swing.plaf.basic.BasicSliderUI;
import com.mxgraph.swing.mxGraphComponent;


public class SliderMoveListener extends MouseAdapter {
	private JSlider zoomSlider;
	private mxGraphComponent graphComponent;
	public SliderMoveListener(JSlider zoomSlider,mxGraphComponent graphComponent){
		this.zoomSlider =zoomSlider;
		this.graphComponent=graphComponent;
		this.zoomSlider.addMouseListener(this);
	}
	
	public void mouseReleased(MouseEvent e) {
		int scale = zoomSlider.getValue();
		graphComponent.zoomTo(scale / 100.0, graphComponent.isCenterZoom());
		graphComponent.setPageVisible(false);
	}

	public void mouseClicked(MouseEvent e) {
		int scale = ((BasicSliderUI)zoomSlider.getUI()).valueForXPosition((int) e.getPoint().getX());
		zoomSlider.setValue(scale);
		graphComponent.zoomTo(scale / 100.0, graphComponent.isCenterZoom());
		graphComponent.setPageVisible(false);
	}
}

