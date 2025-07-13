#!/usr/bin/env python
# -*- coding: utf-8 -*-

"""
例題プログラム：この例題を改変して大きなプログラムを作る足がかりにしてください。
かの有名な「Hello world.」を出力するプログラムを呼び出すだけのプログラムである。
"""

__author__ = 'AOKI Atsushi'
__version__ = '1.0.4'
__date__ = '2022/11/24 (Created: 2016/01/24)'

# 組み込みモジュールsysをインポートする。exit関数を用いるために。
import sys

# jp.ac.kyoto_su.cse.ap.pythonというパッケージの中のHelloWorldモジュールをインポートする。
from jp.ac.kyoto_su.cse.ap.python import HelloWorld

if __name__ == '__main__':
	# 上記のifによって、このスクリプトファイルが直接実行されたときだけ、以下の部分を実行する。

	# HelloWorldモジュールのmain()を呼び出して結果を得て、Pythonシステムに終わりを告げる。
	sys.exit(HelloWorld.main())
