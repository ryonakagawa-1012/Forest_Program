package forest;

import java.awt.Point;
import java.awt.event.MouseWheelListener;
import javax.swing.event.MouseInputAdapter;
import java.awt.event.MouseEvent;

/**
 * 樹状整列のプログラムを管理するためのモデルクラス
 */
public class ForestController extends MouseInputAdapter {

	private ForestModel aModel;

	private ForestView aView;


	/**
	 * 樹状整列のコントローラのインスタンスを生成するためのコンストラクタ
	 */
	public ForestController() {
		System.out.println("Hello from ForestController!");
		
		setModel();
		setView();

		aView.update();
		aModel.nextNode();
		aView.update();
	}

	public void mouseClicked(MouseEvent aMouseEvent) {

	}

	public void mouseDragged(MouseEvent aMouseEvent) {

	}

	public void mouseEntered(MouseEvent aMouseEvent) {

	}

	public void mouseExited(MouseEvent aMouseEvent) {

	}

	public void mouseMoved(MouseEvent aMouseEvent) {

	}

	public void mousePressed(MouseEvent aMouseEvent) {

	}

	public void mouseReleased(MouseEvent aMouseEvent) {

	}

	public void mouseWeelMoved(MouseEvent aMouseEvent) {

	}

	public void setModel() {
		this.aModel = new ForestModel("input.txt");
	}

	public void setView() {
		this.aView = new ForestView(this.aModel);
	}

}
