package forest;

import java.awt.Point;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.util.Map;
import java.util.List;
import java.lang.reflect.Field;
import javax.swing.JPanel;

/**
 * 木構造を表示するためのモデルクラス
 */
public class ForestView extends JPanel {

	private ForestModel aModel;

	// フォント設定: Serif系標準体12ポイント
	private static final Font NODE_FONT = new Font(Font.SERIF, Font.PLAIN, 12);
	
	// ノード間隔設定
	private static final int HORIZONTAL_SPACING = 25; // 横方向間隔（ノード間の最小距離）
	private static final int VERTICAL_SPACING = 2;    // 縦方向間隔（ノードY座標のオフセット）


	/**
	 * 樹状整列のビューのインスタンスを生成し、引数として受け取ったモデルのインスタンスをmodelに束縛するコンストラクタ
	 */
	public ForestView(ForestModel aModel) {
		System.out.println("Hello from ForestView!");
		
		this.aModel = aModel;
	}

	/**
	 * 描画を更新するメソッド
	 */
	public void update() {
		repaint(); // 画面の再描画を行う
	}

	/**
	 * コンポーネントの描画を行うメソッド
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		if (aModel != null) {
			// モデルからnodeListを取得してノードを描画
			// 注意: getNodeList()メソッドがForestModelに追加される予定
			try {
				Map<Integer, Node> nodeList = aModel.getNodeList();
				if (nodeList != null) {
					// 1. まず親子関係の線を描画
					drawEdges(g, nodeList);
					
					// 2. その後にノードを描画（線の上に重ねる）
					for (Node node : nodeList.values()) {
						drawNode(g, node);
					}
				}
			} catch (Exception e) {
				// getNodeList()メソッドがまだ実装されていない場合
				// エラーメッセージを表示
				g.setColor(Color.RED);
				g.drawString("getNodeList()メソッドがForestModelに実装されていません", 10, 20);
			}
		}
	}

	/**
	 * 親子ノード間の線を描画するメソッド
	 */
	private void drawEdges(Graphics g, Map<Integer, Node> nodeList) {
		Graphics2D g2d = (Graphics2D) g;
		
		// フォントを設定
		g2d.setFont(NODE_FONT);
		
		try {
			// グラフの隣接リストを取得
			Map<Integer, java.util.List<Integer>> adjacentList = aModel.getGraphAdjacentList();
			if (adjacentList == null) return;
			
			g2d.setColor(Color.BLACK);
			
			// 各ノードについて、その子ノードとの間に線を描画
			for (Map.Entry<Integer, java.util.List<Integer>> entry : adjacentList.entrySet()) {
				Integer parentId = entry.getKey();
				java.util.List<Integer> children = entry.getValue();
				
				Node parentNode = nodeList.get(parentId);
				if (parentNode == null || children == null) continue;
				
				// 親ノードの描画情報を計算
				String parentName = getNodeName(parentNode);
				FontMetrics fm = g2d.getFontMetrics();
				int parentTextWidth = fm.stringWidth(parentName);
				int parentTextHeight = fm.getHeight();
				int padding = 5;
				int parentRectWidth = parentTextWidth + padding * 2;
				int parentRectHeight = parentTextHeight + padding * 2;
				
				// 親ノードの右端中央の座標
				int parentX = parentNode.getX() + parentRectWidth;
				int parentY = parentNode.getY() + VERTICAL_SPACING + parentRectHeight / 2;
				
				// 各子ノードとの間に線を描画
				for (Integer childId : children) {
					Node childNode = nodeList.get(childId);
					if (childNode == null) continue;
					
					// 子ノードの描画情報を計算
					int childTextHeight = fm.getHeight();
					int childRectHeight = childTextHeight + padding * 2;
					
					// 子ノードの左端中央の座標（縦方向間隔を考慮）
					int childX = childNode.getX();
					int childY = childNode.getY() + VERTICAL_SPACING + childRectHeight / 2;
					
					// 親から子へ線を描画
					g2d.drawLine(parentX, parentY, childX, childY);
				}
			}
		} catch (Exception e) {
			// getGraphAdjacentList()メソッドがまだ実装されていない場合は何もしない
		}
	}

	/**
	 * ノードの名前を取得するヘルパーメソッド
	 */
	private String getNodeName(Node node) {
		try {
			Field nameField = node.getClass().getDeclaredField("name");
			nameField.setAccessible(true);
			Object nameValue = nameField.get(node);
			if (nameValue instanceof String) {
				return (String) nameValue;
			}
		} catch (Exception e) {
			// フィールドアクセスに失敗した場合はデフォルト名を使用
		}
		return "Node";
	}

	/**
	 * ノード間の推奨間隔を取得するメソッド
	 */
	public int getHorizontalSpacing() {
		return HORIZONTAL_SPACING;
	}

	/**
	 * 縦方向の間隔を取得するメソッド
	 */
	public int getVerticalSpacing() {
		return VERTICAL_SPACING;
	}

	/**
	 * 個別のノードを描画するメソッド
	 * ノードを四角形で囲まれた文字列として描画する
	 */
	private void drawNode(Graphics g, Node node) {
		if (node == null) return;
		
		Graphics2D g2d = (Graphics2D) g;
		
		// フォントを設定
		g2d.setFont(NODE_FONT);
		FontMetrics fm = g2d.getFontMetrics();
		
		// ノードの名前を取得
		String nodeName = getNodeName(node);
		
		// 文字列のサイズを測定
		int textWidth = fm.stringWidth(nodeName);
		int textHeight = fm.getHeight();
		
		// 四角形のパディング
		int padding = 3;
		int rectWidth = textWidth + padding * 2;
		int rectHeight = textHeight + padding * 2;
		
		// ノードの座標（縦方向間隔を考慮）
		int x = node.getX();
		int y = node.getY() + VERTICAL_SPACING;
		
		// 四角形を描画（背景）
		g2d.setColor(Color.WHITE);
		g2d.fillRect(x, y, rectWidth, rectHeight);
		
		// 四角形の枠を描画
		g2d.setColor(Color.BLACK);
		g2d.drawRect(x, y, rectWidth, rectHeight);
		
		// 文字列を描画
		g2d.setColor(Color.BLACK);
		g2d.drawString(nodeName, x + padding, y + padding + fm.getAscent());
	}

}
