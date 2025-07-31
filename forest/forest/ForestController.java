package forest;

import java.awt.Point;
import javax.swing.event.MouseInputAdapter;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JPopupMenu;
import javax.swing.JMenuItem;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.SwingUtilities;

/**
 * 樹状整列のプログラムを管理するためのモデルクラス
 */
public class ForestController extends MouseInputAdapter {

	private ForestModel aModel;

	private ForestView aView;

	private JFrame aFrame;

	private JScrollPane aScrollPane;

	private JPopupMenu popupMenu;


	/**
	 * 樹状整列のコントローラのインスタンスを生成するためのコンストラクタ
	 */
	public ForestController() {
		System.out.println("Hello from ForestController!");
		
		initializePopupMenu();
		setModel();
		setView();
		setFrame();

		for (Integer nextNodeId: aModel.getvisitPath()){
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
			aModel.nextNode(nextNodeId);
			aView.update();
		}
	}

	public void mouseClicked(MouseEvent aMouseEvent) {
		if (SwingUtilities.isRightMouseButton(aMouseEvent)) {
			// 右クリック時にポップアップメニューを表示
			popupMenu.show(aMouseEvent.getComponent(), aMouseEvent.getX(), aMouseEvent.getY());
		} else {
			// 左クリック時は従来の処理
			Point clickPoint = aMouseEvent.getPoint();
			this.aModel.nodeClicked(clickPoint);
		}
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

	/**
	 * ポップアップメニューを初期化するメソッド
	 */
	private void initializePopupMenu() {
		popupMenu = new JPopupMenu();
		
		// forest.txtメニューアイテム
		JMenuItem forestItem = new JMenuItem("forest.txt");
		forestItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				createNewWindow("resource/data/forest.txt");
			}
		});
		popupMenu.add(forestItem);
		
		// semilattice.txtメニューアイテム
		JMenuItem semilatticeItem = new JMenuItem("semilattice.txt");
		semilatticeItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				createNewWindow("resource/data/semilattice.txt");
			}
		});
		popupMenu.add(semilatticeItem);
		
		// tree.txtメニューアイテム
		JMenuItem treeItem = new JMenuItem("tree.txt");
		treeItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				createNewWindow("resource/data/tree.txt");
			}
		});
		popupMenu.add(treeItem);
	}

	/**
	 * 指定されたファイルパスで新しいウィンドウを作成するメソッド
	 */
	private void createNewWindow(String filePath) {
		// 新しいモデルを作成
		ForestModel newModel = new ForestModel(filePath);
		
		// 新しいビューを作成
		ForestView newView = new ForestView(newModel);
		newView.addMouseListener(this);
		newView.addMouseMotionListener(this);
		
		// 新しいスクロールペインを作成
		JScrollPane newScrollPane = new JScrollPane(newView);
		newScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		newScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		newScrollPane.getHorizontalScrollBar().setUnitIncrement(16);
		newScrollPane.getVerticalScrollBar().setUnitIncrement(16);
		
		// 新しいフレームを作成
		JFrame newFrame = new JFrame("Forest Viewer - " + filePath);
		newFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		newFrame.add(newScrollPane);
		newFrame.setSize(800, 600);
		newFrame.setLocationRelativeTo(null);
		newFrame.setVisible(true);
		
		// 新しいウィンドウでアニメーションを実行
		new Thread(() -> {
			for (Integer nextNodeId : newModel.getvisitPath()) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					break;
				}
				newModel.nextNode(nextNodeId);
				newView.update();
			}
		}).start();
	}

	public void setModel() {
		this.aModel = new ForestModel("resource/data/tree.txt");
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
