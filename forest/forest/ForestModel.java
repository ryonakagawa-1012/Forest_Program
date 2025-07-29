package forest;

import java.awt.Point;
import java.util.Map;
import java.io.File;

/**
 * 木構造のデータを管理するためのモデルクラス
 */
public class ForestModel extends Object {

	/**
	 * 木構造の隣接配列(key=頂点番号:value=List<E>)を保持する連想配列
	 */
	private Map graphAdjacentList;

	/**
	 * 入力として与えられた木構造の親ノードの番号を保持する配列
	 */
	private Integer[] parentList;

	/**
	 * ノード番号とNode型で保持している型情報を紐づけるための連想配列
	 */
	private Map nodeList;

	/**
	 * 主にdepthFirstSearchメソッド内で使用する、ノードが深さ優先探索で探索済みかどうかを保持する配列
	 */
	private Boolean[] visitedNodeList;

	private File inputFile;

	private ForestView forestView;

	/**
	 * 樹状整列のロジックを処理するモデルのインスタンスを生成するコンストラクタ
	 */
	public ForestModel(String filePath) {

	}

	/**
	 * 入力ファイルを元にnodeListの初期値を決定するメソッド
	 */
	public void makeNodeList(File inputFile) {

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
	public void nodeCliked(Point aPoint) {

	}

}
