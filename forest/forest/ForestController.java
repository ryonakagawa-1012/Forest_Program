package forest;

import java.awt.Point;
import javax.swing.event.MouseInputAdapter;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JPopupMenu;
import javax.swing.JMenuItem;
import java.awt.event.MouseEvent;
import java.util.Map;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.SwingUtilities;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.FlowLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

/**
 * 樹状整列のプログラムを管理するためのモデルクラス
 */
public class ForestController extends MouseInputAdapter {

	/*
	 * 樹状整列のモデルを管理するためのインスタンス
	 */
	private ForestModel aModel;

	/*
	 * 樹状整列のビューを管理するためのインスタンス
	 */
	private ForestView aView;

	/*
	 * 樹状整列のフレームを管理するためのインスタンス
	 */
	private JFrame aFrame;

	/*
	 * 樹状整列のスクロールペインを管理するためのインスタンス
	 */
	private JScrollPane aScrollPane;

	/*
	 * ポップアップメニューを管理するためのインスタンス
	 */
	private JPopupMenu popupMenu;

	/*
	 * アニメーションの速度を管理するための変数
	 * デフォルトは100ミリ秒
	 */
	private int animationDelay = 100;

	/**
	 * 樹状整列のコントローラのインスタンスを生成するためのコンストラクタ
	 */
	public ForestController() {
		System.out.println("Hello from ForestController!");

		initializePopupMenu();
		setModel();
		setView();
		setFrame();

		for (Integer nextNodeId : aModel.getVisitPath()) {
			try {
				Thread.sleep(animationDelay);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
			aModel.nextNode(nextNodeId);
			aView.update();
		}
	}

	/*
	 * マウスがクリックされた時に呼び出されるメソッド
	 */
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

	/**
	 * ポップアップメニューを初期化するメソッド
	 */
	private void initializePopupMenu() {
		popupMenu = new JPopupMenu();

		// アニメーション速度設定パネル
		JPanel speedPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel speedLabel = new JLabel("速度(ms):");
		JTextField speedField = new JTextField(String.valueOf(animationDelay), 8);

		// Enterキーで設定を適用
		speedField.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					String inputValue = speedField.getText();
					// まずメニューを閉じる
					popupMenu.setVisible(false);
					// その後で設定を更新（メニューの再作成を含む）
					javax.swing.SwingUtilities.invokeLater(() -> {
						updateAnimationSpeed(inputValue);
					});
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}

			@Override
			public void keyTyped(KeyEvent e) {
			}
		});

		// フォーカスが外れた時の処理
		speedField.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				// フォーカスを得た時は何もしない
			}

			@Override
			public void focusLost(FocusEvent e) {
				// フォーカスを失った時に値を更新（メニューを閉じる必要はない）
				String inputValue = speedField.getText();
				javax.swing.SwingUtilities.invokeLater(() -> {
					updateAnimationSpeedSilent(inputValue);
				});
			}
		});

		speedPanel.add(speedLabel);
		speedPanel.add(speedField);
		popupMenu.add(speedPanel);

		popupMenu.addSeparator(); // 区切り線を追加

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
	 * アニメーション速度を更新するメソッド
	 */
	private void updateAnimationSpeed(String input) {
		if (input != null && !input.trim().isEmpty()) {
			try {
				int newDelay = Integer.parseInt(input.trim());
				if (newDelay >= 1 && newDelay <= 5000) {
					animationDelay = newDelay;
					// メニューの表示も更新
					updatePopupMenu();
				} else {
					// エラーダイアログを表示してからメニューを閉じる
					javax.swing.SwingUtilities.invokeLater(() -> {
						JOptionPane.showMessageDialog(
								aFrame,
								"1から5000の間の値を入力してください",
								"入力エラー",
								JOptionPane.ERROR_MESSAGE);
					});
				}
			} catch (NumberFormatException e) {
				// エラーダイアログを表示してからメニューを閉じる
				javax.swing.SwingUtilities.invokeLater(() -> {
					JOptionPane.showMessageDialog(
							aFrame,
							"数値を入力してください",
							"入力エラー",
							JOptionPane.ERROR_MESSAGE);
				});
			}
		}
	}

	/**
	 * アニメーション速度を静かに更新するメソッド（エラーメッセージなし）
	 */
	private void updateAnimationSpeedSilent(String input) {
		if (input != null && !input.trim().isEmpty()) {
			try {
				int newDelay = Integer.parseInt(input.trim());
				if (newDelay >= 1 && newDelay <= 5000) {
					animationDelay = newDelay;
					// メニューの表示も更新
					updatePopupMenu();
				}
			} catch (NumberFormatException e) {
				// エラーの場合は何もしない
			}
		}
	}

	/**
	 * ポップアップメニューの表示を更新するメソッド
	 */
	private void updatePopupMenu() {
		// 既存のメニューを削除して再作成
		popupMenu.removeAll();
		initializePopupMenu();
	}

	/**
	 * 指定されたファイルパスで新しいウィンドウを作成するメソッド
	 */
	private void createNewWindow(String filePath) {
		// 元のウィンドウを閉じる
		if (this.aFrame != null) {
			this.aFrame.dispose();
		}

		// 新しいモデルを作成
		ForestModel newModel = new ForestModel(filePath);

		if (filePath == "resource/data/semilattice.txt") {
			Map<Integer, Node> nodeList = newModel.getNodeList();
			Node criticalNode = nodeList.get(68);
			System.out.println(criticalNode.getName());
			System.out.println(criticalNode.getParentId());
			criticalNode.setParentId(56);
		}
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
		JFrame newFrame = new JFrame("Forest Viewer");
		newFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		newFrame.add(newScrollPane);
		newFrame.setSize(800, 600);
		newFrame.setLocationRelativeTo(null);
		newFrame.setVisible(true);

		// 現在のフレームを新しいフレームに更新
		this.aFrame = newFrame;
		this.aModel = newModel;
		this.aView = newView;
		this.aScrollPane = newScrollPane;

		// 新しいウィンドウでアニメーションを実行
		new Thread(() -> {
			for (Integer nextNodeId : newModel.getVisitPath()) {
				try {
					Thread.sleep(animationDelay);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					break;
				}
				newModel.nextNode(nextNodeId);
				newView.update();
			}
		}).start();
	}

	/**
	 * モデルを設定するメソッド
	 */
	public void setModel() {
		this.aModel = new ForestModel("resource/data/tree.txt");
	}

	/**
	 * ビューを設定するメソッド
	 */
	public void setView() {
		this.aView = new ForestView(this.aModel);
		// マウスイベントリスナーを設定
		this.aView.addMouseListener(this);
		this.aView.addMouseMotionListener(this);
	}

	/**
	 * フレームを設定するメソッド
	 */
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
