#!/usr/bin/env python
# -*- coding: utf-8 -*-

"""
かの有名な「Hello world.」を出力するプログラムである。
たかが「Hello world.」だが、されど「Hello world.」である。
Pythonプログラムの行儀作法（世界的な標準：慣習）にならって書き下ろしてある。
これからPythonプログラミングをやろうとするならば、見習って、真似するように。
"""

__author__ = 'AOKI Atsushi'
__version__ = '1.0.4'
__date__ = '2022/11/24 (Created: 2016/01/24)'

def main():
	"""
	かの有名な「Hello world.」を出力するメインプログラム（main関数）である。
	常に0を応答する。それが結果（リターンコード：終了ステータス）になる。
	>>> main()
	Hello world.
	0
	"""

	# print関数を用いて標準出力に「Hello world.」と書き出す。
	print("Hello world.")

	# 結果（リターンコード：終了ステータス）を応答する。
	return 0

if __name__ == '__main__':
	# 上記のif文の記述によって、このスクリプトファイルが起動された時にだけ実行する部分になる。
	# $ python このスクリプトファイル名
	# と起動された時に__name__という変数に'__main__'という文字列が束縛されるゆえに。
	# つまり、このスクリプトがモジュールとしてインポートされた時には実行しないということ。

	# 単体テスト：モジュールのdocstring（ドキュメンテーション文字列）に記載されたすべての対話実行例が書かれている通りに動作するかを検証する。
	# $ python このスクリプトファイル名 -v
	# オプション-vをつけて起動することを忘れずに。
	import doctest
	doctest.testmod()

	# 実際にmain()を呼び出して、結果（リターンコード：終了ステータス）を得て、その結果でPythonシステムに終わりを告げる。
	import sys
	sys.exit(main())
