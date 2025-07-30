package forest;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Dimension;
import java.util.Map;
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
		
		// 初期化時にノードサイズを計算して設定
		initializeNodeSizes();
	}

	/**
	 * 描画を更新するメソッド
	 */
	public void update() {
		repaint(); // 画面の再描画を行う
		revalidate(); // スクロールバーのサイズ更新のため
	}

	/**
	 * スクロール可能領域のサイズを計算するメソッド
	 */
	@Override
	public Dimension getPreferredSize() {
		if (aModel == null) {
			return new Dimension(400, 300); // より小さなデフォルトサイズ
		}
		
		try {
			Map<Integer, Node> nodeList = aModel.getNodeList();
			if (nodeList == null || nodeList.isEmpty()) {
				return new Dimension(400, 300); // より小さなデフォルトサイズ
			}
			
			// 全ノードの位置から必要な描画領域を正確に計算
			int maxX = Integer.MIN_VALUE;
			int maxY = Integer.MIN_VALUE;
			int minX = Integer.MAX_VALUE;
			int minY = Integer.MAX_VALUE;
			
			for (Node node : nodeList.values()) {
				// Nodeオブジェクトから事前計算されたサイズを取得
				Integer rectWidth = node.getRectWidth();
				Integer rectHeight = node.getRectHeight();
				
				// サイズが設定されていない場合のフォールバック
				if (rectWidth == null || rectHeight == null) {
					FontMetrics fm = getFontMetrics(NODE_FONT);
					String nodeName = getNodeName(node);
					int textWidth = fm.stringWidth(nodeName);
					int textHeight = fm.getHeight();
					int padding = 3;
					rectWidth = textWidth + padding * 2;
					rectHeight = textHeight + padding * 2;
				}
				
				// ノードの実際の描画位置
				int nodeX = node.getX();
				int nodeY = node.getY() + VERTICAL_SPACING;
				
				// ノードの範囲を更新
				minX = Math.min(minX, nodeX);
				minY = Math.min(minY, nodeY);
				maxX = Math.max(maxX, nodeX + rectWidth);
				maxY = Math.max(maxY, nodeY + rectHeight);
			}
			
			// ノードが存在しない場合の処理
			if (minX == Integer.MAX_VALUE) {
				return new Dimension(100, 80);
			}
			
			// 実際に必要なサイズを計算（マージンなし、ノードの境界にぴったり）
			int width = maxX - minX;
			int height = maxY - minY;
			
			// 最小サイズの制限を緩和
			width = Math.max(100, width);
			height = Math.max(80, height);
			
			return new Dimension(width, height);
			
		} catch (Exception e) {
			return new Dimension(100, 80); // エラー時はデフォルトサイズ
		}
	}

	/**
	 * コンポーネントの描画を行うメソッド
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		if (aModel != null) {
			// モデルからnodeListを取得してノードを描画
			try {
				Map<Integer, Node> nodeList = aModel.getNodeList();
				if (nodeList != null) {
					// 最小座標を取得して描画位置を正規化
					int minX = Integer.MAX_VALUE;
					int minY = Integer.MAX_VALUE;
					
					for (Node node : nodeList.values()) {
						minX = Math.min(minX, node.getX());
						minY = Math.min(minY, node.getY() + VERTICAL_SPACING);
					}
					
					// 最小座標を原点に調整するためのオフセット
					final int offsetX = (minX != Integer.MAX_VALUE) ? -minX : 0;
					final int offsetY = (minY != Integer.MAX_VALUE) ? -minY : 0;
					
					// Graphics2Dに変換してオフセットを適用
					Graphics2D g2d = (Graphics2D) g.create();
					g2d.translate(offsetX, offsetY);
					
					// 1. まず親子関係の線を描画
					drawEdges(g2d, nodeList);
					
					// 2. その後にノードを描画（線の上に重ねる）
					for (Node node : nodeList.values()) {
						drawNode(g2d, node);
					}
					
					g2d.dispose();
				}
			} catch (Exception e) {
				// エラー時はエラーメッセージを表示
				g.setColor(Color.RED);
				g.drawString("描画エラー: " + e.getMessage(), 10, 20);
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
				
				// 親ノードの描画情報をNodeオブジェクトから取得
				Integer parentRectWidth = parentNode.getRectWidth();
				Integer parentRectHeight = parentNode.getRectHeight();
				
				// サイズが設定されていない場合のフォールバック
				if (parentRectWidth == null || parentRectHeight == null) {
					String parentName = getNodeName(parentNode);
					FontMetrics fm = g2d.getFontMetrics();
					int parentTextWidth = fm.stringWidth(parentName);
					int parentTextHeight = fm.getHeight();
					int padding = 3;
					parentRectWidth = parentTextWidth + padding * 2;
					parentRectHeight = parentTextHeight + padding * 2;
				}
				
				// 親ノードの右端中央の座標
				int parentX = parentNode.getX() + parentRectWidth;
				int parentY = parentNode.getY() + VERTICAL_SPACING + parentRectHeight / 2;
				
				// 各子ノードとの間に線を描画
				for (Integer childId : children) {
					Node childNode = nodeList.get(childId);
					if (childNode == null) continue;
					
					// 子ノードの描画情報をNodeオブジェクトから取得
					Integer childRectHeight = childNode.getRectHeight();
					
					// サイズが設定されていない場合のフォールバック
					if (childRectHeight == null) {
						FontMetrics fm = g2d.getFontMetrics();
						int childTextHeight = fm.getHeight();
						int padding = 3;
						childRectHeight = childTextHeight + padding * 2;
					}
					
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
		// Nodeクラスに追加されたgetName()メソッドを使用
		if (node.getName() != null) {
			return node.getName();
		}
		return "Node";
	}

	/**
	 * 初期化時に全ノードのサイズを計算してNodeオブジェクトに設定するメソッド
	 */
	private void initializeNodeSizes() {
		if (aModel == null) return;
		
		try {
			Map<Integer, Node> nodeList = aModel.getNodeList();
			if (nodeList == null || nodeList.isEmpty()) return;
			
			FontMetrics fm = getFontMetrics(NODE_FONT);
			int padding = 3;
			
			for (Node node : nodeList.values()) {
				String nodeName = getNodeName(node);
				int textWidth = fm.stringWidth(nodeName);
				int textHeight = fm.getHeight();
				
				int rectWidth = textWidth + padding * 2;
				int rectHeight = textHeight + padding * 2;
				
				// Nodeオブジェクトにサイズを設定
				node.setRectWidth(rectWidth);
				node.setRectHeight(rectHeight);
			}
		} catch (Exception e) {
			// 初期化時にエラーが発生した場合はサイズ情報なしで続行
			System.err.println("ノードサイズの初期化でエラーが発生しました: " + e.getMessage());
		}
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
		
		// Nodeオブジェクトから事前計算されたサイズを取得
		Integer rectWidth = node.getRectWidth();
		Integer rectHeight = node.getRectHeight();
		
		// サイズが設定されていない場合のフォールバック
		if (rectWidth == null || rectHeight == null) {
			int textWidth = fm.stringWidth(nodeName);
			int textHeight = fm.getHeight();
			int padding = 3;
			rectWidth = textWidth + padding * 2;
			rectHeight = textHeight + padding * 2;
		}
		
		// ノードの座標（縦方向間隔を考慮）
		int x = node.getX();
		int y = node.getY() + VERTICAL_SPACING;
		
		// 四角形を描画（背景）
		g2d.setColor(Color.WHITE);
		g2d.fillRect(x, y, rectWidth, rectHeight);
		
		// 四角形の枠を描画
		g2d.setColor(Color.BLACK);
		g2d.drawRect(x, y, rectWidth, rectHeight);
		
		// 文字列を描画（中央配置）
		g2d.setColor(Color.BLACK);
		int textWidth = fm.stringWidth(nodeName);
		int textX = x + (rectWidth - textWidth) / 2;
		int textY = y + (rectHeight + fm.getAscent() - fm.getDescent()) / 2;
		g2d.drawString(nodeName, textX, textY);
	}

}
