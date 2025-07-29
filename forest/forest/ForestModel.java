package forest;

import java.awt.Point;
import java.util.Map;
import java.util.HashMap;
import java.io.File;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * 木構造のデータを管理するためのモデルクラス
 */
public class ForestModel extends Object {

	/**
	 * 木構造の隣接配列(key=頂点番号:value=List<E>)を保持する連想配列
	 */
	private Map<Integer, List<Integer>> graphAdjacentList;

	/**
	 * 入力として与えられた木構造の親ノードの番号を保持する配列
	 */
	private Integer[] parentList;

	/**
	 * ノード番号とNode型で保持している型情報を紐づけるための連想配列
	 */
	private Map<Integer, Node> nodeList;

	/**
	 * 主にdepthFirstSearchメソッド内で使用する、ノードが深さ優先探索で探索済みかどうかを保持する配列
	 */
	private Boolean[] visitedNodeList;

	private File inputFile;

	/**
	 * 樹状整列のロジックを処理するモデルのインスタンスを生成するコンストラクタ
	 */
	public ForestModel(String filePath) {
		System.out.println("Hello from ForestModel!");
		
		this.inputFile = new File(filePath);
		makeNodeList();
		makeGraphAdjacentList();
		makeParentList();
		this.visitedNodeList = new Boolean[this.nodeList.size()];
		for (Integer i = 0; i < this.visitedNodeList.length; i++) {
			this.visitedNodeList[i] = false;
		}
	}

	/**
	 * 入力ファイルを元にnodeListの初期値を決定するメソッド
	 */
	public void makeNodeList() {
		this.nodeList = new HashMap<Integer, Node>();

		try (BufferedReader reader = new BufferedReader(new FileReader(this.inputFile))) {
			String line;
			boolean inNodesSection = false;

			while ((line = reader.readLine()) != null) {
				line = line.trim();

				// nodes:セクションの開始を検出
				if (line.equals("nodes:")) {
					inNodesSection = true;
					continue;
				}

				// branches:セクションに到達したらnodes:セクションを終了
				if (line.equals("branches:")) {
					inNodesSection = false;
					break;
				}

				// nodes:セクション内でノード情報を処理
				if (inNodesSection && !line.isEmpty()) {
					String[] parts = line.split(", ");
					if (parts.length == 2) {
						try {
							Integer nodeId = Integer.parseInt(parts[0]);
							String nodeName = parts[1];
							// ノードの初期座標は(0, 0)で設定
							Node node = new Node(0, 0, nodeName);
							this.nodeList.put(nodeId, node);
						} catch (NumberFormatException e) {
							// ノード番号が数値でない場合はスキップ
							continue;
						}
					}
				}
			}
		} catch (IOException e) {
			System.err.println("Error reading file: " + e.getMessage());
		}
	}

	/**
	 * 入力ファイルを元にgraphAdjacentListの値を決定するメソッド
	 */
	public void makeGraphAdjacentList() {

	}

	/**
	 * inputFileを元に
	 * parentListの値を決定するメソッド
	 */
	public void makeParentList() {

	}

	/**
	 * depthFirstSearchを呼び出し、操作すべきNodeを返すメソッド
	 */
	public Node nextNode() {
		return null;
	}

	/**
	 * 深さ優先探索をし、次に訪れるノード番号を返すメソッド
	 */
	public Integer depthFirstSearch() {
		return null;
	}

	/**
	 * 引数で受け取った座標が、ノードをクリックしているかを判定し、クリックしていたらNodeのprintNodeNameを呼び出すメソッド
	 */
	public void nodeClicked(Point aPoint) {

	}

}
