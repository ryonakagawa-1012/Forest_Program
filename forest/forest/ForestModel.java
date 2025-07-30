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

	/**
	 * 深さ優先探索で使用するスタック
	 */
	private List<Integer> dfsStack;

	/**
	 * 現在の根ノードのインデックス
	 */
	private Integer currentRootIndex;

	/**
	 * 深さ優先探索の訪問経路を記録するスタック（戻り処理用）
	 */
	private List<Integer> visitPath;

	/**
	 * 現在が戻り処理中かどうかを示すフラグ
	 */
	private boolean isBacktracking;

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

		// 深さ優先探索の初期化
		this.dfsStack = new ArrayList<Integer>();
		this.visitPath = new ArrayList<Integer>();
		this.currentRootIndex = 0;
		this.isBacktracking = false;

		// 最初の根ノードをスタックにプッシュ
		if (!this.rootList.isEmpty()) {
			this.dfsStack.add(this.rootList.get(0));
		}

		for (int i = 0; i < 200; i++) {
			System.out.println(depthFirstSearch());
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
	 * depthFirstSearchを呼び出し、Nodeを操作するメソッド
	 */
	public Node nextNode() {
		Integer nodeId = depthFirstSearch();
		if (nodeId != null) {
			return nodeList.get(nodeId);
		}
		return null;
	}

	/**
	 * 深さ優先探索をし、次に訪れるノード番号を返すメソッド
	 */
	public Integer depthFirstSearch() {
		// 戻り処理中の場合
		if (this.isBacktracking) {
			if (!this.visitPath.isEmpty()) {
				// 訪問経路から最後の要素を削除して戻る
				Integer backNode = this.visitPath.remove(this.visitPath.size() - 1);

				// 根ノードまで戻った場合は戻り処理終了
				if (this.visitPath.isEmpty() || this.rootList.contains(backNode)) {
					this.isBacktracking = false;
				}

				return backNode;
			} else {
				this.isBacktracking = false;
			}
		}

		// スタックが空で、まだ探索していない根ノードがある場合
		while (this.dfsStack.isEmpty() && this.currentRootIndex < this.rootList.size()) {
			this.currentRootIndex++;
			if (this.currentRootIndex < this.rootList.size()) {
				Integer nextRoot = this.rootList.get(this.currentRootIndex);
				if (!isVisited(nextRoot)) {
					this.dfsStack.add(nextRoot);
					// 新しい根ノードの場合は訪問経路をリセット
					this.visitPath.clear();
				}
			}
		}

		// スタックが空の場合、探索終了
		if (this.dfsStack.isEmpty()) {
			return null;
		}

		// スタックから次のノードを取得
		Integer currentNode = this.dfsStack.remove(this.dfsStack.size() - 1);

		// 既に訪問済みの場合はスキップ
		if (isVisited(currentNode)) {
			return depthFirstSearch(); // 再帰的に次のノードを探索
		}

		// 現在のノードを訪問済みに設定
		setVisited(currentNode, true);

		// 訪問経路に追加
		this.visitPath.add(currentNode);

		// 子ノードを逆順でスタックにプッシュ（深さ優先の順序を保つため）
		List<Integer> children = this.graphAdjacentList.get(currentNode);
		boolean hasUnvisitedChildren = false;

		if (children != null) {
			for (int i = children.size() - 1; i >= 0; i--) {
				Integer child = children.get(i);
				if (!isVisited(child)) {
					this.dfsStack.add(child);
					hasUnvisitedChildren = true;
				}
			}
		}

		// 子ノードがない（葉ノード）または全ての子ノードが訪問済みの場合、戻り処理を開始
		if (!hasUnvisitedChildren && this.dfsStack.isEmpty()) {
			this.isBacktracking = true;
		}

		return currentNode;
	}

	/**
	 * ノードが訪問済みかどうかを確認するヘルパーメソッド
	 */
	private boolean isVisited(Integer nodeId) {
		if (nodeId >= this.visitedNodeList.length) {
			return false;
		}
		return this.visitedNodeList[nodeId] != null && this.visitedNodeList[nodeId];
	}

	/**
	 * ノードの訪問状態を設定するヘルパーメソッド
	 */
	private void setVisited(Integer nodeId, boolean visited) {
		if (nodeId < this.visitedNodeList.length) {
			this.visitedNodeList[nodeId] = visited;
		}
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

	public List<Integer> getRootList() {
		return this.rootList;
	}

}
