package forest;

import java.awt.Point;
import java.awt.event.MouseWheelListener;
import javax.swing.event.MouseInputAdapter;
import javax.swing.JFrame;
import java.awt.event.MouseEvent;

/**
 * 樹状整列のプログラムを管理するためのモデルクラス
 */
public class ForestController extends MouseInputAdapter {

	private ForestModel aModel;

	private ForestView aView;

	private JFrame aFrame;


	/**
	 * 樹状整列のコントローラのインスタンスを生成するためのコンストラクタ
	 */
	public ForestController() {
		System.out.println("Hello from ForestController!");
		
		setModel();
		setView();
		setFrame();

		aView.update();
		// aModel.nextNode();
		// aView.update();
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
		this.aModel = new ForestModel("resource/data/forest.txt");
	}

	public void setView() {
		this.aView = new ForestView(this.aModel);
	}

	public void setFrame() {
		this.aFrame = new JFrame("Forest Viewer");
		this.aFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.aFrame.add(this.aView);
		this.aFrame.setSize(800, 600);
		this.aFrame.setLocationRelativeTo(null); // 画面中央に表示
		this.aFrame.setVisible(true);
	}

}
