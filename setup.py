#!/usr/bin/env python
# -*- coding: utf-8 -*-

"""
ソースコードディストリビューション（sdist）のための設え（しつらえ：setup）です。
$ python setup.py sdist
"""

__author__ = 'AOKI Atsushi'
__version__ = '1.0.4'
__date__ = '2022/11/24 (Created: 2016/01/24)'

from distutils.core import setup
import os
import platform
import re

setup( \
	name=re.sub(r"\-[0-9]+\.[0-9]+\.[0-9]+$", "", os.path.basename(os.getcwd())), \
	version=__version__, \
	description='Example written by Python ' + '.'.join(platform.python_version_tuple()), \
	url='http://www.cc.kyoto-su.ac.jp/~atsushi/', \
	author=__author__, \
	author_email='aokisanhe@gmail.com', \
	license='The BSD 2-Clause License', \
	long_description='このプログラムは京都産業大学・情報理工学部の科目「応用プログラミング（Python）」の例題プログラムです。', \
	platforms='macOS ' + platform.mac_ver()[0], \
	packages=['jp'], \
)
