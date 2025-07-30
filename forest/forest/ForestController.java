package forest;

import java.awt.Point;
import java.awt.event.MouseWheelListener;
import javax.swing.event.MouseInputAdapter;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import java.awt.event.MouseEvent;

/**
 * 樹状整列のプログラムを管理するためのモデルクラス
 */
public class ForestController extends MouseInputAdapter {

	private ForestModel aModel;

	private ForestView aView;

	private JFrame aFrame;

	private JScrollPane aScrollPane;


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
		// マウスイベントリスナーを設定
		this.aView.addMouseListener(this);
		this.aView.addMouseMotionListener(this);
	}

	public void setFrame() {
		// JScrollPaneを作成してForestViewをラップ
		this.aScrollPane = new JScrollPane(this.aView);
		
		// スクロールポリシーを設定
		this.aScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		this.aScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		
		// スクロール速度を調整
		this.aScrollPane.getHorizontalScrollBar().setUnitIncrement(16);
		this.aScrollPane.getVerticalScrollBar().setUnitIncrement(16);
		
		// JFrameを設定
		this.aFrame = new JFrame("Forest Viewer");
		this.aFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.aFrame.add(this.aScrollPane); // ForestViewの代わりにJScrollPaneを追加
		this.aFrame.setSize(800, 600);
		this.aFrame.setLocationRelativeTo(null); // 画面中央に表示
		this.aFrame.setVisible(true);
	}

}
