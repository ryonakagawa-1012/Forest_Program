package forest;

import java.awt.Point;
import java.util.Map;
import java.util.HashMap;
import java.io.File;
import java.util.List;
import java.util.ArrayList;
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
	 * 入力として与えられた木構造の根ノードの番号を保持する配列
	 */
	private List<Integer> rootList;

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

		initialize(filePath);
	}

	/**
	 * 樹状整列のモデルを初期化するメソッド
	 */
	public void initialize(String filePath) {
		this.inputFile = new File(filePath);
		makeNodeList();
		makeGraphAdjacentList();
		System.out.println("graphAdjacentList: " + this.graphAdjacentList);
		makeRootList();
		System.out.println("rootList: " + this.rootList);
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
			Integer initialYGap = 0;

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
							// ノードの初期座標は20間隔にする
							Node node = new Node(0, initialYGap, nodeName);
							initialYGap += 20;
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
		this.graphAdjacentList = new HashMap<Integer, List<Integer>>();

		try (BufferedReader reader = new BufferedReader(new FileReader(this.inputFile))) {
			String line;
			boolean inBranchesSection = false;

			while ((line = reader.readLine()) != null) {
				line = line.trim();

				// branches:セクションの開始を検出
				if (line.equals("branches:")) {
					inBranchesSection = true;
					continue;
				}

				// branches:セクション内でエッジ情報を処理
				if (inBranchesSection && !line.isEmpty()) {
					String[] parts = line.split(", ");
					if (parts.length == 2) {
						try {
							Integer parentId = Integer.parseInt(parts[0]);
							Integer childId = Integer.parseInt(parts[1]);

							// 親ノードの隣接リストを取得または作成
							List<Integer> children = this.graphAdjacentList.get(parentId);
							if (children == null) {
								children = new ArrayList<Integer>();
								this.graphAdjacentList.put(parentId, children);
							}

							// 子ノードを隣接リストに追加
							children.add(childId);

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

		// 子を持たないノードにも空のリストを割り当て
		for (Integer nodeId : this.nodeList.keySet()) {
			if (!this.graphAdjacentList.containsKey(nodeId)) {
				this.graphAdjacentList.put(nodeId, new ArrayList<Integer>());
			}
		}
	}

	/**
	 * inputFileを元に
	 * parentListの値を決定するメソッド
	 */
	public void makeRootList() {
		this.rootList = new ArrayList<Integer>();

		// 根ノード名のリスト（出現順）
		List<String> rootNodeNames = new ArrayList<String>();

		try (BufferedReader reader = new BufferedReader(new FileReader(this.inputFile))) {
			String line;
			boolean inTreesSection = false;

			while ((line = reader.readLine()) != null) {
				String trimmedLine = line.trim();

				// セクション判定
				if (trimmedLine.equals("trees:")) {
					inTreesSection = true;
					continue;
				} else if (trimmedLine.equals("nodes:")) {
					inTreesSection = false;
					continue;
				} else if (trimmedLine.equals("branches:")) {
					break;
				}

				// trees:セクション処理
				if (inTreesSection && !line.isEmpty()) {
					if (!line.startsWith("|") && !line.startsWith(" ")) {
						rootNodeNames.add(trimmedLine);
					}
				}
			}
		} catch (IOException e) {
			System.err.println("Error reading file: " + e.getMessage());
		}

		System.out.println("rootNodeNames: " + rootNodeNames);

		// 根ノード名とノードIDを対応付け
		for (Map.Entry<Integer, Node> entry : this.nodeList.entrySet()) {
			for (String rootName : rootNodeNames) {
				if (entry.getValue().getName().equals(rootName)) {
					this.rootList.add(entry.getKey());
					break; 
				}
			}
		}
	
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

	public Map<Integer, List<Integer>> getGraphAdjacentList() {
		return this.graphAdjacentList;
	}

	public Map<Integer, Node> getNodeList() {
		return this.nodeList;
	}

}
